import {Component} from '@angular/core';
import {AuthService} from "../../services/auth.service";
import {NavigateService} from "../../services/navigate.service";

@Component({
  selector: 'app-logout',
  templateUrl: './logout.component.html',
  styleUrls: ['./logout.component.scss']
})
export class LogoutComponent {

  constructor(
    private readonly auth: AuthService,
    private readonly navigator: NavigateService
  ) {
    this.auth.logout();
    this.navigator.redirect("/")
  }
}
