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
    document.getElementById("instagram").innerHTML = `<a href="https://www.instagram.com/${data.instagram}/" class="text-black font-inter">Instagram</a>`;
    document.getElementById("linkedIn").innerHTML = `<a href="https://www.linkedin.com/in/${data.linkedin}"  class="text-black font-inter">Linkedin</a>`; 
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
function deleteAccount(){
    passwd = document.getElementById("password").value
    fetch("/api/account/delete",{
        method: "POST",
         headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
            password: passwd
        })
        }).then((response)=>{
            if (response.ok){
                alert("account deleted successfully")
                window.location.href = "/";
            }else{
                alert("error deleting account : " , response.json().message)
            }
    })
}
const popUpWindow = document.getElementById("pop-up-window")

function closePopUp(){
    popUpWindow.classList.add("hidden")
    popUpWindow.classList.remove("flex")

}

function openPopUp(){
    popUpWindow.classList.remove("hidden")
    popUpWindow.classList.add("flex")
}