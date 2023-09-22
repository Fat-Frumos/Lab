import {NgModule} from '@angular/core';
import {MainRoutingModule} from "../main-routing.module";
import {CardComponent} from "./card/card.component";
import {NameComponent} from "./card/name/name.component";
import {PriceComponent} from "./card/price/price.component";
import {DescriptionComponent} from "./card/description/description.component";
import {CarouselDirective} from "../../../directive/carousel.directive";
import {HiddenDirective} from "../../../directive/hidden.directive";
import {ExchangePipe} from "../../../components/exchange/exchange.pipe";
import {FilterPipe} from "../../../pipe/filter.pipe";
import {
  ExchangeComponent
} from "../../../components/exchange/exchange.component";
import {
  ExchangeDirective
} from "../../../components/exchange/exchange.directive";
import {ImageModule} from "../../../components/image/image.module";
import {CurrencyPipe} from "@angular/common";

@NgModule({
  declarations: [
    CardComponent,
    NameComponent,
    PriceComponent,
    DescriptionComponent,
    CarouselDirective,
    HiddenDirective,
    ExchangePipe,
    FilterPipe,
    ExchangeComponent,
    ExchangeDirective,
  ],
  imports: [
    MainRoutingModule,
    ImageModule,
    CurrencyPipe
  ],
  exports: [
    CardComponent,
    NameComponent,
    PriceComponent,
    DescriptionComponent,
    HiddenDirective,
    FilterPipe,
    ExchangeComponent,
    ExchangePipe,
    CarouselDirective,
  ],
  providers: [
    ExchangePipe,
    FilterPipe,
  ]
})
export class ProductsModule {
}
