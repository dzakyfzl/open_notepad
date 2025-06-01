const stars = document.querySelectorAll('.star'); 
let currentRating = 0;
let isLocked = false;
noteID = window.noteID
stars.forEach(star => {
  star.addEventListener('mouseover', () => {
    if (!isLocked) {
      const rating = parseInt(star.dataset.index);
      highlightStars(rating);
    }
  });
  star.addEventListener('mouseout', () => {
    if (!isLocked) {
      highlightStars(currentRating);
    }
  });
  star.addEventListener('click', () => {
    if (!isLocked) {
      currentRating = parseInt(star.dataset.index);
      highlightStars(currentRating);
      fetch('/api/interface/rate?noteID=' + noteID + '&rate=' + currentRating, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
      }
      )
      isLocked = true; // Lock after selection
      console.log("Final selected rating:", currentRating);
      stars.forEach(s => {
        s.classList.remove('cursor-pointer');
        s.classList.add('cursor-default');
      });
    }
  });
});

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


fetch('/api/interface/view?noteID=' + noteID,{
  method: 'POST'
})
fetch('/api/note/get?noteID=' + noteID, {
  method: 'GET',
  headers: {
    'Content-Type': 'application/json'
  }
})
  .then(response => response.json())
  .then(data => {
    console.log(data)
    pageTitle.innerHTML = data["name"] + " - Note"
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
    if(data["bookmarked"]){
        bookmarkBTN.innerHTML = `<i class="fa fa-bookmark justify-center text-center self-center"></i><p class="text-center justify-center">Bookmarked</p>`
    }
})


