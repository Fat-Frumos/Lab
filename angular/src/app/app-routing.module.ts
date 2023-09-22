import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';

const routes: Routes = [
  {path: '', redirectTo: 'backoffice', pathMatch: 'full'},
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

