import {Injectable} from '@angular/core';
import {ICertificate} from "../model/entity/ICertificate";
import {LocalStorageService} from "./local-storage.service";

@Injectable()
export class CardService {

  constructor(
    private storage: LocalStorageService
  ) {
  }

  addCart(certificate: ICertificate): void {
    certificate.checkout = !certificate.checkout;
    this.storage.updateCertificate(certificate);
    const message =
      certificate.checkout
        ? ("add to Cart")
        : ("remove from Cart");
    console.log(message);
  }

  getById(id: string): ICertificate {
    return this.storage.getCertificateById(id);
  }
}
