const formSlider = document.getElementById("formSlider");
const overlayText = document.getElementById("overlayText");

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
