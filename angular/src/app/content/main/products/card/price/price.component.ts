import {Component, Input, OnInit, ViewEncapsulation} from '@angular/core';
import {Certificate} from "../../../../../model/Certificate";
import {LocalStorageService} from "../../../../../services/local-storage.service";
import {ModalService} from "../../../../../components/modal/modal.service";
import {ExchangeService} from "../../../../../components/exchange/exchange.service";

@Component({
  selector: 'app-price',
  templateUrl: './price.component.html',
  styleUrls: ['./price.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class PriceComponent implements OnInit {
  @Input() certificate: Certificate = {} as Certificate;
  rates!: { value: number, currency: string }[];
  index!: number;

  constructor(
    public readonly exchange: ExchangeService,
    private readonly modalService: ModalService,
    private readonly storageService: LocalStorageService) {
  }

  ngOnInit() {
    this.exchange.currentRates.subscribe(rates => this.rates = rates);
    this.exchange.index$.subscribe(index => this.index = index)
  }

  public async addToCart(): Promise<void> {
    const {ItemComponent} = await import('../../../../../components/item/item.component');
    this.certificate.checkout = !this.certificate.checkout;
    this.storageService.updateCertificate(this.certificate);
    this.modalService.open({
      component: ItemComponent,
      context: {
        coupon: {...this.certificate},
        save: () => this.modalService.close(),
        close: () => this.modalService.close()
      }
    });
  }
}
