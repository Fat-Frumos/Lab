import {
    Component,
    EventEmitter,
    Input,
    Output,
    ViewChild,
    ViewContainerRef
} from '@angular/core';
import {FilterService} from "../../../services/filter.service";

@Component({
    selector: 'app-nav-dropdown',
    templateUrl: './dropdown.component.html',
    styleUrls: ['./dropdown.component.scss']
})
export class DropdownComponent {

    @Input()
    public category: string;

    @ViewChild('inputSearch', {read: ViewContainerRef})
    searchInput!: ViewContainerRef;

    @Output() public dropdownToggle: EventEmitter<void>;
    @Output() public categoryClick: EventEmitter<string>;
    @Output() public searchInputChange: EventEmitter<string>;

    constructor(private service: FilterService) {
        this.category = 'All Categories';
        this.dropdownToggle = new EventEmitter<void>()
        this.categoryClick = new EventEmitter<string>();
        this.searchInputChange = new EventEmitter<string>();
    }

    onCategoryClick(classList: DOMTokenList, query: string): void {
        this.service.category.tag = query;
        this.category = (query === '') ? 'All Categories' : query;
        this.categoryClick.emit(query);
        this.service.filter();
        classList.toggle("show");
        this.dropdownToggle.emit();
    }

    toggleDropdown(classList: DOMTokenList): void {
        console.log(this.searchInput)
        classList.toggle("show");
        this.dropdownToggle.emit();
    }

    categoryNames: string[] = [
        "Cosmetics",
        "Makeup",
        "Celebration",
        "Travel",
        "Self-care",
        "Culture",
        "Holiday",
        "Anniversary",
    ];

    // categories: Category [] = [
    //   {name: "Travel", tag: "Travel"},
    //   {name: "Celebration", tag: "Celebration"},
    //   {name: "Cosmetics", tag: "Cosmetics"},
    //   {name: "Holiday", tag: "Holiday"},
    //   {name: "Makeup", tag: "Makeup"},
    // ];
}
