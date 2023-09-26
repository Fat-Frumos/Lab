import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {ModalModule} from "./components/modal/modal.module";
import {SharedModule} from "./shared/shared.module";
import {SpinnerModule} from "./components/spinner/spinner.module";

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    ModalModule.forRoot(),
    SharedModule.forRoot(),
    SpinnerModule
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
