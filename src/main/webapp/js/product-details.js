document.addEventListener("DOMContentLoaded", function () {

    const quantityInput = document.getElementById("quantity");
    const cartQuantity = document.getElementById("cartQuantity");

    if (quantityInput && cartQuantity) {

        quantityInput.addEventListener("input", function () {

            cartQuantity.value = this.value;

        });

    }

});