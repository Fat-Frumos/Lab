function createUser(links, host) {
  const user = document.querySelector(".user");
  links.forEach((link) => user.appendChild(createUserLink(link, host)));
}

function createUserLink(data, host) {
  const link = createChild(data.a, host);
  if (data.span) {
    link.appendChild(createChild(data.span));
  }
  return link;
}

function createChild(child, host) {
  const element = document.createElement(child.name);
  if(child.href){
    element.setAttribute("href", host + child.href);
  }
  element.setAttribute("id", child.id);
  element.setAttribute("class", child.class);
  element.innerText = child.text;
  return element;
}

function createLink(data, host) {
  const menu = document.querySelector(".menu");

  const input = create("input", "class", "menu-open", "");
  const label = create("label", "class", "menu-open-button", "");

  input.setAttribute("id", "menu-open");
  input.setAttribute("type", "checkbox");
  input.setAttribute("name", "menu-open");
  label.setAttribute("for", "menu-open");

  for (let i = 1; i < 4; i++) {
    label.appendChild(create("span", "class", `hamburger hamburger-${i}`, ""));
  }

  menu.appendChild(input);
  menu.appendChild(label);

  data.forEach((item) => menu.appendChild(
      createMenuLink("a", "class", "material-symbols-outlined menu-item", item.text, host + item.href)
    )
  );
}

function createMenuLink(element, attr, value, text, href) {
  const child = create(element, attr, value, text);
  child.setAttribute("href", href);
  return child;
}

function create(element, attr, value, text) {
  const child = document.createElement(element);
  child.setAttribute(attr, value);
  child.textContent = text;
  return child;
}

function createText(element, text) {
  const child = document.createElement(element);
  child.textContent = text;
  return child;
}

function createImageChild(element, attr, value, path, alt) {
  const child = document.createElement(element);
  child.setAttribute(attr, value);
  child.src = path;
  child.alt = alt;
  return child;
}
