import { Injectable } from '@angular/core';
import { LocalStorageService } from './local-storage.service';
import { Cart } from '../model/Cart';
import { IFavorite } from '../interfaces/IFavorite';

@Injectable({
  providedIn: 'root',
})
export class FavoriteService implements IFavorite {
  constructor(private storage: LocalStorageService) {}

  public updateFavoriteIcons(): void {
    const username = localStorage.getItem('user');
    const userFavorites =
      this.storage.getFromLocalStorage(`favorite_${username}`) || [];
    userFavorites.forEach((id: number) => {
      const icon = document.getElementById(String(id));
      if (icon) {
        icon.innerText = 'heart_plus';
      }
    });

    const carts: Cart[] =
      this.storage.getFromLocalStorage(`cart_${username}`) || [];
    carts.forEach((cart: Cart): void => {
      const button = document.getElementById(
        'add-button-' + cart.certificate.id
      );
      if (button) {
        const classes = button.classList;
        classes.remove(classes[classes.length - 1]);
        classes.add('heart_plus');
      }
    });
  }

  public add(id: string, name: string) {
    let text: string;
    const username = localStorage.getItem('user') ?? '';
    const storage =
      this.storage.getFromLocalStorage(`${name}_${username}`) || [];
    if (!storage.includes(id)) {
      storage.push(id);
      text = this.save(username, storage, name);
    } else {
      text = this.remove(username, storage, name, id);
    }

    if (name === 'favorite') {
      const element = document.getElementById(id);
      if (element) {
        element.innerText = text;
      }
    }

    if (name === 'cart') {
      let htmlElement = document.getElementById('add-button-' + id);
      if (htmlElement) {
        const classes = htmlElement.classList;
        classes.remove(classes[classes.length - 1]);
        classes.add(text);
      }
    }
    this.counter();
  }

  private save(username: string, storage: string[], name: string): string {
    localStorage.setItem(`${name}_${username}`, JSON.stringify(storage));
    return 'heart_plus';
  }

  private remove(
    username: string,
    favorites: string[],
    name: string,
    id: string
  ): string {
    const index = favorites.indexOf(id);
    if (index !== -1) {
      favorites.splice(index, 1);
      this.save(username, favorites, name);
      return 'favorite';
    }
    return ''; //TODO
  }

  public counter(): void {
    const username = localStorage.getItem('user');
    if (username !== null) {
      const name = username.charAt(0).toUpperCase() + username.slice(1);
      const userSpan = document.getElementById('user-span');
      if (userSpan) {
        userSpan.innerHTML = name ? `${name}&nbsp;&nbsp;|` : '';
      }
    }
    this.count(username, 'favorite');
    this.count(username, 'cart');
  }

  private count(username: string | null, prefix: string): void {
    const storage =
      this.storage.getFromLocalStorage(`${prefix}_${username}`) || [];
    const counter = document.getElementById(`${prefix}-count`);
    if (storage.length === 0) {
      if (counter) {
        counter.style.display = 'none';
      }
    } else {
      if (counter) {
        counter.style.display = 'block';
        counter.textContent = storage.length.toString();
      }
    }
  }
}
