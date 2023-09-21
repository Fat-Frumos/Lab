import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';

const routes: Routes = [
  {path: '', redirectTo: 'backoffice', pathMatch: 'full'},
  // {
  //   path: 'backoffice/login',
  //   loadChildren: () => import('./content/login/login.module')
  //   .then(module => module.LoginModule)
  // },
  // {
  //   path: 'backoffice/signup',
  //   loadChildren: () => import('./content/signup/signup.module')
  //   .then(module => module.SignupModule)
  // },
  {
    path: '',
    loadChildren: () => import('./content/backoffice/backoffice.module')
    .then(module => module.BackofficeModule)
  },
  // {path: '**', redirectTo: 'backoffice'},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}

