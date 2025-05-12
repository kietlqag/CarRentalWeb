document.addEventListener("DOMContentLoaded", () => {
    // Xác định vai trò người dùng dựa trên URL hoặc thuộc tính
    const path = window.location.pathname;
    let sidebarFile = "/sidebar-admin.html";
    if (path.includes("customer")) {
        sidebarFile = "/sidebar-customer.html";
    } else if (path.includes("staff")) {
        sidebarFile = "/sidebar-staff.html";
    }

    // Load sidebar
    fetch(sidebarFile)
        .then(response => response.text())
        .then(html => {
            document.getElementById("sidebar").innerHTML = html;
            attachSectionToggleEvents();
        })
        .catch(error => {
            console.error("Không thể load sidebar:", error);
        });

    // Khởi tạo Flatpickr cho date picker
    flatpickr(".date-picker", {
        dateFormat: "d/m/Y",
        locale: {
            firstDayOfWeek: 1,
            weekdays: {
                shorthand: ["CN", "T2", "T3", "T4", "T5", "T6", "T7"],
                longhand: ["Chủ Nhật", "Thứ Hai", "Thứ Ba", "Thứ Tư", "Thứ Năm", "Thứ Sáu", "Thứ Bảy"],
            },
            months: {
                shorthand: ["Th1", "Th2", "Th3", "Th4", "Th5", "Th6", "Th7", "Th8", "Th9", "Th10", "Th11", "Th12"],
                longhand: ["Tháng Một", "Tháng Hai", "Tháng Ba", "Tháng Tư", "Tháng Năm", "Tháng Sáu", "Tháng Bảy", "Tháng Tám", "Tháng Chín", "Tháng Mười", "Tháng Mười Một", "Tháng Mười Hai"],
            },
        },
    });

    // Handle form submissions
    const forms = [
        { id: "feedbackForm", name: "Đánh giá dịch vụ" },
        { id: "editBookingForm", name: "Lịch đặt xe" },
        { id: "updateCarStatusForm", name: "Tình trạng xe" },
        { id: "supportForm", name: "Hỗ trợ khách hàng" },
    ];

    forms.forEach(({ id, name }) => {
        const form = document.getElementById(id);
        if (form) {
            form.addEventListener("submit", (e) => {
                e.preventDefault();
                const formData = new FormData(form);
                const data = {};
                formData.forEach((value, key) => {
                    data[key] = value;
                });
                console.log(`Dữ liệu ${name}:`, data);
                // TODO: Gửi dữ liệu đến server qua API
                if (form.closest(".modal")) {
                    form.closest(".modal").querySelector(".btn-close").click();
                }
                form.reset();
            });
        }
    });

    // Handle profile edit
    const editProfileBtn = document.getElementById("editProfileBtn");
    const saveProfileBtn = document.getElementById("saveProfileBtn");
    const cancelProfileBtn = document.getElementById("cancelProfileBtn");
    if (editProfileBtn && saveProfileBtn && cancelProfileBtn) {
        const viewSection = document.getElementById("profileView");
        const editSection = document.getElementById("profileEdit");
        const nameText = document.getElementById("profileNameText");
        const emailText = document.getElementById("profileEmailText");
        const phoneText = document.getElementById("profilePhoneText");
        const nameInput = document.getElementById("profileNameInput");
        const emailInput = document.getElementById("profileEmailInput");
        const phoneInput = document.getElementById("profilePhoneInput");

        editProfileBtn.addEventListener("click", () => {
            viewSection.classList.add("d-none");
            editSection.classList.remove("d-none");
            editProfileBtn.classList.add("d-none");
        });

        saveProfileBtn.addEventListener("click", () => {
            nameText.textContent = nameInput.value;
            emailText.textContent = emailInput.value;
            phoneText.textContent = phoneInput.value;
            viewSection.classList.remove("d-none");
            editSection.classList.add("d-none");
            editProfileBtn.classList.remove("d-none");
            console.log("Cập nhật thông tin cá nhân:", { name: nameInput.value, email: emailInput.value, phone: phoneInput.value });
        });

        cancelProfileBtn.addEventListener("click", () => {
            nameInput.value = nameText.textContent;
            emailInput.value = emailText.textContent;
            phoneInput.value = phoneText.textContent;
            viewSection.classList.remove("d-none");
            editSection.classList.add("d-none");
            editProfileBtn.classList.remove("d-none");
        });
    }

    // Handle cancel booking
    const cancelBookingButtons = document.querySelectorAll(".btn-cancel-booking");
    cancelBookingButtons.forEach(btn => {
        btn.addEventListener("click", () => {
            const bookingId = btn.getAttribute("data-id");
            console.log("Hủy đặt xe:", bookingId);
            // TODO: Gọi API để hủy đặt xe
        });
    });

    // Handle edit booking
    const editBookingButtons = document.querySelectorAll(".btn-edit-booking");
    editBookingButtons.forEach(btn => {
        btn.addEventListener("click", () => {
            const bookingId = btn.getAttribute("data-id");
            // Giả lập dữ liệu, thay bằng API nếu có
            document.getElementById("bookingId").value = bookingId;
            console.log("Mở modal chỉnh sửa đặt xe:", bookingId);
        });
    });

    // Handle update car status
    const updateStatusButtons = document.querySelectorAll(".btn-update-status");
    updateStatusButtons.forEach(btn => {
        btn.addEventListener("click", () => {
            const carId = btn.getAttribute("data-id");
            document.getElementById("carId").value = carId;
            console.log("Mở modal cập nhật trạng thái xe:", carId);
        });
    });

    // Handle logout
    const logoutBtn = document.getElementById("logoutBtn");
    if (logoutBtn) {
        logoutBtn.addEventListener("click", (e) => {
            e.preventDefault();
            console.log("Đăng xuất");
            // TODO: Gọi API đăng xuất và chuyển hướng
            window.location.href = "/login.html";
        });
    }
});

function attachSectionToggleEvents() {
    const sectionButtons = document.querySelectorAll('[data-section]');
    const sections = document.querySelectorAll('.content-section');

    sectionButtons.forEach(btn => {
        if (btn.hasAttribute('data-bs-toggle')) return;

        btn.addEventListener('click', function (e) {
            e.preventDefault();
            const targetId = btn.getAttribute('data-section');

            sections.forEach(section => {
                section.classList.remove('active');
            });

            const targetSection = document.getElementById(targetId);
            if (targetSection) {
                targetSection.classList.add('active');
            }
        });
    });
}