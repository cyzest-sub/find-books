# find-books

### OpenAPI를 활용한 책 검색 서비스

1. Java 8 버전을 사용
1. Spring Boot 1.5 사용 (Spring Framework 4.3 기반)
1. Spring Data JPA 사용
1. Spring Boot 에서 제공하는 임베디드 톰캣 사용
1. H2 DB 사용

### Back-end 추가 라이브러리
1. OkHttp3 - Rest API 커넥션 용도
1. Lombok - Getter Setter 자동생성 용도
1. ModelMapper - 오브젝트 변환 용도
1. thymeleaf3 - 화면 뷰 템플릿 용도

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

* 포트는 8080 을 사용합니다. (application.properties 에서 수정 가능)

### 테스트 (Junit)

```
$ mvn clean compile test
```

### 배포 및 실행 (Jar)

```
$ mvn clean compile
$ mvn pakage
$ cd target
```