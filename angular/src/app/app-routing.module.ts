import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HomeComponent} from "./components/pages/home/home.component";
import {CheckoutComponent} from "./components/pages/checkout/checkout.component";

const routes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'pages/checkout.html', component: CheckoutComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
