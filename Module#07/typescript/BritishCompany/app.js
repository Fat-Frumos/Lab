"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var app_1 = require("../EuropeCompany/app");
var arrayLocation = new app_1.CompanyLocationArray();
var localLocation = new app_1.CompanyLocationLocalStorage('company');
var britishCompany = new app_1.Company(arrayLocation);
var localBritishCompany = new app_1.Company(localLocation);
var john = new app_1.Employee('John Smith', 'Web Development');
var emily = new app_1.Employee('Emily Johnson', 'Mobile App Design');
var michael = new app_1.Employee('Michael Williams', 'Database Management');
var sophia = new app_1.Employee('Sophia Brown', 'Backend Development');
britishCompany.addPerson(john);
britishCompany.addPerson(emily);
localBritishCompany.addPerson(emily);
localBritishCompany.addPerson(michael);
localBritishCompany.addPerson(sophia);
console.log('British Company Project List:', britishCompany.getProjectList());
console.log('British Company Name List:', britishCompany.getNameList());
console.log('British Company local storage Project List:', localBritishCompany.getProjectList());
console.log('British Company local storage Name List:', localBritishCompany.getNameList());