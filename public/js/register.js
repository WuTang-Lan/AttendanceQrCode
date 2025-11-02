document.getElementById('registerForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    const confirmPassword = document.getElementById('confirmPassword').value;
    const role = document.getElementById('role').value;
    const statusEl = document.getElementById('status');
    
    if (password !== confirmPassword) {
        statusEl.textContent = '❌ Passwords do not match';
        statusEl.className = 'status error';
        return;
    }
    
    if (password.length < 6) {
        statusEl.textContent = '❌ Password must be at least 6 characters';
        statusEl.className = 'status error';
        return;
    }
    
    try {
        const response = await fetch('/api/register', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, password, role })
        });
        
        const data = await response.json();
        
        if (data.success) {
            statusEl.textContent = '✅ ' + data.message;
            statusEl.className = 'status success';
            
            document.getElementById('registerForm').reset();
            
            setTimeout(() => {
                window.location.href = '/';
            }, 2000);
        } else {
            statusEl.textContent = '❌ ' + data.message;
            statusEl.className = 'status error';
        }
    } catch (error) {
        statusEl.textContent = '❌ Error connecting to server';
        statusEl.className = 'status error';
    }
});
