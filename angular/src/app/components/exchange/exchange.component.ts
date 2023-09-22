import {Component, ViewEncapsulation} from '@angular/core';
import {ExchangeService} from "./exchange.service";
import {IRate} from "../../interfaces/IRate";

@Component({
  selector: 'app-exchange',
  templateUrl: './exchange.component.html',
  styleUrls: ['./exchange.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class ExchangeComponent {

  public rates: IRate[] = [
    {value: 1.00, currency: 'USD'},
    {value: 0.85, currency: 'EUR'},
    {value: 38.0, currency: 'UAH'}
  ];

  public mode: 'off' | 'on' = 'off';
  public ms: number = 5000;

  constructor(private service: ExchangeService) {
    this.service.changeRates(this.rates);
  }
}
