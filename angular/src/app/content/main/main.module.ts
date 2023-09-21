import { NgModule } from '@angular/core';
import { NameComponent } from './products/card/name/name.component';
import { PriceComponent } from './products/card/price/price.component';
import { DescriptionComponent } from './products/card/description/description.component';
import { CardComponent } from './products/card/card.component';
import {CurrencyPipe, NgForOf, NgOptimizedImage} from '@angular/common';
import {ExchangePipe} from "../../components/exchange/exchange.pipe";
import {ImageModule} from "../../components/image/image.module";
import {CategoryComponent} from "./category/category.component";
import {CarouselDirective} from "../../directive/carousel.directive";
import {HiddenDirective} from "../../directive/hidden.directive";

@NgModule({
  declarations: [
    NameComponent,
    PriceComponent,
    DescriptionComponent,
    CardComponent,
    CategoryComponent,
    ExchangePipe,
    CarouselDirective,
    HiddenDirective,
  ],
  imports: [
    NgOptimizedImage,
    NgForOf,
    CurrencyPipe,
    ImageModule,

  ],
  exports: [
    NameComponent,
    PriceComponent,
    DescriptionComponent,
    CardComponent,
    CategoryComponent,
    ExchangePipe,
    HiddenDirective,
  ],
})
export class MainModule {}
