const categoriesSelect = document.querySelector("#drop-header");
const spinner = document.getElementById("loading-indicator");

let page = 0;
let loading = false;

const throttledScrollHandler = _.throttle(() => {
  if (window.innerHeight + window.scrollY >= document.body.offsetHeight - 100) {
    saveScrollPosition();
    load();
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
  loadNextPage();
  updateLoginLink();
  counter();
});

window.addEventListener("beforeunload", () => {
  saveScrollPosition();
});

document.querySelector(".scroll-top").addEventListener("click", () => {
  window.scrollTo(0, 0);
});

async function fetchCertificates(page) {
  try {
    const response = await fetch(`${host}/certificates?page=${page}&size=25`);
    const data = await response.json();
    const newCertificates = data._embedded.certificateDtoList;
    if (Array.isArray(newCertificates)) {
      return newCertificates;
    }
  } catch (error) {
    return [];
  }
}

function saveScrollPosition() {
  localStorage.setItem("scrollPosition", window.scrollY.toString());
}

function restoreScrollPosition() {
  const savedScrollPosition = localStorage.getItem("scrollPosition");
  if (savedScrollPosition) {
    window.scrollTo(0, parseInt(savedScrollPosition));
  }
}

function loadNextPage() {
  const saved = getCertificatesFromLocalStorage() || [];
  if (saved.length !== 0) {
    createCards(saved);
    spinner.style.display = "none";
  } else {
    load();
  }

  const certificatesList = document.getElementsByClassName("certificate-card");
  const savedScrollPosition = localStorage.getItem("scrollPosition");
  const timer = parseInt(savedScrollPosition)/80;
  console.log(saved.length);
  const intervalId = setInterval(() => {
    if (certificatesList.length === saved.length) {
      clearInterval(intervalId);
      restoreScrollPosition();
      console.log(certificatesList.length);
      console.log(timer)
      spinner.style.display = "none";
    } 
  }, timer);
}

async function load() {
  if (loading) return;
  loading = true;

  spinner.style.display = "block";

  const saved = getCertificatesFromLocalStorage() || [];

  page = saved.length / 25;
  const loaded = await fetchCertificates(page);
  if (JSON.stringify(loaded).length !== 0) {
    saveCertificatesToLocalStorage(loaded);
    loadNextPage();
    spinner.style.display = "none";
  }

  page++;
  loading = false;
  spinner.style.display = "none";
}
