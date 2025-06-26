# 1. 베이스 이미지 설정
FROM mcr.microsoft.com/playwright/java:v1.52.0

# 2. JAR 복사 및 실행
COPY target/*.jar /app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]