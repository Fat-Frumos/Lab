import {Component, ViewEncapsulation} from '@angular/core';
import {ExchangeService} from "../../components/exchange/exchange.service";
import {Certificate} from "../../model/Certificate";
import {LocalStorageService} from "../../services/local-storage.service";
import {Router} from "@angular/router";
import {IRate} from "../../interfaces/IRate";

@Component({
  selector: 'app-checkout',
  templateUrl: './checkout.component.html',
  styleUrls: ['./checkout.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class CheckoutComponent {
  rates!: IRate[];
  index!: number;
  coupons: Certificate[];

  constructor(
    private router: Router,
    public readonly exchange: ExchangeService,
    public readonly storage: LocalStorageService
  ) {
    this.coupons = this.storage.getCheckoutCertificates();
    console.log(this.coupons)
  }

  ngOnInit() {
    this.exchange.currentRates.subscribe(rates => this.rates = rates);
    this.exchange.index$.subscribe(index => this.index = index)
  }

  get totalAmount(): number {
    return this.coupons.reduce((total, coupon) => total + coupon.price, 0);
  }

  showDetails(coupon: Certificate) {
    localStorage.setItem('certificate', JSON.stringify(coupon));
    let promise = this.router.navigate([`/product/${coupon.id}/details`]);
    console.log(promise)
    console.log(coupon)
  }
}
