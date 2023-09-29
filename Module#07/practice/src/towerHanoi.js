'use strict';

/**
 * https://en.wikipedia.org/wiki/Tower_of_Hanoi
 *
 * @param {number} disks
 * @return {number}
 */
function towerHanoi(disks) {
  if (disks < 1) {
    return -1;
  }
  return disks === 1 ? 1 : 2 * towerHanoi(disks - 1) + 1;
}

module.exports = towerHanoi;
