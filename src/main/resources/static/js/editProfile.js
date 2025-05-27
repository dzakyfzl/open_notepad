document.getElementById('profilePicture').addEventListener('change', function() {
    const fileInput = document.getElementById('profilePicture');
    const file = fileInput.files[0];
    console.log('Selected file:', file);
    if (file) {
        const reader = new FileReader();
        reader.onload = function(e) {
            document.getElementById('profileImage').src = e.target.result;
        };
        reader.readAsDataURL(file);
    }
});


// Load saved user data if available
fetch('/api/account/info').then(response => {
    if (response.ok) {
        return response.json();
    } else {
        console.error('Error fetching user data:', response.statusText);
        return {};
    }
}).then(userData => {
    console.log('User data loaded:', userData);
    // Populate form fields with existing data
    document.getElementById('username').innerHTML = userData.username;
    if (userData.firstName) document.getElementById('firstName').value = userData.firstName;
    if (userData.lastName) document.getElementById('lastName').value = userData.lastName;
    if (userData.email) document.getElementById('email').value = userData.email;
    if (userData.aboutMe != "") document.getElementById('aboutMe').value = userData.aboutMe;
    // Update social media connection status
    if (userData.linkedin != "") {
        document.getElementById('linkedin').value = userData.linkedin;
    }

    if (userData.instagram != "") {
        document.getElementById('instagram').value = userData.instagram;
    }
}).catch(error => {
    console.error('Error fetching user data:', error);
});

// Password visibility toggle
document.getElementById('togglePassword').addEventListener('click', function() {
    const passwordInput = document.getElementById('password');
    
    if (passwordInput.type === 'password') {
        passwordInput.type = 'text';
        this.innerHTML = `
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13.875 18.825A10.05 10.05 0 0112 19c-4.478 0-8.268-2.943-9.543-7a9.97 9.97 0 011.563-3.029m5.858.908a3 3 0 114.243 4.243M9.878 9.878l4.242 4.242M9.88 9.88l-3.29-3.29m7.532 7.532l3.29 3.29M3 3l3.59 3.59m0 0A9.953 9.953 0 0112 5c4.478 0 8.268 2.943 9.543 7a10.025 10.025 0 01-4.132 5.411m0 0L21 21" />
            </svg>
        `;
    } else {
        passwordInput.type = 'password';
        this.innerHTML = `
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z" />
            </svg>
        `;
    }
});

// Repeat password visibility toggle
document.getElementById('toggleRepeatPassword').addEventListener('click', function() {
    const passwordInput = document.getElementById('repeatPassword');
    
    if (passwordInput.type === 'password') {
        passwordInput.type = 'text';
        this.innerHTML = `
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13.875 18.825A10.05 10.05 0 0112 19c-4.478 0-8.268-2.943-9.543-7a9.97 9.97 0 011.563-3.029m5.858.908a3 3 0 114.243 4.243M9.878 9.878l4.242 4.242M9.88 9.88l-3.29-3.29m7.532 7.532l3.29 3.29M3 3l3.59 3.59m0 0A9.953 9.953 0 0112 5c4.478 0 8.268 2.943 9.543 7a10.025 10.025 0 01-4.132 5.411m0 0L21 21" />
            </svg>
        `;
    } else {
        passwordInput.type = 'password';
        this.innerHTML = `
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z" />
            </svg>
        `;
    }
});

// Password validation
function validatePasswords() {
    const password = document.getElementById('password').value;
    const repeatPassword = document.getElementById('repeatPassword').value;
    const errorElement = document.getElementById('passwordError');
    
    if (password && repeatPassword && password !== repeatPassword) {
        errorElement.classList.remove('hidden');
        return false;
    } else {
        errorElement.classList.add('hidden');
        return true;
    }
}

document.getElementById('password').addEventListener('input', validatePasswords);
document.getElementById('repeatPassword').addEventListener('input', validatePasswords);


// Form submission - FIXED VERSION
function uploadProfile(event){
    if (event) event.preventDefault();
    // Validate passwords if both fields have values
    const password = document.getElementById('password').value;
    const repeatPassword = document.getElementById('repeatPassword').value;
    if ((password || repeatPassword) && !validatePasswords()) {
        alert('Passwords do not match. Please try again.');
        return; // Don't submit if passwords don't match
    }
    // Get form data
    const formData = new FormData();
    if (document.getElementById('profilePicture').files.length > 0) {
        formData.append('file', document.getElementById('profilePicture').files[0]);
        console.log('Profile image selected:', document.getElementById('profilePicture').files[0]);
    }
    formData.append('firstName', document.getElementById('firstName').value);
    formData.append('lastName', document.getElementById('lastName').value);
    formData.append('email', document.getElementById('email').value);
    formData.append('password', password);
    formData.append('aboutMe', document.getElementById('aboutMe').value);
    formData.append('linkedin', document.getElementById('linkedin').value);
    formData.append('instagram', document.getElementById('instagram').value);
    console.log('Form data prepared:', formData);
    fetch('/api/account/edit', {
        method: 'POST',
        body: formData,
    }).then(response => {
        if (response.ok) {
            return response.json();
        } else {
            throw new Error('Error updating profile: ' + response.statusText);
        }
    }).then(data => {
        console.log('Profile updated successfully:', data);
        alert('Profile updated successfully!');
    }).catch(error => {
        console.log('Error updating profile:', error);
        alert('Failed to update profile. Please try again later.');
    });
}
// Attach to form submit event
const profileForm = document.getElementById('profileForm');
if (profileForm) {
    profileForm.addEventListener('submit', uploadProfile);
}