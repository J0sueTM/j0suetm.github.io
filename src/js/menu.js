/*
 * file: src/js/menu.js
 * author: Josue Teodoro Moreira <teodoro.josue@protonmail.ch>
 * date: December 12, 2021
 */

var isMenuHidden = true
function toggleMenu() {
  if (window.innerWidth >= 1024)
    return
  
  const header = document.getElementById('header')
  const menu = document.getElementById('menu')
  if (!header || !menu) {
    console.log('Could not toggle menu: could not retrieve elements.')
    
    return
  }

  if (isMenuHidden) {
    header.classList.remove('h-16')
    header.classList.add('h-initial')

    menu.style.visibility = 'visible'
  } else {
    menu.style.visibility = 'hidden'

    header.classList.remove('h-initial')
    header.classList.add('h-16')
  }


  isMenuHidden = !isMenuHidden
}
