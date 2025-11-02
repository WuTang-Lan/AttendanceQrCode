const studentId = localStorage.getItem('username');

if (studentId) {
    document.getElementById('welcome').textContent = `${studentId}'s Attendance`;
} else {
    window.location.href = '/';
}

async function loadAttendance() {
    try {
        const response = await fetch(`/api/attendance/student/${studentId}`);
        const records = await response.json();
        
        const listEl = document.getElementById('attendanceList');
        if (records.length === 0) {
            listEl.innerHTML = '<p style="text-align: center; padding: 20px;">No attendance records found.</p>';
        } else {
            listEl.innerHTML = records.map(record => `
                <div class="attendance-item">
                    <p><strong>${record.course_name}</strong></p>
                    <p><small>Marked on: ${new Date(record.time_marked).toLocaleString()}</small></p>
                </div>
            `).join('');
        }
    } catch (error) {
        console.error('Error loading attendance:', error);
        document.getElementById('attendanceList').innerHTML = '<p style="text-align: center; color: #e74c3c;">Error loading attendance records</p>';
    }
}

loadAttendance();
