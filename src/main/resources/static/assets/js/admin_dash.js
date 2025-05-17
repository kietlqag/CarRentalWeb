document.addEventListener('DOMContentLoaded', function () {
    const editButtons = document.querySelectorAll('.btn-warning');
    editButtons.forEach(button => {
        button.addEventListener('click', function () {
            const id = button.getAttribute('data-id');
            document.getElementById('promotionId').value = id; // Gán id vào input hidden

            // Gán các trường còn lại
            document.getElementById('promotionCode').value = button.getAttribute('data-code');
            document.getElementById('promotionDescription').value = button.getAttribute('data-description');
            document.getElementById('promotionDiscountPercent').value = button.getAttribute('data-discountpercent');
            document.getElementById('promotionType').value = button.getAttribute('data-type');
            document.getElementById('promotionStatus').value = button.getAttribute('data-isactive');
        });
    });

    document.getElementById('savePromotionButton').addEventListener('click', function () {
        const id = document.getElementById('promotionId').value;

        const payload = {
            code: document.getElementById('promotionCode').value,
            description: document.getElementById('promotionDescription').value,
            discountpercent: document.getElementById('promotionDiscountPercent').value,
            type: document.getElementById('promotionType').value,
            isactive: document.getElementById('promotionStatus').value
        };

        fetch(`/admin/promotions/update/${id}`, {
            method: 'PUT', // Phải dùng PUT vì controller dùng @PutMapping
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(payload)
        })
            .then(response => {
                if (response.ok) {
                    alert('Cập nhật thành công');
                    $('#editPromotionModal').modal('hide');
                } else {
                    alert('Cập nhật thất bại');
                }
            })
            .catch(error => {
                console.error('Lỗi khi gửi dữ liệu:', error);
            });
    });
});
document.getElementById('createPromotionButton').addEventListener('click', function () {
    const payload = {
        code: document.getElementById('newPromotionCode').value,
        description: document.getElementById('newPromotionDescription').value,
        discountpercent: parseInt(document.getElementById('newPromotionDiscountPercent').value),
        type: parseInt(document.getElementById('newPromotionType').value),
        isactive: parseInt(document.getElementById('newPromotionStatus').value)
    };

    fetch('/admin/promotions/create', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(payload)
    })
        .then(response => {
            if (response.ok) {
                alert('Tạo khuyến mãi thành công');
                $('#addPromotionModal').modal('hide');
                // Optional: reload danh sách hoặc reset form
                document.getElementById('addPromotionForm').reset();
            } else {
                alert('Tạo khuyến mãi thất bại');
            }
        })
        .catch(error => {
            console.error('Lỗi khi tạo khuyến mãi:', error);
        });
});
document.addEventListener('DOMContentLoaded', function () {
    const viewButtons = document.querySelectorAll('[data-bs-target="#viewOrderModal"]');

    viewButtons.forEach(btn => {
        btn.addEventListener('click', () => {
            const bookingId = btn.getAttribute('data-booking-id');

            fetch(`/admin/orders/${bookingId}`)
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
                });
        });
    });
});
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
    const editModal = document.getElementById('editAccountModal');

    if (editModal) {
        // Gán dữ liệu vào form khi mở modal
        editModal.addEventListener('show.bs.modal', function (event) {
            const button = event.relatedTarget;
            const email = button.getAttribute('data-email');
            const role = button.getAttribute('data-role');

            document.getElementById('editEmail').value = email;
            document.getElementById('editRole').value = role;
        });

        // Gửi request cập nhật vai trò
        document.getElementById('editRoleForm').addEventListener('submit', function (e) {
            e.preventDefault();

            const email = document.getElementById('editEmail').value;
            const role = document.getElementById('editRole').value;

            fetch('/admin/accounts/update-role', {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ email, role })
            })
                .then(res => {
                    if (res.ok) {
                        alert("Đã cập nhật vai trò");
                        location.reload();
                    } else {
                        return res.text().then(msg => {
                            alert("Lỗi: " + msg);
                        });
                    }
                })
                .catch(err => {
                    alert("Lỗi hệ thống: " + err.message);
                });
        });
    }
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
function deleteAccount(email) {
    if (confirm("Are you sure you want to delete this account?")) {
        const emailPrefix = email.split('@')[0].toLowerCase();

        fetch('/admin/accounts/delete?email=' + encodeURIComponent(email), {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(response => {
                if (response.ok) {
                    alert("Account deleted successfully!");
                    location.reload()
                    location.href = '#users';

                } else {
                    response.text().then(msg => alert("Failed to delete account: " + msg));
                }
            })
            .catch(error => {
                alert("Error: " + error);
            });
    }
}
function deletePromotion(id) {
    if (confirm("Bạn có chắc chắn muốn xóa khuyến mãi này không?")) {
        fetch(`/admin/promotions/delete/${id}`, {
            method: 'DELETE'
        })
            .then(response => {
                if (response.ok) {
                    alert("Đã xóa thành công!");
                    location.reload(); // reload lại trang
                } else {
                    alert("Xóa thất bại. Vui lòng thử lại!");
                }
            })
            .catch(error => {
                console.error("Lỗi khi gửi yêu cầu xóa:", error);
                alert("Đã xảy ra lỗi.");
            });
    }
}

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