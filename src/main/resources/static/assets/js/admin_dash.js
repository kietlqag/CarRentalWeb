document.addEventListener('DOMContentLoaded', function () {
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

    // Initialize Flatpickr for date pickers
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

    // Initialize Flatpickr for dateRangePicker
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
        { id: "addCarForm", name: "Xe" },
        { id: "addPromotionForm", name: "Khuyến mãi" },
        {id: "addServiceForm", name: "Dịch vụ"}
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
                form.closest(".modal").querySelector(".btn-close").click();
                form.reset();
            });
        }
    });
// Edit buttons for Services
    const editServiceButtons = document.querySelectorAll('.btn-warning');
    editServiceButtons.forEach(button => {
        button.addEventListener('click', function () {
            const id = button.getAttribute('data-id');
            document.getElementById('serviceId').value = id;
            document.getElementById('serviceName').value = button.getAttribute('data-nameService');
            document.getElementById('servicePrice').value = button.getAttribute('data-price');
            document.getElementById('serviceStatus').value = button.getAttribute('data-status');
            document.getElementById('servicePicture').value = button.getAttribute('data-picture');
        });
    });

// Save (Update) service button
    document.getElementById('saveServiceButton')?.addEventListener('click', function () {
        const id = document.getElementById('serviceId').value;
        const payload = {
            nameService: document.getElementById('serviceName').value,
            price: parseFloat(document.getElementById('servicePrice').value),
            status: document.getElementById('serviceStatus').value,
            picture: document.getElementById('servicePicture').value
        };

        fetch(`/admin/services/update/${id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(payload)
        })
            .then(response => {
                if (response.ok) {
                    alert('Cập nhật dịch vụ thành công');
                    $('#editServiceModal').modal('hide');

                } else {
                    alert('Cập nhật dịch vụ thất bại');
                }
            })
            .catch(error => {
                console.error('Lỗi khi gửi dữ liệu:', error);
            });
    });

// Create service button
    document.getElementById('createServiceButton')?.addEventListener('click', function () {
        const payload = {
            nameService: document.getElementById('newServiceName').value,
            price: parseFloat(document.getElementById('newServicePrice').value),
            status: document.getElementById('newServiceStatus').value,
            picture: document.getElementById('newServicePicture').value
        };

        fetch('/admin/services/create', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(payload)
        })
            .then(response => {
                if (response.ok) {
                    alert('Tạo dịch vụ thành công');
                    $('#addServiceModal').modal('hide');
                    document.getElementById('addServiceForm').reset();
                } else {
                    alert('Tạo dịch vụ thất bại');
                }
            })
            .catch(error => {
                console.error('Lỗi khi tạo dịch vụ:', error);
            });
    });
    // Promotion edit buttons
    const editButtons = document.querySelectorAll('.btn-warning');
    editButtons.forEach(button => {
        button.addEventListener('click', function () {
            const id = button.getAttribute('data-id');
            document.getElementById('promotionId').value = id;
            document.getElementById('promotionCode').value = button.getAttribute('data-code');
            document.getElementById('promotionDescription').value = button.getAttribute('data-description');
            document.getElementById('promotionValidfrom').value = formatDate(button.getAttribute('data-validfrom'));
            document.getElementById('promotionValidto').value = formatDate(button.getAttribute('data-validto'));
            document.getElementById('promotionDiscountPercent').value = button.getAttribute('data-discountpercent');
            document.getElementById('promotionType').value = button.getAttribute('data-type');
            document.getElementById('promotionStatus').value = button.getAttribute('data-isactive');
        });
    });

    // Save promotion button
    document.getElementById('savePromotionButton')?.addEventListener('click', function () {
        const id = document.getElementById('promotionId').value;
        const payload = {
            code: document.getElementById('promotionCode').value,
            description: document.getElementById('promotionDescription').value,
            validfrom: convertToISODate(document.getElementById('promotionValidfrom').value),
            validto: convertToISODate(document.getElementById('promotionValidto').value),
            discountpercent: document.getElementById('promotionDiscountPercent').value,
            type: document.getElementById('promotionType').value,
            isactive: document.getElementById('promotionStatus').value
        };

        fetch(`/admin/promotions/update/${id}`, {
            method: 'PUT',
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

    // Create promotion button
    document.getElementById('createPromotionButton')?.addEventListener('click', function () {
        const payload = {
            code: document.getElementById('newPromotionCode').value,
            description: document.getElementById('newPromotionDescription').value,
            validfrom: convertToISODate(document.getElementById('newPromotionValidfrom').value),
            validto: convertToISODate(document.getElementById('newPromotionValidto').value),
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
                    document.getElementById('addPromotionForm').reset();
                } else {
                    alert('Tạo khuyến mãi thất bại');
                }
            })
            .catch(error => {
                console.error('Lỗi khi tạo khuyến mãi:', error);
            });
    });

    // View order buttons
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

    // Edit account modal
    const editModal = document.getElementById('editAccountModal');
    if (editModal) {
        editModal.addEventListener('show.bs.modal', function (event) {
            const button = event.relatedTarget;
            const email = button.getAttribute('data-email');
            const role = button.getAttribute('data-role');

            document.getElementById('editEmail').value = email;
            document.getElementById('editRole').value = role;
        });

        document.getElementById('editRoleForm')?.addEventListener('submit', function (e) {
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

    // Car form
    const carForm = document.getElementById("carForm");
    if (carForm) {
        carForm.addEventListener("submit", function (e) {
            e.preventDefault();
            const formData = new FormData(carForm);
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
                        carForm.reset();
                    } else {
                        alert("Lỗi khi tạo xe");
                    }
                })
                .catch(error => {
                    console.error("Lỗi:", error);
                    alert("Lỗi mạng hoặc server");
                });
        });
    }

    // Edit car modal
    const editCarModal = document.getElementById('editCarModal');
    if (editCarModal) {
        editCarModal.addEventListener('show.bs.modal', function (event) {
            const button = event.relatedTarget;
            const carId = button.getAttribute('data-id');
            const price = button.getAttribute('data-price');
            const status = button.getAttribute('data-status');

            document.getElementById('editCarId').value = carId;
            document.getElementById('editCarPrice').value = price;
            document.getElementById('editCarStatus').value = status;
        });

        document.getElementById("editCarForm")?.addEventListener("submit", function (e) {
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
                        location.reload();
                    } else {
                        return res.text().then(t => { throw new Error(t) });
                    }
                })
                .catch(err => {
                    console.error(err);
                    alert("Cập nhật thất bại!");
                });
        });
    }

    // Revenue chart
    const canvas = document.getElementById("revenueChart");
    if (canvas) {
        const revenueDataStr = canvas.getAttribute("data-revenue");
        try {
            const revenueData = JSON.parse(revenueDataStr);
            const labels = Array.from({ length: 12 }, (_, i) => `Tháng ${i + 1}`);
            const data = labels.map((_, i) => revenueData[i + 1] || 0);

            new Chart(canvas, {
                type: "bar",
                data: {
                    labels: labels,
                    datasets: [{
                        label: "Doanh thu (VNĐ)",
                        data: data,
                        backgroundColor: "rgba(75, 192, 192, 0.6)",
                        borderColor: "rgba(75, 192, 192, 1)",
                        borderWidth: 1
                    }]
                },
                options: {
                    responsive: true,
                    scales: {
                        y: {
                            beginAtZero: true,
                            ticks: {
                                callback: value => value.toLocaleString("vi-VN") + "đ"
                            }
                        }
                    }
                }
            });
        } catch (error) {
            console.error("Lỗi khi parse JSON doanh thu:", error);
        }
    }
});

// Utility functions
function deleteService(id) {
    if (confirm("Bạn có chắc chắn muốn xóa dịch vụ này không?")) {
        fetch(`/admin/services/delete/${id}`, {
            method: 'DELETE'
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
                // Giả sử mỗi dòng trong bảng dịch vụ có thuộc tính data-service-id
                const serviceRow = document.querySelector(`tr[data-service-id="${id}"]`);
                if (serviceRow) {
                    serviceRow.remove();
                }
                alert("Xóa dịch vụ thành công!");
            })
            .catch(error => {
                console.error('Error:', error);
                alert(error.message || "Có lỗi xảy ra khi xóa dịch vụ!");
            });
    }
}
function deleteAccount(email) {
    if (confirm("Are you sure you want to delete this account?")) {
        fetch('/admin/accounts/delete?email=' + encodeURIComponent(email), {
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
                const accountRow = document.querySelector(`tr[data-email="${email}"]`);
                if (accountRow) {
                    accountRow.remove();
                }
                alert("Xóa tài khoản thành công!");
            })
            .catch(error => {
                console.error('Error:', error);
                alert(error.message || "Có lỗi xảy ra khi xóa tài khoản!");
            });
    }
}

function deletePromotion(id) {
    if (confirm("Bạn có chắc chắn muốn xóa khuyến mãi này không?")) {
        fetch(`/admin/promotions/delete/${id}`, {
            method: 'DELETE'
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
                const promotionRow = document.querySelector(`tr[data-promotion-id="${id}"]`);
                if (promotionRow) {
                    promotionRow.remove();
                }
                alert("Xóa khuyến mãi thành công!");
            })
            .catch(error => {
                console.error('Error:', error);
                alert(error.message || "Có lỗi xảy ra khi xóa khuyến mãi!");
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

function convertToISODate(dateStr) {
    const [dd, mm, yyyy] = dateStr.split('/');
    const date = new Date(Date.UTC(yyyy, mm - 1, dd, 24, 0, 0));
    const newYear = date.getUTCFullYear();
    const newMonth = String(date.getUTCMonth() + 1).padStart(2, '0');
    const newDay = String(date.getUTCDate()).padStart(2, '0');
    return `${newYear}-${newMonth}-${newDay}`;
}