import {Component, Input} from '@angular/core';
import {Certificate} from "../../../model/Certificate";

@Component({
    selector: 'app-image',
    templateUrl: './image.component.html',
    styleUrls: ['./image.component.scss']
})
export class ImageComponent {
    @Input() certificate: Certificate = {} as Certificate;

    showDetails(certificate: Certificate): void {
        localStorage.removeItem("certificate");
        localStorage.setItem("certificate", JSON.stringify(certificate));
        window.location.href = "./pages/details.html";
    }
}

export function createImage(certificate: Certificate): ImageComponent {
    const component = new ImageComponent();
    component.certificate = certificate;
    return component;
}
