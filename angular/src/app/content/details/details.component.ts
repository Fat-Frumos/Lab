import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {ICertificate} from "../../model/entity/ICertificate";
import {CardService} from "../../services/card.service";

@Component({
  selector: 'app-details',
  templateUrl: './details.component.html',
  styleUrls: ['./details.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class DetailsComponent implements OnInit {
  public itemId!: string;
  certificate: ICertificate;

  constructor(
    private readonly service: CardService,
    private activatedRoute: ActivatedRoute) {
    this.certificate = this.activatedRoute.snapshot.data['product'];
  }

  ngOnInit(): void {
    this.activatedRoute.params.subscribe(({id}) => this.itemId = id);
    const certificate: ICertificate = this.service.getById(this.itemId);
    this.certificate.favorite = certificate.favorite;
    this.certificate.checkout = certificate.checkout;
  }
}
