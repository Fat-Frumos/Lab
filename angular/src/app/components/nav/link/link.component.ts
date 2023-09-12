import {Component, Input} from '@angular/core';
import {Link} from "../../../interfaces/Link";

@Component({
  selector: 'app-user-link',
  templateUrl: './link.component.html',
  styleUrls: ['./link.component.scss']
})
export class LinkComponent {
  @Input()
  link!: Link;
}
