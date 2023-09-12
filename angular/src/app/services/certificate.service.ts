import {Injectable} from '@angular/core';
import {Certificate} from "../model/Certificate";
import {Tag} from "../model/Tag";
import {createImage} from "../components/main/image/image.component";
import {createGroup} from "../components/main/group/group.component";
import {createPrice} from "../components/main/price/price.component";
import {createDescription} from "../components/main/description/description.component";
import {FavoriteService} from "./favorite.service";

@Injectable({
    providedIn: 'root'
})
export class CertificateService {

    constructor(private favorite: FavoriteService) {
    }

    public create(element: string, attr: string, value: string, text: string): HTMLElement {
        const child = document.createElement(element);
        child.setAttribute(attr, value);
        child.textContent = text;
        return child;
    }

    public createMenuLink(element: string, attr: string, value: string, text: string, href: string): HTMLAnchorElement {
        const child = this.create(element, attr, value, text);
        child.setAttribute("href", href);
        return child as HTMLAnchorElement;
    }

    public createCards(certificates: Certificate[]) {
        const certificatesList = document.getElementById("certificates-list");
        if (certificatesList) {
            certificates.forEach((certificate) => {
                const card = document.createElement("div");
                card.className = "certificate-card";
                const params = new Map();
                const tags = [...certificate.tags].map((tag: Tag) => tag.name).join(", ");
                card.setAttribute("data-tags", tags);
                params.set("image", createImage(certificate));
                params.set("name", createGroup(certificate,this.favorite));
                params.set("desc", createDescription(certificate));
                params.set("price", createPrice(certificate,this.favorite));

                for (const element of params.values()) {
                    card.appendChild(element);
                }
                certificatesList.appendChild(card);
            });
        }
        this.favorite.updateFavoriteIcons();
    }
}
