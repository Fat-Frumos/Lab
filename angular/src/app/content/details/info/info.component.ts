import {Component, Input, OnInit, ViewEncapsulation} from '@angular/core';
import {ICertificate} from "../../../model/entity/ICertificate";
import {CardService} from "../../../services/card.service";
import {ExchangeService} from "../../../components/exchange/exchange.service";
import {IRate} from "../../../interfaces/IRate";

@Component({
  selector: 'app-info',
  templateUrl: './info.component.html',
  styleUrls: ['./info.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class InfoComponent implements OnInit {

  @Input() certificate!: ICertificate;
  index!: number;
  rates!: IRate[];

  constructor(
    private cardService: CardService,
    public readonly exchange: ExchangeService
  ) {
  }

  ngOnInit() {
    this.exchange.currentRates.subscribe(rates => this.rates = rates);
    this.exchange.index$.subscribe(index => this.index = index)
  }

  addToCart(certificate: ICertificate): void {
    this.cardService.addCart(certificate);
  }
}
