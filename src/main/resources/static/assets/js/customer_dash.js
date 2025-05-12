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
