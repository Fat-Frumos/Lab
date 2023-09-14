import { Component, Input, ViewEncapsulation } from '@angular/core';
import { Certificate } from '../../../model/Certificate';

@Component({
  selector: 'app-image',
  templateUrl: './image.component.html',
  styleUrls: ['./image.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class ImageComponent {
  @Input() certificate!: Certificate;

  showDetails(certificate: Certificate, path: string): void {
    localStorage.removeItem('certificate');
    localStorage.setItem('certificate', JSON.stringify(certificate));
    window.location.href = `${path}details.html`;
  }
}
