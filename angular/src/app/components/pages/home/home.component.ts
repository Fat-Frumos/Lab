import {Component} from '@angular/core';
import {LocalStorageService} from "../../../services/local-storage.service";
import {Certificate} from "../../../model/Certificate";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent {

  certificateData: Certificate[];

  constructor(private storage: LocalStorageService) {
    this.certificateData = this.storage.getCertificatesFromLocalStorage();
  }
}
