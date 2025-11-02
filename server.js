const express = require('express');
const bodyParser = require('body-parser');
const Database = require('better-sqlite3');
const QRCode = require('qrcode');
const path = require('path');

const app = express();
const db = new Database('attendance.db');

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));
app.use(express.static('public'));

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
        CREATE TABLE IF NOT EXISTS attendance (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            student_id TEXT NOT NULL,
            student_name TEXT NOT NULL,
            session_id TEXT NOT NULL,
            time_marked DATETIME DEFAULT CURRENT_TIMESTAMP
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

app.get('/api/qr', async (req, res) => {
    const url = `${req.protocol}://${req.get('host')}/scan.html`;
    try {
        const qrDataUrl = await QRCode.toDataURL(url, { width: 300 });
        res.json({ qrCode: qrDataUrl });
    } catch (err) {
        res.status(500).json({ error: 'Failed to generate QR code' });
    }
});

app.post('/api/mark-attendance', (req, res) => {
    const { studentId } = req.body;
    
    const student = db.prepare('SELECT username FROM users WHERE username = ? AND role = ?')
        .get(studentId, 'student');
    
    if (student) {
        const insert = db.prepare('INSERT INTO attendance (student_id, student_name, session_id, time_marked) VALUES (?, ?, ?, datetime("now"))');
        insert.run(studentId, student.username, 'SESSION-1');
        res.json({ success: true, message: `✅ Marked Present: ${student.username}` });
    } else {
        res.json({ success: false, message: '❌ Invalid Student ID' });
    }
});

app.get('/api/attendance', (req, res) => {
    const records = db.prepare('SELECT * FROM attendance ORDER BY time_marked DESC').all();
    res.json(records);
});

const PORT = process.env.PORT || 5000;
app.listen(PORT, '0.0.0.0', () => {
    console.log(`🌍 Server running at http://0.0.0.0:${PORT}`);
});
