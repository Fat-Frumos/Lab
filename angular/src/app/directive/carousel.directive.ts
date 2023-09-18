import {
  Directive,
  Input,
  TemplateRef,
  ViewContainerRef,
  OnInit
} from '@angular/core';
import {Category} from "../interfaces/Category";

@Directive({
  selector: '[appCarousel]',
})
export class CarouselDirective implements OnInit {

  @Input('appCarouselInterval')
  public ms: number = 1000;

  @Input('appCarouselFrom')
  public categories: Category[] = [];
  @Input('appCarousel') context: any;
  private intervalId: number = 0;
  private index: number = 0;
  public autoplay: 'off' | 'on' = 'on';

  constructor(
    private templateRef: TemplateRef<any>,
    private viewContainerRef: ViewContainerRef,
  ) {
  }

  ngOnInit() {
    this.context = {
      $implicit: this.categories[this.index],
      controller: {
        next: () => this.next(),
        prev: () => this.prev(),
      }
    }

    this.viewContainerRef.createEmbeddedView(this.templateRef, this.context);
    this.resetInterval();
  }

  private initInterval(): this {
    this.intervalId = setInterval(() => {
    }, this.ms);
    return this;
  }

  private resetInterval(): this {
    if (this.autoplay === 'on') {
      this.clearInterval().initInterval();
    }
    return this;
  }

  private clearInterval(): this {
    if (this.intervalId) {
      clearInterval(this.intervalId);
    }
    return this;
  }

  private next() {
    this.index++;
  }

  private prev() {
    this.index--;
  }
}
