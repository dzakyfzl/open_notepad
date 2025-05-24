// Fungsi untuk membuat card note
function createNoteCard(note) {
  const card = document.createElement('div');
  card.className = 'card';
  
  // Membatasi judul maksimal 22 karakter
  const title = note.noteName 
    ? note.noteName.length > 22 
      ? note.noteName.substring(0, 19) + '...' 
      : note.noteName
    : 'No Title';
  
  card.innerHTML = `
    <h3>${title}</h3>
    <p class="category">${note.major || 'No Category'}</p>
    <div class="card-footer">
      <span>${note.username || 'Anonymous'}</span>
    </div>
  `;
  
  return card;
}

// Fungsi untuk memuat dan menampilkan notes
async function loadNotes() {
  const grid = document.getElementById('notes-grid');
  
  try {
    // Menampilkan loading state
    grid.innerHTML = '<div class="loading">Loading notes...</div>';
    
    // Fetch data dari API
    const response = await fetch('/api/data/getAllnotes');
    
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    
    const notes = await response.json();
    
    // Kosongkan grid dan tambahkan cards
    grid.innerHTML = '';
    notes.forEach(note => {
      grid.appendChild(createNoteCard(note));
    });
    
  } catch (error) {
    console.error('Failed to load notes:', error);
    grid.innerHTML = `<div class="error">Failed to load notes: ${error.message}</div>`;
  }
}

// Panggil fungsi saat halaman dimuat
document.addEventListener('DOMContentLoaded', loadNotes);

// Fungsi pencarian (opsional)
async function searchByName() {
  const searchTerm = document.getElementById('search-input').value.trim();
  if (!searchTerm) return loadNotes();
  
  const grid = document.getElementById('notes-grid');
  grid.innerHTML = '<div class="loading">Searching...</div>';
  
  try {
    const response = await fetch(`/api/data/search?name=${encodeURIComponent(searchTerm)}`);
    const results = await response.json();
    
    grid.innerHTML = '';
    results.forEach(note => {
      grid.appendChild(createNoteCard(note));
    });
    
  } catch (error) {
    console.error('Search failed:', error);
    grid.innerHTML = `<div class="error">Search failed: ${error.message}</div>`;
  }
}