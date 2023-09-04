import { LocalStorage } from 'node-localstorage';

const localStorage = new LocalStorage('./scratch');

interface ILocation {
  addPerson(person: Employee): void;
  getPerson(index: number): Employee | undefined;
  getCount(): number;
}

interface IEmployee {
  getCurrentProject(): string;
  getName(): string;
}

class CompanyLocationLocalStorage implements ILocation {
    private key: string;
    private storage: Employee[];

    constructor(key: string) {
        this.key = key;
        this.storage = this.getLocalStorage();
    }

    private getLocalStorage(): Employee[] {
        const data = localStorage.getItem(this.key);
        return data ? JSON.parse(data) : [];
    }

    private setLocalStorage(storage: Employee[]): void {
        localStorage.setItem(this.key, JSON.stringify(storage));
    }

    addPerson(person: Employee): void {
        this.storage.push(person);
        this.setLocalStorage(this.storage);
    }

    getPerson(index: number): Employee | undefined {
        return this.storage[index];
    }

    getCount(): number {
        return this.storage.length;
    }
}

class CompanyLocationArray implements ILocation {
  private persons: Employee[] = [];

  addPerson(person: Employee): void {
    this.persons.push(person);
  }

  getPerson(index: number): Employee {
    return this.persons[index];
  }

  getCount(): number {
    return this.persons.length;
  }
}

class Employee implements IEmployee {
  constructor(private name: string, private currentProject: string) {}

  getCurrentProject(): string {
    return this.currentProject;
  }

  getName(): string {
    return this.name;
  }
}

class Company {
  private location: ILocation;

  constructor(location: ILocation) {
    this.location = location;
  }

  private employees: IEmployee[] = [];

  addPerson(employee: IEmployee): void {
    this.employees.push(employee);
  }

  getCount(): number {
    return this.location.getCount();
  }

  getPerson(index: number): Employee | undefined {
    return this.location.getPerson(index);
  }

  getProjectList(): string[] {
    return this.employees
        .filter(person => person !== undefined)
        .map(person => person.getCurrentProject());
}

getNameList(): string[] {
    return this.employees
        .filter(person => person !== undefined)
        .map(person => person.getName());
}
}

class FrontEnd extends Employee {}
class BackEnd extends Employee {}

const europeCompany = new Company(new CompanyLocationArray());

const gosling = new BackEnd("James Gosling", "Java");
const berners = new FrontEnd("Tim Berners-Lee", "HTML");
const eich = new FrontEnd("Brendan Eich", "JavaScript");
const rossum = new BackEnd("Guido van Rossum", "Python");
const stroustrup = new BackEnd("Bjarne Stroustrup", "C++");

europeCompany.addPerson(eich);
europeCompany.addPerson(rossum);
europeCompany.addPerson(berners);
europeCompany.addPerson(gosling);
europeCompany.addPerson(stroustrup);

console.log("Project List:", europeCompany.getProjectList());
console.log("Name List:", europeCompany.getNameList());

export { Company, Employee, IEmployee, CompanyLocationArray, CompanyLocationLocalStorage };
