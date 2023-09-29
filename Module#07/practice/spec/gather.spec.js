const gather = require('../src/gather');

describe('gather', () => {
  it('Should gather and order strings correctly', () => {
    expect(gather('a')('b')('c').order(0)(1)(2).get()).toBe('abc');
    expect(gather('a')('b')('c').order(2)(1)(0).get()).toBe('cba');
    expect(gather('e')('l')('o')('l')('!')('h').order(5)(0)(1)(3)(2)(4).get()).toBe('hello!');
  });

  it('Should handle empty input', () => {
    expect(gather().order().get()).toBe('');
  });

  it('Should handle out of order indices', () => {
    expect(gather('a')('b')('c').order(1)(0)(2).get()).toBe('bac');
    expect(gather('x')('y')('z').order(2)(0)(1).get()).toBe('zxy');
  });

  it('Should handle duplicate indices', () => {
    expect(gather('a')('b')('c').order(0)(1)(0).get()).toBe('aba');
    expect(gather('x')('y')('z').order(2)(2)(2).get()).toBe('zzz');
  });

  it('Should handle empty gather calls', () => {
    const result = gather().order(0)(1)(2).get();
    expect(result).toBe('');
  });

  it('Should handle mixed gather and order calls', () => {
    const result3 = gather('1')('2').order(0)(0).get();
    expect(result3).toBe('11');

    const result4 = gather('p')('q').order(1)(1).get();
    expect(result4).toBe('qq');
  });

  it('Should handle repeated order calls', () => {
    const result1 = gather('a')('b').order(0)(0)(1).get();
    expect(result1).toBe('aab');

    const result2 = gather('a')('b').order(1)(0)(0).get();
    expect(result2).toBe('baa');

    const result3 = gather('1')('2').order(0)(1)(1).get();
    expect(result3).toBe('122');

    const result4 = gather('p')('q').order(1)(0)(1).get();
    expect(result4).toBe('qpq');
  });
});
