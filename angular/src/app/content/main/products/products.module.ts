import {NgModule} from '@angular/core';
import {CardModule} from "./card/card.module";
import {ProductsComponent} from "./products.component";
import {AsyncPipe, NgForOf, NgIf} from "@angular/common";

@NgModule({
  declarations: [
    ProductsComponent,
  ],
  imports: [
    CardModule,
    NgForOf,
    NgIf,
    AsyncPipe
  ],
  exports: [
    ProductsComponent
  ],
})
export class ProductsModule {
}
