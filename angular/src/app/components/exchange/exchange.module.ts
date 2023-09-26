import {NgModule} from '@angular/core';
import {ExchangePipe} from "./exchange.pipe";
import {ExchangeComponent} from "./exchange.component";
import {ExchangeDirective} from "./exchange.directive";
import {HiddenDirective} from "../../directive/hidden.directive";

@NgModule({
  declarations: [
    ExchangePipe,
    ExchangeComponent,
    ExchangeDirective,
    HiddenDirective,
  ],
  exports: [
    ExchangeComponent,
    ExchangePipe,
    HiddenDirective,
  ],
  providers: [
    ExchangePipe,
    HiddenDirective,
  ]
})
export class ExchangeModule {
}
