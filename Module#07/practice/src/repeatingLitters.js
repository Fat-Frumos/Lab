'use strict';

/**
 * You must create a function that takes a string and returns a string in which each character is repeated once.
 *
 * @param {string} string
 * @return {string}
 *
 * @example
 *      'Hello' -> 'HHeelloo'
 *      'Hello world' -> 'HHeello  wworrldd' // o, l is repeated more then once. Space was also repeated
 */

function repeatingLitters(string) {
  const arr = string.split('');
  let unique = new Set(arr);
  let result = '';

  for (let i = 0; i < arr.length; i++) {
    if (i === arr.length - 1 || arr[i] !== arr[i + 1]) {
      if (unique.has(arr[i])) {
        result += arr[i] + arr[i];
        unique.delete(arr[i]);
      } else {
        result += arr[i];
      }
    } else if (unique.has(arr[i])) {
      unique.delete(arr[i]);
      result += arr[i];
    }
  }
  return result;
}

module.exports = repeatingLitters;