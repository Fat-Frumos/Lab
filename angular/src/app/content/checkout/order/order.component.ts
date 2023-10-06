import {
  Component,
  EventEmitter,
  Input,
  Output,
  ViewEncapsulation
} from '@angular/core';
import {ExchangeService} from "../../../components/exchange/exchange.service";
import {IRate} from "../../../interfaces/IRate";
import {ICertificate} from "../../../model/entity/ICertificate";

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
  @Output() public remove = new EventEmitter();
  @Output() public increment = new EventEmitter();
  @Output() public decrement = new EventEmitter();

  constructor(
    public readonly exchange: ExchangeService) {
  }

  ngOnInit() {
    this.exchange.currentRates.subscribe(rates => this.rates = rates);
    this.exchange.index$.subscribe(index => this.index = index)
  }
}
