const stars = document.querySelectorAll('#stars i');
    const ratingText = document.getElementById('rating-value');
    let currentRating = 0;

    stars.forEach(star => {
        star.addEventListener('click', () => {
        currentRating = star.getAttribute('data-value');
        updateStars(currentRating);
        ratingText.textContent = `You rated: ${currentRating} star${currentRating > 1 ? 's' : ''}`;

        // Kirim rating ke Spring Boot
        fetch('/api/interface/rate', {
            method: 'POST',
            headers: {
            'Content-Type': 'application/json'
            },
            body: JSON.stringify({
            noteId: 123,            // <-- ganti dengan ID catatan sebenarnya
            rating: currentRating
            })
        })
        .then(response => {
            if (!response.ok) throw new Error('Network error');
            return response.json();
        })
        .then(data => {
            console.log('Rating submitted:', data);
        })
        .catch(err => {
            console.error('Submit failed:', err);
        });
        });

        star.addEventListener('mouseover', () => updateStars(star.getAttribute('data-value')));
        star.addEventListener('mouseout', () => updateStars(currentRating));
    });

    function updateStars(rating) {
        stars.forEach(star => {
        star.classList.toggle('active', star.getAttribute('data-value') <= rating);
        });
    }