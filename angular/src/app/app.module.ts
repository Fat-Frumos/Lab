import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HeaderModule } from './shared/module/header.module';
import { HomeComponent } from './components/pages/home/home.component';
import {CommonModule, NgOptimizedImage} from '@angular/common';
import { MainModule } from './shared/module/main.module';
import { HttpClientModule } from '@angular/common/http';
import { ChevronComponent } from './components/footer/chevron/chevron.component';
import { FooterComponent } from './components/footer/footer/footer.component';
import { SpinnerComponent } from './components/footer/spinner/spinner.component';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    ChevronComponent,
    FooterComponent,
    SpinnerComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    CommonModule,
    FormsModule,
    HeaderModule,
    MainModule,
    NgOptimizedImage,
    HttpClientModule,
  ],
  providers: [],
  bootstrap: [AppComponent],
})
export class AppModule {}
