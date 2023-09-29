const matrixMultiplication = require('../src/matrixMultiplication');

describe('MatrixMultiplication', () => {
  it('Should multiply two matrices correctly', () => {
    const matrix1 = [
      [1, 2],
      [3, 4],
    ];

    const matrix2 = [
      [5, 6],
      [7, 8],
    ];

    const expected = [
      [19, 22],
      [43, 50],
    ];

    expect(matrixMultiplication(matrix1, matrix2)).toEqual(expected);
  });

  it('Should handle edge cases', () => {
    const matrix1 = [[1]];

    const matrix2 = [[2]];

    const expected = [[2]];

    expect(matrixMultiplication(matrix1, matrix2)).toEqual(expected);
  });

  it('Should multiply two matrices of size 2x2', () => {
    const matrix1 = [
      [1, 2],
      [3, 4],
    ];

    const matrix2 = [
      [5, 6],
      [7, 8],
    ];

    const expectedResult = [
      [19, 22],
      [43, 50],
    ];

    expect(matrixMultiplication(matrix1, matrix2)).toEqual(expectedResult);
  });

  it('Should multiply two matrices of size 3x3', () => {
    const matrix1 = [
      [1, 2, 3],
      [4, 5, 6],
      [7, 8, 9],
    ];

    const matrix2 = [
      [9, 8, 7],
      [6, 5, 4],
      [3, 2, 1],
    ];

    const expectedResult = [
      [30, 24, 18],
      [84, 69, 54],
      [138, 114, 90],
    ];

    expect(matrixMultiplication(matrix1, matrix2)).toEqual(expectedResult);
  });

  it('Should multiply two matrices of size 1x1', () => {
    const matrix1 = [[5]];
    const matrix2 = [[2]];
    const expectedResult = [[10]];
    expect(matrixMultiplication(matrix1, matrix2)).toEqual(expectedResult);
  });

  it('Should handle multiplying with an identity matrix', () => {
    const matrix1 = [
      [1, 2],
      [3, 4],
    ];

    const matrix2 = [
      [1, 0],
      [0, 1],
    ];

    expect(matrixMultiplication(matrix1, matrix2)).toEqual(matrix1);
  });

  it('Should handle multiplying with a zero matrix', () => {
    const matrix1 = [
      [1, 2],
      [3, 4],
    ];

    const matrix2 = [
      [0, 0],
      [0, 0],
    ];

    const expectedResult = [
      [0, 0],
      [0, 0],
    ];

    expect(matrixMultiplication(matrix1, matrix2)).toEqual(expectedResult);
  });

  it('Should multiply two matrices of size 10x10 with 0s and 1s', () => {
    const matrix1 = [];
    const matrix2 = [];
    const expectedResult = [];

    for (let i = 0; i < 10; i++) {
      const row1 = [];
      const row2 = [];
      const resultRow = [];

      for (let j = 0; j < 10; j++) {
        row1.push(i % 2 === 0 ? 0 : 1);
        row2.push(j % 2 === 0 ? 1 : 0);
        resultRow.push((i % 2 === 0 ? 0 : 1) * (j % 2 === 0 ? 1 : 0) * 10);
      }

      matrix1.push(row1);
      matrix2.push(row2);
      expectedResult.push(resultRow);
    }

    expect(matrixMultiplication(matrix1, matrix2)).toEqual(expectedResult);
  });

  it('Should multiply two matrices of size 10x10 with alternating odd and even numbers', () => {
    const matrix1 = [];
    const matrix2 = [];
    for (let i = 0; i < 10; i++) {
      const row1 = [];
      const row2 = [];
      for (let j = 0; j < 10; j++) {
        row1.push(i * 10 + j + 1);
        row2.push(100 - (i * 10 + j));
      }
      matrix1.push(row1);
      matrix2.push(row2);
    }

    const n = 10;
    const expectedResult = [];

    for (let i = 0; i < n; i++) {
      expectedResult[i] = [];
      for (let j = 0; j < n; j++) {
        let sum = 0;
        for (let k = 0; k < n; k++) {
          sum += matrix1[i][k] * matrix2[k][j];
        }
        expectedResult[i][j] = sum;
      }
    }
    expect(matrixMultiplication(matrix1, matrix2)).toEqual(expectedResult);
  });

  it('Should multiply two matrices of size 10x10 with alternating 0s and 1s based on odd/even numbers', () => {
    const matrix1 = [];
    const matrix2 = [];
    const expectedResult = [];
    let n = 100;

    for (let i = 0; i < n; i++) {
      const row1 = [];
      const row2 = [];
      const result = [];

      for (let j = 0; j < n; j++) {
        row1.push((i * 10 + j + 1) % 2 === 0 ? 0 : 1);
        row2.push((100 - (i * 10 + j)) % 2 === 0 ? 0 : 1);
        result.push(j % 2 == 0 ? 0 : Math.ceil(n / 2));
      }
      matrix1.push(row1);
      matrix2.push(row2);
      expectedResult.push(result);
    }
    expect(matrixMultiplication(matrix1, matrix2)).toEqual(expectedResult);
  });
});
