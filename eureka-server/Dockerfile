# Gradle 빌드 이미지 사용
FROM gradle:7.6-jdk17 AS build

# 작업 디렉토리 설정
WORKDIR /app

# 소스 복사 및 빌드
COPY . .
RUN gradle build --no-daemon

# 실행 이미지 생성
FROM openjdk:17-jdk
WORKDIR /app

# 빌드 결과 복사
COPY --from=build /app/build/libs/*.jar app.jar

# 컨테이너 실행
ENTRYPOINT ["java", "-jar", "app.jar"]
