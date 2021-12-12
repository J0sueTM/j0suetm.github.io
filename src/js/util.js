/*
 * file: src/js/util.js
 * author: Josue Teodoro Moreira <teodoro.josue@protonmail.ch>
 * date: December 12, 2021
 */

DESKTOP_BREAKPOINT = 1024

function isMobile() {
  return (window.innerHeight < DESKTOP_BREAKPOINT)
}
