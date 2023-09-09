function addToFavorite(id) {
  add(id, "favorite");
}

function add(id, name) {
  let text = "";
  // const username = localStorage.getItem("user");
  const storage = getFromLocalStorage(`${name}_${username}`) || [];
  if (!storage.includes(id)) {
    storage.push(id);
    text = save(username, storage, name);
  } else {
    text = remove(username, storage, name, id);
  }

  if (name === "favorite") {
    document.getElementById(id).innerText = text;
  }

  if (name === "cart") {
    const classes = document.getElementById("add-button-" + id).classList;
    classes.remove(classes[classes.length - 1]);
    classes.add(text);
  }
  counter();
}

function save(username, storage, name) {
  localStorage.setItem(`${name}_${username}`, JSON.stringify(storage));
  return "heart_plus";
}

function remove(username, favorites, name, id) {
  const index = favorites.indexOf(id);
  if (index !== -1) {
    favorites.splice(index, 1);
    save(username, favorites, name);
    return "favorite";
  }
}

function addToCarts(id) {
  add(id, "cart");
}

function counter() {
  const username = localStorage.getItem("user");
  if (username !== null) {
    const name = username.charAt(0).toUpperCase() + username.slice(1);
    document.getElementById("user-span").innerHTML = name
      ? `${name}&nbsp;&nbsp;|`
      : "";
  }
  count(username, "favorite");
  count(username, "cart");
}

function count(username, prefix) {
  const storage = getFromLocalStorage(`${prefix}_${username}`) || [];
  const counter = document.getElementById(`${prefix}-count`);
  if (storage == 0) {
    counter.style.display = "none";
  } else {
    counter.style.display = "block";
    counter.textContent = storage.length;
  }
}
