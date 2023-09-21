import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {LoadService} from "../../../services/load.service";
import {User} from "../../../model/User";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-login-form',
  templateUrl: './login-form.component.html',
  styleUrls: ['./login-form.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class LoginFormComponent implements OnInit {
  user: User = {
    username: '',
    password: '',
    access_token: '',
    refresh_token: '',
    expired_at: ''
  };

  loginForm!: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private load: LoadService) {
  }

  onSubmit(): void {
    if (this.loginForm.invalid) {
      return;
    }

    this.user = {
      username: this.loginForm.value.username,
      password: this.loginForm.value.password,
      access_token: '',
      refresh_token: '',
      expired_at: '',
    };

    this.load.loginUser(this.user).subscribe({
      next: (response: any) => {
        console.log(response); //TODO modal
      },
      error: (error: any) => {
        console.log(error) //TODO modal
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
