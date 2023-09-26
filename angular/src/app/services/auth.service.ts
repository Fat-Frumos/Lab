import {Injectable} from '@angular/core';
import {BehaviorSubject} from "rxjs";
import {IUser} from "../model/entity/IUser";
import {LocalStorageService} from "./local-storage.service";
import {LoginState} from "../model/enum/LoginState";

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private subject: BehaviorSubject<IUser | null>;
  loginState: BehaviorSubject<LoginState>;

  constructor(
    private readonly storage: LocalStorageService
  ) {
    this.loginState = new BehaviorSubject<LoginState>(LoginState.LOGGED_OUT);
    this.subject = new BehaviorSubject<IUser | null>(null);
    let user: IUser = this.getUser();
    if (user) {
      this.loginState.next(user.state);
    }
  }

  public getUser(): IUser {
    return this.storage.getUser();
  }

  public setUser(user: IUser): void {
    if (user) {
      this.login(user);
    } else {
      this.logout();
    }
  }

  private login(user: IUser) {
    user.state = LoginState.LOGGED_IN;
    this.subject.next(user);
    this.storage.saveUser(user)
    this.loginState.next(user.state);
  }

  private logout(): void {
    this.storage.removeUser();
    this.subject.next(null);
    this.loginState.next(LoginState.LOGGED_OUT);
    console.log(this.loginState.value);
  }
}
