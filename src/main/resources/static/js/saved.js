// Variabel global untuk menyimpan data
let allBookmarkedNotesData = []

// Fungsi untuk inisialisasi aplikasi
function initializeApp() {
  console.log("Initializing saved notes app...")

  // Ambil elemen HTML berdasarkan ID
  const cardGrid = document.getElementById("card-grid")
  const searchInput = document.getElementById("search-input")
  const searchButton = document.getElementById("search-button")

  // Pastikan elemen ditemukan
  if (!cardGrid) {
    console.error("Elemen dengan ID 'card-grid' tidak ditemukan di DOM.")
    return
  }
  if (!searchInput) {
    console.error("Elemen dengan ID 'search-input' tidak ditemukan di DOM.")
    return
  }
  if (!searchButton) {
    console.error("Elemen dengan ID 'search-button' tidak ditemukan di DOM.")
    return
  }

  // Setup event listeners
  setupEventListeners()

  // Load data awal - load bookmarked notes
  loadBookmarkedNotes()
}

// Fungsi untuk setup event listeners
function setupEventListeners() {
  const searchInput = document.getElementById("search-input")
  const searchButton = document.getElementById("search-button")

  // Event listener untuk search input (Enter key)
  if (searchInput) {
    searchInput.addEventListener("keypress", (event) => {
      if (event.key === "Enter") {
        handleSearch()
      }
    })
  }

  // Event listener untuk search button
  if (searchButton) {
    searchButton.addEventListener("click", handleSearch)
  }
}

// Fungsi untuk handle search
function handleSearch() {
  const searchInput = document.getElementById("search-input")
  const searchTerm = searchInput.value.trim()

  if (!searchTerm) {
    // Jika search kosong, tampilkan semua data
    displayNotes(allBookmarkedNotesData)
    return
  }

  // Filter data berdasarkan search term
  const filteredData = allBookmarkedNotesData.filter((item) => {
    const name = (item.name || "").toLowerCase()
    const course = (item.course || "").toLowerCase()
    const major = (item.major || "").toLowerCase()
    const username = (item.username || "").toLowerCase()
    const searchLower = searchTerm.toLowerCase()

    return (
      name.includes(searchLower) ||
      course.includes(searchLower) ||
      major.includes(searchLower) ||
      username.includes(searchLower)
    )
  })

  displayNotes(filteredData)
}

// Fungsi untuk memuat bookmarked notes
function loadBookmarkedNotes() {
  console.log("Memuat bookmarked notes...")
  const cardGrid = document.getElementById("card-grid")

  // Tampilkan loading indicator
  showLoadingIndicator()

  fetch("/api/data/getBookmarkedNotes", {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
    },
  })
    .then((response) => {
      console.log("Response status:", response.status)
      if (!response.ok) {
        throw new Error(`Network response was not ok: ${response.status} ${response.statusText}`)
      }
      return response.json()
    })
    .then((data) => {
      console.log("Data diterima dari getBookmarkedNotes:", data)

      // Check if response contains a message (no bookmarks found)
      if (Array.isArray(data) && data.length === 1 && data[0].message) {
        allBookmarkedNotesData = []
        displayNotes([])
        return
      }

      // Simpan data ke variabel global
      allBookmarkedNotesData = data

      // Tampilkan data
      displayNotes(data)
    })
    .catch((error) => {
      console.error("Error fetching bookmarked notes:", error)
      showErrorMessage(`Failed to load bookmarked notes: ${error.message}`)
    })
}

// Fungsi untuk menampilkan loading indicator
function showLoadingIndicator() {
  const cardGrid = document.getElementById("card-grid")
  cardGrid.innerHTML = `
    <div class="col-span-full text-center py-8">
      <p class="text-gray-500">Loading saved notes...</p>
    </div>
  `
}

// Fungsi untuk menampilkan error message
function showErrorMessage(message) {
  const cardGrid = document.getElementById("card-grid")
  cardGrid.innerHTML = `
    <div class="col-span-full text-center py-8">
      <p class="text-red-500">${message}</p>
      <button onclick="loadBookmarkedNotes()" class="mt-4 px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600">
        Retry
      </button>
    </div>
  `
}

// Fungsi untuk menampilkan catatan
function displayNotes(data) {
  const cardGrid = document.getElementById("card-grid")

  if (!Array.isArray(data) || data.length === 0) {
    cardGrid.innerHTML = `
      <div class="col-span-full text-center py-8">
        <div class="bg-gray-50 border border-gray-200 rounded-lg p-8">
          <h3 class="text-xl font-semibold text-gray-900 mb-2">No Saved Notes</h3>
          <p class="text-gray-500 mb-4">You haven't saved any notes yet.</p>
          <a href="/" class="inline-flex items-center px-4 py-2 bg-blue-500 text-white rounded-lg hover:bg-blue-600 transition-colors">
            <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"></path>
            </svg>
            Explore Notes
          </a>
        </div>
      </div>
    `
    return
  }

  cardGrid.innerHTML = "" // Kosongkan grid

  data.forEach((item) => {
    const noteCard = document.createElement("a")
    noteCard.href = `/note/view/${item.id}`
    noteCard.className =
      "flex h-[250px] flex-col text-left bg-white px-5 rounded-lg shadow-md transition-all duration-200 hover:translate-y-[-4px] hover:shadow-lg"

    noteCard.innerHTML = `
      <h3 class="text-2xl m-0 pt-2">${item.name || "Untitled"}</h3>
      <p class="font-semibold text-base text-[#555]">${item.course || "Unknown Course"} - ${item.major || "Unknown Major"}</p>
      <span class="font-extralight text-sm text-[#555] mb-2.5 pt-2">${item.username || "Anonymous"}</span>
      <div class="h-full mt-2.5 pb-5 flex flex-col justify-end items-baseline text-sm text-[#444]">
        <div class="flex justify-center items-center">
          <img class="h-[15px] w-[15px] mr-1" src="/img/star.png" alt="Rating">
          <p>${item.rating || 0}/5.0</p>
        </div>
        <div class="flex justify-center items-center">
          <img class="h-[15px] w-[15px] mr-1" src="/img/view.png" alt="Views">
          <p>${item.views || 0} Views</p>
        </div>
      </div>
    `

    cardGrid.appendChild(noteCard)
  })
}

// Inisialisasi aplikasi ketika DOM sudah siap
document.addEventListener("DOMContentLoaded", initializeApp)

// Fallback jika DOMContentLoaded sudah terlewat
if (document.readyState === "loading") {
  document.addEventListener("DOMContentLoaded", initializeApp)
} else {
  initializeApp()
}
