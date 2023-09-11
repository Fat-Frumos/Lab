import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HeaderComponent } from './components/nav/header/header.component';
import { MenuButtonComponent } from './components/nav/menu-button/menu-button.component';
import { UserComponent } from './components/nav/user/user.component';
import { LogoComponent } from './components/nav/logo/logo.component';
import { SearchComponent } from './components/nav/search/search.component';
import { HomeComponent } from './components/pages/home/home.component';
import { CheckoutComponent } from './components/pages/checkout/checkout.component';
import { ImageComponent } from './components/main/image/image.component';
import { GroupComponent } from './components/main/group/group.component';
import { PriceComponent } from './components/main/price/price.component';
import { DescriptionComponent } from './components/main/description/description.component';
import { CardComponent } from './components/main/card/card.component';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    MenuButtonComponent,
    UserComponent,
    LogoComponent,
    SearchComponent,
    HomeComponent,
    CheckoutComponent,
    ImageComponent,
    GroupComponent,
    PriceComponent,
    DescriptionComponent,
    CardComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
