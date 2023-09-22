import {NgModule} from '@angular/core';
import {CurrencyPipe, NgForOf, NgIf, NgOptimizedImage} from '@angular/common';
import {ImageModule} from "../../components/image/image.module";
import {CategoryComponent} from "./category/category.component";
import {ProductsComponent} from "./products/products.component";
import {ProductsModule} from "./products/products.module";

@NgModule({
  declarations: [
    ProductsComponent,
    CategoryComponent
  ],
  imports: [
    NgOptimizedImage,
    NgForOf,
    CurrencyPipe,
    ImageModule,
    ProductsModule,
    NgIf,
  ],
  exports: [
    ProductsComponent,
    CategoryComponent
  ]
})
export class MainModule {}
