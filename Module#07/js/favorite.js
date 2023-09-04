function addToFavorite(id) {
  const username = localStorage.getItem("user");
  const userFavorites = getFromLocalStorage(`favorites_${username}`) || [];
  if (!userFavorites.includes(id)) {
    userFavorites.push(id);
    saveFavorites(username, userFavorites);
    document.getElementById(id).innerText = "heart_plus";
  } else {
    removeToFavorite(username, userFavorites, id);
  }
  counter();
}


function counter() {
  const username = localStorage.getItem("user");
  if (username !== null) {
    const name = username.charAt(0).toUpperCase() + username.slice(1);
    document.getElementById("user-span").textContent = name ? `${name} |` : "";
  }

  const userFavorites = getFromLocalStorage(`favorites_${username}`) || [];
  const favoriteCounter = document.getElementById("favorite-count");

  const carts = localStorage.getItem("carts") || [];
  const cartCounter = document.getElementById("cart-count");

  if (userFavorites == 0) {
    favoriteCounter.style.display = "none";
  } else {
    favoriteCounter.style.display = "block";
    favoriteCounter.textContent = userFavorites.length;
  }

  if (carts == 0) {
    cartCounter.style.display = "none";
  } else {
    cartCounter.style.display = "block";
    cartCounter.textContent = carts.length;
  }
}
