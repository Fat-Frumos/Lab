import {Component, ViewEncapsulation} from '@angular/core';
import {ExchangeService} from "../../components/exchange/exchange.service";
import {ICertificate} from "../../model/entity/ICertificate";
import {LocalStorageService} from "../../services/local-storage.service";
import {Router} from "@angular/router";
import {IRate} from "../../interfaces/IRate";
import {IState} from "../../store/reducers";
import {Store} from "@ngrx/store";
import {selectProductInCard} from "../../store/reducers/cart.reducer";
import {ICartProduct} from "../../interfaces/ICartProduct";

@Component({
  selector: 'app-checkout',
  templateUrl: './checkout.component.html',
  styleUrls: ['./checkout.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class CheckoutComponent {
  rates!: IRate[];
  index!: number;
  coupons!: ICertificate[];
  products$!: ICartProduct[];

  constructor(
    private router: Router,
    public readonly exchange: ExchangeService,
    public readonly storage: LocalStorageService,
    public readonly store: Store<IState>
  ) {
    this.store.select(selectProductInCard)
    .subscribe((data: ICartProduct[]) => this.products$ = data);
    this.coupons = this.storage.getCheckoutCertificates();
    console.log(this.products$)
    console.log(this.coupons)
  }

  ngOnInit() {
    this.exchange.currentRates.subscribe(rates => this.rates = rates);
    this.exchange.index$.subscribe(index => this.index = index)
  }

  public trackByFn(_index: number, item: ICertificate): string {
    return item.id;
  }

  get totalAmount(): number {
    return this.coupons.reduce((total, coupon) => total + coupon.price, 0);
  }

  showDetails(coupon: ICertificate) {
    localStorage.setItem('certificate', JSON.stringify(coupon));
    this.router.navigate([`/product/${coupon.id}/details`])
    .then(() => {
      console.log('Navigation was successful');
    })
    .catch(error => {
      console.error('Navigation failed:', error);
    });
  }
}
