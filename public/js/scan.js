document.getElementById('scanForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const studentId = document.getElementById('studentId').value;
    const resultEl = document.getElementById('result');
    
    try {
        const response = await fetch('/api/mark-attendance', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ studentId })
        });
        
        const data = await response.json();
        
        if (data.success) {
            resultEl.textContent = data.message;
            resultEl.className = 'status success';
            document.getElementById('studentId').value = '';
        } else {
            resultEl.textContent = data.message;
            resultEl.className = 'status error';
        }
    } catch (error) {
        resultEl.textContent = '❌ Error connecting to server';
        resultEl.className = 'status error';
    }
});
