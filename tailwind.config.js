/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["./src/main/resources/templates/*.html"],
  theme: {
    extend: {
      colors: {
        ijomuda: '#d9f1e2',
        ijomudagelap: '#cae3d3',
        ijotua: '#599E87',
        primary: '#599E87',
        'primary-hover': '#64C0A2',
        'navbar-bg': '#d9f1e2'
      },
      fontFamily: {
        inter: ["Inter", "sans-serif"],
      },
    },
  },
  plugins: [],
}

