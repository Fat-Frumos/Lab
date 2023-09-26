import {NgModule} from '@angular/core';
import {InfoComponent} from "./info/info.component";
import {ExchangeModule} from "../../components/exchange/exchange.module";
import {CurrencyPipe} from "@angular/common";
import {RouterLink} from "@angular/router";
import {DetailsComponent} from "./details.component";
import {ImageModule} from "../../components/image/image.module";
import {DetailsRoutingModule} from "./details-routing-module";

@NgModule({
  declarations: [
    InfoComponent,
    DetailsComponent
  ],
  exports: [
    InfoComponent
  ],
  imports: [
    DetailsRoutingModule,
    ExchangeModule,
    CurrencyPipe,
    RouterLink,
    ImageModule
  ]
})
export class DetailsModule {
}
