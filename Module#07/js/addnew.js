const spinner = document.getElementById("loading-indicator");
const column = document.getElementById("add-new-wrapper");
counter();
updateLoginLink();

const checkboxDropdown = document.querySelector(".checkbox-dropdown");

checkboxDropdown.addEventListener("click", () => {
  checkboxDropdown.classList.toggle("open");
});

document.addEventListener("click", (event) => {
  if (!checkboxDropdown.contains(event.target)) {
    checkboxDropdown.classList.remove("open");
  }
});

async function saveCertificates() {
  spinner.style.display = "block";
  column.style.display = "none";

  saveImage();
  saveForm();

  spinner.style.display = "none";
  column.style.display = "flex";
}

async function saveForm() {
  const form = document.getElementById("new-coupon");
  const formData = new FormData(form);
  const expirationDate = formData.get("expired");
  const currentDate = new Date();
  const selectedDate = new Date(expirationDate);
  const durationInDays = Math.floor(
    (selectedDate - currentDate) / (24 * 60 * 60 * 1000)
  );

  const checkboxes = document.querySelectorAll(
    'input[type="checkbox"][name="tags"]:checked'
  );
  const selectedTags = Array.from(checkboxes).map((checkbox) => ({
    name: checkbox.value,
  }));

  const imagePath = formData.get("file").name;
  let path = "";
  if (imagePath) {
    path = host + "/upload/" + imagePath;
  }

  const certificateData = {
    name: formData.get("name"),
    description: formData.get("description"),
    company: formData.get("company"),
    shortDescription: formData.get("shortDescription"),
    price: parseFloat(formData.get("price")),
    duration: durationInDays,
    path: path,
    tags: selectedTags,
  };

  const accessToken = localStorage.getItem("accessToken");

  const headers = {
    Authorization: `Bearer ${accessToken}`,
    "Content-Type": "application/json",
  };

  const requestBody = JSON.stringify(certificateData);

  const options = {
    method: "POST",
    headers: headers,
    body: requestBody,
  };

  try {
    const response = await fetch(`${host}/certificates`, options);
    if (response.ok) {
      const jsonData = await response.json();
      localStorage.setItem("certificate", JSON.stringify(jsonData));
      showMessage("Certificate created successfully.", "green");
      // form.reset();
      redirect(2500, "details.html");
    } else {
      showMessage("Failed to create certificate.", "red");
    }
  } catch (error) {
    showMessage("Error:" + error, "red");
  }
}

async function saveImage() {
  const imageInput = document.getElementById("images");
  const imageFile = imageInput.files[0];

  const imageData = new FormData();
  imageData.append("file", imageFile);

  const response = await fetch(`${host}/upload`, {
    method: "POST",
    body: imageData,
  });
  if (!response.ok) {
    // showMessage(response.json().errorMessage, "red");
  }
}
