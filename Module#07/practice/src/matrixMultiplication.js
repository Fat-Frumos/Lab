'use strict';

/**
 * You must create a function that multiplies two matricies (n x n each).
 *
 * @param {array} matrix1
 * @param {array} matrix2
 * @return {array}
 *
 */
function matrixMultiplication(matrix1, matrix2) {
  return matrix1.map((row, r) =>
    row.map((k, b) =>
      row.reduce((sum, k, a) => sum + matrix1[r][a] * matrix2[a][b], 0)
    )
  );
}

module.exports = matrixMultiplication;
