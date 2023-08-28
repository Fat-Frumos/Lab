const substringOccurrencesCounter = require('../src/substringOccurrencesCounter');

describe('substringOccurrencesCounter', () => {
  it('Should count occurrences of a substring in a text', () => {
    expect(substringOccurrencesCounter('a', 'test it')).toBe(0);
    expect(substringOccurrencesCounter('t', 'test it')).toBe(3);
    expect(substringOccurrencesCounter('T', 'test it')).toBe(3);
    expect(substringOccurrencesCounter('e', 'example text with extra elements')).toBe(7);
    expect(substringOccurrencesCounter('fgh', 'fghfghfgh')).toBe(3);
    expect(substringOccurrencesCounter('fgh', 'ghffghghf')).toBe(1);
    expect(substringOccurrencesCounter('ab', 'abba')).toBe(1);
  });

  it('Should handle edge cases', () => {
    expect(substringOccurrencesCounter('', 'test it')).toBe(0);
    expect(substringOccurrencesCounter('a', '')).toBe(0);
    expect(substringOccurrencesCounter('', '')).toBe(0);
    expect(substringOccurrencesCounter('a', 'a')).toBe(1);
    expect(substringOccurrencesCounter('a', 'aaaa')).toBe(4);
  });
});
