'use strict';

/**
 * You must create a function that takes two strings as arguments and returns the number of times the first string
 * is found in the text.
 * @param {string} substring
 * @param {string} text
 * @return {number}
 *
 * @example
 *      'a', 'test it' -> 0
 *      't', 'test it' -> 3
 *      'T', 'test it' -> 3
 */
function substringOccurrencesCounter(substring, text) {
  return substring.length === 0 
  ? 0 : text.toLowerCase()
            .split(substring.toLowerCase())
            .length - 1;
}

module.exports = substringOccurrencesCounter;
