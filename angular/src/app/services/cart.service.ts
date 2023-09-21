import {Injectable} from '@angular/core';
import {Certificate} from "../model/Certificate";
import {LocalStorageService} from "./local-storage.service";
import {Router} from "@angular/router";

@Injectable({
  providedIn: 'root'
})
export class CartService {

  constructor(
    private router: Router,
    private storage: LocalStorageService
  ) {
  }

  addCart(certificate: Certificate): void {
    certificate.checkout = !certificate.checkout;
    this.storage.updateCertificate(certificate);
    this.storage.saveCertificate(certificate);
    const message = certificate.checkout
      ? ("add to Cart")
      : ("remove from Cart");
    console.log(message);
  }

  showDetails(certificate: Certificate): void {
    this.storage.saveCertificate(certificate);
    this.navigate(`product/${certificate.id}/details`);
  }

  public navigate(path: string): void {
    this.router.navigate([path]).then((success): void => {
      let message: string = success
        ? `Navigation to ${path} was successful`
        : `Navigation to ${path} failed`;
      console.log(message);
    }).catch((error) => {
      console.error('Error during navigation:', error);
    });
  }
}
