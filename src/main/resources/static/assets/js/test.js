document.addEventListener('DOMContentLoaded', function () {
    const decreaseBtn = document.getElementById('decrease');
    const increaseBtn = document.getElementById('increase');
    const quantityInput = document.querySelector('.input-group input');

    let quantity = parseInt(quantityInput.value);

    decreaseBtn.addEventListener('click', function () {
        if (quantity > 1) {
            quantity--;
            quantityInput.value = quantity;
        }
    });

    increaseBtn.addEventListener('click', function () {
        quantity++;
        quantityInput.value = quantity;
    });
});