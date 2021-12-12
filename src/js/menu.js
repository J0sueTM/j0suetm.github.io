/*
 * file: src/js/menu.js
 * author: Josue Teodoro Moreira <teodoro.josue@protonmail.ch>
 * date: December 12, 2021
 */

import { isMobile } from './util.js'

function toggleMenu() {
  if (!isMobile()) {
    console.log('Could not toggle menu: not on mobile mode')

    return
  }

  const menu = document.getElementById('menu')
  if (!menu) {
    console.log('Could not toggle menu: could not retrieve element.')
    
    return
  }
}
