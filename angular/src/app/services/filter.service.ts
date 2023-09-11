import {Injectable} from '@angular/core';

@Injectable({
    providedIn: 'root'
})
export class FilterService {
    public toggleDropbox() {
        const dropdown = document.getElementById("dropdown-search");
        if (dropdown) {
            dropdown.classList.toggle("show");
        }
    }

    public filter(_category: string) {
        // const query = this.inputCategory!.value;
        // this.filterBy(query, category);
    }

    public category(header: string) {
        if (header === '') {
            header = 'All Categories';
        }
        document.getElementById('drop-header')!.innerText = header;
        this.toggleDropbox();
        this.filter(header);
    }

    public filterBy(_query: string, _category: string) {
        //TODO
    }
}
