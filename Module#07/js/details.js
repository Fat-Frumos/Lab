function createDetails(certificate) {
    const container = document.getElementById("certificates-container");
    const detailsWrapper = create("div", "class", "details-wrapper", "");
    const detailsGroup = create("div", "class", "details-group", "");
    const image = createImageChild("img", "class", "image", certificate.path.replace('/300/', '/600/'), "");
    const itemDetails = create("div", "class", "item-details", "");
    const moreDescription = create("div", "class", "more-description", "");
    const h3 = createText("h3", certificate.name);
    const p = createText("p", certificate.description);
    const timeLeft = create("div", "class", "time-left", "Time Left To Buy");
    const time = create("div", "class", "time",`${certificate.duration} ${certificate.duration > 1 ? "days" : "day"}`);
    const cart = create("div", "class", "cart");
    const price = create("div", "class", "price", `$${certificate.price}`);
    const itemTitle = create("div", "class", "item-title", "Item Detailed Description");

    detailsGroup.appendChild(image);
    moreDescription.appendChild(h3);
    moreDescription.appendChild(p);
    moreDescription.appendChild(timeLeft);
    moreDescription.appendChild(time);
    cart.appendChild(price);

    const addToCart = document.createElement("a");
    addToCart.className = "add-to-cart";
    addToCart.href = "checkout.html";

    const name = localStorage.getItem("user");
    const carts = localStorage.getItem(`cart_${name}`);
    const addIcon = create("span", "class", "material-symbols-outlined add-icon", "");

    if(carts && carts.includes(certificate.id)) {
        addToCart.setAttribute("onclick", `goToCarts()`);
        addToCart.textContent = "Go to Cart";
        addIcon.setAttribute("onclick", `addToCarts(${certificate.id})`);
        addIcon.textContent = "remove_shopping_cart";
      } else {
        addToCart.setAttribute("onclick", `addToCarts(${certificate.id})`);
        addToCart.textContent = "Add to Cart";
        addIcon.textContent = "shopping_cart";
      
    }

    addToCart.appendChild(addIcon);
    cart.appendChild(addToCart);
    moreDescription.appendChild(cart);
    itemDetails.appendChild(moreDescription);
    detailsGroup.appendChild(itemDetails);
    detailsWrapper.appendChild(detailsGroup);
    detailsWrapper.appendChild(itemTitle);

    const description = document.createElement("div");
    description.className = "description";
    description.innerHTML = certificate.description;
    detailsWrapper.appendChild(description);

    container.appendChild(detailsWrapper);
  }