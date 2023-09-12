import {Component} from '@angular/core';
import {Link} from "../../../interfaces/Link";

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.scss']
})
export class UserComponent {

  public userLinks: Link[] = [
    {
      a: {
        name: "a",
        href: "favorites.html",
        id: "",
        class: "material-symbols-outlined shop-icon",
        text: "favorite",
      },
      span: {name: "span", id: "favorite-count", class: "counter", text: ""},
    },
    {
      a: {
        name: "a",
        href: "checkout.html",
        id: "",
        class: "material-symbols-outlined shop-icon",
        text: "shopping_cart",
      },
      span: {name: "span", id: "cart-count", class: "counter", text: ""},
    },
    {
      a: {name: "a", href: "login.html", id: "", class: "login", text: ""},
      span: {name: "span", id: "user-span", class: "", text: ""},
    },
    {
      a: {
        name: "a",
        href: "login.html",
        id: "login-link",
        class: "login",
        text: "Login",
      },
      span: {name: "span", id: "", class: "", text: ""},
    },
    {
      a: {name: "a", href: "/", id: "", class: "login", text: "|"},
      span: {name: "span", id: "", class: "", text: ""},
    },
    {
      a: {name: "a", href: "register.html", id: "", class: "signup", text: "SignUp"},
      span: {name: "span", id: "", class: "", text: ""},
    },
  ];
}
