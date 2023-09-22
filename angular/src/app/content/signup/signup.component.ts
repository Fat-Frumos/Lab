import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {LoadService} from "../../services/load.service";

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class SignupComponent implements OnInit {

  registrationForm!: FormGroup;

  constructor(
    private readonly service: LoadService,
    private formBuilder: FormBuilder) {
  }

  onSubmit() {
    if (this.registrationForm.valid) {
      const formData = this.registrationForm.value;
      this.service.signup(formData).subscribe({
        next: (response: any) => {
          console.log(response); //TODO modal
        },
        error: (error: any) => {
          console.log(error) //TODO modal
        }
      });
    } else {
      alert("fill the data")
    }
  }

  onCancel() {
    this.service.back();
  }

  ngOnInit(): void {
    this.registrationForm = this.formBuilder.group({
      username: ['', Validators.required],
      firstName: ['', Validators.required],
      password: ['', Validators.required],
      repeatPassword: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      address: ['', Validators.required]
    });
  }
}
