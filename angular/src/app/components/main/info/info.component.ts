import {Component, Input} from '@angular/core';
import {Certificate} from "../../../model/Certificate";
import {Router} from "@angular/router";
import {CartService} from "../../../services/cart.service";

@Component({
  selector: 'app-info',
  templateUrl: './info.component.html',
  styleUrls: ['./info.component.scss']
})
export class InfoComponent {

  @Input() certificate!: Certificate;

  constructor(
    private router: Router,
    private cartService: CartService
  ) {
  }

  addToCart(certificate: Certificate): void {
    this.cartService.addCart(certificate);
    let promise = this.router.navigate(['/cart']);
    console.log(promise)
  }

  isAddedToCart(certificate: Certificate): boolean {
    return this.cartService.isAddedToCart(certificate);
  }
}
