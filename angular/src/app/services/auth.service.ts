import {Injectable} from '@angular/core';
import {BehaviorSubject, Observable} from "rxjs";
import {User} from "../model/User";
import {LocalStorageService} from "./local-storage.service";
import {NavigateService} from "./navigate.service";

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private subject: BehaviorSubject<User | null>;
  public currentUser$: Observable<User | null>;

  constructor(
    private navigator: NavigateService,
    private storage: LocalStorageService
  ) {
    this.subject = new BehaviorSubject<User | null>(null);
    this.currentUser$ = this.subject.asObservable();

  }

  getUser(): User {
    return this.storage.getUser();
  }

  setUser(user: User): void {
    if (user) {
      this.storage.saveUser(user)
      this.subject.next(user);
    } else {
      this.logout();
    }
  }

  logout(): void {
    this.storage.removeUser();
    this.subject.next(null);
  }

  redirect(href: string) {
    this.navigator.redirect(href);
  }

  goBack() {
    this.navigator.back();
  }
}
