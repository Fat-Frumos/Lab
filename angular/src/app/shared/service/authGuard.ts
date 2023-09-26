import {
  ActivatedRouteSnapshot,
  CanActivateFn, Router,
  RouterStateSnapshot
} from '@angular/router';
import {inject} from "@angular/core";
import {AuthService} from "../../services/auth.service";
import {map, take} from "rxjs";
import {LoginState} from "../../model/enum/LoginState";

export const AuthGuard: CanActivateFn = (
  _route: ActivatedRouteSnapshot,
  state: RouterStateSnapshot) => {
  const url = state.url;
  return inject(AuthService).loginState.pipe(
    take(1),
    map((state) => {
      let condition = url === '/login' || url === '/signup';
      if ((state !== LoginState.LOGGED_IN) && condition) {
        return true;
      }
      if (state === LoginState.LOGGED_IN && condition) {
        inject(Router).createUrlTree(["/backoffice"]);
        return false;
      }
      return state === LoginState.LOGGED_IN;
    })
  );
};
