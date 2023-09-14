import {Injectable, Input} from '@angular/core';
import {LocalStorageService} from './local-storage.service';
import {ICriteria} from "../interfaces/ICriteria";
import {Certificate} from "../model/Certificate";

@Injectable({
  providedIn: 'root',
})
export class FilterService {

  @Input() criteria: ICriteria;


  constructor(private storage: LocalStorageService) {
    this.criteria = {name: '', tag: ''} as ICriteria;
  }

  public filter(): void {
    this.filterCertificates(this.storage.getCertificatesFromLocalStorage());
  }

  filterCertificates(items: Certificate[]): Certificate[] {
    return items.filter((certificate: Certificate) => {
      const nameMatch = this.criteria.name
        ? certificate.name.toLowerCase().includes(this.criteria.name.toLowerCase())
        : true;
      const tagMatch = this.criteria.tag
        ? Array.from(certificate.tags).some(tag => tag.name === this.criteria.tag)
        : true;
      return nameMatch && tagMatch;
    });
  }
}
