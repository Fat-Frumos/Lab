import {
  Directive,
  Input,
  TemplateRef,
  ViewContainerRef,
} from '@angular/core';
import {Category} from "../interfaces/Category";

@Directive({
  selector: '[appCarousel]',
})
export class CarouselDirective {

  @Input('appCarouselFrom')
  public categories: Category[] = [];
  @Input('appCarousel') context: any;

  constructor(
    private templateRef: TemplateRef<any>,
    private containerRef: ViewContainerRef,
  ) {
    this.containerRef.createEmbeddedView(this.templateRef, this.context);
  }
}
