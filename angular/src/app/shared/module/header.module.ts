import { NgModule } from '@angular/core';
import { HeaderComponent } from '../../components/nav/header/header.component';
import { MenuButtonComponent } from '../../components/nav/menu-button/menu-button.component';
import { UserComponent } from '../../components/nav/user/user.component';
import { LogoComponent } from '../../components/nav/logo/logo.component';
import { SearchComponent } from '../../components/nav/search/search.component';
import { CheckoutComponent } from '../../components/pages/checkout/checkout.component';
import { DropdownComponent } from '../../components/nav/dropdown/dropdown.component';
import { LinkComponent } from '../../components/nav/link/link.component';
import { NgForOf, NgIf, NgOptimizedImage } from '@angular/common';
import { CategoryComponent } from '../../components/main/category/category.component';

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
    CategoryComponent,
  ],
  imports: [NgForOf, NgIf, NgOptimizedImage],
  exports: [
    HeaderComponent,
    MenuButtonComponent,
    UserComponent,
    LogoComponent,
    SearchComponent,
    CheckoutComponent,
    DropdownComponent,
    LinkComponent,
    CategoryComponent,
  ],
})
export class HeaderModule {}
