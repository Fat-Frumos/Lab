import { Company, CompanyLocationArray, IEmployee } from "../EuropeCompany/app";

class FrontEnd implements IEmployee {

  constructor(private name: string, private currentProject: string) {}
  
  getCurrentProject(): string {
    return this.currentProject;
  }
  getName(): string {
    return this.name;
  }
}
class BackEnd implements IEmployee {

  constructor(private name: string, private currentProject: string) {}
  
  getCurrentProject(): string {
    return this.currentProject;
  }
  getName(): string {
    return this.name;
  }
}

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

export { FrontEnd, BackEnd };
