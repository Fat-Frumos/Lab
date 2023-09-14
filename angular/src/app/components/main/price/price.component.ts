import {Component, Input, ViewEncapsulation} from '@angular/core';
import {FavoriteService} from "../../../services/favorite.service";

@Component({
  selector: 'app-price',
  templateUrl: './price.component.html',
  styleUrls: ['./price.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class PriceComponent {

  @Input() id!: string;
  @Input() mainPrice!: number;

  constructor(private favorite: FavoriteService) {
  }

  public addToCarts(id: string) {
    this.favorite.add(id, "cart");
  }
}
