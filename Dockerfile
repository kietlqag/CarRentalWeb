# Stage 1: Build ứng dụng
FROM maven:3.8.5-openjdk-17 AS builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Tạo image chạy ứng dụng
FROM openjdk:17-jdk-slim
WORKDIR /app
ENV APP_NAME=CarRentalWeb-1.0.0.jar
COPY --from=builder /app/target/${APP_NAME} ${APP_NAME}
EXPOSE 5000
ENTRYPOINT ["java", "-jar", "CarRentalWeb-1.0.0.jar"]
