import {EventEmitter, Injectable} from '@angular/core';
import {Certificate} from '../model/Certificate';
import {Category} from "../interfaces/Category";
import {Tag} from "../model/Tag";

@Injectable({
  providedIn: 'root',
})
export class LocalStorageService {

  cardCounter: EventEmitter<number> = new EventEmitter<number>();
  favoriteCounter: EventEmitter<number> = new EventEmitter<number>();

  public getFromLocalStorage(key: string): any[] {
    const favoritesJSON = localStorage.getItem(key);
    return favoritesJSON ? JSON.parse(favoritesJSON) : [];
  }

  public saveCertificatesToLocalStorage(certificates: Certificate[]): void {
    const saved: Certificate[] = this.getCertificatesFromLocalStorage() || [];
    const unique: Certificate[] = this.removeDuplicate(saved, certificates);
    this.sortCertificatesByCreationDate(unique);
    localStorage.setItem('certificates', JSON.stringify(unique));
    console.log(unique.length);
  }

  private removeDuplicate(saved: Certificate[], loaded: Certificate[]): Certificate[] {
    return [...saved, ...loaded]
    .filter((a: Certificate, index: number, items: Certificate[]): boolean =>
      index === items.findIndex((b: Certificate): boolean => b.id === a.id)
    );
  }

  public getCertificatesFromLocalStorage(): Certificate[] {
    const certificates = localStorage.getItem('certificates');
    return certificates ? JSON.parse(certificates) : [];
  }

  private sortCertificatesByCreationDate(certificates: Certificate[]): void {
    certificates.sort((a: Certificate, b: Certificate) =>
      new Date(b.createDate).getTime() - new Date(a.createDate).getTime()
    );
  }

  public getCertificatesSize(): number {
    return this.getCertificatesFromLocalStorage().length;
  }

  public updateCertificateInLocalStorage(updated: Certificate): void {
    const saved: Certificate[] = this.getCertificatesFromLocalStorage();
    const index: number = saved.findIndex((certificate: Certificate): boolean =>
      certificate.id === updated.id);
    if (index !== -1) {
      saved[index] = updated;
      localStorage.setItem('certificates', JSON.stringify(saved));
      const cardCounter = saved.filter(card => card.checkout).length;
      const favoriteCounter = saved.filter(favorite => favorite.favorite).length;
      this.cardCounter.emit(cardCounter);
      this.favoriteCounter.emit(favoriteCounter);
    }
  }

  saveTagsToLocalStorage(tags: Tag[]): void {
    localStorage.setItem('tags', JSON.stringify(tags));
  }

  public getTagsFromLocalStorage(): Category[] {
    const tags = localStorage.getItem('tags');
    return tags ? JSON.parse(tags) : [];
  }
}
