function saveCertificatesToLocalStorage(certificates) {
  const saved = getCertificatesFromLocalStorage() || [];
  const unique = removeDuplicate(saved, certificates);
  sortCertificatesByCreationDate(unique);
  localStorage.setItem("certificates", JSON.stringify(unique));
}

function removeDuplicate(saved, certificates) {
  return [...saved, ...certificates]
  .filter((a, b, self) => b === self
  .findIndex((c) => c.id === a.id)
  );
}

function getCertificatesFromLocalStorage() {
  const savedCertificates = localStorage.getItem("certificates");
  return savedCertificates ? JSON.parse(savedCertificates) : [];
}

function getFromLocalStorage(key) {
  const favoritesJSON = localStorage.getItem(key);
  return favoritesJSON ? JSON.parse(favoritesJSON) : [];
}
