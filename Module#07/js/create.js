function createImage(imageSrc) {
    const div = document.createElement("div");
    div.className = "medium-image";
    const image = document.createElement("img");
    image.src = imageSrc;
    div.appendChild(image);
    return div;
  }
  
  function createGroup(name) {
    const group = document.createElement("div");
    group.className = "certificate-grp";
    const h2 = document.createElement("h2");
    h2.className = "certificate-name";
    h2.textContent = name;
    group.appendChild(h2);
    const iconSpan = document.createElement("span");
    iconSpan.className = "material-symbols-outlined certificate-icon";
    iconSpan.textContent = "favorite";
    group.appendChild(iconSpan);
    return group;
  }
  
  function createPrice(mainPrice) {
    const group = document.createElement("div");
    group.className = "certificate-grp";
    const price = document.createElement("p");
    price.className = "price";
    price.textContent = mainPrice;
    group.appendChild(price);
    const addButton = document.createElement("button");
    addButton.className = "add-button";
    addButton.value = "";
    addButton.type = "submit";
    const addLabel = document.createElement("p");
    addLabel.className = "add-to-cart-text";
    addLabel.textContent = "Add to Cart";
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
    const currentDate = new Date();
    const lastUpdateDate = new Date(lastUpdate);
    const timeDifferenceMs = currentDate - lastUpdateDate;
    const expiresInDays = Math.floor(timeDifferenceMs / (1000 * 60 * 60 * 24));
    expires.textContent = `Expires in ${expiresInDays} days`;
    group.appendChild(expires);
    return group;
  }
  
  function createCards(certificates) {
    const certificatesList = document.getElementById("certificates-list");
    certificates.forEach((certificate) => {
      const card = document.createElement("form");
      card.action = "./pages/details.html";
      card.className = "certificate-card";
      const params = new Map();
      card.setAttribute(
        "data-tags",
        certificate.tags.map((tag) => tag.name).join(", ")
      );
      params.set("image", createImage(`${certificate.path}`));
      params.set("name", createGroup(`${certificate.name}`));
      params.set("desc", createDescription(
          `${certificate.description}`,
          `${certificate.lastUpdateDate}`
        )
      );
      params.set("price", createPrice(`$${certificate.price}`));
  
      for (const element of params.values()) {
        card.appendChild(element);
      }
      certificatesList.appendChild(card);
    });
  }
  