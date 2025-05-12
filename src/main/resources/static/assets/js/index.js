document.addEventListener("DOMContentLoaded", function () {
    const detailButtons = document.querySelectorAll(".details-btn");
    const modal = new bootstrap.Modal(document.getElementById("carRentalModal"));

    detailButtons.forEach((button) => {
        button.addEventListener("click", function () {
            modal.show();
        });
    });
});