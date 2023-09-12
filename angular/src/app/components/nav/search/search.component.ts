import {
    Component,
    EventEmitter,
    OnInit,
    Output,
    ViewChild, ViewContainerRef
} from '@angular/core';
import {FilterService} from "../../../services/filter.service";

@Component({
    selector: 'app-nav-search',
    templateUrl: './search.component.html',
    styleUrls: ['./search.component.scss']
})
export class SearchComponent implements OnInit{
    @Output() searchQueryChange: EventEmitter<string>;

    @ViewChild('searchInput', {read: ViewContainerRef})
    searchInput!: ViewContainerRef;

    constructor(private service: FilterService) {
        this.searchQueryChange = new EventEmitter<string>()
    }

    onInputChange(event: Event): void {
        console.log(this.searchInput)
        const query = (event.target as HTMLInputElement).value;
        this.service.category.name = query;
        this.service.filter();
        this.searchQueryChange.emit(query);
    }

    ngOnInit(): void {
    }
}
