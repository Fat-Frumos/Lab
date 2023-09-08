const navigator = document.querySelector(".categories-nav");

categories.forEach((category) => {
  const image = document.createElement("img");
  image.src = `https://source.unsplash.com/featured/200x150/?${category.tag}`;
  image.alt = "tag";
  image.onerror = () => {
    this.onerror = null;
    this.src = "./css/logo.svg";
  };

  const props = new Map();
  const card = create("div", "class", "category-card", "");
  const element = create("div", "class", "category", "");
  
  props.set("image", image);
  props.set("overlay", create("div", "class", "overlay", ""));
  props.set("text", create("span", "class", "text", category.name));
  props.forEach((value) => element.appendChild(value));

  element.addEventListener("click", () => searchByTags(category.tag));
  card.appendChild(element);
  navigator.appendChild(card);
});
