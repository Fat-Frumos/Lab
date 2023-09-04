"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.BackEnd = exports.FrontEnd = void 0;
var app_1 = require("../EuropeCompany/app");
var FrontEnd = /** @class */ (function () {
    function FrontEnd(name, currentProject) {
        this.name = name;
        this.currentProject = currentProject;
    }
    FrontEnd.prototype.getCurrentProject = function () {
        return this.currentProject;
    };
    FrontEnd.prototype.getName = function () {
        return this.name;
    };
    return FrontEnd;
}());
exports.FrontEnd = FrontEnd;
var BackEnd = /** @class */ (function () {
    function BackEnd(name, currentProject) {
        this.name = name;
        this.currentProject = currentProject;
    }
    BackEnd.prototype.getCurrentProject = function () {
        return this.currentProject;
    };
    BackEnd.prototype.getName = function () {
        return this.name;
    };
    return BackEnd;
}());
exports.BackEnd = BackEnd;
var americanCompany = new app_1.Company(new app_1.CompanyLocationArray());
var alice = new FrontEnd("Alice Smith", "E-commerce Website");
var bob = new FrontEnd("Bob Johnson", "Dashboard Redesign");
var charlie = new BackEnd("Charlie Brown", "API Development");
var diana = new BackEnd("Diana Wilson", "Database Optimization");
americanCompany.addPerson(alice);
americanCompany.addPerson(bob);
americanCompany.addPerson(charlie);
americanCompany.addPerson(diana);
console.log("Project List:", americanCompany.getProjectList());
console.log("Name List:", americanCompany.getNameList());
