'use strict';

/**
 * You must create a function that returns a base 2 (binary) representation of a base 10 (decimal) string number
 * ! Numbers will always be below 1024 (not including 1024)
 * ! You are not able to use parseInt
 * @param {number} decimal
 * @return {string}
 *
 * @example
 *      5 -> '101'
 *      10 -> '1010'
 *
 */
function toBase2Converter(decimal) {
  const max = 1024;
  if (decimal < 0 || decimal >= max || isNaN(decimal)) {
    return '';
  }
  return (decimal >>> 0).toString(2);
}
module.exports = toBase2Converter;
