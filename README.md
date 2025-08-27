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

**실행 방법**

Docker, git이 설치되어 있음을 가정
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
    - 포트 8080으로 프로젝트 실행   
    - http://localhost:8080/swagger-ui.html 접속   
<br/>
<br/>
Basic Auth 로그인   
<br/>
<img width="700" height="372" alt="Image" src="https://github.com/user-attachments/assets/e6145e10-fa9f-416a-a4ba-657512ccd785" />   
<img width="700" height="618" alt="Image" src="https://github.com/user-attachments/assets/10131ee1-7532-4c6f-a738-8cf207e42f93" />
