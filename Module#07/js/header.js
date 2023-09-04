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

async function searchByTags(name) {
  const url = `https://gift-store.onrender.com/api/certificates/search?tagNames=${name}`;
  const spinner = document.getElementById("loading-indicator");
  spinner.style.display = "block";
  document.getElementById("drop-header").innerText = name;
  try {
    const response = await fetch(url);
    const data = await response.json();
    const newCertificates = data._embedded.certificateDtoList;
    if (Array.isArray(newCertificates)) {
      document.getElementById("certificates-list").innerHTML = "";
      createCards(newCertificates);
    }
  } catch (error) {
    console.error("Error fetching data:", error);
  }
  spinner.style.display = "none";
}

const menu = document.querySelector(".menu");

const input = create("input", "class", "menu-open", "");
input.setAttribute("id", "menu-open");
input.setAttribute("type", "checkbox");
input.setAttribute("name", "menu-open");

const label = create("label", "class", "menu-open-button", "");
label.setAttribute("for", "menu-open");

for (let i = 1; i <= 3; i++) {
  label.appendChild(create("span", "class", `hamburger hamburger-${i}`, ""));
}

menu.appendChild(input);
menu.appendChild(label);

menuItems.forEach((item) => {
  menu.appendChild(
    createLink(
      "a",
      "class",
      "material-symbols-outlined menu-item",
      item.text,
      item.href
    )
  );
});

function toggleDropbox() {
  document.getElementById("dropdown-search").classList.toggle("show");
}

function create(element, attr, value, text) {
  const child = document.createElement(element);
  child.setAttribute(attr, value);
  child.textContent = text;
  return child;
}

function createLink(element, attr, value, text, href) {
  const child = create(element, attr, value, text);
  child.setAttribute("href", href);
  return child;
}

function debounce(func, wait) {
  let timeout;
  return function (...args) {
    clearTimeout(timeout);
    timeout = setTimeout(() => func.apply(this, args), wait);
  };
}

const searchContainer = document.getElementById("search");

const inputCategory = create("input", "class", "categories-input", "");
inputCategory.setAttribute("type", "text");
inputCategory.setAttribute("placeholder", "Search by item name");

inputCategory.addEventListener(
  "input",
  debounce(() => {
    const query = inputCategory.value;
    if (categoriesSelect) {
      const category = categoriesSelect.innerHTML;
      filterBy(query, category);
    }
  }, 300)
);

const dropdown = create("div", "class", "dropdown", "");
const dropButton = create("button", "class", "drop-button", "");
dropButton.addEventListener("click", toggleDropbox);

const header = create("span", "id", "drop-header", "All Categories");
const expandIcon = create(
  "span",
  "class",
  "material-symbols-search material-symbols-outlined",
  "expand_more"
);

dropButton.appendChild(header);
dropButton.appendChild(expandIcon);

const dropdownContent = create("div", "id", "dropdown-search", "");
dropdownContent.setAttribute("class", "dropdown-content");

const searchIcon = create("div", "class", "search-icon", "");

const spanIcon = create(
  "span",
  "class",
  "material-symbols-search material-symbols-outlined",
  "search"
);

searchIcon.appendChild(spanIcon);

const searchCategory = create("input", "id", "input-search", "");

searchCategory.setAttribute("type", "text");
searchCategory.setAttribute("placeholder", "Search...");
searchCategory.addEventListener("click", () => filter(searchCategory.value));

inputCategory.addEventListener("keydown", (event) => {
  if (event.key === "Enter") {
    filter(inputCategory.value);
  }
});

searchIcon.appendChild(searchCategory);
dropdownContent.appendChild(searchIcon);

categoryNames.forEach((categoryName) => {
  const categoryLink = create("a", "href", "#", categoryName);
  categoryLink.setAttribute("data-category", categoryName);
  categoryLink.addEventListener("click", () => category(categoryName));
  dropdownContent.appendChild(categoryLink);
});

dropdown.appendChild(dropButton);
dropdown.appendChild(dropdownContent);
searchContainer.appendChild(inputCategory);
searchContainer.appendChild(dropdown);

searchCategory.addEventListener("keydown", function (event) {
  if (event.key === "Enter") {
    category(this.value);
  }
});

searchCategory.addEventListener("input", function () {
  filterBy(inputCategory.value.trim());
});

function category(header) {
  if (header === "") {
    header = "All Categories";
  }
  document.getElementById("drop-header").innerText = header;
  toggleDropbox();
  filter(header);
}

function filter(category) {
  const query = inputCategory.value;
  filterBy(query, category);
}

function filterBy(query, category) {
  document
    .getElementById("certificates-list")
    .querySelectorAll(".certificate-card")
    .forEach((certificate) => {
      let found = false;
      if (category !== undefined) {
        found = certificate
          .getAttribute("data-tags")
          .split(", ")
          .some((tag) => tag.toLowerCase().includes(category.toLowerCase()));
      }

      if (category === "All Categories") {
        category = "";
      }

      const name = certificate
        .querySelector(".certificate-name")
        .textContent.toLowerCase();
      const description = certificate
        .querySelector(".certificate-description")
        .textContent.toLowerCase();
      const queryMatch =
        name.includes(query.toLowerCase()) ||
        description.includes(query.toLowerCase());
      certificate.style.display =
        (category === "" || found) && (query === "" || queryMatch)
          ? "block"
          : "none";
    });
}
