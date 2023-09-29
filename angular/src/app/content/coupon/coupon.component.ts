import {Component, Input, ViewEncapsulation} from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  Validators
} from "@angular/forms";
import {CertificateService} from "../../services/certificate.service";
import {userMatch} from "../../directive/form-validator.directive";

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
      price: ['', [Validators.required, Validators.min(0)]],
      expired: ['', Validators.required],
      file: [null, Validators.required],
      tags: this.formBuilder.array(
        this.tags.map((tag) => this.formBuilder
        .control(tag, [userMatch.bind(this)])))
    });
  }

  public saveCertificate(): void {
    this.service.saveCertificate(this.form);  //validators invalid
    //TODO validator + promise + image + date
  }
}
