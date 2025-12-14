# Build stage
FROM gradle:8.14-jdk17 AS build
WORKDIR /app
COPY . .
RUN gradle clean build -x test

# 실행 스테이지
FROM eclipse-temurin:17-jre

# tzdata 패키지 설치 및 시간대 설정
RUN apt-get update && apt-get install -y tzdata && \
    ln -sf /usr/share/zoneinfo/Asia/Seoul /etc/localtime && \
    echo "Asia/Seoul" > /etc/timezone

WORKDIR /app
EXPOSE 8080

# 빌드 스테이지에서 생성된 JAR 파일 복사
COPY --from=build /app/build/libs/*.jar app.jar

# 환경을 운영으로 설정하고, 포트를 설정
# JVM 메모리 옵션:
# -Xms: 초기 힙 메모리 (512MB)
# -Xmx: 최대 힙 메모리 (1GB)
# -XX:MaxMetaspaceSize: 메타스페이스 최대 크기 (256MB)
# -XX:+UseG1GC: G1 가비지 컬렉터 사용 (메모리 효율적)
CMD java -Xms512m -Xmx1024m \
        -XX:MaxMetaspaceSize=256m \
        -XX:+UseG1GC \
        -Dserver.port=8080 \
        -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE:-prod} \
        -jar app.jar