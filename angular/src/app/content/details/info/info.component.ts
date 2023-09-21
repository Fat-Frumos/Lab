import {Component, Input, OnInit, ViewEncapsulation} from '@angular/core';
import {Certificate} from "../../../model/Certificate";
import {CartService} from "../../../services/cart.service";
import {ExchangeService} from "../../../components/exchange/exchange.service";

@Component({
  selector: 'app-info',
  templateUrl: './info.component.html',
  styleUrls: ['./info.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class InfoComponent implements OnInit {

  @Input() certificate!: Certificate;
  index!: number;
  rates!: { value: number, currency: string }[];

  constructor(
    private cartService: CartService,
    public readonly exchange: ExchangeService
  ) {
  }

  ngOnInit() {
    this.exchange.currentRates.subscribe(rates => this.rates = rates);
    this.exchange.index$.subscribe(index => this.index = index)
  }

  addToCart(certificate: Certificate): void {
    this.cartService.addCart(certificate);
  }

  goToCart() {
    this.cartService.navigate('/checkout');
  }
}
