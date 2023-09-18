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
import { FilterPipe } from './pipe/filter.pipe';
import { ContainerComponent } from "./components/main/container/container.component";
import { DetailsComponent } from './components/pages/details/details.component';
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {CategoryComponent} from "./components/main/category/category.component";
import { CarouselDirective } from './directive/carousel.directive';
import { HiddenDirective } from './directive/hidden.directive';
import { DisplayDirective } from './directive/display.directive';
import { DetailComponent } from './components/main/detail/detail.component';
import { InfoComponent } from './components/main/info/info.component';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    ChevronComponent,
    FooterComponent,
    SpinnerComponent,
    ContainerComponent,
    FilterPipe,
    DetailsComponent,
    CategoryComponent,
    CarouselDirective,
    HiddenDirective,
    DisplayDirective,
    DetailComponent,
    InfoComponent,
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
    BrowserAnimationsModule,

  ],
  providers: [
    FilterPipe,
    DisplayDirective],
  bootstrap: [AppComponent],
  exports: [
    FilterPipe
  ]
})
export class AppModule {}
