
FROM ubuntu:latest

# 작업 디렉토리 설정
WORKDIR /logistics-platform

# 프로젝트 파일 복사
COPY . .

# 패키지 업데이트 및 설치
RUN apt-get update && apt-get install -y openjdk-17-jdk curl findutils && \
    chmod +x ./gradlew


# 엔트리포인트 설정
ENTRYPOINT ["./gradlew", "bootRun"]

