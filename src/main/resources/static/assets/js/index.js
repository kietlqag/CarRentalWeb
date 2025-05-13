document.addEventListener("DOMContentLoaded", function () {
    const detailButtons = document.querySelectorAll(".details-btn");
    const modal = new bootstrap.Modal(document.getElementById("carRentalModal"));

    detailButtons.forEach((button) => {
        button.addEventListener("click", function () {
            modal.show();
        });
    });
});

document.addEventListener("DOMContentLoaded", function () {
    const detailButtons = document.querySelectorAll('.details-btn');

    detailButtons.forEach(button => {
        button.addEventListener('click', function () {
            const carId = button.getAttribute('data-car-id');
            console.log("Car ID:", carId);
            showCarDetails(carId);
        });
    });
});

function showCarDetails(carId) {
    fetch(`/api/cars/${carId}`)
        .then(response => {
            if (!response.ok) {
                throw new Error("Không thể tải chi tiết xe");
            }
            return response.json();
        })
        .then(data => {
            console.log(data);
            populateCarModal(data); // ✅ Gọi cập nhật nội dung modal
            const modal = new bootstrap.Modal(document.getElementById('carRentalModal'));
            modal.show(); // ✅ Hiển thị modal sau khi cập nhật nội dung
        })
        .catch(error => {
            alert(error.message);
        });
}

function populateCarModal(car) {
    document.getElementById("car-name").textContent = car.name;
    document.getElementById("car-price").textContent = `${car.price}đ/ngày`;
    document.getElementById("car-image").src = car.image || "./img/default.jpg";
    document.getElementById("car-engine").textContent = car.engine;
    document.getElementById("car-seat").textContent = `${car.seat} chỗ`;
    document.getElementById("car-model").textContent = car.model;
    document.getElementById("car-style").textContent = car.bodystyle;
}



