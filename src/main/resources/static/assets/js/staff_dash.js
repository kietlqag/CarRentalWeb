// Load sidebar
fetch("/staff_sb.html")
    .then(response => response.text())
    .then(html => {
        document.getElementById("sidebar").innerHTML = html;

        // Gắn các sự kiện sau khi sidebar đã được chèn vào DOM
        attachSectionToggleEvents();

        // Nút logout
        document.getElementById('logoutBtn')?.addEventListener('click', function (e) {
            e.preventDefault();
            if (confirm('Bạn có chắc muốn đăng xuất?')) {
                window.location.href = '/logout';
            }
        });

        // Toggle sidebar trên mobile
        document.getElementById('sidebarToggle')?.addEventListener('click', function () {
            document.getElementById('sidebar').classList.toggle('active');
            document.getElementById('content').classList.toggle('active');
        });
    })
    .catch(error => {
        console.error("Không thể load sidebar:", error);
    });


// Khởi tạo Flatpickr cho input ngày
flatpickr('.date-picker', {
    dateFormat: 'd/m/Y',
    minDate: 'today',
    disableMobile: true
});

// Hàm xử lý hủy đặt xe
function cancelBooking(bookingId) {
    if (confirm('Bạn có chắc muốn hủy đặt xe #' + bookingId + '? Hủy chỉ được phép trước 24 giờ.')) {
        alert('Đặt xe #' + bookingId + ' đã được hủy.');
        // TODO: Gọi API để hủy
    }
}

// Populate modify booking modal
document.querySelectorAll('[data-bs-target="#modifyBookingModal"]').forEach(button => {
    button.addEventListener('click', function () {
        const bookingId = this.getAttribute('data-booking-id');
        document.getElementById('bookingId').value = bookingId;
        // TODO: Populate thêm nếu cần
    });
});

// Populate edit car modal
document.querySelectorAll('[data-bs-target="#editCarModal"]').forEach(button => {
    button.addEventListener('click', function () {
        const carId = this.getAttribute('data-car-id');
        document.getElementById('carId').value = carId;
    });
});

// Hàm gắn sự kiện chuyển section
function attachSectionToggleEvents() {
    const sectionButtons = document.querySelectorAll('[data-section]');
    const sections = document.querySelectorAll('.content-section');

    sectionButtons.forEach(btn => {
        // Nếu là nút mở modal thì bỏ qua
        if (btn.hasAttribute('data-bs-toggle')) return;

        btn.addEventListener('click', function (e) {
            e.preventDefault();
            const targetId = btn.getAttribute('data-section');

            // Ẩn tất cả các section
            sections.forEach(section => {
                section.classList.remove('active');
            });

            // Hiển thị section được chọn
            const targetSection = document.getElementById(targetId);
            if (targetSection) {
                targetSection.classList.add('active');
            }
// Cập nhật trạng thái active trong sidebar
            document.querySelectorAll('.nav-link').forEach(nav => {
                nav.classList.remove('active');
            });
            this.classList.add('active');
        });
    });
}

//Chi tiet don hang
document.addEventListener('DOMContentLoaded', function () {
    const viewButtons = document.querySelectorAll('[data-bs-target="#viewOrderModal"]');

    viewButtons.forEach(btn => {
        btn.addEventListener('click', () => {
            const bookingId = btn.getAttribute('data-booking-id');

            fetch(`/staff/orders/${bookingId}`)
                .then(res => res.json())
                .then(order => {
                    document.getElementById('orderId').textContent = order.id;
                    document.getElementById('orderName').textContent = order.name;
                    document.getElementById('orderEmail').textContent = order.accountemail;
                    document.getElementById('orderStatus').textContent = order.status;
                    document.getElementById('orderCreatedAt').textContent = formatDateCreate(order.createdat);
                    document.getElementById('orderTotal').textContent = order.total + 'đ';
                    document.getElementById('orderPaymentStatus').textContent = order.paymentstatus;
                    document.getElementById('orderPaymentMethod').textContent = order.paymentmethod;
                    document.getElementById('orderService').textContent = order.service;
                    document.getElementById('orderReceiveDate').textContent = formatDate(order.receivedate);
                    document.getElementById('orderReturnDate').textContent = formatDate(order.returndate);
                    document.getElementById('orderCountDate').textContent = order.countdate;
                    document.getElementById('orderCustomer').textContent = order.customer;
                    document.getElementById('orderPhone').textContent = order.phone;
                    document.getElementById('orderPickLocation').textContent = order.picklocation;
                    document.getElementById('orderNote').textContent = order.note;
                    document.getElementById('orderDiscount').textContent = order.discount;
                    document.getElementById('orderCarPrice').textContent = order.price;
                    document.getElementById('orderCarId').textContent = order.carid;
                    document.getElementById('orderServiceId').textContent = order.serviceid;
                    document.getElementById('orderServicePrice').textContent = order.serviceprice;
                });
        });
    });
});

//Chinh sua don hang
document.addEventListener("DOMContentLoaded", function () {
    const editButtons = document.querySelectorAll("button[data-bs-target='#editOrderModal']");

    editButtons.forEach(button => {
        button.addEventListener("click", () => {
            const orderId = button.getAttribute("data-booking-id");
            fetch(`/staff/orders/${orderId}`)
                .then(res => res.json())
                .then(order => {
                    document.getElementById("editOrderId").value = order.id;
                    document.getElementById("editStatus").value = order.status;
                    document.getElementById("editPaymentStatus").value = order.paymentstatus;
                    document.getElementById("editReceiveDate").value = formatDate(order.receivedate);
                    document.getElementById("editReturnDate").value = formatDate(order.returndate);
                });
        });
    });

    document.getElementById("saveEditOrderBtn").addEventListener("click", () => {
        const id = document.getElementById("editOrderId").value;
        const payload = {
            status: document.getElementById("editStatus").value,
            paymentstatus: document.getElementById("editPaymentStatus").value,
            receivedate: convertToISODate(document.getElementById("editReceiveDate").value) || null,
            returndate: convertToISODate(document.getElementById("editReturnDate").value) || null,
        };

        fetch(`/staff/orders/update/${id}`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(payload)
        }).then(res => {
            if (res.ok) {
                alert("Cập nhật đơn hàng thành công!");
                location.reload();
            } else {
                res.text().then(msg => alert("Cập nhật thất bại: " + msg));
            }
        }).catch(err => alert("Lỗi kết nối: " + err));
    });
});
function convertToISODate(dateStr) {
    const [dd, mm, yyyy] = dateStr.split('/');
    const date = new Date(Date.UTC(yyyy, mm - 1, dd, 24, 0, 0));
    const newYear = date.getUTCFullYear();
    const newMonth = String(date.getUTCMonth() + 1).padStart(2, '0');
    const newDay = String(date.getUTCDate()).padStart(2, '0');

    return `${newYear}-${newMonth}-${newDay}`;
}
function formatDateCreate(isoString) {
    let date = new Date(isoString);
    date = new Date(date.getTime() - (7 * 3600000));
    const dd = String(date.getDate()).padStart(2, '0');
    const mm = String(date.getMonth() + 1).padStart(2, '0');
    const yyyy = date.getFullYear();

    return `${dd}/${mm}/${yyyy}`;
}
function formatDate(isoString) {
    let date = new Date(isoString);
    const dd = String(date.getDate()).padStart(2, '0');
    const mm = String(date.getMonth() + 1).padStart(2, '0');
    const yyyy = date.getFullYear();

    return `${dd}/${mm}/${yyyy}`;
}

//Chinh sua ho so
document.getElementById('editProfileForm').addEventListener('submit', function(e) {
    e.preventDefault();

    const phone = document.getElementById('profilePhone').value;
    const address = document.getElementById('profileAddress').value;

    fetch('/profile/update', {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ phone, address })
    })
        .then(res => {
            if (res.ok) {
                alert("Cập nhật thành công!");
                location.reload(); // hoặc cập nhật phần UI nếu không muốn reload
            } else {
                return res.text().then(text => { throw new Error(text); });
            }
        })
        .catch(err => {
            alert("Lỗi: " + err.message);
        });
});

//Modal chinh sua ho so
document.addEventListener("DOMContentLoaded", function () {
    const profileModal = document.getElementById('editProfileModal');

    if (profileModal) {
        profileModal.addEventListener('show.bs.modal', function (event) {
            const button = event.relatedTarget;
            const phone = button.getAttribute('data-phone');
            const address = button.getAttribute('data-address');

            document.getElementById('profilePhone').value = phone || '';
            document.getElementById('profileAddress').value = address || '';
        });
    }
});