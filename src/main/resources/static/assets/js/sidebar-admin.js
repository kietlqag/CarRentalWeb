document.addEventListener("DOMContentLoaded", () => {
    fetch("/admin_sb.html")
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
});

function attachSectionToggleEvents() {
    const sectionButtons = document.querySelectorAll('[data-section]');
    const sections = document.querySelectorAll('.content-section');

    sectionButtons.forEach(btn => {
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
