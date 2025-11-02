const username = localStorage.getItem('username');
if (username) {
    document.getElementById('welcome').textContent = `Welcome, ${username}!`;
} else {
    document.getElementById('welcome').textContent = 'Welcome, Student!';
}
