import {RouterModule, Routes} from "@angular/router";
import {NgModule} from "@angular/core";
import {BackofficeComponent} from "./backoffice.component";
import {CouponComponent} from "../coupon/coupon.component";
import {DetailsComponent} from "../details/details.component";
import {CheckoutComponent} from "../checkout/checkout.component";
import {FavoriteComponent} from "../favorite/favorite.component";

const routes: Routes = [
  {
    path: '', component: BackofficeComponent,
    children: [
      {
        path: '',
        loadChildren: () => import('../main/main.module')
        .then(module => module.MainModule)
      },
      {path: 'coupon', component: CouponComponent},
      {path: 'details', component: DetailsComponent},
      {path: 'checkout', component: CheckoutComponent},
      {path: 'favorite', component: FavoriteComponent},
      {path: 'product/:id/details', component: DetailsComponent},
      {path: 'signup', loadChildren: () => import('../signup/signup.module')
        .then(module => module.SignupModule)},
      {path: 'login',  loadChildren: () => import('../login/login.module')
        .then(module => module.LoginModule)},
    ]
  }];

@NgModule({
  imports: [
    RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class BackofficeRoutingModule {
}
