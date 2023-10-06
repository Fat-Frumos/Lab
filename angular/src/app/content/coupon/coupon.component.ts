import {Component, Input, ViewEncapsulation} from '@angular/core';
import {
  FormBuilder, FormControl,
  FormGroup,
  Validators
} from "@angular/forms";
import {CertificateService} from "../../services/certificate.service";
// import {userMatch} from "../../directive/form-validator.directive";

@Component({
  selector: 'app-coupon',
  templateUrl: './coupon.component.html',
  styleUrls: ['./coupon.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class CouponComponent {

  @Input()
  form: FormGroup;

  tags: string[] = ['Cosmetics', 'Makeup', 'Travel', 'Celebration', 'Culture', 'Holiday'];

  constructor(
    private formBuilder: FormBuilder,
    public readonly service: CertificateService) {

    this.form = this.formBuilder.group({
      name: ['', Validators.required],
      description: ['', Validators.required],
      company: ['', Validators.required],
      shortDescription: [''],
      price: ['', Validators.required],
      expired: new FormControl('2024-02-24'),
      file: [],
      tags: this.formBuilder.array(this.tags.map((tag) => this.formBuilder.control(tag)))
    });
  }

  public saveCertificate(): void {
    this.service.saveCertificate(this.form);
    //TODO validator + promise + image + date
  }
}
