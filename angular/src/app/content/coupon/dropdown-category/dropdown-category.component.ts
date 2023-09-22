import {
  Component,
  ElementRef,
  forwardRef,
  HostListener,
  ViewEncapsulation
} from '@angular/core';
import {ControlValueAccessor, NG_VALUE_ACCESSOR} from "@angular/forms";

@Component({
  selector: 'app-dropdown-category',
  templateUrl: './dropdown-category.component.html',
  styleUrls: ['./dropdown-category.component.scss'],
  encapsulation: ViewEncapsulation.None,
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => DropdownCategoryComponent),
      multi: true
    }
  ],
})
export class DropdownCategoryComponent implements ControlValueAccessor {
  isOpen: boolean = false;
  tags: string[] = ['Cosmetics', 'Makeup', 'Course', 'Travel', 'Celebration', 'Culture', 'Holiday'];
  value: string[] = [];
  onChange = (_: any) => {
  };
  onTouched = () => {
  };

  writeValue(value: string[]): void {
    this.value = value || [];
  }

  registerOnChange(fn: any): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: any): void {
    this.onTouched = fn;
  }

  toggleTag(tag: string): void {
    const index = this.value.indexOf(tag);
    if (index >= 0) {
      this.value.splice(index, 1);
    } else {
      this.value.push(tag);
    }
    this.onChange(this.value);
    this.onTouched();
  }

  toggleOpen() {
    this.isOpen = !this.isOpen;
  }

  constructor(private elementRef: ElementRef) {
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
