document.addEventListener('DOMContentLoaded', function() {
    // Get tab elements
    const loginTab = document.getElementById('login-tab');
    const registerTab = document.getElementById('register-tab');
    
    // Switch to Register form
    document.getElementById('switchToRegister').addEventListener('click', function(e) {
        e.preventDefault();
        registerTab.click();
    });

    // Switch to Login form
    document.getElementById('switchToLogin').addEventListener('click', function(e) {
        e.preventDefault();
        loginTab.click();
    });

    // Form submission handlers
    document.getElementById('loginForm').addEventListener('submit', function(e) {
        e.preventDefault();
        const email = this.querySelector('input[type="email"]').value;
        const password = this.querySelector('input[type="password"]').value;
        
        if(email && password) {
            alert('Login attempted with: ' + email);
            // Add your login logic here
        }
    });

    document.getElementById('registerForm').addEventListener('submit', function(e) {
        e.preventDefault();
        const name = this.querySelector('input[type="text"]').value;
        const email = this.querySelector('input[type="email"]').value;
        const password = this.querySelectorAll('input[type="password"]')[0].value;
        const confirmPassword = this.querySelectorAll('input[type="password"]')[1].value;
        
        if(password !== confirmPassword) {
            alert('Passwords do not match!');
            return;
        }

        if(name && email && password) {
            alert('Registration attempted with: ' + email);
            // Add your registration logic here
        }
    });
});