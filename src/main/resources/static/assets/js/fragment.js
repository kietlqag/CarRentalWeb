let currentPage = 0;
let totalPages = 1;
let currentCarPage = 0;
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

            // Cập nhật số trang từ thuộc tính ẩn trong fragment (ví dụ bạn thêm data-total-pages)
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
    console.log("Clicked with delta:", delta); // DEBUG
    const nextPage = currentPage + delta;
    if (nextPage >= 0 && nextPage < totalPages) {
        loadServices(nextPage);
    }
}

function loadCars(page) {
    fetch(`/home/cars?page=${page}`)
        .then(response => response.text())
        .then(html => {
            document.getElementById('car-list').innerHTML = html;
            currentCarPage = page;

            // Gắn lại sự kiện click
            attachCarDetailButtons();

            // Lấy tổng số trang từ thẻ ẩn sau khi innerHTML đã được cập nhật
            const totalCarPagesHidden = document.getElementById('totalCarPagesHidden');
            const totalCarPages = parseInt(totalCarPagesHidden?.getAttribute('data-total-pages')) || 1;

            // Cập nhật phân trang
            const carPageInfo = document.getElementById('car-page-info');
            if (carPageInfo) {
                carPageInfo.innerText = `Trang ${page + 1} / ${totalCarPages}`;
            }
        })
        .catch(err => {
            console.error("Lỗi khi tải xe:", err);
        });
}



function changeCarPage(delta) {
    console.log("Car page clicked with delta:", delta); // DEBUG
    const totalCarPages = parseInt(document.getElementById('totalCarPagesHidden').getAttribute('data-total-pages'));
    const nextPage = currentCarPage + delta;

    if (nextPage >= 0 && nextPage < totalCarPages) {
        loadCars(nextPage);
        currentCarPage = nextPage;
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

                        // Gán ID vào thuộc tính data
                        const bookBtn = document.getElementById('bookNowBtn');
                        bookBtn.setAttribute('data-car-id', car.id);

                        // Hiển thị modal
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

            // ✅ Gắn lại sự kiện click sau khi thay đổi DOM
            attachCarDetailButtons();

            // ✅ Cập nhật thông tin trang
            const totalCarPagesHidden = document.getElementById('totalCarPagesHidden');
            const totalPages = parseInt(totalCarPagesHidden?.getAttribute('data-total-pages')) || 1;
            document.getElementById('car-page-info').innerText = `Trang ${page + 1} / ${totalPages}`;
        })
        .catch(err => {
            console.error("Lỗi khi lọc xe:", err);
        });
}







