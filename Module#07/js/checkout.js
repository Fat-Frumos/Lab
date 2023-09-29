let totalAmount = 0;
const items = getFromLocalStorage("certificates") || [];
let cartsIds = localStorage.getItem(`cart_${username}`);

if (cartsIds !== "") {
  let coupons = items.filter((item) => JSON.parse(cartsIds).includes(item.id));
  if (coupons.length) {
    createFavorives(coupons);
    createBottom();
  } else {
    showMessage("Cart empty Redirecting to main page", "gray");
    redirect(2000, "../index.html");
  }
  console.log(cartsIds);
  console.log(coupons);
}

function createFavorives(coupons) {
  const container = document.querySelector(".checkout-wrapper");
  coupons.forEach((coupon) => {
    const couponItem = create("div", "class", "item-coupon", "");
    couponItem.setAttribute(
      "onclick",
      `showDetails(${JSON.stringify(coupon)}, '')`
    );
    const image = document.createElement("img");
    image.className = "image-small";
    if (coupon.path.includes("/300/")) {
      image.src = coupon.path.replace("/300/", "/600/");
    } else {
      image.src = coupon.path;
    }
    image.alt = coupon.alt;
    const article = document.createElement("article");
    const header = create("h3", "class", "coupon-name", coupon.name);
    const desc = create("p", "class", "coupon-desc", coupon.description);
    const price = create("div", "class", "coupon-price", "$" + coupon.price);

    totalAmount += coupon.price;
    article.appendChild(header);
    article.appendChild(desc);
    couponItem.appendChild(image);
    couponItem.appendChild(article);
    couponItem.appendChild(price);
    container.appendChild(couponItem);
  });
}

function createBottom() {
  const checkoutWrapper = document.querySelector(".checkout-wrapper");
  const divider = create("hr", "class", "divider", "");
  const checkoutConfirm = create("div", "class", "checkout-confirm", "");
  const totalPriceGroup = create("div", "class", "checkout-grp", "");
  const totalLabel = create("div", "class", "coupon-total-price", "Total");
  const totalPrice = create(
    "div",
    "class",
    "coupon-total-price",
    "$" + totalAmount
  );
  const buttonsGroup = create("div", "class", "checkout-grp", "");
  const backSpan = create("span", "class", "button-label", "Back");
  const checkoutButton = create(
    "button",
    "class",
    "button-save submit_btn",
    ""
  );
  const checkoutButtonSpan = create(
    "span",
    "class",
    "button-label",
    "Checkout"
  );
  checkoutButtonSpan.setAttribute(
    "onclick",
    `sendOrders("${username}", ${cartsIds})`
  );
  const backButton = create("button", "class", "button-back submit_btn", "");
  backButton.type = "reset";
  backButton.setAttribute("onclick", "goBack()");

  checkoutWrapper.appendChild(divider);
  totalPriceGroup.appendChild(totalLabel);
  totalPriceGroup.appendChild(totalPrice);
  backButton.appendChild(backSpan);
  checkoutButton.appendChild(checkoutButtonSpan);
  buttonsGroup.appendChild(backButton);
  buttonsGroup.appendChild(checkoutButton);
  checkoutConfirm.appendChild(totalPriceGroup);
  checkoutConfirm.appendChild(buttonsGroup);
  checkoutWrapper.appendChild(checkoutConfirm);
}

function sendOrders(username, certificateIds) {
  const url = `${host}/orders/${username}?certificateIds=${certificateIds}`;
  const accessToken = localStorage.getItem("accessToken");
  console.log(url);
  console.log(accessToken);
 const wrapper = document.getElementById("container");
 const spinner = document.getElementById("loading-indicator");
 wrapper.style.display = "none";
 spinner.style.display = "block";
 
  const requestBody = {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${accessToken}`,
    },
    body: JSON.stringify({
      certificateIds: certificateIds.join(","),
    }),
  };

  fetch(url, requestBody)
    .then((response) => {
      if (!response.ok) {
        if (response.status === 401) {
          throw new Error(`Failed to send orders: Unauthorized user`);
        } else {
          throw new Error("Failed to send orders" + response.status);
        }
      }
      return response.json();
    })
    .then((data) => {
      showMessage("Orders sent successfully", "green");
      localStorage.setItem(`cart_${username}`, []);
      redirect(1000, "/");
    })
    .catch((error) => {
      showMessage(error.message, "red");
    });
  spinner.style.display = "none";
  wrapper.style.display = "flex";
}
