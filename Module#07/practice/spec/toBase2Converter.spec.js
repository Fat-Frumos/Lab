const toBase2Converter = require('../src/toBase2Converter');

describe('toBase2Converter', () => {
  it('Should return binary representation of decimal numbers', () => {
    expect(toBase2Converter(0)).toBe('0');
    expect(toBase2Converter(1)).toBe('1');
    expect(toBase2Converter(5)).toBe('101');
    expect(toBase2Converter(10)).toBe('1010');
    expect(toBase2Converter(15)).toBe('1111');
    expect(toBase2Converter(31)).toBe('11111');
    expect(toBase2Converter(1023)).toBe('1111111111');
  });

  it('Should return an empty string for invalid input', () => {
    expect(toBase2Converter(-1)).toBe('');
    expect(toBase2Converter(1024)).toBe('');
    expect(toBase2Converter(NaN)).toBe('');
  });
});
