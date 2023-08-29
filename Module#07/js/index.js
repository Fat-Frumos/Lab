function toggleCheckbox() {
  const checkbox = document.getElementById("menu-open");
  checkbox.checked = !checkbox.checked;
}

document.getElementById('input-search').addEventListener('keydown', function (event) {
  if (event.key === 'Enter') {
    category(this.value);
  }
});

function toggleDropbox() {
  document.getElementById("dropdown-search").classList.toggle("show");
}

function category(header) {
  if (header === '') {
    header = 'All Categories'
  }
  document.getElementById("drop-header").innerText = header;
  toggleDropbox();
  filter(header);
}
