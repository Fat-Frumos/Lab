import {Component, Input, ViewEncapsulation} from '@angular/core';
import {FavoriteService} from "../../../services/favorite.service";

@Component({
  selector: 'app-name',
  templateUrl: './name.component.html',
  styleUrls: ['./name.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class NameComponent {

  @Input() id: string;
  @Input() name: string;

  constructor(private favorite: FavoriteService) {
    this.id = '';
    this.name = '';
  }

  public addToFavorite(id: string) {
    this.favorite.add(id, "favorite");
  }
}
