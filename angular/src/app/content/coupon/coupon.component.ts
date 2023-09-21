import {Component, ViewEncapsulation} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {CertificateService} from "../../services/certificate.service";

@Component({
  selector: 'app-coupon',
  templateUrl: './coupon.component.html',
  styleUrls: ['./coupon.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class CouponComponent {
  form!: FormGroup;

  constructor(private formBuilder: FormBuilder,
  private readonly service: CertificateService) {
  }

  ngOnInit(): void {
    this.form = this.formBuilder.group({
      name: ['', Validators.required],
      description: ['', Validators.required],
      company: ['', Validators.required],
      shortDescription: [''],
      price: ['', [Validators.required, Validators.min(0)]],
      expired: ['', Validators.required],
      file: [null, Validators.required],
      tags: this.formBuilder.array([]),
    });
  }

  saveCertificates() {
    const formData = this.form.value;
    console.log("TODO" + formData) //TODO
  }

  goBack (){
    this.service.goBack();
  }
}
