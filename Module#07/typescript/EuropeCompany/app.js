"use strict";
var __extends = (this && this.__extends) || (function () {
    var extendStatics = function (d, b) {
        extendStatics = Object.setPrototypeOf ||
            ({ __proto__: [] } instanceof Array && function (d, b) { d.__proto__ = b; }) ||
            function (d, b) { for (var p in b) if (Object.prototype.hasOwnProperty.call(b, p)) d[p] = b[p]; };
        return extendStatics(d, b);
    };
    return function (d, b) {
        if (typeof b !== "function" && b !== null)
            throw new TypeError("Class extends value " + String(b) + " is not a constructor or null");
        extendStatics(d, b);
        function __() { this.constructor = d; }
        d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
    };
})();
Object.defineProperty(exports, "__esModule", { value: true });
exports.CompanyLocationLocalStorage = exports.CompanyLocationArray = exports.Employee = exports.Company = void 0;
var node_localstorage_1 = require("node-localstorage");
var localStorage = new node_localstorage_1.LocalStorage('./scratch');
var CompanyLocationLocalStorage = /** @class */ (function () {
    function CompanyLocationLocalStorage(key) {
        this.key = key;
        this.storage = this.getLocalStorage();
    }
    CompanyLocationLocalStorage.prototype.getLocalStorage = function () {
        var data = localStorage.getItem(this.key);
        return data ? JSON.parse(data) : [];
    };
    CompanyLocationLocalStorage.prototype.setLocalStorage = function (storage) {
        localStorage.setItem(this.key, JSON.stringify(storage));
    };
    CompanyLocationLocalStorage.prototype.addPerson = function (person) {
        this.storage.push(person);
        this.setLocalStorage(this.storage);
    };
    CompanyLocationLocalStorage.prototype.getPerson = function (index) {
        return this.storage[index];
    };
    CompanyLocationLocalStorage.prototype.getCount = function () {
        return this.storage.length;
    };
    return CompanyLocationLocalStorage;
}());
exports.CompanyLocationLocalStorage = CompanyLocationLocalStorage;
var CompanyLocationArray = /** @class */ (function () {
    function CompanyLocationArray() {
        this.persons = [];
    }
    CompanyLocationArray.prototype.addPerson = function (person) {
        this.persons.push(person);
    };
    CompanyLocationArray.prototype.getPerson = function (index) {
        return this.persons[index];
    };
    CompanyLocationArray.prototype.getCount = function () {
        return this.persons.length;
    };
    return CompanyLocationArray;
}());
exports.CompanyLocationArray = CompanyLocationArray;
var Employee = /** @class */ (function () {
    function Employee(name, currentProject) {
        this.name = name;
        this.currentProject = currentProject;
    }
    Employee.prototype.getCurrentProject = function () {
        return this.currentProject;
    };
    Employee.prototype.getName = function () {
        return this.name;
    };
    return Employee;
}());
exports.Employee = Employee;
var Company = /** @class */ (function () {
    function Company(location) {
        this.employees = [];
        this.location = location;
    }
    Company.prototype.addPerson = function (employee) {
        this.employees.push(employee);
    };
    Company.prototype.getCount = function () {
        return this.location.getCount();
    };
    Company.prototype.getPerson = function (index) {
        return this.location.getPerson(index);
    };
    Company.prototype.getProjectList = function () {
        return this.employees
            .filter(function (person) { return person !== undefined; })
            .map(function (person) { return person.getCurrentProject(); });
    };
    Company.prototype.getNameList = function () {
        return this.employees
            .filter(function (person) { return person !== undefined; })
            .map(function (person) { return person.getName(); });
    };
    return Company;
}());
exports.Company = Company;
var FrontEnd = /** @class */ (function (_super) {
    __extends(FrontEnd, _super);
    function FrontEnd() {
        return _super !== null && _super.apply(this, arguments) || this;
    }
    return FrontEnd;
}(Employee));
var BackEnd = /** @class */ (function (_super) {
    __extends(BackEnd, _super);
    function BackEnd() {
        return _super !== null && _super.apply(this, arguments) || this;
    }
    return BackEnd;
}(Employee));
var europeCompany = new Company(new CompanyLocationArray());
var gosling = new BackEnd("James Gosling", "Java");
var berners = new FrontEnd("Tim Berners-Lee", "HTML");
var eich = new FrontEnd("Brendan Eich", "JavaScript");
var rossum = new BackEnd("Guido van Rossum", "Python");
var stroustrup = new BackEnd("Bjarne Stroustrup", "C++");
europeCompany.addPerson(eich);
europeCompany.addPerson(rossum);
europeCompany.addPerson(berners);
europeCompany.addPerson(gosling);
europeCompany.addPerson(stroustrup);
console.log("Project List:", europeCompany.getProjectList());
console.log("Name List:", europeCompany.getNameList());
