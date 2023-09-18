import {Component, Input, ViewEncapsulation} from '@angular/core';
import {Certificate} from "../../../model/Certificate";
import {LocalStorageService} from "../../../services/local-storage.service";

@Component({
  selector: 'app-price',
  templateUrl: './price.component.html',
  styleUrls: ['./price.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class PriceComponent {
  @Input() certificate: Certificate = {} as Certificate;

  constructor(private storageService: LocalStorageService) {
  }

  public addToCarts() {
    this.certificate.checkout = !this.certificate.checkout;
    this.storageService.updateCertificateInLocalStorage(this.certificate);
  }
}
