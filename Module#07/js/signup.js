const wrapper = document.querySelector(".wrapper");
const spinner = document.getElementById("loading-indicator");
const form = document.getElementById("registration-form");

async function signup() {
  wrapper.style.display = "none";
  spinner.style.display = "block";

  try {
    const formData = {};
    new FormData(form).forEach((value, key) => formData[key] = value);
    
    const response = await fetch(form.action, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(formData),
    });
    if (response.ok) {
      const data = await response.json();
      localStorage.setItem("accessToken", data.access_token);
      showMessage("Login successful! Redirecting...", "green");
      redirect(1000, "./login.html");
    } else {
      showMessage("Register failed. Please check your credentials", "red");
    }
  } catch (error) {
    console.error("Error:", error);
    showMessage("An error occurred. Please try again later", "red");
  }
  spinner.style.display = "none";
  form.style.display = "block";
  wrapper.style.display = "flex";
}

const submitButton = document.getElementById("signup-button");
submitButton.addEventListener("click", async (event) => {
  event.preventDefault();
  await signup();
});

const cancelButton = document.getElementById("cancel-button");
cancelButton.addEventListener("click", () => {
  form.reset();
  redirect(100, "/");
});
