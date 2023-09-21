import {Component, ViewEncapsulation} from '@angular/core';
import {ExchangeService} from "./exchange.service";

@Component({
  selector: 'app-exchange',
  templateUrl: './exchange.component.html',
  styleUrls: ['./exchange.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class ExchangeComponent {

  public rates: { value: number, currency: string }[] = [
    {value: 1.00, currency: 'USD'},
    {value: 0.85, currency: 'EUR'},
    {value: 36.0, currency: 'UAH'}
  ];

  public mode: 'off' | 'on' = 'off';
  public ms: number = 5000;

  constructor(private service: ExchangeService) {
    this.service.changeRates(this.rates);
  }
}
