const express = require('express');
const bodyParser = require('body-parser');
const Database = require('better-sqlite3');
const QRCode = require('qrcode');
const crypto = require('crypto');

const app = express();
const db = new Database('attendance.db');

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));
app.use(express.static('public'));

const CODE_VALIDITY_MINUTES = 5;

function initializeDatabase() {
    db.exec(`
        CREATE TABLE IF NOT EXISTS users (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            username TEXT UNIQUE NOT NULL,
            password TEXT NOT NULL,
            role TEXT NOT NULL
        )
    `);
    
    db.exec(`
        CREATE TABLE IF NOT EXISTS sessions (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            session_code TEXT UNIQUE NOT NULL,
            course_name TEXT NOT NULL,
            lecturer_id TEXT NOT NULL,
            created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
            expires_at DATETIME NOT NULL,
            is_active BOOLEAN DEFAULT 1
        )
    `);
    
    db.exec(`
        CREATE TABLE IF NOT EXISTS attendance (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            student_id TEXT NOT NULL,
            student_name TEXT NOT NULL,
            session_code TEXT NOT NULL,
            time_marked DATETIME DEFAULT CURRENT_TIMESTAMP,
            UNIQUE(student_id, session_code)
        )
    `);
    
    const count = db.prepare('SELECT COUNT(*) as count FROM users').get();
    if (count.count === 0) {
        const insert = db.prepare('INSERT INTO users (username, password, role) VALUES (?, ?, ?)');
        insert.run('lecturer1', 'pass123', 'lecturer');
        insert.run('student1', 'pass123', 'student');
        insert.run('student2', 'pass123', 'student');
        console.log('✅ Sample users created (lecturer1/pass123, student1/pass123, student2/pass123)');
    }
    
    console.log('✅ Database initialized successfully');
}

initializeDatabase();

function generateSessionCode() {
    return crypto.randomBytes(16).toString('hex');
}

app.post('/api/register', (req, res) => {
    const { username, password, role } = req.body;
    
    if (!username || !password || !role) {
        return res.json({ success: false, message: 'All fields are required' });
    }
    
    if (role !== 'student' && role !== 'lecturer') {
        return res.json({ success: false, message: 'Invalid role. Choose student or lecturer.' });
    }
    
    try {
        const insert = db.prepare('INSERT INTO users (username, password, role) VALUES (?, ?, ?)');
        insert.run(username, password, role);
        res.json({ success: true, message: 'Registration successful! You can now login.' });
    } catch (error) {
        if (error.message.includes('UNIQUE constraint failed')) {
            res.json({ success: false, message: 'Username already exists. Please choose another.' });
        } else {
            res.json({ success: false, message: 'Registration failed. Please try again.' });
        }
    }
});

app.post('/api/login', (req, res) => {
    const { username, password } = req.body;
    const user = db.prepare('SELECT role FROM users WHERE username = ? AND password = ?')
        .get(username, password);
    
    if (user) {
        res.json({ success: true, role: user.role, username });
    } else {
        res.json({ success: false, message: 'Invalid credentials' });
    }
});

app.post('/api/session/start', (req, res) => {
    const { lecturerId, courseName } = req.body;
    
    const stmt = db.prepare('UPDATE sessions SET is_active = 0 WHERE lecturer_id = ? AND is_active = 1');
    stmt.run(lecturerId);
    
    const sessionCode = generateSessionCode();
    const expiresAt = new Date(Date.now() + CODE_VALIDITY_MINUTES * 60 * 1000).toISOString();
    
    const insert = db.prepare('INSERT INTO sessions (session_code, course_name, lecturer_id, expires_at) VALUES (?, ?, ?, ?)');
    const result = insert.run(sessionCode, courseName, lecturerId, expiresAt);
    
    res.json({ success: true, sessionCode, expiresAt, sessionId: result.lastInsertRowid });
});

app.post('/api/session/refresh', (req, res) => {
    const { sessionId } = req.body;
    
    const session = db.prepare('SELECT * FROM sessions WHERE id = ? AND is_active = 1').get(sessionId);
    
    if (!session) {
        return res.json({ success: false, message: 'Session not found or inactive' });
    }
    
    const newCode = generateSessionCode();
    const expiresAt = new Date(Date.now() + CODE_VALIDITY_MINUTES * 60 * 1000).toISOString();
    
    const update = db.prepare('UPDATE sessions SET session_code = ?, expires_at = ? WHERE id = ?');
    update.run(newCode, expiresAt, sessionId);
    
    res.json({ success: true, sessionCode: newCode, expiresAt });
});

app.post('/api/session/stop', (req, res) => {
    const { lecturerId } = req.body;
    
    const stmt = db.prepare('UPDATE sessions SET is_active = 0 WHERE lecturer_id = ? AND is_active = 1');
    stmt.run(lecturerId);
    
    res.json({ success: true, message: 'Session stopped' });
});

app.get('/api/session/active/:lecturerId', (req, res) => {
    const { lecturerId } = req.params;
    
    const session = db.prepare('SELECT * FROM sessions WHERE lecturer_id = ? AND is_active = 1 ORDER BY created_at DESC LIMIT 1').get(lecturerId);
    
    if (!session) {
        return res.json({ active: false });
    }
    
    const now = new Date();
    const expiresAt = new Date(session.expires_at);
    
    if (now > expiresAt) {
        const newCode = generateSessionCode();
        const newExpiresAt = new Date(Date.now() + CODE_VALIDITY_MINUTES * 60 * 1000).toISOString();
        
        const update = db.prepare('UPDATE sessions SET session_code = ?, expires_at = ? WHERE id = ?');
        update.run(newCode, newExpiresAt, session.id);
        
        session.session_code = newCode;
        session.expires_at = newExpiresAt;
    }
    
    res.json({ active: true, session });
});

app.get('/api/qr/:sessionCode', async (req, res) => {
    const { sessionCode } = req.params;
    const url = `${req.protocol}://${req.get('host')}/scan.html?code=${sessionCode}`;
    
    try {
        const qrDataUrl = await QRCode.toDataURL(url, { width: 300 });
        res.json({ qrCode: qrDataUrl });
    } catch (err) {
        res.status(500).json({ error: 'Failed to generate QR code' });
    }
});

app.post('/api/mark-attendance', (req, res) => {
    const { studentId, sessionCode } = req.body;
    
    const student = db.prepare('SELECT username FROM users WHERE username = ? AND role = ?')
        .get(studentId, 'student');
    
    if (!student) {
        return res.json({ success: false, message: '❌ Invalid Student ID' });
    }
    
    const session = db.prepare('SELECT * FROM sessions WHERE session_code = ? AND is_active = 1').get(sessionCode);
    
    if (!session) {
        return res.json({ success: false, message: '❌ Invalid or inactive session code' });
    }
    
    const now = new Date();
    const expiresAt = new Date(session.expires_at);
    
    if (now > expiresAt) {
        const stmt = db.prepare('UPDATE sessions SET is_active = 0 WHERE id = ?');
        stmt.run(session.id);
        return res.json({ success: false, message: '❌ Session has expired' });
    }
    
    try {
        const insert = db.prepare('INSERT INTO attendance (student_id, student_name, session_code) VALUES (?, ?, ?)');
        insert.run(studentId, student.username, sessionCode);
        res.json({ success: true, message: `✅ Attendance marked for ${session.course_name}` });
    } catch (error) {
        if (error.message.includes('UNIQUE constraint failed')) {
            res.json({ success: false, message: '❌ You have already marked attendance for this session' });
        } else {
            res.json({ success: false, message: '❌ Failed to mark attendance' });
        }
    }
});

app.get('/api/attendance/:lecturerId', (req, res) => {
    const { lecturerId } = req.params;
    
    const records = db.prepare(`
        SELECT a.*, s.course_name, s.created_at as session_time 
        FROM attendance a 
        JOIN sessions s ON a.session_code = s.session_code 
        WHERE s.lecturer_id = ? 
        ORDER BY a.time_marked DESC
    `).all(lecturerId);
    
    res.json(records);
});

app.get('/api/attendance/student/:studentId', (req, res) => {
    const { studentId } = req.params;
    
    const records = db.prepare(`
        SELECT a.*, s.course_name, s.lecturer_id 
        FROM attendance a 
        JOIN sessions s ON a.session_code = s.session_code 
        WHERE a.student_id = ? 
        ORDER BY a.time_marked DESC
    `).all(studentId);
    
    res.json(records);
});

const PORT = process.env.PORT || 5000;
app.listen(PORT, '0.0.0.0', () => {
    console.log(`🌍 Server running at http://0.0.0.0:${PORT}`);
});
