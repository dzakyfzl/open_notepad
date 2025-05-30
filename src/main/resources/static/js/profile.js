fetch("/api/account/info",{
    method: "GET",
    headers: {
        "Content-Type": "application/json",
    },
}).then(response => {
    if (response.ok) {
        return response.json();
    } else {
        throw new Error("Network response was not ok");
    }
}).then(data => {
    // Populate the form fields with the fetched data
    document.getElementById("username").innerHTML = data.username;
    document.getElementById("email").innerHTML = data.email;
    document.getElementById("fullName").innerHTML = data.firstName.charAt(0).toUpperCase() + data.firstName.slice(1) + " " + data.lastName.charAt(0).toUpperCase() + data.lastName.slice(1);
    document.getElementById("instagram").innerHTML = data.instagram;
    document.getElementById("linkedIn").innerHTML = data.linkedin;
    document.getElementById("aboutMe").innerHTML = data.aboutMe;

    if (data.instagram === ""){
        document.getElementById("instagram").innerHTML = "Not provided";
    }
    if (data.linkedin === ""){
        document.getElementById("linkedIn").innerHTML = "Not provided";
    }
    
}).catch(error => {
    console.error("Error fetching account info:", error);
    alert("Failed to load account information. Please try again later.");
});

function logout(){
    fetch("/api/account/logout",{
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
    }).then((response)=>{
        if (response.ok){
            window.location.href = "/";
        }})
}