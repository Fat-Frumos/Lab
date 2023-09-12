import {Component, Input} from '@angular/core';
import {FavoriteService} from "../../../services/favorite.service";
import {Certificate} from "../../../model/Certificate";

@Component({
    selector: 'app-group',
    templateUrl: './group.component.html',
    styleUrls: ['./group.component.scss']
})
export class GroupComponent {
    @Input() id: string;
    @Input() name: string;

    constructor(private favorite: FavoriteService) {
        this.id = '';
        this.name = '';
    }

    public addToFavorite(id: string) {
        this.favorite.add(id, "favorite");
    }
}

export function createGroup(certificate: Certificate, service: FavoriteService): GroupComponent {
    const component = new GroupComponent(service);
    component.id = certificate.id;
    component.name = certificate.name;
    return component;
}
