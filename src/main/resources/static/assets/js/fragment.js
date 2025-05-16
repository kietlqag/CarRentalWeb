let currentPage = 0;
let totalPages = 1;
let currentCarPage = 0;
let currentCarTotalPages = 1;
let currentCarFilters = {
    brand: '',
    seat: '',
    price: ''
};

function loadServices(page) {
    fetch('/home/services?page=' + page)
        .then(response => {
            if (!response.ok) {
                throw new Error("Có lỗi khi tải dịch vụ.");
            }
            return response.text();
        })
        .then(html => {
            document.getElementById('service-list').innerHTML = html;
            currentPage = page;

            const hiddenTotal = document.getElementById('totalPagesHidden');
            if (hiddenTotal) {
                totalPages = parseInt(hiddenTotal.getAttribute('data-total-pages')) || 1;
            }

            updatePagination();
        })
        .catch(error => {
            alert(error.message);
        });
}

function updatePagination() {
    document.getElementById('page-info').innerText = `Trang ${currentPage + 1} / ${totalPages}`;
    document.getElementById('prev-btn').disabled = currentPage === 0;
    document.getElementById('next-btn').disabled = currentPage + 1 >= totalPages;
}

function changePage(delta) {
    const nextPage = currentPage + delta;
    if (nextPage >= 0 && nextPage < totalPages) {
        loadServices(nextPage);
    }
}

function loadCars(page) {
    const params = new URLSearchParams({
        page: page,
        brand: currentCarFilters.brand,
        seat: currentCarFilters.seat,
        price: currentCarFilters.price
    });

    fetch(`/home/cars?${params.toString()}`)
        .then(response => response.text())
        .then(html => {
            document.getElementById('car-list').innerHTML = html;
            currentCarPage = page;

            attachCarDetailButtons();

            const totalCarPagesHidden = document.getElementById('totalCarPagesHidden');
            currentCarTotalPages = parseInt(totalCarPagesHidden?.getAttribute('data-total-pages')) || 1;

            console.log("📦 Tải trang xe:", page);
            console.log("📦 Số trang tổng cộng:", currentCarTotalPages);

            const carPageInfo = document.getElementById('car-page-info');
            if (carPageInfo) {
                carPageInfo.innerText = `Trang ${page + 1} / ${currentCarTotalPages}`;
            }

            document.getElementById('car-prev-btn').disabled = page === 0;
            document.getElementById('car-next-btn').disabled = page + 1 >= currentCarTotalPages;

        })
        .catch(err => {
            console.error("Lỗi khi tải xe:", err);
        });
}

function changeCarPage(delta) {
    const nextPage = currentCarPage + delta;
    console.log("➡️ Chuyển trang xe:", nextPage, "trong", currentCarTotalPages);
    if (nextPage >= 0 && nextPage < currentCarTotalPages) {
        loadCars(nextPage);
    }
}

document.addEventListener('DOMContentLoaded', () => {
    loadCars(0);
});

function attachCarDetailButtons() {
    const buttons = document.querySelectorAll('.details-btn');
    buttons.forEach(button => {
        button.addEventListener('click', function () {
            const carId = this.getAttribute('data-car-id');
            if (carId) {
                fetch(`/api/cars/${carId}`)
                    .then(response => response.json())
                    .then(car => {
                        document.getElementById('car-image').src = car.image;
                        document.getElementById('car-name').innerText = car.name;
                        document.getElementById('car-price').innerText = car.price + "đ/ngày";
                        document.getElementById('car-engine').innerText = car.engine;
                        document.getElementById('car-seat').innerText = car.seat + " chỗ";
                        document.getElementById('car-model').innerText = car.model;
                        document.getElementById('car-style').innerText = car.style;

                        const bookBtn = document.getElementById('bookNowBtn');
                        bookBtn.setAttribute('data-car-id', car.id);

                        const modal = new bootstrap.Modal(document.getElementById('carRentalModal'));
                        modal.show();
                    })
                    .catch(error => {
                        console.error("Không thể tải thông tin xe:", error);
                        alert("Có lỗi xảy ra khi tải chi tiết xe.");
                    });
            }
        });
    });
}

function applyCarFilter(page = 0) {
    const brand = document.getElementById('filter-brand').value;
    const seat = document.getElementById('filter-seat').value;
    const price = document.getElementById('filter-price').value;

    const params = new URLSearchParams({
        page: page,
        brand: brand,
        seat: seat,
        price: price
    });

    fetch(`/home/cars?${params.toString()}`)
        .then(response => response.text())
        .then(html => {
            document.getElementById('car-list').innerHTML = html;

            attachCarDetailButtons();

            const totalCarPagesHidden = document.getElementById('totalCarPagesHidden');
            currentCarTotalPages = parseInt(totalCarPagesHidden?.getAttribute('data-total-pages')) || 1;

            console.log("🔍 Lọc xe - Trang:", page);
            console.log("🔍 Tổng số trang sau lọc:", currentCarTotalPages);

            document.getElementById('car-page-info').innerText = `Trang ${page + 1} / ${currentCarTotalPages}`;
            currentCarFilters = { brand, seat, price };
            currentCarPage = page;

            const prevBtn = document.getElementById('prev-car-btn');
            const nextBtn = document.getElementById('next-car-btn');
            if (prevBtn && nextBtn) {
                prevBtn.disabled = page === 0;
                nextBtn.disabled = page + 1 >= currentCarTotalPages;
            }

        })
        .catch(err => {
            console.error("Lỗi khi lọc xe:", err);
        });
}
