import {NgModule} from '@angular/core';
import {BackofficeRoutingModule} from "./backoffice-routing-module";
import {HeaderComponent} from "./header/header.component";
import {MenuButtonComponent} from "./header/menu-button/menu-button.component";
import {UserComponent} from "./header/user/user.component";
import {LogoComponent} from "./header/logo/logo.component";
import {SearchComponent} from "./header/search/search.component";
import {DropdownComponent} from "./header/dropdown/dropdown.component";
import {LinkComponent} from "./header/link/link.component";
import {
  CommonModule,
  CurrencyPipe,
  NgClass,
  NgForOf,
  NgIf,
  NgOptimizedImage
} from "@angular/common";
import {BackofficeComponent} from "./backoffice.component";
import {FooterModule} from "./footer/footer.module";
import {MainModule} from "../main/main.module";
import {LoginModule} from "../login/login.module";
import {SignupModule} from "../signup/signup.module";
import {ProductsModule} from "../main/products/products.module";
import {
  DropdownCategoryComponent
} from "../coupon/dropdown-category/dropdown-category.component";
import {CouponComponent} from "../coupon/coupon.component";
import {ReactiveFormsModule} from "@angular/forms";

@NgModule({
  declarations: [
    HeaderComponent,
    MenuButtonComponent,
    UserComponent,
    LogoComponent,
    SearchComponent,
    DropdownComponent,
    LinkComponent,
    BackofficeComponent,
    DropdownCategoryComponent,
    CouponComponent
  ],
  exports: [
    HeaderComponent,
  ],
  imports: [
    NgForOf,
    NgIf,
    NgOptimizedImage,
    NgClass,
    CurrencyPipe,
    CommonModule,
    BackofficeRoutingModule,
    FooterModule,
    MainModule,
    LoginModule,
    SignupModule,
    ProductsModule,
    ReactiveFormsModule
  ]
})
export class BackofficeModule {
}
