const certificatesList = document.getElementById('certificates-list');
  for (let i = 1; i <= 25; i++) {
    const card = document.createElement('div');
    card.className = 'certificate-card';
    const mediumImage = document.createElement('div');
    mediumImage.className = 'medium-image';
    const certificateGrp1 = document.createElement('div');
    certificateGrp1.className = 'certificate-grp';
    const h2 = document.createElement('h2');
    h2.textContent = 'Coupon name';
    const iconSpan = document.createElement('span');
    iconSpan.className = 'material-symbols-outlined certificate-icon';
    iconSpan.textContent = 'favorite';
    const certificateGrp2 = document.createElement('div');
    certificateGrp2.className = 'certificate-grp';
    const descriptionP = document.createElement('p');
    descriptionP.className = 'certificate-description';
    descriptionP.textContent = 'Some brief description';
    const expiresP = document.createElement('p');
    expiresP.className = 'expires';
    expiresP.textContent = 'Expires in 3 days';
    const certificateGrp3 = document.createElement('div');
    certificateGrp3.className = 'certificate-grp';
    const priceP = document.createElement('p');
    priceP.className = 'price';
    priceP.textContent = '$235';
    const addButton = document.createElement('button');
    addButton.className = 'add-button';
    addButton.value = '';
    const addToCartText = document.createElement('p');
    addToCartText.className = 'add-to-cart-text';
    addToCartText.className = 'button-cart-text';
    addToCartText.textContent = 'Add to Cart';

    certificateGrp1.appendChild(h2);
    certificateGrp1.appendChild(iconSpan);
    certificateGrp2.appendChild(descriptionP);    
    certificateGrp2.appendChild(expiresP);
    certificateGrp3.appendChild(priceP);
    certificateGrp3.appendChild(addButton);
    card.appendChild(mediumImage);
    card.appendChild(certificateGrp1);
    card.appendChild(certificateGrp2);
    card.appendChild(certificateGrp3);
    addButton.appendChild(addToCartText);
    certificatesList.appendChild(card);  
  }
  