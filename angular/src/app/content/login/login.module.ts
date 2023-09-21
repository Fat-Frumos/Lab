import {NgModule} from '@angular/core';
import {LoginRoutingModule} from "./login-routing-module";
import {LoginComponent} from "./login.component";
import {LoginFormComponent} from "./login-form/login-form.component";
import {ReactiveFormsModule} from "@angular/forms";

@NgModule({
  declarations: [
    LoginComponent,
    LoginFormComponent],
  imports: [
    LoginRoutingModule,
    ReactiveFormsModule,
  ]
})
export class LoginModule {
}
