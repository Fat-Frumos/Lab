import {Component, NgModule} from '@angular/core';
import {Certificate} from "../../model/Certificate";
import {CommonModule} from "@angular/common";
import {MatCardModule} from "@angular/material/card";
import {MatButtonModule} from "@angular/material/button";
import {FlexLayoutModule} from "@angular/flex-layout";
import {ImageModule} from "../image/image.module";

@Component({
  selector: 'app-item',
  templateUrl: './item.component.html',
  styleUrls: ['./item.component.scss']
})
export class ItemComponent {

  public coupon!: Certificate;
  public save!: () => void;
  public close!: () => void;
}

@NgModule({
  declarations: [ItemComponent],
  imports: [CommonModule, MatCardModule, MatButtonModule,FlexLayoutModule, ImageModule ]
})
export class ItemModule {
}
