import {Component, Input, ViewEncapsulation} from '@angular/core';
import {FormBuilder, Validators} from "@angular/forms";
import {CertificateService} from "../../services/certificate.service";

@Component({
  selector: 'app-coupon',
  templateUrl: './coupon.component.html',
  styleUrls: ['./coupon.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class CouponComponent {

  @Input()
  form = this.formBuilder.group({
    name: ['', Validators.required],
    description: ['', Validators.required],
    company: ['', Validators.required],
    shortDescription: [''],
    price: ['', [Validators.required, Validators.min(0)]],
    expired: ['', Validators.required],
    file: [null, Validators.required],
    tags: this.formBuilder.array([])
  });

  constructor(
    private formBuilder: FormBuilder,
    public readonly service: CertificateService) {
  }

  public saveCertificate(): void {
    this.service.saveCertificate(this.form); //TODO
  }
}
