function createImage(certificate) {
  const div = document.createElement("div");
  div.className = "medium-image";
  const image = document.createElement("img");
  image.src = certificate.path;
  image.setAttribute("onclick", `showDetails(${JSON.stringify(certificate)})`);
  image.alt = "medium";
  image.width = 300;
  image.height = 300;
  div.appendChild(image);
  return div;
}

function createGroup(name, id) {
  const group = document.createElement("div");
  group.className = "certificate-grp";
  const h2 = document.createElement("h2");
  h2.className = "certificate-name";
  h2.textContent = name;
  group.appendChild(h2);
  const iconSpan = document.createElement("span");
  iconSpan.className = "material-symbols-outlined certificate-icon";
  iconSpan.id = id;
  iconSpan.textContent = "favorite";
  iconSpan.setAttribute("onclick", "addToFavorite(" + id + ")");
  group.appendChild(iconSpan);
  return group;
}

function createPrice(mainPrice, id) {
  const group = document.createElement("div");
  group.className = "certificate-grp";
  const price = document.createElement("p");
  price.className = "price";
  price.textContent = mainPrice;
  group.appendChild(price);
  const addButton = document.createElement("button");
  addButton.className = "add-button favorite";
  addButton.id = "add-button-" + id;
  addButton.value = "";
  addButton.type = "";
  const addLabel = document.createElement("p");
  addLabel.className = "add-to-cart-text";
  addLabel.textContent = "Add to Cart";
  addLabel.setAttribute("onclick", "addToCarts(" + id + ")");
  addButton.appendChild(addLabel);
  group.appendChild(addButton);
  return group;
}

function createDescription(desc, lastUpdate) {
  const group = document.createElement("div");
  group.className = "certificate-grp";
  const description = document.createElement("p");
  description.className = "certificate-description";
  description.textContent = desc;
  group.appendChild(description);
  const expires = document.createElement("p");
  expires.className = "expires";
  const expiresInDays = `${lastUpdate} ${lastUpdate > 1 ? "days" : "day"}`;
  expires.textContent = `Expires in ${expiresInDays}`;
  group.appendChild(expires);
  return group;
}

function createCards(certificates) {
  const certificatesList = document.getElementById("certificates-list");
  certificates.forEach((certificate) => {
    const card = document.createElement("div");
    card.className = "certificate-card";
    const params = new Map();
    const tags = certificate.tags.map((tag) => tag.name).join(", ");
    card.setAttribute("data-tags", tags);
    params.set("image", createImage(certificate));
    params.set("name", createGroup(`${certificate.name}`, `${certificate.id}`));
    params.set("desc", createDescription(`${certificate.description}`, `${certificate.duration}`));
    params.set("price", createPrice(`$${certificate.price}`, `${certificate.id}`));

    for (const element of params.values()) {
      card.appendChild(element);
    }
    certificatesList.appendChild(card);
  });
  updateFavoriteIcons();
}

function updateFavoriteIcons() {
  const username = localStorage.getItem("user");
  const userFavorites = getFromLocalStorage(`favorite_${username}`) || [];
  userFavorites.forEach((id) => {
    const icon = document.getElementById(id);
    if (icon) {
      icon.innerText = "heart_plus";
    }
  });

  const buttons = getFromLocalStorage(`cart_${username}`) || [];

  buttons.forEach((id) => {
    const button = document.getElementById("add-button-" + id);
    if (button) {
      const classes = button.classList;
      classes.remove(classes[classes.length - 1]);
      classes.add("heart_plus");
    }
  });
}

