import {
  AfterViewInit,
  Component,
  ElementRef,
  ViewChild,
  ViewEncapsulation
} from '@angular/core';
import {IMessage} from "../../../../interfaces/IMessage";

@Component({
  selector: 'app-menu-button',
  templateUrl: './menu-button.component.html',
  styleUrls: ['./menu-button.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class MenuButtonComponent implements AfterViewInit {

  @ViewChild('menuOpenCheckbox', {static: false})
  menuCheckbox!: ElementRef<HTMLInputElement>;
  @ViewChild('logoLabel')
  logoLabel!: ElementRef<HTMLInputElement>;
  @ViewChild('logoName')
  logoName!: ElementRef<HTMLInputElement>;

  public checkbox!: HTMLInputElement;
  public logo!: HTMLElement;
  public name!: HTMLElement;

  public menuItems: IMessage[] = [
    {name: "home", href: "/"},
    {name: "playlist_add_check", href: "details"},
    {name: "add_to_drive", href: "coupon"},
    {name: "shopping_cart_checkout", href: "checkout"},
    {name: "person_add", href: "signup"},
    {name: "login", href: "login"},
  ];

  ngAfterViewInit() {
    if (this.menuCheckbox) {
      this.menuCheckbox.nativeElement.checked = false;
    }
  }

  public toggleCheckbox(): void {
    this.checkbox = this.menuCheckbox.nativeElement;
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
