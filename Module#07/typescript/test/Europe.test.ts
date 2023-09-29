import {
  Company,
  Employee,
  CompanyLocationArray,
  CompanyLocationLocalStorage,
} from "../EuropeCompany/app";

describe("EuropeCompany", () => {
  let arrayLocation: CompanyLocationArray;
  let localLocation: CompanyLocationLocalStorage;
  let europe: Company;
  let company2: Company;

  beforeEach(() => {
    arrayLocation = new CompanyLocationArray();
    localLocation = new CompanyLocationLocalStorage("companyLocalStorage");
    europe = new Company(arrayLocation);
    company2 = new Company(localLocation);
  });

  const location = new CompanyLocationArray();
  const europeCompany = new Company(location);

  const gosling = new Employee("James Gosling", "Java");
  const berners = new Employee("Tim Berners-Lee", "HTML");
  const eich = new Employee("Brendan Eich", "JavaScript");
  const rossum = new Employee("Guido van Rossum", "Python");
  const stroustrup = new Employee("Bjarne Stroustrup", "C++");

  europeCompany.addPerson(eich);
  europeCompany.addPerson(rossum);
  europeCompany.addPerson(berners);
  europeCompany.addPerson(gosling);
  europeCompany.addPerson(stroustrup);

  test("Getting project and name lists", () => {
    const employee1 = new Employee("John", "Project A");
    const employee2 = new Employee("Emily", "Project B");

    europe.addPerson(employee1);
    europe.addPerson(employee2);
    // company2.addPerson(employee3);

    expect(europe.getProjectList()).toEqual(["Project A", "Project B"]);
    expect(europe.getNameList()).toEqual(["John", "Emily"]);
    expect(europeCompany.getProjectList()).toEqual([
      "JavaScript",
      "Python",
      "HTML",
      "Java",
      "C++",
    ]);
  });

  it("Should add employees to the company", () => {
    expect(europeCompany.getNameList()).toEqual([
      "Brendan Eich",
      "Guido van Rossum",
      "Tim Berners-Lee",
      "James Gosling",
      "Bjarne Stroustrup",
    ]);
  });
});
