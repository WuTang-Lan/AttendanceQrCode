document.getElementById('loginForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    const statusEl = document.getElementById('status');
    
    try {
        const response = await fetch('/api/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, password })
        });
        
        const data = await response.json();
        
        if (data.success) {
            statusEl.textContent = `✅ Logged in as ${data.role}`;
            statusEl.className = 'status success';
            
            localStorage.setItem('username', data.username);
            
            setTimeout(() => {
                if (data.role === 'lecturer') {
                    window.location.href = '/lecturer.html';
                } else {
                    window.location.href = '/student.html';
                }
            }, 500);
        } else {
            statusEl.textContent = '❌ Invalid login. Try again.';
            statusEl.className = 'status error';
        }
    } catch (error) {
        statusEl.textContent = '❌ Error connecting to server';
        statusEl.className = 'status error';
    }
});
