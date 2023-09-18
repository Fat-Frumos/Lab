import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {Certificate} from "../../../model/Certificate";

@Component({
  selector: 'app-detail',
  templateUrl: './detail.component.html',
  styleUrls: ['./detail.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class DetailComponent implements OnInit {

  certificate!: Certificate;


  ngOnInit(): void {
    // this.route.paramMap.subscribe(params => {
    let item = localStorage.getItem('certificate');
    if (item) {
      this.certificate = JSON.parse(item);
    }
  }
}
