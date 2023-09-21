import {Component, Input, ViewEncapsulation} from '@angular/core';
import {Certificate} from "../../../model/Certificate";
import {ExchangeService} from "../../../components/exchange/exchange.service";

@Component({
  selector: 'app-order',
  templateUrl: './order.component.html',
  styleUrls: ['./order.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class OrderComponent {
  @Input() coupon!: Certificate;
  rates!: { value: number, currency: string }[];
  index!: number;

  constructor(
    public readonly exchange: ExchangeService) {
  }

  ngOnInit() {
    this.exchange.currentRates.subscribe(rates => this.rates = rates);
    this.exchange.index$.subscribe(index => this.index = index)
  }
}
