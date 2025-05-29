// Daftar semua mata kuliah
const allCourses = [
  "Agama",
  "Algoritma dan Pemrograman 1",
  "Kalkulus",
  "Logika Matematika",
  "Matematika Diskrit",
  "Pancasila",
  "Pendidikan Karakter",
  "Statistika",
  "Algoritma dan Pemrograman 2",
  "Bahasa Inggris",
  "Etika dalam AI",
  "Kalkulus Lanjut",
  "Matriks dan Ruang Vektor",
  "Organisasi dan Arsitektur Komputer",
  "Pemodelan Basis Data",
  "Analisis dan Perancangan PL",
  "Analisis Kompleksitas Algoritma",
  "Sistem Basis Data",
  "Sistem Operasi",
  "Struktur Data",
  "Teori Bahasa dan Automata",
  "Teori Peluang",
  "Dasar Kecerdasan Artifisial",
  "Interaksi Manusia dan Komputer",
  "Jaringan Komputer",
  "Pemrograman Berorientasi Objek",
  "Strategi Algoritma",
  "Wawasan Global TIK",
  "Sosio-Informatik dan Keprofesian",
  "Implementasi dan Pengujian PL",
  "Keamanan Siber",
  "Kecerdasan Artifisial",
  "Komputasi Awan dan Terdistribusi",
  "Manajemen Projek TIK",
  "Bahasa Indonesia",
  "Statika",
  "Pengenalan Pemrograman",
  "Aljabar Linear untuk Sains Data",
  "Algoritma dan Pemrograman",
  "Teori Peluang dan Implementasi",
  "Perancangan dan Implementasi Basis Data",
  "Analisi Kompleksitas Algoritma",
  "Sistem Manajemen Basis Data",
  "Pemodelan, Simulasi, dan Optimasi",
  "Infrastruktur dan Platform untuk Sains Data",
  "Kecerdasan Buatan",
  "Analisa Data",
  "Perancangan Aplikasi Sains Data",
  "Metode Visualisasi Data",
  "Pembelajaran Mesin",
  "Manajemen Proyek",
  "Keamanan Data",
  "Infrastruktur dan Teknologi Big Data",
  "Analisa Deret Waktu",
  "Rekayasa Sistem Informasi",
  "Sains Data untuk Masyarakat",
  "Pembentukan Karakter",
  "Algoritma Pemrograman",
  "Manajemen Proses Bisnis",
  "Pengantar Rekayasa Perangkat Lunak",
  "Dasar Pemrograman Berorientasi Objek",
  "Rekayasa Kebutuhan Perangkat Lunak",
  "Design Thinking",
  "Pemodelan Perangkat Lunak",
  "Proses Perangkat Lunak",
  "Keprofesian Rekayasa Perangkat Lunak",
  "Arsitektur dan Desain Perangkat Lunak",
  "Basis Data",
  "Dasar Jaringan Komputer",
  "Proyek Tingkat II",
  "Konstruksi Perangkat Lunak",
  "Kewirausahaan",
  "Pemrograman Perangkat Bergerak",
  "Pengalaman Pengguna (UX)",
  "Perancangan dan Pemrograman Web",
  "Penjaminan Mutu Perangkat Lunak",
  "Berpikir Komputasional & Pengenalan Pemrograman",
  "Pengantar Teknologi Informasi",
  "Statistika dan Analitik Data",
  "Aljabar Linear dan Matriks",
  "Pemeliharaan dan Adminstrasi Teknologi Informasi",
  "Sistem Multimedia",
  "Manajemen Layanan Teknologi Informasi",
  "Keterampilan dan Profesionalisme Teknologi Informasi",
  "Sistem Cerdas",
  "Teknologi Informasi Untuk Masyarakat",
  "Pengalaman Pengguna",
  "Pemrograman Web",
  "Pemrograman Platform & IOT",
  "Rekayasa Perangkat Lunak"
];

// Ambil elemen HTML berdasarkan ID
const informatika = document.getElementById("informatika");
const dataSains = document.getElementById("data-sains");
const rekayasaPerangkatLunak = document.getElementById("rekayasa-perangkat-lunak");
const teknologiInformasi = document.getElementById("teknologi-informasi");
const courseSelect = document.getElementById("course");
const cardGrid = document.getElementById("card-grid");

// Tambahkan daftar mata kuliah ke dropdown
allCourses.forEach(course => {
  const option = document.createElement("option");
  option.value = course;
  option.textContent = course;
  courseSelect.appendChild(option);
});

// Fungsi untuk memuat semua catatan
function loadNotes() {
  fetch("/api/data/getAllnotes", {
    method: "GET",
    headers: {
      "Content-Type": "application/json"
    }
  })
    .then(response => {
      if (!response.ok) {
        throw new Error("Network response was not ok");
      }
      return response.json();
    })
    .then(data => {
      cardGrid.innerHTML = ""; // Kosongkan grid sebelum menambahkan catatan
      data.forEach(item => {
        cardGrid.innerHTML += `
          <a href="/note/view/${item.id}" class="flex h-[250px] flex-col text-left bg-white px-5 rounded-lg shadow-md transition-all duration-200 hover:translate-y-[-4px] hover:shadow-lg">
            <h3 class="text-2xl m-0 pt-2">${item.name}</h3>
            <p class="font-semibold text-base text-[#555]">${item.course} - ${item.major}</p>
            <span class="font-extralight text-sm text-[#555] mb-2.5 pt-2">${item.username}</span>
            <div class="h-full mt-2.5 pb-5 flex flex-col justify-end items-baseline text-sm text-[#444]">
              <div class="flex justify-center items-center"><img class="h-[15px] w-[15px] mr-1" src="/img/star.png"><p>${item.rating}/5.0</p></div>
              <div class="flex justify-center items-center"><img class="h-[15px] w-[15px] mr-1" src="/img/view.png"><p>${item.views} Views</p></div>
            </div>
          </a>
        `;
      });
    })
    .catch(error => console.error("Error fetching notes:", error));
}

// Fungsi untuk menyortir catatan
function sortNotes() {
  const sortBy = document.getElementById("sort-by").value;
  const sortOrder = document.getElementById("sort-order").value;
  const course = document.getElementById("course").value;

  const isInformatikaChecked = informatika.checked;
  const isDataSainsChecked = dataSains.checked;
  const isRekayasaPerangkatLunakChecked = rekayasaPerangkatLunak.checked;
  const isTeknologiInformasiChecked = teknologiInformasi.checked;

  // Buat query string untuk parameter
  const queryParams = new URLSearchParams({
    sortBy: sortBy,
    order: sortOrder,
    course: course,
    IF: isInformatikaChecked,
    DS: isDataSainsChecked,
    RPL: isRekayasaPerangkatLunakChecked,
    IT: isTeknologiInformasiChecked
  });

  fetch(`/api/data/filterNotes?${queryParams.toString()}`, {
    method: "GET",
    headers: {
      "Content-Type": "application/json"
    }
  })
    .then(response => {
      if (!response.ok) {
        throw new Error("Network response was not ok");
      }
      return response.json();
    })
    .then(data => {
      cardGrid.innerHTML = ""; // Kosongkan grid sebelum menambahkan catatan
      data.forEach(item => {
        cardGrid.innerHTML += `
          <a href="/note/view/${item.id}" class="flex h-[250px] flex-col text-left bg-white px-5 rounded-lg shadow-md transition-all duration-200 hover:translate-y-[-4px] hover:shadow-lg">
            <h3 class="text-2xl m-0 pt-2">${item.name}</h3>
            <p class="font-semibold text-base text-[#555]">${item.course} - ${item.major}</p>
            <span class="font-extralight text-sm text-[#555] mb-2.5 pt-2">${item.username}</span>
            <div class="h-full mt-2.5 pb-5 flex flex-col justify-end items-baseline text-sm text-[#444]">
              <div class="flex justify-center items-center"><img class="h-[15px] w-[15px] mr-1" src="/img/star.png"><p>${item.rating}/5.0</p></div>
              <div class="flex justify-center items-center"><img class="h-[15px] w-[15px] mr-1" src="/img/view.png"><p>${item.views} Views</p></div>
            </div>
          </a>
        `;
      });
    })
    .catch(error => console.error("Error fetching sorted notes:", error));
}

// Fungsi untuk mencari catatan berdasarkan nama
async function searchByName() {
  const searchTerm = document.getElementById("search-input").value.trim();
  if (!searchTerm) return loadNotes();

  const grid = document.getElementById("notes-grid");
  grid.innerHTML = '<div class="loading">Searching...</div>';

  try {
    const response = await fetch(`/api/data/search?name=${encodeURIComponent(searchTerm)}`);
    const results = await response.json();

    grid.innerHTML = "";
    results.forEach(note => {
      grid.innerHTML += `
        <a href="/note/view/${note.id}" class="flex h-[250px] flex-col text-left bg-white px-5 rounded-lg shadow-md transition-all duration-200 hover:translate-y-[-4px] hover:shadow-lg">
          <h3 class="text-2xl m-0 pt-2">${note.name}</h3>
          <p class="font-semibold text-base text-[#555]">${note.course} - ${note.major}</p>
          <span class="font-extralight text-sm text-[#555] mb-2.5 pt-2">${note.username}</span>
          <div class="h-full mt-2.5 pb-5 flex flex-col justify-end items-baseline text-sm text-[#444]">
            <div class="flex justify-center items-center"><img class="h-[15px] w-[15px] mr-1" src="/img/star.png"><p>${note.rating}/5.0</p></div>
            <div class="flex justify-center items-center"><img class="h-[15px] w-[15px] mr-1" src="/img/view.png"><p>${note.views} Views</p></div>
          </div>
        </a>
      `;
    });
  } catch (error) {
    console.error("Search failed:", error);
    grid.innerHTML = `<div class="error">Search failed: ${error.message}</div>`;
  }
}

// Panggil fungsi untuk memuat semua catatan saat halaman dimuat
loadNotes();