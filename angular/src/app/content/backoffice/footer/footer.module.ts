import { NgModule } from '@angular/core';
import {ChevronComponent} from "./chevron/chevron.component";
import {FooterComponent} from "./footer.component";
import {SpinnerComponent} from "./spinner/spinner.component";


@NgModule({
  declarations: [
    ChevronComponent,
    FooterComponent,
    SpinnerComponent,
  ],
  exports: [
    SpinnerComponent,
    ChevronComponent,
    FooterComponent,
  ],
  imports: []
})
export class FooterModule { }
