document.addEventListener("DOMContentLoaded", () => {
    // Load sidebar
    fetch("/admin_sb.html")
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

    // Khởi tạo Flatpickr cho dateRangePicker
    flatpickr("#dateRangePicker", {
        mode: "range",
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
        onChange: (selectedDates, dateStr, instance) => {
            console.log("Khoảng thời gian được chọn:", dateStr);
        }
    });

    // Handle form submissions
    const forms = [
        { id: "addUserForm", name: "Người dùng" },
        { id: "addCarForm", name: "Xe" },
        { id: "addContractForm", name: "Hợp đồng" },
        { id: "addPromotionForm", name: "Khuyến mãi" },
        { id: "addPaymentMethodForm", name: "Phương thức thanh toán" },
        { id: "addContentForm", name: "Nội dung" },
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
                console.log(`Dữ liệu ${name} mới:`, data);
                // TODO: Gửi dữ liệu đến server qua API
                form.closest(".modal").querySelector(".btn-close").click(); // Đóng modal
                form.reset(); // Reset form
            });
        }
    });
});
document.addEventListener("DOMContentLoaded", function () {
    const form = document.getElementById("carForm");

    form.addEventListener("submit", function (e) {
        e.preventDefault();

        const formData = new FormData(form);
        const car = {};

        for (let [key, value] of formData.entries()) {
            car[key] = value;
        }

        fetch("/admin/cars/create", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(car)
        })
            .then(response => {
                if (response.ok) {
                    alert("Tạo xe thành công");
                    form.reset();
                } else {
                    alert("Lỗi khi tạo xe");
                }
            })
            .catch(error => {
                console.error("Lỗi:", error);
                alert("Lỗi mạng hoặc server");
            });
    });
});
document.addEventListener("DOMContentLoaded", () => {
    // Khi bấm nút Edit
    const editCarModal = document.getElementById('editCarModal');

    editCarModal.addEventListener('show.bs.modal', function (event) {
        const button = event.relatedTarget;
        const carId = button.getAttribute('data-id');
        const price = button.getAttribute('data-price');
        const status = button.getAttribute('data-status');

        // Gán dữ liệu vào form
        document.getElementById('editCarId').value = carId;
        document.getElementById('editCarPrice').value = price;
        document.getElementById('editCarStatus').value = status;
    });

    // Gửi dữ liệu cập nhật khi submit
    const editCarForm = document.getElementById("editCarForm");
    editCarForm.addEventListener("submit", function (e) {
        e.preventDefault();

        const id = document.getElementById("editCarId").value;
        const price = document.getElementById("editCarPrice").value;
        const status = document.getElementById("editCarStatus").value;

        fetch(`/admin/cars/update/${id}`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ price, status })
        })
            .then(res => {
                if (res.ok) {
                    alert("Cập nhật thành công!");
                    location.reload(); // hoặc cập nhật bảng dữ liệu nếu dùng JS
                } else {
                    return res.text().then(t => { throw new Error(t) });
                }
            })
            .catch(err => {
                console.error(err);
                alert("Cập nhật thất bại!");
            });
    });
});
function deleteCar(id) {
    if (confirm("Bạn có chắc chắn muốn xóa xe này?")) {
        fetch(`/admin/cars/delete/${id}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            }
        })
        .then(response => {
            if (!response.ok) {
                return response.text().then(text => {
                    throw new Error(text || 'Xóa thất bại!');
                });
            }
            return response;
        })
        .then(() => {
            // Remove the car row from the table without reloading
            const carRow = document.querySelector(`tr[data-car-id="${id}"]`);
            if (carRow) {
                carRow.remove();
            }
            alert("Xóa xe thành công!");
        })
        .catch(error => {
            console.error('Error:', error);
            alert(error.message || "Có lỗi xảy ra khi xóa xe!");
        });
    }
}
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