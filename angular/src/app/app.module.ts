import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {ModalModule} from "./components/modal/modal.module";
import {SharedModule} from "./shared/shared.module";
import {SpinnerModule} from "./components/spinner/spinner.module";
import {StoreModule} from "@ngrx/store";
import {reducers} from "./store/reducers";
import {StoreDevtoolsModule} from "@ngrx/store-devtools";
import {environment} from "../environments/environment.prod";
import {EffectsModule} from "@ngrx/effects";
import {effects} from "./store/effects";

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
    SpinnerModule,
    StoreModule.forRoot(reducers,
      {
        runtimeChecks: {
          strictActionImmutability: false,
          strictStateImmutability: false
        }
      }),
    StoreDevtoolsModule.instrument({
      maxAge: 25,
      logOnly: environment.production
    }),
    EffectsModule.forRoot(effects)
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
