import {EventEmitter, Injectable} from '@angular/core';
import {Certificate} from '../model/Certificate';
import {Category} from "../interfaces/Category";
import {Tag} from "../model/Tag";
import {BehaviorSubject} from "rxjs";

@Injectable({
  providedIn: 'root',
})
export class LocalStorageService {
  private favorites$: BehaviorSubject<Certificate[]>;
  public cardCounter: EventEmitter<number> = new EventEmitter<number>();
  public favoriteCounter: EventEmitter<number> = new EventEmitter<number>();

  get favoriteCertificates$() {
    return this.favorites$.asObservable();
  }

  constructor() {
    this.favorites$ = new BehaviorSubject<Certificate[]>(this.getFavoriteCertificates());
  }

  public saveCertificates(certificates: Certificate[]): void {
    const saved: Certificate[] = this.getCertificates() || [];
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

  public getCertificates(): Certificate[] {
    const certificates = localStorage.getItem('certificates');
    return certificates ? JSON.parse(certificates) : [];
  }

  private sortCertificatesByCreationDate(certificates: Certificate[]): void {
    certificates.sort((a: Certificate, b: Certificate) =>
      new Date(b.createDate).getTime() - new Date(a.createDate).getTime()
    );
  }

  public getCertificatesSize(): number {
    return this.getCertificates().length;
  }

  public updateCertificate(updated: Certificate): void {
    const saved: Certificate[] = this.getCertificates();
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
    this.favorites$.next(this.getFavoriteCertificates());
  }

  public saveCertificate(certificate: Certificate): void {
    localStorage.removeItem('certificate');
    localStorage.setItem('certificate', JSON.stringify(certificate));
  }

  public saveTagsToLocalStorage(tags: Tag[]): void {
    localStorage.setItem('tags', JSON.stringify(tags));
  }

  public getTagsFromLocalStorage(): Category[] {
    const tags = localStorage.getItem('tags');
    return tags ? JSON.parse(tags) : [];
  }

  public getCheckoutIds(): string[] {
    return this.getCheckoutCertificates()
    .map((certificate) => certificate.id);
  }

  public getCheckoutCertificates(): Certificate[] {
    return this.getCertificates()
    .filter(certificate => certificate.checkout);
  }

  public getFavoriteCertificates(): Certificate[] {
    return this.getCertificates()
    .filter(certificate => certificate.favorite);
  }
}
