import {Component, Input, ViewEncapsulation} from '@angular/core';
import {ILink} from "../../../interfaces/ILink";

@Component({
  selector: 'app-user-link',
  templateUrl: './link.component.html',
  styleUrls: ['./link.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class LinkComponent {

  @Input() link!: ILink;
}
