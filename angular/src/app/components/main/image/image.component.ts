import { Component, Input, ViewEncapsulation } from '@angular/core';
import { Certificate } from '../../../model/Certificate';

@Component({
  selector: 'app-image',
  templateUrl: './image.component.html',
  styleUrls: ['./image.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class ImageComponent {
  @Input() certificate: Certificate = {} as Certificate;

  showDetails(path: string): void {
    localStorage.removeItem('certificate');
    localStorage.setItem('certificate', JSON.stringify(this.certificate));
    window.location.href = `${path}details`;
  }
}
