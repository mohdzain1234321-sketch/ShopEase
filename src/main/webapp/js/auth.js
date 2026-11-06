document.addEventListener("DOMContentLoaded", function () {
    document.querySelectorAll(".toggle-password").forEach(function (button) {
        button.addEventListener("click", function () {
            const input = document.getElementById(button.dataset.target);
            if (!input) return;

            const reveal = input.type === "password";
            input.type = reveal ? "text" : "password";
            button.textContent = reveal ? "Hide" : "Show";
        });
    });

    const registerForm = document.getElementById("registerForm");
    if (registerForm) {
        registerForm.addEventListener("submit", function (event) {
            const password = document.getElementById("password");
            const confirmPassword = document.getElementById("confirmPassword");

            if (password.value.length < 8) {
                event.preventDefault();
                alert("Password must contain at least 8 characters.");
                password.focus();
                return;
            }

            if (password.value !== confirmPassword.value) {
                event.preventDefault();
                alert("Passwords do not match.");
                confirmPassword.focus();
            }
        });
    }
});
