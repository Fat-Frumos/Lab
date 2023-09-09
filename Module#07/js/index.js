const host = "https://gift-store.onrender.com/api";
// const host = "http://localhost:8080/api";
const certificatesList = document.getElementById('certificates-list');
const username = localStorage.getItem("user");

function goBack() {
  window.history.back();
}

function showDetails(certificate) {
  localStorage.removeItem("certificate");
  localStorage.setItem("certificate", JSON.stringify(certificate));
  window.location.href = "./pages/details.html";
}

function toggleCheckbox() {
  const checkbox = document.getElementById("menu-open");
  const logo = document.getElementById("logo-label");
  const name = document.getElementById("logo-name");
  if (checkbox.checked) {
    name.style.opacity = 1;
    name.style.zIndex = 1;
    logo.style.transform = "none";
  } else {
    name.style.opacity = 0;
    name.style.zIndex = -5;
    logo.style.transform = "rotate(42deg)";
  }
  checkbox.checked = !checkbox.checked;
}

function updateLoginLink() {
  document.getElementById("login-link").textContent =
    localStorage.getItem("userLoggedIn") === "true" ? "Logout" : "Login";
}

if ("serviceWorker" in navigator) {
  navigator.serviceWorker
    .register("/js/service-worker.js")
    .then((registration) => {
      console.log("Service Worker registered with scope:", registration.scope);
    })
    .catch((error) => {
      console.error("Service Worker registration failed:", error);
    });
}
