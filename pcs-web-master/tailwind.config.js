/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ['./index.html', './src/**/*.{vue,js,ts,jsx,tsx}'],
  theme: {
    extend: {
      colors: {
        'pcs-primary': '#086b45',
        'pcs-text-primary': '#f5ffff',
        'pcs-second': '#086C46',
        'pcs-species': '#f0f6f4',
        'pcs-border': 'rgba(8, 108, 70, 0.30)',
      },
      scroll: {
        'scroll-hidden': {
          /* Hide the scroll bar but still allow scrolling */
          '-webkit-scrollbar': 'display: none;',
          '-ms-overflow-style': 'none;',
          'scrollbar-width': 'none;',
          overflow: '-moz-scrollbars-none;',
        },
      },
    },
  },
  daisyui: {
    themes: [
      'light',
      'dark',
      'cupcake',
      'bumblebee',
      'emerald',
      'corporate',
      'synthwave',
      'retro',
      'cyberpunk',
      'valentine',
      'halloween',
      'garden',
      'forest',
      'aqua',
      'lofi',
      'pastel',
      'fantasy',
      'wireframe',
      'black',
      'luxury',
      'dracula',
      'cmyk',
      'autumn',
      'business',
      'acid',
      'lemonade',
      'night',
      'coffee',
      'winter',
      'dim',
      'nord',
      'sunset',
    ],
  },
  plugins: [require('daisyui')],
}