async function loadQRCode() {
    try {
        const response = await fetch('/api/qr');
        const data = await response.json();
        document.getElementById('qrCode').src = data.qrCode;
    } catch (error) {
        console.error('Error loading QR code:', error);
    }
}

async function loadAttendance() {
    try {
        const response = await fetch('/api/attendance');
        const records = await response.json();
        
        const listEl = document.getElementById('attendanceList');
        if (records.length === 0) {
            listEl.innerHTML = '<p>No attendance records yet.</p>';
        } else {
            listEl.innerHTML = records.map(record => `
                <div class="attendance-item">
                    <p><strong>${record.student_name}</strong> (ID: ${record.student_id})</p>
                    <p><small>Session: ${record.session_id}</small></p>
                    <p><small>Time: ${new Date(record.time_marked).toLocaleString()}</small></p>
                </div>
            `).join('');
        }
    } catch (error) {
        console.error('Error loading attendance:', error);
    }
}

loadQRCode();
loadAttendance();

setInterval(loadAttendance, 5000);
