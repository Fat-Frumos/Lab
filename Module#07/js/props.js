const menuItems = [
  { text: "home", href: "../index.html" },
  { text: "playlist_add_check", href: "details.html" },
  { text: "add_to_drive", href: "addnew.html" },
  { text: "shopping_cart_checkout", href: "checkout.html" },
  { text: "person_add", href: "register.html" },
  { text: "login", href: "login.html" },
];

const userLinks = [
  {
    a: {
      name: "a",
      href: "favorites.html",
      id: "",
      class: "material-symbols-outlined shop-icon",
      text: "favorite",
    },
    span: { name: "span", id: "favorite-count", class: "counter", text: "" },
  },
  {
    a: {
      name: "a",
      href: "checkout.html",
      id: "",
      class: "material-symbols-outlined shop-icon",
      text: "shopping_cart",
    },
    span: { name: "span", id: "cart-count", class: "counter", text: "" },
  },
  {
    a: { name: "a", href: "login.html", id: "", class: "login", text: "" },
    span: { name: "span", id: "user-span", class: "", text: "" },
  },
  {
    a: {
      name: "a",
      href: "login.html",
      id: "login-link",
      class: "login",
      text: "Login",
    },
    span: { name: "span", id: "", class: "", text: "" },
  },
  {
    a: { name: "a", href: "/", id: "", class: "login", text: "|" },
    span: { name: "span", id: "", class: "", text: "" },
  },
  {
    a: { name: "a", href: "register.html", id: "", class: "signup", text: "" },
    span: { name: "span", id: "", class: "", text: "SignUp" },
  },
];

const categoryNames = [
  "Cosmetics",
  "Makeup",
  "Celebration",
  "Travel",
  "Self-care",
  "Culture",
  "Holiday",
  "Anniversary",
];

const categories = [
  { name: "Travel", tag: "Travel" },
  { name: "Celebration", tag: "Celebration" },
  { name: "Cosmetics", tag: "Cosmetics" },
  { name: "Holiday", tag: "Holiday" },
  { name: "Makeup", tag: "Makeup" },
];
