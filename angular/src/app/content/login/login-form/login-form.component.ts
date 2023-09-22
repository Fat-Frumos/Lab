import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {LoadService} from "../../../services/load.service";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {User} from "../../../model/User";

@Component({
  selector: 'app-login-form',
  templateUrl: './login-form.component.html',
  styleUrls: ['./login-form.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class LoginFormComponent implements OnInit {

  loginForm!: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private service: LoadService) {
  }

  onSubmit(): void {
    if (this.loginForm.invalid) {
      return this.service.showMessage("Invalid form data. Please check your inputs.");
    }

    const user: User = {
      username: this.loginForm.value.username,
      password: this.loginForm.value.password,
      access_token: '',
      refresh_token: '',
      expired_at: '',
      certificates: []
    };

    this.service.loginUser(user).subscribe({
      next: (response: any) => {
        this.service.redirect({ name: response.username, href: '/' });
      },
      error: (error: any) => {
        alert(error.toString());
      }
    });
  }

  ngOnInit() {
    this.loginForm = this.formBuilder.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });
  }
}
