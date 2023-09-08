const categoriesSelect = document.querySelector("#drop-header");

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
  updateLoginLink();
  counter();
  restoreScrollPosition();
});

window.addEventListener("beforeunload", () => {
  saveScrollPosition();
});

document.querySelector(".scroll-top").addEventListener("click", () => {
  window.scrollTo(0, 0);
});

async function fetchCertificates(page) {
  try {
    const response = await fetch(
      `${host}/certificates?page=${page}&size=25`
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

async function loadNextPage() {
  if (loading) return;
  loading = true;
  const spinner = document.getElementById("loading-indicator");
  spinner.style.display = "block";
  const loaded = await fetchCertificates(page);
  if(JSON.stringify(loaded).length !==0){
    saveCertificatesToLocalStorage(loaded);
    createCards(loaded);
  }
  
  spinner.style.display = "none";
  page++;
  loading = false;
  saveScrollPosition();
}

loadNextPage();