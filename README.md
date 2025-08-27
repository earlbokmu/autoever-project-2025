**개발 환경**
- Server
    - Java17
    - Spring Boot 3.5.5
    - JPA
    - H2
    - Gradle
    - Junit5
  <br/>
    <br/>
      <br/>
**실행 방법**

Docker가 설치되어 있음을 가정함
 <br/>
 <br/>
git에서 프로젝트 클론   
```git clone https://gitlab.com/yalco/practice-docker.git```   
 <br/>
  <br/>
스프링 부트 이미지 build 명령어 (api:latest 라는 이미지 생성)   
```docker build -t api:latest ./```   
 <br/> 
 <br/>
컨테이너 run 명령어 (백그라운드 실행)   
실행은 해당 Dockerfile이 있는 프로젝트 위치에서 실행      
```docker run -d -p 8080:8080 api:latest```   
 <br/>
  <br/>
Redis 설치   
```docker pull redis:latest```   
  <br/>
Redis 실행   
```docker run --rm -p 6379:6379 -it redis:latest```   
<br/>
<br/>
실행중지      
```docker rmi api:latest```      
```docker rmi redis:latest```   
<br/>
<br/>
<br/>
**API 테스트 방법**   
Swagger API 테스트   
    - http://localhost:8080/swagger-ui.html 접속   
