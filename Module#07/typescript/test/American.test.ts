import { Company, CompanyLocationArray } from "../EuropeCompany/app";
import { FrontEnd, BackEnd } from "../AmericanCompany/app";

describe("AmericanCompany", () => {
  test("Project and Name Lists", () => {
    const americanCompany = new Company(new CompanyLocationArray());

    const alice = new FrontEnd("Alice Smith", "E-commerce Website");
    const bob = new FrontEnd("Bob Johnson", "Dashboard Redesign");
    const charlie = new BackEnd("Charlie Brown", "API Development");
    const diana = new BackEnd("Diana Wilson", "Database Optimization");

    americanCompany.addPerson(alice);
    americanCompany.addPerson(bob);
    americanCompany.addPerson(charlie);
    americanCompany.addPerson(diana);

    console.log("Project List:", americanCompany.getProjectList());
    console.log("Name List:", americanCompany.getNameList());

    const projectList = americanCompany.getProjectList();
    const nameList = americanCompany.getNameList();

    expect(projectList).toEqual([
      "E-commerce Website",
      "Dashboard Redesign",
      "API Development",
      "Database Optimization",
    ]);

    expect(nameList).toEqual([
      "Alice Smith",
      "Bob Johnson",
      "Charlie Brown",
      "Diana Wilson",
    ]);
  });
});
