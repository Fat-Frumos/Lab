import {
  Component,
  ElementRef,
  ViewChild,
  ViewEncapsulation
} from '@angular/core';
import {IMenuItem} from "../../../interfaces/IMenuItem";

@Component({
  selector: 'app-menu-button',
  templateUrl: './menu-button.component.html',
  styleUrls: ['./menu-button.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class MenuButtonComponent {

  @ViewChild('menuOpenCheckbox')
  menuOpenCheckbox!: ElementRef;
  @ViewChild('logoLabel')
  logoLabel!: ElementRef;
  @ViewChild('logoName')
  logoName!: ElementRef;

  public checkbox!: HTMLInputElement;
  public logo!: HTMLElement;
  public name!: HTMLElement;

  public getHostUrl(): string {
    return './pages/';
  }

  public menuItems: IMenuItem[] = [
    {text: "home", href: "../index.html"},
    {text: "playlist_add_check", href: "details.html"},
    {text: "add_to_drive", href: "add-new.html"},
    {text: "shopping_cart_checkout", href: "checkout.html"},
    {text: "person_add", href: "register.html"},
    {text: "login", href: "login.html"},
  ];

  public toggleCheckbox(): void {
    this.checkbox = this.menuOpenCheckbox.nativeElement;
    this.logo = this.logoLabel.nativeElement;
    this.name = this.logoName.nativeElement;

    if (this.checkbox.checked) {
      this.name.style.opacity = '1';
      this.name.style.zIndex = '1';
      this.logo.style.transform = 'none';
    } else {
      this.name.style.opacity = '0';
      this.name.style.zIndex = '-5';
      this.logo.style.transform = 'rotate(42deg)';
    }
    this.checkbox.checked = !this.checkbox.checked;
  }
}
