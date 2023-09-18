import { NgModule } from '@angular/core';
import { ImageComponent } from '../../components/main/image/image.component';
import { NameComponent } from '../../components/main/name/name.component';
import { PriceComponent } from '../../components/main/price/price.component';
import { DescriptionComponent } from '../../components/main/description/description.component';
import { CardComponent } from '../../components/main/card/card.component';
import {NgForOf, NgOptimizedImage} from '@angular/common';

@NgModule({
  declarations: [
    ImageComponent,
    NameComponent,
    PriceComponent,
    DescriptionComponent,
    CardComponent
  ],
  imports: [NgOptimizedImage, NgForOf],
  exports: [
    ImageComponent,
    NameComponent,
    PriceComponent,
    DescriptionComponent,
    CardComponent
  ],
})
export class MainModule {}
