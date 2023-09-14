import {Injectable} from '@angular/core';
import {Certificate} from '../model/Certificate';

@Injectable({
  providedIn: 'root',
})
export class LocalStorageService {

  public getFromLocalStorage(key: string): any[] {
    const favoritesJSON = localStorage.getItem(key);
    return favoritesJSON ? JSON.parse(favoritesJSON) : [];
  }

  public saveCertificatesToLocalStorage(certificates: Certificate[]): void {
    const saved = this.getCertificatesFromLocalStorage() || [];
    const unique = this.removeDuplicate(saved, certificates);
    const ids: string[] = certificates.map((certificate) => certificate.id);
    console.log(ids);
    this.sortCertificatesByCreationDate(unique);
    localStorage.setItem('certificates', JSON.stringify(unique));
  }

  private removeDuplicate(
    saved: Certificate[],
    certificates: Certificate[]
  ): Certificate[] {
    return [...saved, ...certificates].filter(
      (a: Certificate, index: number, items: Certificate[]): boolean =>
        index === items.findIndex((c: Certificate): boolean => c.id === a.id)
    );
  }

  public getCertificatesFromLocalStorage(): Certificate[] {
    const savedCertificates = localStorage.getItem('certificates');
    return savedCertificates ? JSON.parse(savedCertificates) : [];
  }

  private sortCertificatesByCreationDate(certificates: Certificate[]): void {
    certificates.sort((a: Certificate, b: Certificate) =>
      new Date(b.createDate).getTime() - new Date(a.createDate).getTime()
    );
  }

  getCertificatesSize(): number {
    return this.getCertificatesFromLocalStorage().length;
  }
}
