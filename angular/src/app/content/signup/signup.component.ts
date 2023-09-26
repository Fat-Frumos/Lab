import {Component, ViewEncapsulation} from '@angular/core';
import {
  AbstractControl,
  FormBuilder,
  FormGroup,
  ValidationErrors,
  Validators
} from "@angular/forms";
import {LoadService} from "../../services/load.service";

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class SignupComponent {

  form!: FormGroup;

  constructor(
    private readonly loadService: LoadService,
    private formBuilder: FormBuilder) {
    this.form = this.formBuilder.group({
      username: ['', Validators.required],
      firstName: ['', Validators.required],
      password: ['', Validators.required],
      repeatPassword: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      address: ['', Validators.required]
    }, {validators: this.passwordMatchValidator});
  }

  onSubmit() {
    if (!this.form.valid) {
      this.loadService.showByStatus(40001)
    } else {
      this.loadService.signup(this.form.value).subscribe({
        next: (response: any) => this.loadService.showByStatus(response.status),
        error: (error: any) => this.loadService.showByStatus(error.status)
      });
    }
  }

  passwordMatchValidator(control: AbstractControl): ValidationErrors | null {
    const password = control.get('password');
    const repeatPassword = control.get('repeatPassword');
    return password && repeatPassword && password.value !== repeatPassword.value
      ? {passwordMismatch: true}
      : null;
  }

  onCancel() {
    this.loadService.back();
  }
}
