let totalAmount = 0;
const spinner = document.getElementById("loading-indicator");
const cartsIds = localStorage.getItem(`cart_${username}`);
const items = getFromLocalStorage("certificates") || [];
console.log(cartsIds);
const coupons = items.filter((item) => cartsIds.includes(item.id));
const container = document.querySelector(".checkout-wrapper");
if (coupons.length) {
  createFavorives(coupons);
  createBottom();
} else {
  showMessage("Cart empty Redirecting to main page", "gray");
  redirect(2000, "../index.html");
}

function createFavorives(coupons) {
  coupons.forEach((coupon) => {
    const couponItem = create("div", "class", "item-coupon", "");
    const image = document.createElement("img");
    image.className = "image-small";
    image.src = coupon.path.replace("/300/", "/600/");
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
  spinner.style.display = "block";
  container.style.display = "none";
  const url = `${host}/orders/${username}?certificateIds=${certificateIds}`;
  const accessToken = localStorage.getItem("accessToken");
  console.log(url);
  console.log(accessToken);
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
        throw new Error("Failed to send orders.");
      }
      return response.json();
    })
    .then((data) => {
      alert("Orders sent successfully");
      localStorage.setItem(`cart_${username}`, []);
      window.location.href = "/";
    })
    .catch((error) => {
      console.error("Error sending orders:", error);
    });
  spinner.style.display = "none";
  container.style.display = "block";
}
