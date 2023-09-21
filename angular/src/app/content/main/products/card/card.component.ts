import {Component, Input, ViewEncapsulation} from '@angular/core';
import {Tag} from '../../../../model/Tag';
import {Certificate} from '../../../../model/Certificate';
import {CartService} from "../../../../services/cart.service";

@Component({
  selector: 'app-card',
  templateUrl: './card.component.html',
  styleUrls: ['./card.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class CardComponent {
  @Input() tags!: Set<Tag>;
  @Input() certificate!: Certificate;

  constructor(
    public readonly service: CartService
  ) {
  }
}
