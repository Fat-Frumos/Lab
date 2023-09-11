import {Component, Input} from '@angular/core';
import {Certificate} from "../../model/Certificate";

@Component({
    selector: 'app-description',
    templateUrl: './description.component.html',
    styleUrls: ['./description.component.scss']
})
export class DescriptionComponent {

    @Input() description: string;
    @Input() lastUpdate: Date;

    constructor() {
        this.description = '';
        this.lastUpdate = new Date();
    }

    calculateDaysDifference(lastUpdate: Date): string {
        const currentDate = new Date();
        const differenceInMilliseconds = lastUpdate.getTime() - currentDate.getTime();
        const differenceInDays = Math.ceil(differenceInMilliseconds / (1000 * 60 * 60 * 24));
        return differenceInDays > 1 ? `${differenceInDays} days` : '1 day';
    }
}

export function createDescription(certificate: Certificate): DescriptionComponent {
    const component = new DescriptionComponent();
    component.description = certificate.description;
    component.lastUpdate = certificate.lastUpdate;
    return component;
}
