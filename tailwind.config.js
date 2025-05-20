/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["./src/main/resources/templates/*.html"],
  theme: {
    extend: {
      colors: {
        ijomuda: '#d9f1e2',
        ijomudagelap: '#cae3d3',
      },
      fontFamily: {
        inter: ["Inter", "sans-serif"],
      },
    },
  },
  plugins: [],
}

