document.addEventListener("DOMContentLoaded", () => {
    fetch("/sidebar-admin.html")
        .then(response => response.text())
        .then(html => {
            document.getElementById("sidebar").innerHTML = html;

            // Sau khi load xong sidebar, gán event cho các nút có data-section
            attachSectionToggleEvents();
        })
        .catch(error => {
            console.error("Không thể load sidebar:", error);
        });
});

function attachSectionToggleEvents() {
    const sectionButtons = document.querySelectorAll('[data-section]');
    const sections = document.querySelectorAll('.content-section');

    sectionButtons.forEach(btn => {
        btn.addEventListener('click', function (e) {
            e.preventDefault();
            const targetId = btn.getAttribute('data-section');

            // Ẩn tất cả sections
            sections.forEach(section => {
                section.classList.remove('active');
            });

            // Hiện section tương ứng
            const targetSection = document.getElementById(targetId);
            if (targetSection) {
                targetSection.classList.add('active');
            }
        });
    });
}
