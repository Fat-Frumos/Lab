import {Component, Input, ViewEncapsulation} from '@angular/core';
import {ExchangeService} from "../../../components/exchange/exchange.service";
import {CertificateService} from "../../../services/certificate.service";

@Component({
  selector: 'app-confirm',
  templateUrl: './confirm.component.html',
  styleUrls: ['./confirm.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class ConfirmComponent {
  @Input() total!: number;
  rates!: { value: number, currency: string }[];
  index!: number;

  constructor(
    public readonly service: CertificateService,
    public readonly exchange: ExchangeService) {
  }

  ngOnInit() {
    this.exchange.currentRates.subscribe(rates => this.rates = rates);
    this.exchange.index$.subscribe(index => this.index = index)
  }
}
