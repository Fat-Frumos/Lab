import {Component, Input, ViewEncapsulation} from '@angular/core';
import {ICertificate} from "../../../model/entity/ICertificate";
import {ExchangeService} from "../../../components/exchange/exchange.service";
import {IRate} from "../../../interfaces/IRate";

@Component({
  selector: 'app-order',
  templateUrl: './order.component.html',
  styleUrls: ['./order.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class OrderComponent {
  @Input() coupon!: ICertificate;
  rates!: IRate[];
  index!: number;

  constructor(
    public readonly exchange: ExchangeService) {
  }

  ngOnInit() {
    this.exchange.currentRates.subscribe(rates => this.rates = rates);
    this.exchange.index$.subscribe(index => this.index = index)
  }
}
