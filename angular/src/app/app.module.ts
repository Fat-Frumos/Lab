import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {CommonModule, NgOptimizedImage} from '@angular/common';
import {MainModule} from './content/main/main.module';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {FilterPipe} from './pipe/filter.pipe';
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {DisplayDirective} from './directive/display.directive';
import {InfoComponent} from './content/details/info/info.component';
import {BASE_URL_TOKEN, baseUrl, SRC_URL_TOKEN, srcUrl} from "./config";
import {LoadInterceptor} from "./services/load.interceptor";
import {ExchangePipe} from './components/exchange/exchange.pipe';
import { FavoriteComponent } from './content/favorite/favorite.component';
import {ImageModule} from "./components/image/image.module";
import {CouponComponent} from "./content/coupon/coupon.component";
import {DetailsComponent} from "./content/details/details.component";
import {CheckoutComponent} from "./content/checkout/checkout.component";
import {ConfirmComponent} from "./content/checkout/confirm/confirm.component";
import {OrderComponent} from "./content/checkout/order/order.component";
import { ProductsComponent } from './content/main/products/products.component';
import { MainComponent } from './content/main/main.component';
import { ButtonsComponent } from './components/buttons/buttons.component';
import {ModalModule} from "./components/modal/modal.module";
import {BackofficeModule} from "./content/backoffice/backoffice.module";
import {
  DropdownCategoryComponent
} from "./content/coupon/dropdown-category/dropdown-category.component";
import {FooterModule} from "./content/backoffice/footer/footer.module";
import {RouterLink} from "@angular/router";

@NgModule({
  declarations: [
    AppComponent,
    CheckoutComponent,
    FilterPipe,
    DetailsComponent,
    DisplayDirective,
    InfoComponent,
    ConfirmComponent,
    OrderComponent,
    CouponComponent,
    FavoriteComponent,
    ProductsComponent,
    MainComponent,
    ButtonsComponent,
    DropdownCategoryComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    CommonModule,
    ImageModule,
    MainModule,
    NgOptimizedImage,
    HttpClientModule,
    BrowserAnimationsModule,
    ModalModule,
    FooterModule,
    BackofficeModule,
    RouterLink,
  ],
  providers: [
    FilterPipe,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: LoadInterceptor,
      multi: true
    },
    {
      provide: BASE_URL_TOKEN,
      useValue: baseUrl,
      multi: true
    }, {
      provide: SRC_URL_TOKEN,
      useValue: srcUrl
    }
  ],
  bootstrap: [AppComponent],
  exports: [
    FilterPipe,
    ExchangePipe,
  ]
})
export class AppModule {
}
