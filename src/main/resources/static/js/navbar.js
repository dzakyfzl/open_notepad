searchInput = document.getElementById('search-input');
profilePicture = document.getElementById('profile-picture');

searchInput.addEventListener('keydown', () => {
    if (event.key === 'Enter') {
        searchByName();
    }
});

function searchByName() {
    const searchValue = searchInput.value.toLowerCase();
    console.log(searchValue);
}

profilePicture.addEventListener('mouseover', function() {
    profilePicture.classList.remove('border-gray-300');
    profilePicture.classList.add('border-sky-500');
});
profilePicture.addEventListener('mouseout', function() {
    profilePicture.classList.add('border-gray-300');
    profilePicture.classList.remove('border-sky-500');
});
