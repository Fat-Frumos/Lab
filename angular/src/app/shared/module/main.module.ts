import { NgModule } from '@angular/core';
import { ImageComponent } from '../../components/main/image/image.component';
import { NameComponent } from '../../components/main/name/name.component';
import { PriceComponent } from '../../components/main/price/price.component';
import { DescriptionComponent } from '../../components/main/description/description.component';
import { CardComponent } from '../../components/main/card/card.component';
import { ContainerComponent } from '../../components/main/container/container.component';
import {AsyncPipe, NgForOf, NgOptimizedImage} from '@angular/common';

@NgModule({
  declarations: [
    ImageComponent,
    NameComponent,
    PriceComponent,
    DescriptionComponent,
    CardComponent,
    ContainerComponent,
  ],
  imports: [NgOptimizedImage, NgForOf, AsyncPipe],
  exports: [
    ImageComponent,
    NameComponent,
    PriceComponent,
    DescriptionComponent,
    CardComponent,
    ContainerComponent,
  ],
})
export class MainModule {}
