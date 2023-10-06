import {NgModule} from '@angular/core';
import {BillRoutingModule} from "./bill-routing.module";
import {CurrencyPipe, DatePipe, NgForOf, NgIf} from "@angular/common";
import {BillComponent} from "./bill.component";
import {ExchangeModule} from "../../../components/exchange/exchange.module";

@NgModule({
  declarations: [
    BillComponent
  ],
  imports: [
    BillRoutingModule,
    NgForOf,
    DatePipe,
    CurrencyPipe,
    ExchangeModule,
    NgIf
  ]
})
export class BillModule {
}
