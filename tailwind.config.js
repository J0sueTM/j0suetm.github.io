/** @type {import('tailwindcss').Config} */
export default {
  content: ['./src/**/*.{html,js,svelte,ts}'],
  theme: {
    extend: {
      aspectRatio: {
        'gr-v': '1 / 1.618',
        'gr-h': '1.618 / 1',
      }
    },
    colors: {
      dark: '#222323',
      light: '#f0f6f0'
    },
    goldenRatio: {
      prefix: true,
    }
  },
  plugins: [
    require('tailwindcss-golden-ratio'), 
  ],
}