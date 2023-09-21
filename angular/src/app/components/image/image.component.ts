import {
  Component, EventEmitter,
  Input, OnInit, Output,
  ViewEncapsulation
} from '@angular/core';

@Component({
  selector: 'app-image',
  templateUrl: './image.component.html',
  styleUrls: ['./image.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class ImageComponent implements OnInit {
  @Input() src: string = '';
  @Input() width: number = 0;
  @Input() height: number = 0;
  @Output() action: EventEmitter<void> = new EventEmitter<void>();


  onImageClick() {
    this.action.emit();
  }

  ngOnInit(): void {
    if (this.src.includes('300/')) {
      if (this.width > 300) {
        this.src = this.src.replace('300', String(600))
      } else if (this.width < 300) {
        this.src = this.src.replace('300', String(200))
      }
    }
  }
}
