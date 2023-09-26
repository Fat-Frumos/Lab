import {Component, Input, OnInit, ViewEncapsulation} from '@angular/core';
import {ICertificate} from "../../../../../model/entity/ICertificate";
import {LocalStorageService} from "../../../../../services/local-storage.service";
import {ExchangeService} from "../../../../../components/exchange/exchange.service";
import {IRate} from "../../../../../interfaces/IRate";

@Component({
  selector: 'app-price',
  templateUrl: './price.component.html',
  styleUrls: ['./price.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class PriceComponent implements OnInit {
  @Input() certificate: ICertificate = {} as ICertificate;
  rates!: IRate[];
  index!: number;

  constructor(
    public readonly exchange: ExchangeService,
    private readonly storageService: LocalStorageService) {
  }

  ngOnInit() {
    this.exchange.currentRates.subscribe(rates => this.rates = rates);
    this.exchange.index$.subscribe(index => this.index = index)
  }

  public async addToCart(): Promise<void> {
    this.certificate.checkout = !this.certificate.checkout;
    this.storageService.updateCertificate(this.certificate);
  }
}
