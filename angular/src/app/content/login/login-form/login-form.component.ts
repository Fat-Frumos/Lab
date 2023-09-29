import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {LoadService} from "../../../services/load.service";
import {FormBuilder, FormGroup} from "@angular/forms";
import {IUser} from "../../../model/entity/IUser";
import {LoginState} from "../../../model/enum/LoginState";
import {
  minLength,
  userMatch
} from "../../../directive/form-validator.directive";

@Component({
  selector: 'app-login-form',
  templateUrl: './login-form.component.html',
  styleUrls: ['./login-form.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class LoginFormComponent implements OnInit {

  public loginForm!: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private service: LoadService) {
    this.service.loginState(LoginState.LOGGED_OUT);
  }

  login(): void {
    if (this.loginForm.invalid) {
      return this.service.showByStatus(40001);
    }
    const user: IUser = {
      username: this.loginForm.value.username,
      password: this.loginForm.value.password,
      access_token: '',
      refresh_token: '',
      expired_at: '',
      certificates: [],
      state: LoginState.LOGGED_OUT
    };

    this.service.loginUser(user).subscribe({
      next: (response: any) => {
        this.service.showByText(20101, response.username);
      },
      error: (error: any) => {
        this.service.showByStatus(error.statusCode);
      }
    });
  }

  ngOnInit() {
    this.loginForm = this.formBuilder.group({
      username: ['', minLength, userMatch.bind(this)],
      password: ['', minLength]
    });
  }
}
