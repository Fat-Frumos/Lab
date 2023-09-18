import { NgModule } from '@angular/core';
import { HeaderComponent } from '../../components/nav/header/header.component';
import { MenuButtonComponent } from '../../components/nav/menu-button/menu-button.component';
import { UserComponent } from '../../components/nav/user/user.component';
import { LogoComponent } from '../../components/nav/logo/logo.component';
import { SearchComponent } from '../../components/nav/search/search.component';
import { CheckoutComponent } from '../../components/pages/checkout/checkout.component';
import { DropdownComponent } from '../../components/nav/dropdown/dropdown.component';
import { LinkComponent } from '../../components/nav/link/link.component';
import {NgClass, NgForOf, NgIf, NgOptimizedImage} from '@angular/common';

@NgModule({
  declarations: [
    HeaderComponent,
    MenuButtonComponent,
    UserComponent,
    LogoComponent,
    SearchComponent,
    CheckoutComponent,
    DropdownComponent,
    LinkComponent,
  ],
  imports: [NgForOf, NgIf, NgOptimizedImage, NgClass],
  exports: [
    HeaderComponent,
    MenuButtonComponent,
    UserComponent,
    LogoComponent,
    SearchComponent,
    CheckoutComponent,
    DropdownComponent,
    LinkComponent,
  ],
})
export class HeaderModule {}
