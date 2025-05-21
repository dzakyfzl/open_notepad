document.addEventListener('DOMContentLoaded', function () {
  const loginForm = document.querySelector('#login');
  const signupForm = document.querySelector('#signup');

  loginForm.addEventListener('submit', async function (e) {
    e.preventDefault();

    const formData = new FormData(loginForm);
    const data = {
      username: formData.get('username'),
      password: formData.get('password')
    };

    const response = await fetch('/api/account/signin', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      credentials: 'include', // penting untuk mengirim dan menerima session cookie
      body: JSON.stringify(data)
    });

    const result = await response.json();
    alert(result.message);

    if (response.ok) {
      window.location.href = '/index.html'; // ganti dengan halaman utama kamu
    }
  });

  signupForm.addEventListener('submit', async function (e) {
    e.preventDefault();

    const formData = new FormData(signupForm);
    const password = formData.get('password');
    const confirmedPassword = formData.get('confirmedPassword');

    if (password !== confirmedPassword) {
      alert("Passwords do not match!");
      return;
    }

    const data = {
      username: formData.get('username'),
      password: password,
      email: formData.get('email'),
      firstName: formData.get('firstName'),
      lastName: formData.get('lastName')
    };

    const response = await fetch('/api/account/signup', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      credentials: 'include', // kirim dan terima session cookie
      body: JSON.stringify(data)
    });

    const result = await response.json();
    alert(result.message);

    if (response.ok) {
      window.location.href = '/home'; // ganti juga dengan halaman utama
    }
  });
});

// Fungsi untuk toggle tampilan login <-> register
function toggleForm(formType) {
  const formSlider = document.getElementById('formSlider');
  if (formType === 'signup') {
    formSlider.style.transform = 'translateX(-100%)';
  } else {
    formSlider.style.transform = 'translateX(0)';
  }
}
