import {
  Company,
  Employee,
  CompanyLocationArray,
  CompanyLocationLocalStorage,
} from "../EuropeCompany/app";

describe("Company", () => {
  const arrayLocation = new CompanyLocationArray();
  const localLocation = new CompanyLocationLocalStorage("company");

  const britishCompany = new Company(arrayLocation);
  const localBritishCompany = new Company(localLocation);

  const john = new Employee("John Smith", "Web Development");
  const emily = new Employee("Emily Johnson", "Mobile App Design");
  const michael = new Employee("Michael Williams", "Database Management");
  const sophia = new Employee("Sophia Brown", "Backend Development");

  britishCompany.addPerson(john);
  britishCompany.addPerson(emily);

  localBritishCompany.addPerson(emily);
  localBritishCompany.addPerson(michael);
  localBritishCompany.addPerson(sophia);

  it("should add employees to the company", () => {
    expect(britishCompany.getNameList()).toEqual([
      john.getName(),
      emily.getName(),
    ]);
    expect(localBritishCompany.getNameList()).toEqual([
      emily.getName(),
      michael.getName(),
      sophia.getName(),
    ]);
  });
});
