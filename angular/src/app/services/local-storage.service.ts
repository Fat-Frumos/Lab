import {EventEmitter, Injectable} from '@angular/core';
import {ICertificate} from '../model/entity/ICertificate';
import {ICategory} from "../interfaces/ICategory";
import {ITag} from "../model/entity/ITag";
import {IUser} from "../model/entity/IUser";
import {User} from "../model/user";
import {Certificate} from "../model/certificate";

@Injectable()
export class LocalStorageService {
  public favoriteCounter: EventEmitter<number> = new EventEmitter<number>();
  public cardCounter: EventEmitter<number> = new EventEmitter<number>();
  private TOKEN_KEY: string = 'user';
  private TOKEN: string = this.TOKEN_KEY + '_certificates';

  public saveCertificates(certificates: ICertificate[]): void {  // TODO orders cards
    const saved: ICertificate[] = this.getCertificates() || [];
    const unique: ICertificate[] = this.removeDuplicate(saved, certificates);
    this.sortCertificatesByCreationDate(unique);
    localStorage.setItem(this.TOKEN, JSON.stringify(unique));
  }

  private removeDuplicate(saved: ICertificate[], loaded: ICertificate[]): ICertificate[] {
    return [...saved, ...loaded]
    .filter((a: ICertificate, index: number, items: ICertificate[]): boolean =>
      index === items.findIndex((b: ICertificate): boolean => b.id === a.id)
    );
  }

  public getCertificates(): ICertificate[] { //TODO 25 limit on the same page
    const certificates = localStorage.getItem(this.TOKEN);
    return certificates ? JSON.parse(certificates) : [];
  }

  private sortCertificatesByCreationDate(certificates: ICertificate[]): void {
    certificates.sort((a: ICertificate, b: ICertificate) =>
      new Date(b.createDate).getTime() - new Date(a.createDate).getTime()
    );
  }

  public getCertificatesSize(): number {
    return this.getCertificates().length;
  }

  public updateCertificate(updated: ICertificate): void {
    const saved: ICertificate[] = this.getCertificates();
    const index: number = saved.findIndex((certificate: ICertificate): boolean =>
      certificate.id === updated.id);
    if (index !== -1) {
      saved[index] = updated;
      localStorage.setItem(this.TOKEN, JSON.stringify(saved));
      const cardCounter = saved.filter(card => card.checkout).length;
      const favoriteCounter = saved.filter(favorite => favorite.favorite).length;
      this.cardCounter.emit(cardCounter);
      this.favoriteCounter.emit(favoriteCounter);
    }
  }

  public saveTagsToLocalStorage(tags: ITag[]): void {
    localStorage.setItem('tags', JSON.stringify(tags));
  }

  public getTagsFromLocalStorage(): ICategory[] {
    const tags = localStorage.getItem('tags');
    return tags ? JSON.parse(tags) : [];
  }

  public getCheckoutCertificates(): ICertificate[] {
    return this.getCertificates()
    .filter(certificate => certificate.checkout);
  }

  public getFavoriteCertificates(): ICertificate[] {
    return this.getCertificates()
    .filter(certificate => certificate.favorite);
  }

  saveUser(user: IUser) {
    this.TOKEN_KEY = user.username;
    localStorage.setItem(this.TOKEN_KEY, JSON.stringify(user));
    console.log(this.TOKEN_KEY);
  }

  getUser(): IUser {
    const user = localStorage.getItem(this.TOKEN_KEY);
    return user ? JSON.parse(user) : new User();
  }

  removeUser() {
    localStorage.removeItem(this.TOKEN_KEY);
    this.TOKEN_KEY = 'user';
    console.log(this.TOKEN_KEY);
  }

  getCertificateById(id: string): ICertificate {
    const certificate = this.getCertificates()
    .find((item) => item.id.toString() === id);
    return !certificate ? new Certificate() : certificate;
  }
}
