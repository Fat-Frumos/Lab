import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {Certificate} from "../../model/Certificate";

@Component({
  selector: 'app-details',
  templateUrl: './details.component.html',
  styleUrls: ['./details.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class DetailsComponent implements OnInit {
  itemId!: string;
  certificate!: Certificate;

  constructor(private route: ActivatedRoute) {
  }

  ngOnInit(): void {
    let certificate = localStorage.getItem('certificate');
    if (certificate) {
      this.certificate = JSON.parse(certificate);
    }
    this.route.params.subscribe((params) => {
      this.itemId = params['id'];
    });
  }

  checkout() {
    window.location.href = `checkout`;
  }
}
