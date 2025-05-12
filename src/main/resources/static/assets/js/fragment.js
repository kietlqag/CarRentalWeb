let currentPage = 0;
let totalPages = 1; // mặc định 1, sẽ cập nhật sau

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

