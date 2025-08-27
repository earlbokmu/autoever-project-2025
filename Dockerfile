# 베이스 이미지 지정
FROM openjdk:17

# 작업 디렉토리
WORKDIR /api

# 환경변수 설정
ARG JAR_FILE=./build/libs/*SNAPSHOT.jar

# 파일 복사 (로컬->컨테이너)
COPY ${JAR_FILE} app.jar

# 포트 익스포트
EXPOSE 8080

# 실행 명령 (컨테이너)
CMD ["java", "-jar", "app.jar"]