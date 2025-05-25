# 1. Dùng hình ảnh JDK 17 làm nền
FROM openjdk:17-jdk-slim

# 2. Biến môi trường tên file JAR
ENV APP_NAME=CarRentalWeb-1.0.0.jar

# 3. Tạo thư mục làm việc trong container
WORKDIR /app

# 4. Copy file JAR từ target/ vào container
COPY target/*.jar ${APP_NAME}

# 5. Mở cổng ứng dụng
EXPOSE 5000

# 6. Câu lệnh chạy ứng dụng
ENTRYPOINT ["java", "-jar", "app.jar"]