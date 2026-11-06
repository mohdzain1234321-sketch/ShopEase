document.addEventListener("DOMContentLoaded", function () {
  document.querySelectorAll(".quantity-form").forEach(function (form) {
    const input = form.querySelector(".qty-input");
    form.querySelectorAll(".qty-btn").forEach(function (button) {
      button.addEventListener("click", function () {
        const step = Number(button.dataset.step || 0);
        const min = Number(input.min || 1);
        const max = Number(input.max || 999);
        const current = Number(input.value || min);
        input.value = Math.min(max, Math.max(min, current + step));
      });
    });
    input.addEventListener("change", function () {
      const min = Number(input.min || 1);
      const max = Number(input.max || 999);
      input.value = Math.min(max, Math.max(min, Number(input.value || min)));
    });
  });
});
