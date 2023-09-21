import {RouterModule, Routes} from "@angular/router";
import {NgModule} from "@angular/core";
import {SignupComponent} from "./signup.component";

const routes: Routes = [
  {path: 'signup', component: SignupComponent},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
})
export class SignupRoutingModule {
}
