const towerHanoi = require('../src/towerHanoi');

describe('towerHanoi', () => {

  it('Should solve Tower of Hanoi puzzle for negative number of disk', () => {
    expect(towerHanoi(-1)).toBe(-1);
  });

  it('Should solve Tower of Hanoi puzzle for 0 disk', () => {
    expect(towerHanoi(0)).toBe(-1);
  });

  it('Should solve Tower of Hanoi puzzle for 1 disk', () => {
    expect(towerHanoi(1)).toBe(1);
  });

  it('Should solve Tower of Hanoi puzzle for 2 disks', () => {
    expect(towerHanoi(2)).toBe(3);
  });

  it('Should solve Tower of Hanoi puzzle for 3 disks', () => {
    expect(towerHanoi(3)).toBe(7);
  });

  it('Should solve Tower of Hanoi puzzle for 4 disks', () => {
    expect(towerHanoi(4)).toBe(15);
  });

  it('Should solve Tower of Hanoi puzzle for 5 disks', () => {
    expect(towerHanoi(5)).toBe(31);
  });
});
