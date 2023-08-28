const searchInput = document.querySelector(".categories-input");
const categoriesSelect = document.querySelector(".categories-select");

let page = 0;
let loading = false;

const throttledScrollHandler = _.throttle(() => {
  if (window.innerHeight + window.scrollY >= document.body.offsetHeight - 100) {
    loadNextPage();
  }
  const scrollButton = document.querySelector(".scroll-top");
  if (this.scrollY > 300) {
    scrollButton.classList.add("show");
  } else {
    scrollButton.classList.remove("show");
  }
}, 300);

window.addEventListener("scroll", throttledScrollHandler);

window.addEventListener("load", () => {
  restoreScrollPosition();
  createCards(restoreCertificatesFromLocalStorage());
});

window.addEventListener("beforeunload", () => {
  saveScrollPosition();
});

searchInput.addEventListener(
  "input",
  debounce(() => {
    const query = searchInput.value.trim();
    const category = categoriesSelect.value;
    filterBy(query, category);
  }, 300)
);

categoriesSelect.addEventListener("change", () => {
  const query = searchInput.value.trim();
  const category = categoriesSelect.value;
  filterBy(query, category);
});

document.querySelector(".scroll-top").addEventListener("click", () => {
  window.scrollTo(0, 0);
});

loadNextPage();

async function fetchCertificates(page) {
  try {
    const response = await fetch(
      `https://gift-store.onrender.com/api/certificates?page=${page}&size=25`
    );
    const data = await response.json();
    const newCertificates = data._embedded.certificateDtoList;
    if (Array.isArray(newCertificates)) {
      return newCertificates;
    }
  } catch (error) {
    console.error("Error fetching data:", error);
  }
}

function debounce(func, wait) {
  let timeout;
  return function (...args) {
    clearTimeout(timeout);
    timeout = setTimeout(() => func.apply(this, args), wait);
  };
}

function saveScrollPosition() {
  localStorage.setItem("scrollPosition", window.scrollY.toString());
}

function restoreScrollPosition() {
  const savedScrollPosition = localStorage.getItem("scrollPosition");
  if (savedScrollPosition) {
    const scrollY = parseInt(savedScrollPosition, 10);
    window.scrollTo(0, scrollY);
  }
}

function sortCertificatesByCreationDate(certificates) {
  certificates.sort((a, b) => new Date(b.createDate) - new Date(a.createDate));
}

function saveCertificatesToLocalStorage(certificates) {
  let restored = restoreCertificatesFromLocalStorage();
  if (restored.length === 0) {
    restored = certificates;
  }
  const checked = checkDuplicate(restored, certificates);
  sortCertificatesByCreationDate(checked);
  localStorage.setItem("certificates", JSON.stringify(checked));
}

function restoreCertificatesFromLocalStorage() {
  const savedCertificates = localStorage.getItem("certificates");
  if (savedCertificates) {
    return JSON.parse(savedCertificates);
  } else {
    return [];
  }
}

function checkDuplicate(restored, loaded) {
  const existed = new Set(restored.map((cert) => cert.id));
  return loaded.filter((cert) => !existed.has(cert.id));
}

async function loadNextPage() {
  if (loading) return;
  loading = true;
  const spinner = document.getElementById("loading-indicator");
  spinner.style.display = "block";
  const loaded = await fetchCertificates(page);
  saveCertificatesToLocalStorage(loaded);
  createCards(loaded);
  spinner.style.display = "none";
  page++;
  loading = false;
  saveScrollPosition();
}

function filterBy(query, category) {
  const cardContainer = document.getElementById("certificates-list");
  const certificates = cardContainer.querySelectorAll(".certificate-card");
  certificates.forEach((certificate) => {
    const dataTags = certificate.getAttribute("data-tags").split(", ");
    const found = dataTags.some((tag) => tag.toLowerCase() === category.toLowerCase());

    const name = certificate.querySelector(".certificate-name").textContent.toLowerCase();
    const description = certificate.querySelector(".certificate-description").textContent.toLowerCase();
    const queryMatch = name.includes(query.toLowerCase()) || description.includes(query.toLowerCase());
    certificate.style.display =
      (category === "" || found) && (query === "" || queryMatch)
        ? "block" : "none";
  });
}
