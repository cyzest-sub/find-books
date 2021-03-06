# find-books

[![Build Status](https://travis-ci.org/cyzest/find-books.svg?branch=master)](https://travis-ci.org/cyzest/find-books)
[![Code Coverage](https://codecov.io/gh/cyzest/find-books/branch/master/graph/badge.svg)](https://codecov.io/gh/cyzest/find-books)
[![Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=com.cyzest%3Afind-books&metric=alert_status)](https://sonarcloud.io/dashboard?id=com.cyzest%3Afind-books)

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

### Vault로 관리하는 설정정보
```txt
findbooks.searcher.kakaoApiKey          // Kakao API Key
findbooks.searcher.naverClientId        // Naver API ID
findbooks.searcher.naverClientSecret    // Naver API Secret
spring.datasource.username              // DB User
spring.datasource.password              // DB User Password
```
* Vault 정보를 자신의 환경에 맞게 변경해야 합니다. (bootstrap.properties 에서 수정가능)
* 테스트 및 실행 시 Java System Property로 Vault Token을 추가해야 합니다.

### 설치

```console
$ git clone https://github.com/cyzest-sub/find-books.git
$ cd find-books
```

### 테스트

```console
$ mvn clean test -DVAULT_TOKEN={VAULT_TOKEN}
```

### 실행 (Local)

```console
$ mvn clean package -Dmaven.test.skip=true
$ java -DVAULT_TOKEN={VAULT_TOKEN} -jar ./target/find-books-1.0.0.jar
```
* http://localhost:8080 으로 접속하여 확인
* 포트는 8080 을 사용합니다. (application.properties 에서 수정가능)

### 실행 (Dev)

```console
$ mvn clean package -Pdev -Dmaven.test.skip=true
$ java -DVAULT_TOKEN={VAULT_TOKEN} -Dsentry.dsn={SENTRY_DSN} -jar ./target/find-books-1.0.0.jar
```
* Dev서버는 에러트래킹을 위해 Sentry를 연동
* Java System Property로 Sentry DSN을 추가해야 합니다.

### Docker 빌드 및 실행 (Dev)

```console
$ mvn clean package -Pdev -Dmaven.test.skip=true
$ docker build -t find-books:1.0.0 ./
$ docker run -d --name find-books -p 8080:8080 -e VAULT_TOKEN="{VAULT_TOKEN}" -e SENTRY_DSN="{SENTRY_DSN}" --link valut find-books:1.0.0
```
