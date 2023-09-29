const repeatingLitters = require('../src/repeatingLitters');

describe('repeatingLitters', () => {
  it('Should repeat each character in the string once', () => {
    expect(repeatingLitters('Hello')).toBe('HHeelloo');
    expect(repeatingLitters('Hello world')).toBe('HHeelloo  wworrldd');
    expect(repeatingLitters('a b')).toBe('aa  bb');
    expect(repeatingLitters('a')).toBe('aa');
    expect(repeatingLitters('Test case')).toBe('TTeesstt  ccaase');
    expect(repeatingLitters('12345678900')).toBe('11223344556677889900');
    expect(repeatingLitters('AbCdEf')).toBe('AAbbCCddEEff');
    expect(repeatingLitters('!@#$%^&*()')).toBe('!!@@##$$%%^^&&**(())');
  });

  it('Should handle empty string', () => {
    expect(repeatingLitters('')).toBe('');
  });

  it('should handle characters with different cases', () => {
    expect(repeatingLitters('Hello World')).toBe('HHeelloo  WWorrldd');
    expect(repeatingLitters('eLemEnT')).toBe('eeLLemmEEnnTT');
  });
});
