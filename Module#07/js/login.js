const loginForm = document.getElementById("login-form");
const spinner = document.getElementById("loading-indicator");
const headers = {
  "Content-Type": "application/json",
};

async function login(username, password) {
  loginForm.style.display = "none";
  spinner.style.display = "block";
  try {
    const response = await fetch(`${host}/login`, {
      method: "POST",
      body: JSON.stringify({ username, password }),
      headers,
    });
    if (response.ok) {
      const data = await response.json();
      handleSuccessfulLogin(username, data.access_token);
      showMessage("Login successful! Redirecting...", "green");
      redirect(1500, "../index.html");
    } else if (response.status === 401) {
      console.log(response.status);
    } else {
      showMessage("Login failed. Please check your credentials", "red");
    }
  } catch (error) {
    console.error("Error:", error);
    showMessage("An error occurred. Please try again later", "red");
  }
  spinner.style.display = "none";
  loginForm.style.display = "block";
}

const submitButton = document.getElementById("submit-button");
submitButton.addEventListener("click", async (event) => {
  event.preventDefault();

  const usernameInput = document.querySelector('[name="username"]');
  const passwordInput = document.querySelector('[name="password"]');
  const username = usernameInput.value;
  const password = passwordInput.value;
  if (!username || !password) {
    showMessage("Please enter username and password", "red");
    return;
  }
  await login(username, password);
});

function setUserInformation(username, accessToken) {
  localStorage.setItem("user", username);
  localStorage.setItem("accessToken", accessToken);
  localStorage.setItem("userLoggedIn", "true");
  updateLoginLink();
}

function handleSuccessfulLogin(username, accessToken) {
  setUserInformation(username, accessToken);  
}

function setUserLoggedOut() {
  localStorage.removeItem("userLoggedIn");
  localStorage.removeItem("user");
  localStorage.removeItem("accessToken");
  updateLoginLink();
}

function handleLogout() {
  setUserLoggedOut();
  updateLoginLink();
}

setUserLoggedOut();