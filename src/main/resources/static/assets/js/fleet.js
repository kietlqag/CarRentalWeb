document.addEventListener('DOMContentLoaded', function () {
    const detailModal = new bootstrap.Modal(document.getElementById('carDetailsModal'));
    const modalTitle = document.getElementById('modalCarName');
    const modalImage = document.getElementById('modalCarImage');
    const modalPrice = document.getElementById('modalCarPrice');
    const modalDetails = document.getElementById('modalCarDetails');
    const modalColorSelect = document.getElementById('carColorSelect');
    const modalServiceSelect = document.getElementById('carServiceSelect');
    const quantityMinus = document.getElementById('quantityMinus');
    const quantityPlus = document.getElementById('quantityPlus');
    const quantityInput = document.getElementById('quantityInput');

    // Dữ liệu xe
    const carDetails = {
        'Toyota Land Cruiser': {
            price: '$810 per day',
            specs: [
                'Engine: 3.5L V6 - Twin Turbo',
                'Seat: 7 seats',
                'Model: Toyota Land Cruiser (2022)',
                'Type: SUV',
                'Body Type: 5-door style',
                'Fuel Type: Diesel'
            ],
            colors: ['Pure White', 'Black', 'Silver', 'Gray'],
            services: ['Service I', 'Service II', 'Service III']
        },
        'Cadillac Escalade': {
            price: '$850 per day',
            specs: [
                'Engine: 6.2L V8',
                'Seat: 7 seats',
                'Model: Cadillac Escalade (2023)',
                'Type: Luxury SUV',
                'Fuel Type: Petrol'
            ],
            colors: ['Black', 'White', 'Silver'],
            services: ['VIP Service', 'Standard Rental']
        },
        'Range Rover LWB': {
            price: '$900 per day',
            specs: [
                'Engine: 5.0L V8 Supercharged',
                'Seat: 7 seats',
                'Model: Range Rover LWB (2023)',
                'Type: Luxury SUV',
                'Fuel Type: Petrol'
            ],
            colors: ['Dark Blue', 'Black', 'White'],
            services: ['Luxury Package', 'Standard']
        },
        'Lincoln Navigator': {
            price: '$880 per day',
            specs: [
                'Engine: 3.5L Twin-Turbo V6',
                'Seat: 7 seats',
                'Model: Lincoln Navigator (2022)',
                'Type: Luxury SUV',
                'Fuel Type: Petrol'
            ],
            colors: ['Black', 'Silver'],
            services: ['Business Class', 'Economy']
        }
    };

    // Lắng nghe sự kiện click trên document (event delegation)
    document.addEventListener('click', function (event) {
        if (event.target.classList.contains('details-btn')) { 
            const card = event.target.closest('.fleet-card');
            if (!card) return;

            const carName = card.querySelector('h3').textContent.trim();
            const carImage = card.querySelector('img').src;
            const details = carDetails[carName];

            if (!details) {
                console.error("Không tìm thấy dữ liệu cho xe:", carName);
                return;
            }

            // Gán nội dung modal
            modalTitle.textContent = carName;
            modalImage.src = carImage;
            modalPrice.textContent = details.price;
            modalDetails.innerHTML = details.specs.map(spec => `<p>${spec}</p>`).join('');
            modalColorSelect.innerHTML = details.colors.map(color => `<option value="${color}">${color}</option>`).join('');
            modalServiceSelect.innerHTML = details.services.map(service => `<option value="${service}">${service}</option>`).join('');

            // Reset số lượng
            quantityInput.value = 1;

            // Hiển thị modal
            detailModal.show();
        }
    });

    // Sự kiện tăng giảm số lượng
    quantityMinus.addEventListener('click', function () {
        let currentValue = parseInt(quantityInput.value);
        if (currentValue > 1) {
            quantityInput.value = currentValue - 1;
        }
    });

    quantityPlus.addEventListener('click', function () {
        let currentValue = parseInt(quantityInput.value);
        quantityInput.value = currentValue + 1;
    });
});
