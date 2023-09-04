function saveCertificatesToLocalStorage(certificates) {
  const saved = getCertificatesFromLocalStorage();
  const unique = removeDuplicate(certificates, saved);
  sortCertificatesByCreationDate(unique);
  localStorage.setItem("certificates", JSON.stringify(unique));
}

function removeDuplicate(newCertificates, saved) {
  const ids = new Set(saved.map((certificate) => certificate.id));
  return newCertificates.filter((certificate) => !ids.has(certificate.id));
}

function getCertificatesFromLocalStorage() {
  const savedCertificates = localStorage.getItem("certificates");
  return savedCertificates ? JSON.parse(savedCertificates) : [];
}

function getFromLocalStorage(key) {
  const favoritesJSON = localStorage.getItem(key);
  return favoritesJSON ? JSON.parse(favoritesJSON) : [];
}

function saveFavorites(username, favorites) {
  const favoritesJSON = JSON.stringify(favorites);
  localStorage.setItem(`favorites_${username}`, favoritesJSON);
}

function removeToFavorite(username, favorites, id) {
  const index = favorites.indexOf(id);
  if (index !== -1) {
    favorites.splice(index, 1);
    saveFavorites(username, favorites);
    document.getElementById(id).innerText = "favorite";
  }
}
