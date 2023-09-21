import {
  Component,
  ElementRef,
  HostListener,
  ViewEncapsulation
} from '@angular/core';

@Component({
  selector: 'app-dropdown-category',
  templateUrl: './dropdown-category.component.html',
  styleUrls: ['./dropdown-category.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class DropdownCategoryComponent {
  isOpen: boolean = false;
  tags: string[] = ['Cosmetics', 'Makeup', 'Course', 'Travel', 'Celebration', 'Culture', 'Holiday'];

  constructor(private elementRef: ElementRef) {
  }

  toggleOpen() {
    this.isOpen = !this.isOpen;
  }

  @HostListener('window:keyup', ['$event'])
  @HostListener('document:click', ['$event'])
  onEvent(event: KeyboardEvent | Event) {
    if (event instanceof KeyboardEvent && event.key === 'Escape') {
      this.isOpen = false;
    } else if (event instanceof Event) {
      const target = event.target as HTMLElement;
      if (this.isOpen && !this.elementRef.nativeElement.contains(target)) {
        this.isOpen = false;
      }
    }
  }
}
