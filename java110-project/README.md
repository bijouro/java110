# java110-project

## v1.6
 - Command 디자인 패턴 적용
 - 컨트롤러 구조 변경


## v1.5

- 리플렉션 API 활용
- 에노테이션 활용
- 미니 IoC 컨테이너 개선
- 작업
    - 자동으로 생성되어야 할 객체에 붙일 애노테이션 정의
    - 컨트롤러 객체에 애노테이션 적용
    - 애노테이션이 붙은 클래스만 객체를 생성


## v1.4

- 리플렉션 API 활용
- File 클래스로 디렉토리 및 파일 다루기
- 미니 IoC 컨테이너 작성

## v1.3

- 인터페이스 활용
- App 클래스와 Controller 들 사이의 호출규칙(사용규칙)을 인터페이스로 정의한다.

## v1.2

- 자바 컬렉션 API 사용
- 직접 제작한 List, ArrayList, LinkedList 대신에 자바에서 제공하는 Collection API를 사용한다.

## v1.1

- 인터페이스 적용
- 의존 객체 주입(Dependency Injection; DI) 적용
- 데이터 목록을 다룰 때 호출하는 메서드의 규칙을 정의한다.
    - 다양한 방법으로 구현한 객체를 사용할 수 있다.
    - 즉 규칙을 따르기만 한다면 어떤 객체라도 대체할 수 있다.
- 인터페이스의 default 메서드의 활용

## v1.0

- LinkedList 구동 원리 및 적용

## v0.9

- 제네릭 활용법
- ArrayList에 제네릭 적용하기

## v0.8

- 목록을 다루는 클래스들을 리팩토링하기
- 다형적 변수의 활용법
- 클래스 멤버(변수,메서드,블록)와 인스턴스 멤버의 차이점
- 생성자 사용
- StudentList, ManagerList, TeacherList 클래스를 한 클래스로 합친다.
- 다형적 변수를 사용하여 Student,Manager,Teacher 객체를 모두 저장할 수 있게 한다.

## tag: v0.7

수업 관리 시스템 만들기

- 목록 다루기
- 배열에 항목을 추가하고 삭제하는 기능을 추가한다.
- 배열의 값 목록을 다루는 기능을 별도의 클래스로 분리한다.

## tag: v0.6

- 패키지 활용 및 클래스의 접근 범위

## tag: v0.5

- 조건문, 반복문, 메서드 활용
- 강사, 학생, 매니저를 구분하여 입력받고 출력한다.

## v0.4

- 여러 속성의 값을 다루기 쉽도록 클래스를 이용하여 사용자 정의 데이터 타입을 만든다. 또한 그 데이터를 다룰 연산자를 정의한다.

## v0.3

- 관련된 명령들을 재사용하기 쉽도록 메서드 블록으로 묶는다.

## v0.2

- 배열을 이용하여 여러 회원의 정보를 저장하기

## v0.1

- 반복문을 이용하여 여러 회원의 정보를 입력 받아 출력하기
- 작업
    - App.java 변경

## Initial Commit 

- 자바 프로젝트 디렉토리 구성하기

```
1) 자바 프로젝트 디렉토리 구성하기
$ gradle init --type java-application

2) Gradle 설정 파일에 'eclipse' 플러그인 추가
build.gradle 파일에 id 'eclipse' 추가

3) 이클립스 설정 파일 생성
$ gradle eclipse

4) 이클립스에서 프로젝트를 임포트
```