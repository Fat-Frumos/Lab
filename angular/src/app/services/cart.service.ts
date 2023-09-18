import { Injectable } from '@angular/core';
import {Certificate} from "../model/Certificate";

@Injectable({
  providedIn: 'root'
})
export class CartService {
  private carts$: Certificate[] = [];

  addToCart(certificate: Certificate) {
    this.carts$.push(certificate);
  }

  removeFromCart(certificate: Certificate): void {
    const index = this.carts$.findIndex(item => item.id === certificate.id);
    if (index !== -1) {
      this.carts$.splice(index, 1);
    }
  }

  isAddedToCart(certificate: Certificate) {
    return this.carts$.some(item => item.id === certificate.id);
  }

  addCart(certificate: Certificate) {
    if (this.isAddedToCart(certificate)) {
      this.removeFromCart(certificate);
    } else {
      this.addToCart(certificate);
    }
  }
}
