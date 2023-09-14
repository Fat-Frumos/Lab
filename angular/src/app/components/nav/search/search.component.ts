import {
  Component,
  EventEmitter,
  Output,
  ViewEncapsulation
} from '@angular/core';
import {FilterService} from "../../../services/filter.service";

@Component({
  selector: 'app-nav-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class SearchComponent {
  @Output()
  searchQueryChange: EventEmitter<string>;

  constructor(private service: FilterService) {
    this.searchQueryChange = new EventEmitter<string>()
  }

  onInputChange(event: Event): void {
    const query = (event.target as HTMLInputElement).value;
    this.service.criteria.name = query;
    this.service.filter();
    this.searchQueryChange.emit(query);
  }
}
