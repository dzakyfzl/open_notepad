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
        document.getElementById('uploadForm').addEventListener('submit', async function (e) {
        e.preventDefault();

        const title = document.getElementById('title').value;
        const description = document.getElementById('description').value;
        const major = document.getElementById('major').value;
        const course = document.getElementById('course').value;
        const fileInput = document.getElementById('noteFile');
        const file = fileInput.files[0];

        if (!file) {
          alert('Please select a file to upload.');
          return;
        }

        const formData = new FormData();
        formData.append('title', title);
        formData.append('description', description);
        formData.append('major', major);
        formData.append('course', course);
        formData.append('file', file);

        try {
          const response = await fetch('/api/notes/upload', {
            method: 'POST',
            body: formData
          });

          if (response.ok) {
            const result = await response.text();
            alert(result);
          } else {
            const errorText = await response.text();
            alert('Upload failed: ' + errorText);
          }
        } catch (error) {
          console.error('Error uploading note:', error);
          alert('An error occurred while uploading the note.');
        }
      });