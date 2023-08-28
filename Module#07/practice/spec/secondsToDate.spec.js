const secondsToDate = require('../src/secondsToDate');

describe('secondsToDate', function () {
  it('Should return a date that comes after a certain number of seconds', function () {
    expect(secondsToDate(0)).toEqual(new Date('06/01/2020'));
    expect(secondsToDate(86400)).toEqual(new Date('06/02/2020'));
    expect(secondsToDate(31536000)).toEqual(new Date('06/01/2021'));
  });
});
