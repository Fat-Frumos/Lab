fetch("https://gift-store.onrender.com/api/certificates")
  .then((response) => response.json())
  .then((data) => {
    const certificatesList = document.getElementById("certificates-list");
    const certificates = data._embedded.certificateDtoList;

    if (Array.isArray(certificates)) {
      certificates.forEach((certificate) => {
        const card = document.createElement("form");
        card.action="./pages/details.html"
        card.className = "certificate-card";

        const image = document.createElement("div");
        image.className = "medium-image";
        image.style.backgroundImage = `url(${certificate.path})`;
        card.appendChild(image);

        const group1 = document.createElement("div");
        group1.className = "certificate-grp";
        card.appendChild(group1);

        const h2 = document.createElement("h2");
        h2.textContent = certificate.name;
        group1.appendChild(h2);

        const iconSpan = document.createElement("span");
        iconSpan.className = "material-symbols-outlined certificate-icon";
        iconSpan.textContent = "favorite";
        group1.appendChild(iconSpan);

        const group2 = document.createElement("div");
        group2.className = "certificate-grp";
        card.appendChild(group2);

        const description = document.createElement("p");
        description.className = "certificate-description";
        description.textContent = certificate.description;
        group2.appendChild(description);

        const expires = document.createElement("p");
        expires.className = "expires";
        const currentDate = new Date();
        const lastUpdateDate = new Date(certificate.lastUpdateDate);
        const timeDifferenceMs = currentDate- lastUpdateDate;
        const expiresInDays = Math.floor(timeDifferenceMs / (1000 * 60 * 60 * 24));
        expires.textContent = `Expires in ${expiresInDays} days`;
        group2.appendChild(expires);

        const group3 = document.createElement("div");
        group3.className = "certificate-grp";
        card.appendChild(group3);

        const price = document.createElement("p");
        price.className = "price";
        price.textContent = `$${certificate.price}`;
        group3.appendChild(price);

        const addButton = document.createElement("button");
        addButton.className = "add-button";
        addButton.value = "";
        addButton.type = "submit";
        group3.appendChild(addButton);

        const addLabel = document.createElement("p");
        addLabel.className = "add-to-cart-text";
        addLabel.textContent = "Add to Cart";
        addButton.appendChild(addLabel);
        certificatesList.appendChild(card);
      });
    }
  })
  .catch((error) => {
    console.error("Error fetching data:", error);
  });
