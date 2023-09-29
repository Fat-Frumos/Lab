const redundant = require('../src/redundant');

describe('redundant', () => {
  it('Should return a function that returns the input string', () => {
    const f1 = redundant('apple');
    expect(f1()).toBe('apple');

    const f2 = redundant('pear');
    expect(f2()).toBe('pear');

    const f3 = redundant('');
    expect(f3()).toBe('');
  });

  it('Should handle different input strings', () => {
    const f4 = redundant('banana');
    expect(f4()).toBe('banana');

    const f5 = redundant('123');
    expect(f5()).toBe('123');
  });

  it('Should handle special characters', () => {
    const f6 = redundant('!@#');
    expect(f6()).toBe('!@#');

    const f7 = redundant('$%^&*');
    expect(f7()).toBe('$%^&*');
  });
});
