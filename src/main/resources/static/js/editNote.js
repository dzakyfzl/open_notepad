// mbil ID dari path URL
  noteId = window.noteID;
  console.log('Note ID:', noteId);
  // Panggil fungsi getNoteById dengan noteId

  // Data major untuk dropdown
  getNoteById(noteId);
  getFileById(noteId);
  document.getElementById('deleteNote').addEventListener('click', function() {
    // Gunakan noteId dari atas, tidak perlu ambil ulang
    if (!noteId || isNaN(noteId)) {
      alert('Note ID tidak ditemukan!');
      return;
    }
    if (confirm('Are you sure you want to delete this note?')) {
      fetch('/api/note/delete?noteID=' + noteID, {
        method: 'POST',
      })
      .then(response => {
        if (response.ok) {
          alert('Note deleted successfully!');
          window.location.href = '/user/notes';
        } else {
          alert('Failed to delete note.');
        }
      })
      .catch(error => {
        console.error('Error deleting note:', error);
        alert('An error occurred while deleting the note.');
      });
    }
  });
  /// Function to fetch note data by ID
  function getNoteById(noteId) {
      fetch(`/api/note/get?noteID=${encodeURIComponent(noteId)}`, {
        method: 'GET',
        credentials: 'include'
      })
      .then(response => {
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        return response.json();
      })
      .then(data => {
        document.getElementById('title').value = data.name || '';
        document.getElementById('description').value = data.description || '';
        // Pastikan major dan course tidak null/undefined
        const majorKey = reverseMajorData[data.major] || '';
        document.getElementById('major').value = majorKey;
        updateCourses(); // Update courses dropdown based on major
        setTimeout(() => {
          document.getElementById('course').value = data.course || '';
        }, 0);
        if (typeof data.visibility !== 'undefined') {
          document.getElementById('visibility').value = data.visibility ? '1' : '0';
        }
      })
      .catch(error => {
        console.error('Error fetching note:', error);
        alert('Failed to fetch note data.');
      });
  }
  /// Function to fetch file data by ID
  function getFileById(noteId) {
      fetch(`/api/note/getFileName?noteID=${encodeURIComponent(noteId)}`, {
        method: 'GET',
        credentials: 'include'
      })
      .then(response => {
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        return response.json();
      })
      .then(data => {
        const fileNameSpan = document.getElementById('file-name');
        fileNameSpan.textContent = data.fileName || 'No file chosen';
        // Set the file input value to the file name
        fileInput.value = data.fileName || '';
      })
  }
    
  const reverseMajorData = {
      'S1 Informatika': 'informatika',
      'S1 Data Sains': 'data-sains',
      'S1 Rekayasa Perangkat Lunak': 'rekayasa-perangkat-lunak',
      'S1 Teknologi Informasi': 'teknologi-informasi'
    };
   // Course data for dropdown
    const courseData = {
      "informatika": [
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
      ],
      "data-sains": [
        "Bahasa Indonesia",
        "Pancasila",
        "Pendidikan Karakter",
        "Statika",
        "Pengenalan Pemrograman",
        "Logika Matematika",
        "Kalkulus",
        "Aljabar Linear untuk Sains Data",
        "Agama",
        "Algoritma dan Pemrograman",
        "Organisasi dan Arsitektur Komputer",
        "Matematika Diskrit",
        "Teori Peluang dan Implementasi",
        "Perancangan dan Implementasi Basis Data",
        "Bahasa Inggris",
        "Struktur Data",
        "Analisi Kompleksitas Algoritma",
        "Sistem Manajemen Basis Data",
        "Pemodelan, Simulasi, dan Optimasi",
        "Infrastruktur dan Platform untuk Sains Data",
        "Kecerdasan Buatan",
        "Wawasan Global TIK",
        "Interaksi Manusia dan Komputer",
        "Sistem Operasi",
        "Analisa Data",
        "Perancangan Aplikasi Sains Data",
        "Metode Visualisasi Data",
        "Pembelajaran Mesin",
        "Manajemen Proyek",
        "Pemrograman Berorientasi Objek",
        "Keamanan Data",
        "Infrastruktur dan Teknologi Big Data",
        "Analisa Deret Waktu",
        "Rekayasa Sistem Informasi",
        "Sains Data untuk Masyarakat"
      ],
      "rekayasa-perangkat-lunak": [
        "Agama",
        "Pembentukan Karakter",
        "Kalkulus",
        "Logika Matematika",
        "Algoritma Pemrograman",
        "Manajemen Proses Bisnis",
        "Pengantar Rekayasa Perangkat Lunak",
        "Bahasa Inggris",
        "Statistika",
        "Matematika Diskrit",
        "Dasar Pemrograman Berorientasi Objek",
        "Organisasi dan Arsitektur Komputer",
        "Rekayasa Kebutuhan Perangkat Lunak",
        "Design Thinking",
        "Struktur Data",
        "Analisis Kompleksitas Algoritma",
        "Pemodelan Perangkat Lunak",
        "Proses Perangkat Lunak",
        "Keprofesian Rekayasa Perangkat Lunak",
        "Arsitektur dan Desain Perangkat Lunak",
        "Pancasila",
        "Interaksi Manusia dan Komputer",
        "Basis Data",
        "Dasar Jaringan Komputer",
        "Proyek Tingkat II",
        "Konstruksi Perangkat Lunak",
        "Kecerdasan Buatan",
        "Kewirausahaan",
        "Pemrograman Perangkat Bergerak",
        "Manajemen Proyek",
        "Pengalaman Pengguna (UX)",
        "Perancangan dan Pemrograman Web",
        "Wawasan Global TIK",
        "Penjaminan Mutu Perangkat Lunak"
      ],
      "teknologi-informasi": [
        "Logika Matematika",
        "Berpikir Komputasional & Pengenalan Pemrograman",
        "Kalkulus",
        "Pengantar Teknologi Informasi",
        "Pancasila",
        "Bahasa Indonesia",
        "Pendidikan Karakter",
        "Statistika dan Analitik Data",
        "Aljabar Linear dan Matriks",
        "Algoritma Pemrograman",
        "Matematika Diskrit",
        "Bahasa Inggris",
        "Pemeliharaan dan Adminstrasi Teknologi Informasi",
        "Agama",
        "Teori Peluang",
        "Struktur Data",
        "Sistem Multimedia",
        "Manajemen Layanan Teknologi Informasi",
        "Organisasi dan Arsitektur Komputer",
        "Keterampilan dan Profesionalisme Teknologi Informasi",
        "Wawasan Global TIK",
        "Jaringan Komputer",
        "Interaksi Manusia dan Komputer",
        "Kewirausahaan",
        "Sistem Operasi",
        "Sistem Cerdas",
        "Sistem Manajemen Basis Data",
        "Teknologi Informasi Untuk Masyarakat",
        "Keamanan Siber",
        "Pengalaman Pengguna",
        "Pemrograman Berorientasi Obyek",
        "Pemrograman Web",
        "Pemrograman Platform & IOT",
        "Rekayasa Perangkat Lunak"
      ]
    };
    const majorData = {
      'informatika':'S1 Informatika',
      'data-sains':'S1 Data Sains',
      'rekayasa-perangkat-lunak':'S1 Rekayasa Perangkat Lunak',
      'teknologi-informasi':'S1 Teknologi Informasi'
    }
    // Update courses dropdown based on selected major
    function updateCourses() {
      const major = document.getElementById('major').value;
      const courseSelect = document.getElementById('course');
      courseSelect.innerHTML = '<option value="" disabled selected>Select course</option>'; // Reset options
      if (major && courseData[major]) {
        courseData[major].forEach(course => {
          const option = document.createElement('option');
          option.value = course;
          option.textContent = course;
          courseSelect.appendChild(option);
        });
      }
    }
  
    // buat file tidak bisa diubah
    document.getElementById('noteFile').addEventListener('change', function(event) {
      const fileInput = event.target;
      const fileName = fileInput.files.length > 0 ? fileInput.files[0].name : 'No file chosen';
      document.getElementById('file-name').textContent = fileName;
    });
    
      // Handle form submission
      document.getElementById('uploadForm').addEventListener('submit', function(event) {
        event.preventDefault(); // Prevent default form submission
        const title = document.getElementById('title').value;
        const major = majorData[document.getElementById('major').value];
        const course = document.getElementById('course').value;
        const description = document.getElementById('description').value;
        const visibility = parseInt(document.getElementById('visibility').value, 10);
        if (!title || !major || !course || !description) {
          alert('Please fill in all fields and select a file.');
          return;
        }
        // Create FormData object to send data
        const formData = new FormData();
        formData.append('noteID', noteId); // Include note ID for update
        formData.append('name', title);
        formData.append('major', major);
        formData.append('course', course);
        formData.append('description', description);
        formData.append('visibility', visibility);
        fetch('/api/note/update', {
          method: 'POST',
          body: formData
        }
        ).then(response => {
          if (response.ok) {
            alert('Note updated successfully!');
            window.location.href = '/user/notes'; // Redirect to notes page
          } else {
            alert('Failed to update note.');
          }
        }).catch(error => {
          console.error('Error updating note:', error);
          alert('An error occurred while updating the note.');
        });
      });