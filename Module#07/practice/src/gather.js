"use strict";

/**
 * Create a gather function that accepts a string argument and returns another function.
 * The function calls should support continued chaining until order is called.
 * order should accept a number as an argument and return another function.
 * The function calls should support continued chaining until get is called.
 * get should return all of the arguments provided to the gather functions as a string in the order specified in the order functions.
 *
 * @param {string} str
 * @return {string}
 *
 * @example
 *      gather('a')('b')('c').order(0)(1)(2).get() ➞ 'abc'
 *      gather('a')('b')('c').order(2)(1)(0).get() ➞ 'cba'
 *      gather('e')('l')('o')('l')('!')('h').order(5)(0)(1)(3)(2)(4).get()  ➞ 'hello'
 */

function gather(str) {
  const arr = [str];
  const ordered = [];

  const build = (str) => {
    arr.push(str);
    return build;
  };

  build.order = (...order) => {
    ordered.push(...order);
    return build.order;
  };

  build.order.get = () => ordered.map((order) => arr[order]).join('');

  return build;
}

module.exports = gather;
