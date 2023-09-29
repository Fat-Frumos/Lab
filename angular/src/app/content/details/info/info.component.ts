import {Component, Input, OnInit, ViewEncapsulation} from '@angular/core';
import {ICertificate} from "../../../model/entity/ICertificate";
import {ExchangeService} from "../../../components/exchange/exchange.service";
import {IRate} from "../../../interfaces/IRate";
import {Store} from "@ngrx/store";
import {IState} from "../../../store/reducers";
import {addProductToCart} from "../../../store/actions/cart";
import {ICartProduct} from "../../../interfaces/ICartProduct";
import {CertificateService} from "../../../services/certificate.service";

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
    private store: Store<IState>,
    public readonly exchange: ExchangeService,
    public readonly service: CertificateService,
  ) {
  }

  ngOnInit() {
    this.exchange.currentRates.subscribe(rates => this.rates = rates);
    this.exchange.index$.subscribe(index => this.index = index)
  }

  addToCart(certificate: ICertificate): void {
    const product: ICartProduct = {...certificate, count: 1}; //TODO store
    this.store.dispatch(addProductToCart({product}));
    this.service.addCart(certificate);
  }
}
