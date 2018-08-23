# find-books

[![Build Status](https://travis-ci.org/cyzest/find-books.svg?branch=master)](https://travis-ci.org/cyzest/find-books)
[![codecov](https://codecov.io/gh/cyzest/find-books/branch/master/graph/badge.svg)](https://codecov.io/gh/cyzest/find-books)

### OpenAPI를 활용한 책 검색 서비스

1. Java 8 버전을 사용
1. Spring Boot 2.0 사용 (Spring Framework 5.0 기반)
1. Spring Data JPA 사용 (Hibernate 5.2 구현체 사용)
1. Spring Boot 에서 제공하는 임베디드 Undertow 사용
1. H2 DB 사용

### Back-end 추가 라이브러리
1. OkHttp3 - Rest API 커넥션 용도
1. Lombok - Boilerplate 코드 자동생성 용도
1. ModelMapper - 오브젝트 변환 용도
1. thymeleaf3 - 화면 뷰 템플릿 용도
1. Spring Cloud Vault - 시크릿 설정 정보 분리 용도

### Front-end 추가 라이브러리
1. Bootstrap3 - 화면 구성을 위해 이용

### 설치

```
$ git clone https://github.com/cyzest-sub/find-books.git
$ cd find-books
```

### 실행 (로컬)

```
$ mvn clean compile
$ mvn spring-boot:run
```
* http://localhost:8080 으로 접속하여 확인
* 포트는 8080 을 사용합니다. (application.properties 에서 수정가능)
* Vault 정보를 자신의 환경에 맞게 변경해야 합니다. (bootstrap.properties 에서 수정가능)

### 테스트 (Junit)

```
$ mvn clean compile test

// Vault 정보 System Property 주입 시
$ mvn clean compile test -DVAULT_HOST={HOST} -DVAULT_TOKEN={TOKEN}
```

### 배포 및 실행 (Jar)

```
$ mvn package
$ cd target
$ java -jar find-books-1.0.0.jar

// Vault 정보 System Property 주입 시
$ mvn package -DVAULT_HOST={HOST} -DVAULT_TOKEN={TOKEN}
$ java -DVAULT_HOST={HOST} -DVAULT_TOKEN={TOKEN} -jar find-books-1.0.0.jar
```