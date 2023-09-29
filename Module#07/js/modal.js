const modal = document.querySelector(".modal-frame");
const modalOverlay = document.querySelector(".modal-overlay");
const closeButton = document.querySelector(".modal-body");

function showMessage(message, color) {
  const popup = document.getElementById("message");
  popup.textContent = message;
  popup.style.color = color;
  showModal();
}

function showModal() {
  modal.classList.toggle("active");
  modal.classList.remove("leave");
}

function closeModal() {
  modal.classList.remove("active");
  modal.classList.add("leave");
}

modalOverlay.addEventListener("click", function () {
  closeModal();
});

closeButton.addEventListener("click", function () {
  closeModal();
});

document.addEventListener("keyup", function (event) {
  if (event.key === "Escape" || event.key === "Enter") {
    closeModal();
  }
});

function redirect(delay, url) {
  if (delay) {
    setTimeout(() => {
      closeModal();
      window.location.href = url;
    }, delay);
  }
}
