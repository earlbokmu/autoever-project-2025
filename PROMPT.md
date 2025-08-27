**프로젝트 기초세팅**   
자바 스프링 프로젝트를 만든다   
<br>
아래 조건을 참고

Java 17<br>
Spring boot framework <br>
MVC 패턴<br>
JPA<br>
H2<br>
Spring Data Redis<br>
API에 대한 테스트 코드(JUnit, Mockito) 작성<br>
Swegger 사용<br>
기본 lombok을 사용<br>
실행은 docker 사용<br>
<br>
<br>
**1. 간단한 회원가입 API**<br>
rrn이나 phone 자릿수가 안 맞을 경우에는 예외처리<br>
API는 무조건 성공 처리<br>
<br>
UserController에 간단한 회원가입 API 작성<br>
프로젝트 구조를 참고해서 Entity와 DTO, DAO를 작성하고<br>
Spring JPA로 아래 조건에 맞는 User Entity를 생성<br>
<br>
H2에 User 테이블 생성<br>

테이블명: Users<br>
ID 기본키<br>
계정 Type : String, unique<br>
암호 Type : String<br>
성명 Type : String<br>
주민등록번호 Type : String, Length : 13, unique<br>
핸드폰번호 Type : String, Length : 11<br>
주소 Type : String<br>
<br>
회원가입 시 정말 간단한 INSERT API만 생성<br>
주민등록번호나 핸드폰번호는 길이가 맞지 않을 경우 예외처리<br>
<br>
<br>
<br>
**2. 회원 조회, 수정, 삭제 API**
<br>
조회는 pagination 필요<br>
수정은 암호, 주소만 가능 (암호만 수정, 주소만 수정, 암호와 주소 수정)<br>
<br>
API인증 수단은 basic auth 기반<br>
관리자 계정 사용자명: admin / 암호: 1212<br>
<br>
삭제는 단일 삭제, 전체 삭제 구현<br>
필요한 파라미터는 USER의 계정으로 받도록 구현<br>
<br>
<br>
**3. 사용자 로그인 API**<br>
사용자가 로그인 할 수 있도록 구현<br>
API는 basic auth 기반<br>
User 테이블 데이터를 사용한다<br>
<br>
<br>
**4. 자신의 회원 상세정보 조회**<br>
본인의 상세정보를 확인할 수 있는 API <br>
사용자 로그인 기반<br>
세션이 있을 경우에는 로그인 가능<br>
계정/암호가 맞지 않을 겅우 예외처리<br>
자신의 계정, 성명, 주민등록번호, 핸드폰번호, 주소 조회 가능<br>
주소는 가장 큰 행정구역만 (예시: 서울특별시, 경기도, 강원특별자치도 등)<br>
<br>
<br>
**5. 메시지 발송 API**
사용자수가 3천만명 이상<br>
모든 회원을 대상으로, 연령대별 카카오톡 메세지를 발송할 수 있는 API<br>
<br>
연령대는 10대 20대 30대 40대 50대 60대 70대 80대 등..<br>
카카오톡 메세지를 보내는 데 실패할 경우 SMS 문자메세지 대체문자<br>
<br>
Spring Data Redis 사용<br>
카카오톡 메세지는 발급된 토큰 당 1분당 100회까지만 호출 가능<br>
문자 메세지는 분당 500회 제한<br>
<br>
**카카오톡 메세지 발송을 위한 API 명세**<br>
POST http://localhost:8081/kakaotalk-messages<br>
헤더<br>
Authorization (Basic auth)<br>
사용자명: autoever<br>
암호: 1234<br>
content-type (applciation/json)<br>
<br>
요청바디 {"phone": "xxx-xxxx-xxxx", "message": "blabla"}<br>
서버 response http status code: 200 or 400 or 401 or 500<br>
응답 바디: 없음<br>
<br>
**문자메세지 발송을 위한 API 명세**<br>
POST [http://localhost:8082/sms?phone={phone}](http://localhost:8082/sms?phone=%7Bphone%7D)<br>
헤더<br>
Authorization (Basic auth)<br>
사용자명: autoever<br>
암호: 5678<br>
content-type (application/x-www-form-urlencoded)<br>
<br>
요청바디<br>
{"message": "blabla"}<br>
서버 response http status code: 200 or 400 or 401 or 500<br>
응답 바디: application/json {"result": "OK"}<br>
