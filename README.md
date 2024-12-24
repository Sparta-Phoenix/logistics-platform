# 🚚 logistics-platform

## 📅 프로젝트 진행 기간

2024.12.05 - 2024.12.17 (13일 동안 진행)

## ❓ 프로젝트 목적
- 물류 관리 및 배송 시스템
- MSA 기반의 시스템을 설계하고 구현하면서 다양한 기술과 방법론을 적용함.
- 프로젝트를 통해 MSA의 복잡성을 이해하고, 실무에서 발생할 수 있는 문제를 간접적으로 경험하고 해결하고자 함.  

## 🗂️ 주요 서비스
- eureka-server
전체 어플리케이션 관리
- config-server
전체 어플리케이션 설정 파일 관리
- gateway-service
요청의 모든 인입 관리
- auth-service
인증 및 권한 확인 관리
- company-service
업체 관리
- hub-service
허브와 허브간 이동정보 관리
- order-service
주문 관리
- delivery-manager-service
배송과 배송 경로와 배송 담당자 관리
- product-service
상품 관리
- slack-service
슬랙 메시지 관리
AI 연동 관리
  
## 🧑🏻‍💻 팀 구성
<table>
<tbody>
<tr>
<td align="center">
<a href="https://github.com/k-jaehyun">
<img src="https://ca.slack-edge.com/T06B9PCLY1E-U07Q5EL6JPQ-f14a05f6d345-512" width="100px;" alt="프로필이미지"/>
<br />
<sub><b>[팀장] 김재현</b></sub>
<br />
</a>
<span>Order, Product, Slack</span>
</td>
<td align="center">
<a href="https://github.com/drinkgalaxy">
<img src="docs/images/hyunjin.jpg" width="100px;" alt="프로필이미지"/>
<br />
<sub><b>이현진</b></sub>
<br />
</a>
<span>Company, Hub, HubRoute, Ai</span>
</td>
<td align="center">
<a href="https://github.com/hiimsajo">
<img src="docs/images/seunga.jpg" width="100px;" alt="프로필이미지"/>
<br />
<sub><b>조승아</b></sub>
</a>
<br />
<span>Delivery, DeliveryRoute, DeliveryManager</span>
</td>
<td align="center">
<a href="https://github.com/Yuurim98">
<img src="docs/images/yurim.jpg" width="100px;" alt="프로필이미지"/>
<br />
<sub><b>최유림</b></span>
<br />
</a>
<span>Auth, 인증/인가, Swagger</span>
</td>
</tr>
</tbody>
</table>

## 📄 API docs
<img src="docs/images/auth, user API.png" alt="auth, user API"/>
<img src="docs/images/company API.png" alt="company API"/>
<img src="docs/images/deliveryManager API.png" alt="deliveryManager API"/>
<img src="docs/images/hub API.png" alt="hub API"/>
<img src="docs/images/order API.png" alt="order API"/>
<img src="docs/images/product API.png" alt="product API"/>
<img src="docs/images/slack API.png" alt="slack API"/>

## 🛠 개발 환경
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-336791?style=for-the-badge&logo=postgresql&logoColor=white)
![Redis](https://img.shields.io/badge/redis-%23DD0031.svg?style=for-the-badge&logo=redis&logoColor=white)
![Git](https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=git&logoColor=white)
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)
![Google Gemini AI](https://img.shields.io/badge/Google%20Gemini%20AI-4285F4?style=for-the-badge&logo=google&logoColor=white)
![Slack](https://img.shields.io/badge/Slack-4A154B?style=for-the-badge&logo=slack&logoColor=white)

## 📃 인프라 설계서
>redis: 캐싱 / Geo Service: 길찾기 / AI: 메세지 발송

![인프라 설계도](https://github.com/user-attachments/assets/e92ef1bd-7807-4c2d-9a03-e787616ba871)

## 📑 ERD
![team4erd](https://github.com/user-attachments/assets/297b320f-a622-4329-8847-8ff088fe336e)

## 📁 프로젝트 파일 구조
- Microservices Architecture
- Layered Architecture + DDD
```
예시 : auth-service
├─auth-service
│  ├─build
│  │  ├─classes
│  │  │  └─java
│  │  │      └─main
│  │  │          └─com
│  │  │              └─logistics
│  │  │                  └─platform
│  │  │                      └─auth_service
│  │  │                          │  AuthServiceApplication.class
│  │  │                          │
│  │  │                          ├─application
│  │  │                          │  ├─dto
│  │  │                          │  │      CustomUserDetails.class
│  │  │                          │  │      SignupResDto.class
│  │  │                          │  │      UserResDto.class
│  │  │                          │  │
│  │  │                          │  └─service
│  │  │                          │          AuthService.class
│  │  │                          │          CustomUserDetailsService.class
│  │  │                          │          UserService.class
│  │  │                          │
│  │  │                          ├─common
│  │  │                          │  │  ResponseDto.class
│  │  │                          │  │
│  │  │                          │  ├─entity
│  │  │                          │  │      BaseEntity.class
│  │  │                          │  │
│  │  │                          │  └─exception
│  │  │                          │          CustomApiException.class
│  │  │                          │          CustomExceptionHandler.class
│  │  │                          │
│  │  │                          ├─config
│  │  │                          │  │  AuditorAwareImpl.class
│  │  │                          │  │  SecurityConfig.class
│  │  │                          │  │
│  │  │                          │  └─jwt
│  │  │                          │          JwtAuthenticationFilter.class
│  │  │                          │          JwtUtil.class
│  │  │                          │          LoginFilter.class
│  │  │                          │
│  │  │                          ├─domain
│  │  │                          │  ├─model
│  │  │                          │  │      Role.class
│  │  │                          │  │      User$UserBuilder.class
│  │  │                          │  │      User.class
│  │  │                          │  │
│  │  │                          │  └─repository
│  │  │                          │          UserRepository.class
│  │  │                          │
│  │  │                          └─presentation
│  │  │                              ├─controller
│  │  │                              │      AuthController.class
│  │  │                              │      UserController.class
│  │  │                              │
│  │  │                              └─request
│  │  │                                      DeleteReqDto.class
│  │  │                                      PasswordUpdateReqDto.class
│  │  │                                      SigninReqDto.class
│  │  │                                      SignupReqDto.class
│  │  │
│  │  ├─generated
│  │  │  └─sources
│  │  │      ├─annotationProcessor
│  │  │      │  └─java
│  │  │      │      └─main
│  │  │      └─headers
│  │  │          └─java
│  │  │              └─main
│  │  ├─reports
│  │  │  └─problems
│  │  │          problems-report.html
│  │  │
│  │  ├─resources
│  │  │  └─main
│  │  │          application.yml
│  │  │
│  │  └─tmp
│  │      └─compileJava
│  │          │  previous-compilation-data.bin
│  │          │
│  │          └─compileTransaction
│  │              ├─backup-dir
│  │              └─stash-dir
│  │                      SecurityConfig.class.uniqueId0
│  │
│  ├─gradle
│  │  └─wrapper
│  │          gradle-wrapper.jar
│  │          gradle-wrapper.properties
│  │
│  └─src
│      ├─main
│      │  ├─java
│      │  │  └─com
│      │  │      └─logistics
│      │  │          └─platform
│      │  │              └─auth_service
│      │  │                  │  AuthServiceApplication.java
│      │  │                  │
│      │  │                  ├─application
│      │  │                  │  ├─dto
│      │  │                  │  │      CustomUserDetails.java
│      │  │                  │  │
│      │  │                  │  └─service
│      │  │                  │          AuthService.java
│      │  │                  │          CustomUserDetailsService.java
│      │  │                  │          UserService.java
│      │  │                  │
│      │  │                  ├─domain
│      │  │                  │  ├─model
│      │  │                  │  │      Role.java
│      │  │                  │  │      User.java
│      │  │                  │  │
│      │  │                  │  └─repository
│      │  │                  │          UserRepository.java
│      │  │                  │
│      │  │                  ├─infrastructure
│      │  │                  │  └─config
│      │  │                  │      │  AuditorAwareImpl.java
│      │  │                  │      │  SecurityConfig.java
│      │  │                  │      │  SwaggerConfig.java
│      │  │                  │      │
│      │  │                  │      └─jwt
│      │  │                  │          │  JwtUtil.java
│      │  │                  │          │
│      │  │                  │          └─filter
│      │  │                  │                  JwtAuthorizationFilter.java
│      │  │                  │                  LoginFilter.java
│      │  │                  │
│      │  │                  └─presentation
│      │  │                      ├─controller
│      │  │                      │      AuthController.java
│      │  │                      │      UserController.java
│      │  │                      │
│      │  │                      ├─global
│      │  │                      │  │  ResponseDto.java
│      │  │                      │  │
│      │  │                      │  ├─entity
│      │  │                      │  │      BaseEntity.java
│      │  │                      │  │
│      │  │                      │  ├─exception
│      │  │                      │  │      CustomApiException.java
│      │  │                      │  │
│      │  │                      │  └─hadler
│      │  │                      │          CustomExceptionHandler.java
│      │  │                      │
│      │  │                      ├─request
│      │  │                      │      DeleteReqDto.java
│      │  │                      │      PasswordUpdateReqDto.java
│      │  │                      │      SigninReqDto.java
│      │  │                      │      SignupReqDto.java
│      │  │                      │
│      │  │                      └─response
│      │  │                              SignupResDto.java
│      │  │                              UserResDto.java
│      │  │
│      │  └─resources
│      │          application.yml
│      │
│      └─test
│          └─java
│              └─com
│                  └─logistics
│                      └─platform
│                          └─auth_service
│                                  AuthServiceApplicationTests.java
│

```

## ⚙️ 실행 방법
- Java 17
- Docker에서 openzipkin/zipkin, redis 이미지 실행
- postgreSQL DB 설정 정보 환경변수에 등록
```
spring:
    datasource:
      driver-class-name: org.postgresql.Driver
      url: ${DB_URL} # jdbc:postgresql://localhost:5432/{DB이름}
      username: ${DB_USERNAME} # postgres
      password: ${DB_PASSWORD}
```

- 필요한 API KEY 환경변수에 등록
- hub-service : 구글 API KEY, 카카오 API KEY
```
google:
  maps:
    api:
      key: ${GOOGLE_API_KEY}

kakao:
  api:
    key: ${KAKAO_API_KEY}
```
  
- slack-service : 슬랙 봇 API KEY, 구글 Gemini API KEY
```
slack:
  bot:
    token: ${BOT_TOKEN}

google:
  gemini:
    api:
      key: ${GEMINI_TOKEN}
```

  
