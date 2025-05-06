const formSlider = document.getElementById("formSlider");
const overlayText = document.getElementById("overlayText");
const signup = document.getElementById("signup");
const login = document.getElementById("login");

// Toggle between login and signup forms
function toggleForm(form) {
    if (form === "signup") {
        formSlider.style.transform = "translateX(-50%)";
        overlayText.innerHTML = `
          <h1>Having difficulty understanding lecture material?</h1>
          <p>Join us now!</p>
        `;
    } else {
        formSlider.style.transform = "translateX(0)";
        overlayText.innerHTML = `
          <h1>Start studying slowly,</h1>
          <p>Later there will be big results</p>
        `;
    }
}

signup.addEventListener("submit", function (event) {
    event.preventDefault(); // Prevent the default form submission
    const formData = new FormData(signup);
    const data = Object.fromEntries(formData.entries());
    fetch("/api/signup", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(data),
    })
        .then((response) =>{
            if (response.ok) {
              window.location.href = "/"; // Redirect to home page on success
            }else if (response.status === 400) {
                return response.json().then((data) => {
                    alert(data.message); // Show error message
                });
                
            }
        })
});

login.addEventListener("submit", function (event) {
    event.preventDefault(); // Prevent the default form submission
    const formData = new FormData(login);
    const data = Object.fromEntries(formData.entries());
    fetch("/api/auth/signin", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(data),
    })
    .then((response) => {
      if (response.ok) {
        window.location.href = "/"; // Redirect to home page on success
      }else if (response.status === 400) {
          return response.json().then((data) => {
              alert(data.message); // Show error message
          });
          
      }
  })
} );


