import {NgModule} from '@angular/core';
import {ProductsModule} from "./products/products.module";
import {CategoryModule} from "./category/category.module";
import {MainRoutingModule} from "./main-routing.module";
import {MainComponent} from "./main.component";
import {SpinnerModule} from "../../components/spinner/spinner.module";

@NgModule({
  declarations: [
    MainComponent,
  ],
  imports: [
    ProductsModule,
    CategoryModule,
    SpinnerModule,
    MainRoutingModule
  ],
  exports: [MainComponent],
})
export class MainModule {
}
