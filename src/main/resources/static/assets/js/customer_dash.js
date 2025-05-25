// Load sidebar
fetch("/customer_sb.html")
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

function cancelBooking(bookingId) {
    if (confirm('Bạn có chắc muốn hủy đặt xe #' + bookingId + '? Hủy chỉ được phép trước 24 giờ.')) {
        fetch(`/customer/orders/cancel/${bookingId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ status: 'Đã huỷ' })
        })
            .then(res => {
                if (res.ok) {
                    alert('✅ Đơn hàng #' + bookingId + ' đã được hủy.');
                    location.reload();
                } else {
                    res.text().then(msg => alert('❌ Không thể hủy: ' + msg));
                }
            })
            .catch(error => {
                alert('❌ Lỗi kết nối: ' + error);
            });
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



document.addEventListener('DOMContentLoaded', function () {
    const viewButtons = document.querySelectorAll('[data-bs-target="#viewOrderModal"]');

    viewButtons.forEach(btn => {
        btn.addEventListener('click', () => {
            const bookingId = btn.getAttribute('data-booking-id');

            fetch(`/customer/orders/${bookingId}`)
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
                    document.getElementById('promotion').textContent = "Giảm " + order.discount + "%";
                    document.getElementById('orderReceiveDate').textContent = formatDate(order.receivedate);
                    document.getElementById('orderReturnDate').textContent = formatDate(order.returndate);
                    document.getElementById('orderCountDate').textContent = order.countdate;
                    document.getElementById('orderCustomer').textContent = order.customer;
                    document.getElementById('orderPhone').textContent = order.phone;
                    document.getElementById('orderPickLocation').textContent = order.picklocation;
                    document.getElementById('orderNote').textContent = order.note;
                });
        });
    });
});

// Load dữ liệu từ be lên
document.addEventListener("DOMContentLoaded", function () {
    const editButtons = document.querySelectorAll("button[data-bs-target='#editOrderModal']");

    // Mở modal và load thông tin đơn hàng
    editButtons.forEach(button => {
        button.addEventListener("click", () => {
            const orderId = button.getAttribute("data-booking-id");

            // Gán ID vào input ẩn
            document.getElementById("editOrderId").value = orderId;

            fetch(`/customer/orders/${orderId}`)
                .then(res => res.json())
                .then(order => {
                    // Gán dữ liệu vào form
                    document.getElementById("editReceiveDate").value = formatDate(order.receivedate);
                    document.getElementById("editReturnDate").value = formatDate(order.returndate);
                    document.getElementById("editNote").value = order.note || "";
                }).catch(err => alert("Lỗi khi tải dữ liệu đơn hàng: " + err));
        });
    });

    // Save thông tin đã chỉnh sửa lại
    document.getElementById("saveEditOrderBtn").addEventListener("click", () => {
        const id = document.getElementById("editOrderId").value;

        const payload = {
            receivedate: formatDateInput(document.getElementById("editReceiveDate").value) || null,
            returndate: formatDateInput(document.getElementById("editReturnDate").value) || null,
            note: document.getElementById("editNote").value || null,
        };

        fetch(`/customer/orders/update/${id}`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(payload)
        }).then(res => {
            if (res.ok) {
                alert("✅ Cập nhật đơn hàng thành công!");
                location.reload();
            } else {
                res.text().then(msg => alert("❌ Cập nhật thất bại: " + msg));
            }
        }).catch(err => alert("❌ Lỗi kết nối: " + err));
    });

    function formatDateInput(dateStr) {
        const [dd, mm, yyyy] = dateStr.split('/');
        const date = new Date(Date.UTC(yyyy, mm - 1, dd, 24, 0, 0));
        const newYear = date.getUTCFullYear();
        const newMonth = String(date.getUTCMonth() + 1).padStart(2, '0');
        const newDay = String(date.getUTCDate()).padStart(2, '0');

        return `${newYear}-${newMonth}-${newDay}`;
    }
});

