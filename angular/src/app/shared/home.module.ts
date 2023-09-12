import {NgModule} from '@angular/core';
import {HeaderComponent} from "../components/nav/header/header.component";
import {MenuButtonComponent} from "../components/nav/menu-button/menu-button.component";
import {UserComponent} from "../components/nav/user/user.component";
import {LogoComponent} from "../components/nav/logo/logo.component";
import {SearchComponent} from "../components/nav/search/search.component";
import {CheckoutComponent} from "../components/pages/checkout/checkout.component";
import {ImageComponent} from "../components/main/image/image.component";
import {GroupComponent} from "../components/main/group/group.component";
import {PriceComponent} from "../components/main/price/price.component";
import {DescriptionComponent} from "../components/main/description/description.component";
import {CardComponent} from "../components/main/card/card.component";
import {DropdownComponent} from "../components/nav/dropdown/dropdown.component";
import {LinkComponent} from "../components/nav/link/link.component";
import {NgForOf, NgIf} from "@angular/common";

@NgModule({
  declarations: [
    HeaderComponent,
    MenuButtonComponent,
    UserComponent,
    LogoComponent,
    SearchComponent,
    CheckoutComponent,
    ImageComponent,
    GroupComponent,
    PriceComponent,
    DescriptionComponent,
    CardComponent,
    DropdownComponent,
    LinkComponent],
  imports: [
    NgForOf,
    NgIf
  ],
  exports: [
    HeaderComponent,
    MenuButtonComponent,
    UserComponent,
    LogoComponent,
    SearchComponent,
    CheckoutComponent,
    ImageComponent,
    GroupComponent,
    PriceComponent,
    DescriptionComponent,
    CardComponent,
    DropdownComponent,
    LinkComponent
  ]
})
export class HomeModule {
}
