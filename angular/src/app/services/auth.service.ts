import {Injectable} from '@angular/core';
import {BehaviorSubject, Observable} from "rxjs";
import {User} from "../model/User";

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private subject: BehaviorSubject<User | null>;
  public currentUser$: Observable<User | null>;
  private TOKEN_KEY: string = 'user';

  constructor() {
    this.subject = new BehaviorSubject<User | null>(null);
    this.currentUser$ = this.subject.asObservable();
  }

  getUser(): string {
    return localStorage.getItem(this.TOKEN_KEY) ?? '';
  }

  setUser(user: User): void {
    if (user) {
      localStorage.setItem(this.TOKEN_KEY, JSON.stringify(user));
      this.subject.next(user);
    } else {
      this.logout();
    }
  }

  logout(): void {
    localStorage.removeItem(this.TOKEN_KEY);
    this.subject.next(null);
  }
}
