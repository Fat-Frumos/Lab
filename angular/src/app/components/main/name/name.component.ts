import {Component, Input, ViewEncapsulation} from '@angular/core';
import {Certificate} from "../../../model/Certificate";
import {LocalStorageService} from "../../../services/local-storage.service";

@Component({
  selector: 'app-name',
  templateUrl: './name.component.html',
  styleUrls: ['./name.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class NameComponent {

  @Input() certificate: Certificate = {} as Certificate;

  constructor(private storageService: LocalStorageService) {
  }

  public addToFavorite() {
    this.certificate.favorite = !this.certificate.favorite;
    this.storageService.updateCertificateInLocalStorage(this.certificate);
  }
}
