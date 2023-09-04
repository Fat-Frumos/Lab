import { Company, Employee, CompanyLocationArray, CompanyLocationLocalStorage } from '../EuropeCompany/app';

const arrayLocation = new CompanyLocationArray();
const localLocation = new CompanyLocationLocalStorage('company');

const britishCompany = new Company(arrayLocation);
const localBritishCompany = new Company(localLocation);

const john = new Employee('John Smith', 'Web Development');
const emily = new Employee('Emily Johnson', 'Mobile App Design');
const michael = new Employee('Michael Williams', 'Database Management');
const sophia = new Employee('Sophia Brown', 'Backend Development');

britishCompany.addPerson(john);
britishCompany.addPerson(emily);

localBritishCompany.addPerson(emily);
localBritishCompany.addPerson(michael);
localBritishCompany.addPerson(sophia);

console.log('British Company Project List:', britishCompany.getProjectList());
console.log('British Company Name List:', britishCompany.getNameList());
console.log('British Company local storage Project List:', localBritishCompany.getProjectList());
console.log('British Company local storage Name List:', localBritishCompany.getNameList());
