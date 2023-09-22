import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {CommonModule, NgOptimizedImage} from '@angular/common';
import {MainModule} from './content/main/main.module';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {DisplayDirective} from './directive/display.directive';
import {InfoComponent} from './content/details/info/info.component';
import {BASE_URL_TOKEN, baseUrl, SRC_URL_TOKEN, srcUrl} from "./config";
import {LoadInterceptor} from "./services/load.interceptor";
import { FavoriteComponent } from './content/favorite/favorite.component';
import {ImageModule} from "./components/image/image.module";
import {DetailsComponent} from "./content/details/details.component";
import {CheckoutComponent} from "./content/checkout/checkout.component";
import {ConfirmComponent} from "./content/checkout/confirm/confirm.component";
import {OrderComponent} from "./content/checkout/order/order.component";
import { MainComponent } from './content/main/main.component';
import { ButtonsComponent } from './components/buttons/buttons.component';
import {ModalModule} from "./components/modal/modal.module";
import {BackofficeModule} from "./content/backoffice/backoffice.module";
import {FooterModule} from "./content/backoffice/footer/footer.module";
import {RouterLink} from "@angular/router";
import {ProductsModule} from "./content/main/products/products.module";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";

@NgModule({
  declarations: [
    AppComponent,
    CheckoutComponent,
    DetailsComponent,
    DisplayDirective,
    InfoComponent,
    ConfirmComponent,
    OrderComponent,
    FavoriteComponent,
    MainComponent,
    ButtonsComponent,
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
    ProductsModule,
    ReactiveFormsModule,
    FormsModule,
  ],
  providers: [
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
  bootstrap: [AppComponent]
})
export class AppModule {
}
