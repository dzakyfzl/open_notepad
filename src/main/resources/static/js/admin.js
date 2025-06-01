// Fungsi untuk inisialisasi aplikasi
function initializeApp() {
  console.log("Initializing app...")

  // Load data awal
  loadNotes()
}

// Fungsi untuk memuat semua catatan
function loadNotes() {
  console.log("Memuat semua catatan...")
  const cardGrid = document.getElementById("card-grid")

  // Tampilkan loading indicator
  showLoadingIndicator()

  fetch("/api/data/getAllnotes", {
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
      console.log("Data diterima dari getAllnotes:", data)

      // Simpan data ke variabel global
      allNotesData = data
      currentDisplayData = data

      // Tampilkan data
      displayNotes(data)
    })
    .catch((error) => {
      console.error("Error fetching notes:", error)
      showErrorMessage(`Failed to load notes: ${error.message}`)
    })
}

// Fungsi untuk menampilkan loading indicator
function showLoadingIndicator() {
  const cardGrid = document.getElementById("card-grid")
  cardGrid.innerHTML = `
    <div class="col-span-full text-center py-8">
      <p class="text-gray-500">Loading notes...</p>
    </div>
  `
}

// Fungsi untuk menampilkan error message
function showErrorMessage(message) {
  const cardGrid = document.getElementById("card-grid")
  cardGrid.innerHTML = `
    <div class="col-span-full text-center py-8">
      <p class="text-red-500">${message}</p>
      <button onclick="loadNotes()" class="mt-4 px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600">
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
        <p class="text-gray-500">No notes available</p>
      </div>
    `
    return
  }

  cardGrid.innerHTML = "" // Kosongkan grid

  data.forEach((item) => {
    const noteCard = document.createElement("a")
    noteCard.href = `admin/note/${item.id}`
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
