import {Component, Input} from '@angular/core';
import {FavoriteService} from "../../../services/favorite.service";
import {Certificate} from "../../../model/Certificate";

@Component({
    selector: 'app-price',
    templateUrl: './price.component.html',
    styleUrls: ['./price.component.scss']
})
export class PriceComponent {
    @Input() id: string;
    @Input() mainPrice: number;

    constructor(private favorite: FavoriteService) {
        this.id = '';
        this.mainPrice = 0;
    }

    get buttonText(): string {
        return `Add to Cart - ${this.id}`;
    }

    public addToCarts(id: string) {
        this.favorite.add(id, "cart");
    }
}

export function createPrice(certificate: Certificate, favorite: FavoriteService): PriceComponent {
    const component = new PriceComponent(favorite);
    component.id = certificate.id;
    component.mainPrice = certificate.price;
    return component;
}
