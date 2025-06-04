const formSlider = document.getElementById("formSlider");
// Toggle between login and signup forms
function toggleForm(form) {
  if (form === "signup") {
    formSlider.style.transform = "translateX(-50%)";
  } else {
    formSlider.style.transform = "translateX(0)";
  }
}
document.getElementById("signup").addEventListener("submit", function (event) {
  event.preventDefault(); // Prevent the default form submission
  const formData = new FormData(this);
  const data = Object.fromEntries(formData.entries());
  password = formData.get("password");
  repeatPassword = formData.get("confirmedPassword");
  if (password !== repeatPassword) {
    alert("Passwords do not match");
    return;
  }
  fetch("/api/account/signup", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(data),
  })
    .then((response) => {
      if (response.ok) {
        window.location.href = "/"; // Redirect to home page on success
      } else if (response.status === 400) {
        return response.json().then((data) => {
          alert(data.message); // Show error message
        });
      }
    })
});
document.getElementById("login").addEventListener("submit", function (event) {
  event.preventDefault(); // Prevent the default form submission
  const formData = new FormData(this);
  const data = Object.fromEntries(formData.entries());
  fetch("/api/account/signin", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(data),
  })
    .then((response) => {
      if (response.ok) {
        return response.json(); 
      } else if (response.status === 400) {
        return response.json().then((data) => {
          alert(data.message);
        });
      }
    })
    .then((data) => {
      if (!data) return;
      if (data.role === "admin") {
        window.location.href = "/admin";
      } else {
        window.location.href = "/";
      }
    });
});