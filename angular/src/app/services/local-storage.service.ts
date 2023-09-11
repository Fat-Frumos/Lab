import {Injectable} from '@angular/core';
import {Certificate} from "../components/model/Certificate";

@Injectable({
    providedIn: 'root'
})
export class LocalStorageService {

    public getFromLocalStorage(key: string): any[] {
        const favoritesJSON = localStorage.getItem(key);
        return favoritesJSON ? JSON.parse(favoritesJSON) : [];
    }

    public saveCertificatesToLocalStorage(certificates: Certificate[]): void {
        const saved = this.getCertificatesFromLocalStorage() || [];
        const unique = this.removeDuplicate(saved, certificates);
        this.sortCertificatesByCreationDate(unique);
        localStorage.setItem('certificates', JSON.stringify(unique));
    }

    private removeDuplicate(saved: Certificate[], certificates: Certificate[]): Certificate[] {
        return [...saved, ...certificates]
            .filter((a, b, self) => b === self
                .findIndex((c) => c.id === a.id));
    }

    public getCertificatesFromLocalStorage(): Certificate[] {
        const savedCertificates = localStorage.getItem('certificates');
        return savedCertificates ? JSON.parse(savedCertificates) : [];
    }

    private sortCertificatesByCreationDate(certificates: Certificate[]): void {
        certificates.sort((a, b) =>
            new Date(b.createDate).getTime() - new Date(a.createDate).getTime());
    }
}
