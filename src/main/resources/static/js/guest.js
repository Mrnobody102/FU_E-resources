/*
    LOGIN MODAL
 */

const loginModal = document.getElementById("loginModal");
const closeLoginForm = document.getElementById("closeLoginForm");
const openLoginModal = document.getElementById("openLoginModal");

openLoginModal.addEventListener("click", function () {
    loginModal.style.display = "flex";
});

closeLoginForm.addEventListener("click", function () {
    loginModal.style.display = "none";
});

if (window.location.pathname === "/login") {
    loginModal.style.display = "flex";
}