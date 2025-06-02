const stars = document.querySelectorAll('.star'); 
let currentRating = 0;
let isLocked = false;
noteID = window.noteID

function highlightStars(rating) {
      stars.forEach((star, index) => {
        if (index < rating) {
          star.classList.add('text-yellow-400');
          star.classList.remove('text-gray-400');
        } else {
          star.classList.add('text-gray-400');
          star.classList.remove('text-yellow-400');
        }
    });
}

pageTitle = document.getElementById("page-title")
title = document.getElementById("title")
major = document.getElementById("major")
course = document.getElementById("course")
owner = document.getElementById("owner")
description = document.getElementById("description")
bookmarkBTN = document.getElementById("bookmark-button")
rating = document.getElementById("rate-count")
views = document.getElementById("views")
noteRate = document.querySelectorAll('.rate')
visibility = document.getElementById("set-visibility")



fetch('/api/note/get?noteID=' + noteID, {
  method: 'GET',
  headers: {
    'Content-Type': 'application/json'
  }
})
  .then(response => response.json())
  .then(data => {
    console.log(data)
    pageTitle.innerHTML = "Admin Open Notepad - " + data["name"] 
    document.title = data["name"] + " - Open Notepad"
    title.innerHTML = data["name"]
    major.innerHTML = data["major"]
    course.innerHTML = data["course"]
    owner.innerHTML += data["username"]
    description.innerHTML = data["description"]
    rating.innerHTML = data["rating"] + "/5"
    views.innerHTML = data["views"] + " View"
    if(data["userRate"] != 0){
        highlightStars(data["userRate"])
        isLocked = true;
    }
    noteRate.forEach((star, index) => {
        if (index < data["rating"]) {
          star.classList.add('text-yellow-400');
          star.classList.remove('text-gray-400');
        } else {
          star.classList.add('text-gray-400');
          star.classList.remove('text-yellow-400');
        }
    });
})

visibility.addEventListener("change", function() {
  if(confirm("Are you sure you want to change the visibility of this note?")){
    console.log(visibility.value)
    var visibilityBoolean = visibility.value == "public";
    console.log(visibilityBoolean)
    fetch('/api/note/visibility?noteID=' + noteID + '&visibility=' + visibilityBoolean, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
    })
    .then(response => {
      if (!response.ok) {
        alert('Failed to change visibility : ' + response.json().message);

      }else{
        alert('Visibility changed successfully!');
        window.location.href = '/admin';
      }
    })
  }
})

function download() {
  const downloadUrl = `/api/interface/download?noteID=${noteID}`;

  // Buat elemen <a> secara dinamis untuk memicu download
  const link = document.createElement('a');
  link.href = downloadUrl;
  link.download = ''; // kosongkan jika backend sudah set Content-Disposition

  document.body.appendChild(link);
  link.click();
  document.body.removeChild(link);
}

function deleteNote(){
  if (confirm('Are you sure you want to delete this note?')) {
    fetch('/api/note/delete?noteID=' + noteID, {
      method: 'POST',
    })
    .then(response => {
      if (response.ok) {
        alert('Note deleted successfully!');
        window.location.href = '/admin';
      } else {
        alert('Failed to delete note.');
      }
    })
  }
}