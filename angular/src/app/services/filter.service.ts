import {Injectable, Input} from '@angular/core';
import {Certificate} from "../model/Certificate";
import {LocalStorageService} from "./local-storage.service";
import {Tag} from "../model/Tag";
import {Category} from "../interfaces/Category";

@Injectable({
  providedIn: 'root'
})
export class FilterService {

  @Input()
  certificates: Certificate[];

  @Input()
  category: Category;

  @Input()
  filtered: Certificate[];

  constructor(private storage: LocalStorageService) {
    this.category = { name: '', tag: '' } as Category;
    this.filtered = [];
    this.certificates = this.storage.getCertificatesFromLocalStorage()
  }

  public filter(): void {

    console.log("Service: filterBy " + this.category.name + " " + this.category.tag);

    this.filtered = this.certificates.filter((certificate: Certificate) => {

      const matchesCategory = this.category.tag === 'All Categories'
        || Array.from(certificate.tags)
          .some((tag: Tag) => tag.name === this.category.tag);

      const matchesQuery =
        certificate.name.toLowerCase()
          .includes(this.category.name.toLowerCase())
        || certificate.description.toLowerCase()
          .includes(this.category.name.toLowerCase());

      return matchesCategory && matchesQuery;
    });
  }
}
