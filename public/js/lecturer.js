let currentSessionCode = null;
let currentSessionId = null;
let refreshInterval = null;
let lastCodeUpdate = null;

const lecturerId = localStorage.getItem('username');

async function checkActiveSession() {
    try {
        const response = await fetch(`/api/session/active/${lecturerId}`);
        const data = await response.json();
        
        if (data.active) {
            const newCode = data.session.session_code;
            
            if (newCode !== currentSessionCode) {
                currentSessionCode = newCode;
                currentSessionId = data.session.id;
                loadQRCode(currentSessionCode);
                lastCodeUpdate = new Date();
                console.log('IRVR code refreshed');
            }
            
            document.getElementById('sessionStatus').textContent = `Active Session: ${data.session.course_name}`;
            document.getElementById('sessionStatus').className = 'status success';
            document.getElementById('startBtn').style.display = 'none';
            document.getElementById('stopBtn').style.display = 'block';
            document.getElementById('qrContainer').style.display = 'block';
            
            loadAttendance();
            
            const expiresAt = new Date(data.session.expires_at);
            const timeLeft = Math.floor((expiresAt - new Date()) / 1000);
            updateTimer(timeLeft);
            
            if (!refreshInterval) {
                refreshInterval = setInterval(() => {
                    checkActiveSession();
                    loadAttendance();
                }, 3000);
            }
        } else {
            currentSessionCode = null;
            currentSessionId = null;
            document.getElementById('sessionStatus').textContent = 'No active session';
            document.getElementById('sessionStatus').className = 'status';
            document.getElementById('startBtn').style.display = 'block';
            document.getElementById('stopBtn').style.display = 'none';
            document.getElementById('qrContainer').style.display = 'none';
            document.getElementById('attendanceList').innerHTML = '<p>Start a session to view attendance</p>';
            
            if (refreshInterval) {
                clearInterval(refreshInterval);
                refreshInterval = null;
            }
        }
    } catch (error) {
        console.error('Error checking session:', error);
    }
}

async function startSession() {
    const courseName = document.getElementById('courseName').value.trim();
    
    if (!courseName) {
        alert('Please enter a course name');
        return;
    }
    
    try {
        const response = await fetch('/api/session/start', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ lecturerId, courseName })
        });
        
        const data = await response.json();
        
        if (data.success) {
            currentSessionId = data.sessionId;
            checkActiveSession();
        }
    } catch (error) {
        console.error('Error starting session:', error);
    }
}

async function stopSession() {
    try {
        const response = await fetch('/api/session/stop', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ lecturerId })
        });
        
        const data = await response.json();
        
        if (data.success) {
            checkActiveSession();
        }
    } catch (error) {
        console.error('Error stopping session:', error);
    }
}

async function loadQRCode(sessionCode) {
    try {
        const response = await fetch(`/api/qr/${sessionCode}`);
        const data = await response.json();
        document.getElementById('qrCode').src = data.qrCode;
    } catch (error) {
        console.error('Error loading QR code:', error);
    }
}

async function loadAttendance() {
    try {
        const response = await fetch(`/api/attendance/${lecturerId}`);
        const records = await response.json();
        
        const listEl = document.getElementById('attendanceList');
        if (records.length === 0) {
            listEl.innerHTML = '<p>No attendance records yet.</p>';
        } else {
            listEl.innerHTML = records.map(record => `
                <div class="attendance-item">
                    <p><strong>${record.student_name}</strong></p>
                    <p><small>Course: ${record.course_name}</small></p>
                    <p><small>Time: ${new Date(record.time_marked).toLocaleString()}</small></p>
                </div>
            `).join('');
        }
    } catch (error) {
        console.error('Error loading attendance:', error);
    }
}

function updateTimer(seconds) {
    const timerEl = document.getElementById('timer');
    if (seconds <= 0) {
        timerEl.textContent = 'Code refreshing...';
        timerEl.style.color = '#f39c12';
        return;
    }
    
    const minutes = Math.floor(seconds / 60);
    const secs = seconds % 60;
    timerEl.textContent = `Code expires in: ${minutes}:${secs.toString().padStart(2, '0')}`;
    timerEl.style.color = minutes < 1 ? '#e74c3c' : '#27ae60';
}

checkActiveSession();
