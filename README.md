**개발 환경**
- Server
    - Java17
    - Spring Boot 2.2.2
    - JPA
    - H2
    - Gradle
    - Junit5
 

git에서 프로젝트 클론 
git clone https://gitlab.com/yalco/practice-docker.git

스프링 부트 이미지 build 명령어 (api:latest 라는 이미지 생성)
docker build -t api:latest ./

컨테이너 run 명령어 (백그라운드 실행)
실행은 해당 Dockerfile이 있는 프로젝트 위치에서 실행한다
docker run -d -p 8080:8080 api:latest

실행중지
docker rmi api:latest
