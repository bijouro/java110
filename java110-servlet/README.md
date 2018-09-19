# 자바 서블릿 프로그래밍

## 개발 환경 구축

### Tomcat Server 디렉토리 구조

'''
 bin  - Tomcat 서버 실행/종료와 관련된 파일
 conf - Tomcat 서버 설정 파일
 logs - Tomcat 실행 상황 기록한 파일
 lib  - Tomcat 서버 실행과 관련된 자바 라이브러리 파일
 temp - Tomcat 서버를 실행하는 중에 임시로 저장하는 파일
 webapps - Web Application 배포
 work - JSP를 자바 소스파일로 변환한 파일
 
  
'''


### 이클립스에서 톰캣 서버 연동

 - 이클립스에서 Tomcat Server를 실행하고 종료할수 있다.
 - 웹 프로젝트를 테스트하기 위해 별도의 테스트 환경을 구축한다.
 - 그래서 원본 톰캣 서버의 실행 환경을 손대지 않는다.
 - 원본 톰캣 서버에서 설정 파일을 복사해서 사용한다.
 
### 이클립스에서 톰캣 서버 테스트 환경 구축하기
 -"Servers" 뷰에서 "New > Server" 메뉴를 선택한다.
 - 서버 유형 목록에서 이미 eclipse에 등록 되어 있는 Tomcat 버전을 선택 한다.
 - 적당한 서버 이름을 지정한 후 "finish" 를 클릭한다.
 - eclipse workspace에 "Servers" 라는 프로젝트 폴더가 생성된다.
    - 지정한 서버 이름으로 폴더가 생성된다. 예) servlet test-config
    - 이 폴더에는 tomcat server 폴더에서 복사해온 설정 파일들이 들어 있다.
    - 톰캣 서버의 원본 설정 파일을 손대지 않고 별도로 해서 사용하는 것이다.
    - 개발하는 동안에는 복사해온 설정 파일을 사용하여 톰캣 서버를 실행한다.
   
   
   
 - Eclipse 에서 tomcat server 테스트 환경을 실행한다.
    - 원본 tomcat server의 폴더와는 별개로 테스트를 위한 별도의 폴더를 구축한다.
    - 각 Web application 별로 다양한 조건의 테스트를 수행하기 위함.
    - tomcat server 테스트 환경을 실행 하면 Eclipse workspace 폴더에 해당 디렉토리가 구축된다.
 
 '''
 
 구축 된  톰캣 테스트 환경 디렉토리 구조
 ( 톰캣 서버 테스트 환경을 생성 한 후 서버를 한번 실행해야만 다음 폴더가 생성된다)
  [이클립스 작업폴더] 예 C:\Users\bit\workspace2
  .metadata\
    .plugins\
      org.eclipse.wst.server.core\
        tmp0\   - 톰캣 서버 테스트 환경 ( 생성된 순서대로 0,1,2... 순으로 생성 된다)
          conf\  - 이클립스 "Servers\테스트 환경 폴더"에 별도로 편집된 설정 파일을 복사해 온다
          logs\  - 테스트 환경에서 톰캣 서버를 실행하는 중에 출력된 log 파일
          temp\  -  테스트 환경에서 톰캣 서버를 실행하는 중에 임시 작업 파일
          webapps\ - 테스트 환경에서 tomcat server를 실행할때는 이 폴더를 사용하지 않는다.
          work\ - 테스트 환경에서 톰캣 서버를 실행할때 JSP 변환 파일
          wtpwebapps\ - 테스트 환경에서 톰캣 서버를 실행할때 Web application을 배치 (war)
        tmp1\
        tmp2\
'''

## tomcat server 테스트 환경에 application 배포

 - 톰캣 서버 테스트 환경(Servers 뷰에 등록한 테스트 환경) 메뉴에서 "Add and Remove..." 선택
 - 왼쪽에 나열된 프로젝트 중에서 테스트 할 프로젝트를 선택한 후 "add" 버튼을 클릭한다.
 - 톰캣 서버 테스트 환경에 "Publish" 메뉴를 선택한다. 또는 "Start"메뉴를 선택한다.
    - 선택한 프로젝트에서 배포할 파일들이 테스트 환경의 배포 폴더에 자동으로 복사된다.
   
 - 주의!!!!
    - 서버가 실행중일때 배포를 하면 자동으로 배포 파일이 복사된다.
    - 그러면 톰캣 서버는 배포한 웹 애플리케이션을 자동으로 로딩한다.
    - 하지만 가끔 자동 복사가 안될 때가 있다.
    - 그럴 경우 톰캣 서버를 멈추고, 배포 프로젝트를 제거한 후 위의 과정을 다서 수행한다.
 
 - 톰캣 서버 테스트 환경에는 톰캣 서버 원본에 있던 web application이 없기 떄문에 기본으로 웹 페이지가 없다
    
      
 ### 애플리케이션 실행
  톰캣 서버에 배치된 web application을 실행할 때 다음의 규칙에 따라 요청한다.
 '''
 
  http://localhost:port/projectname/path
  ex) http://localhost:8888/java110-servlet/index.html
  
 '''
 
 ## 애플리케이션 배치명을 변경하기
 
  - 애플리케이션의 배치 이름을 지정하지 않으면 기본이 프로젝트 명이다.
  - settings.gradle 파일에서 애플리케이션 이름을 변경 한다.
    - 'gradle eclipse'를 다시 실행해야 한ㄷ
    
  
  '''
    rootProject.name = 'web01'
  '''
  
 배치 폴더(/tmp0/wtpwebapps)에 프로젝트를 배치할 때 프로젝트 이름으로 배치되더라도
 웹브라우저에서 요청할 때는 프로젝트명이 아니라 다음과 같이 배치 이름으로 실행해야 한다.
 
 '''
  http://localhost:8888/web01/index.html

 '''
 
 ### 웹 패플리케이션을 root로 만들기
 
 웹 애플리케이션을 실행할 때 웹 애플리케이션 이름을 지정하기 싫다면
 다음과 같이 설정한다.
 
 - 톰캣 서버 테스트 환경 목록에서 해당 서버를 "open" 한다.
 - 화면에 "Modules"  탭을 선택한다.
 - 웹애플리케이션을 선택한 후 "edit" 버튼을 클릭한ㄷ.
 - "Path" 값을 "/" 로 설정후 저장한다.
    - 실제는 servlet.xml 파일의 배치 정보가 변경된다.
    - 톰캣 서버 재 시작
  
  
'''
웹브라우저에서 톰캣 서버에 요청할 때 웹 애플리케이션 이름을 지정하지 않아도 된다.
http://localhost:8888/index.html
'''

## 웹 애플리케이션을 만드는 방법

### 웹 애플리케이션의 구성요소

 - 컴포넌트
    - 서블릿,필터,리스너
    - 컴포넌트는 배치 될떄 '/WEB-INF/classes' 배치 폴더에 배치된다
   
 - 웹 애플리케이션 배치 정보 파일
    - Deployemenet Descriptor 파일이라 부른다.
    - 줄여서 DD 파일이라 부른다
    - /WEB-INF/web.xml 파일이다.
    - 이 파일에 웹 컴포넌트에 대한 설정 정보를 작성한다.
    
 - 의존 라이브러리
    - 웹애플리케이션이 사용하는 외부 라이브러리이다.
    - 배치 폴더에서 '/WEB-INF/lib' 폴더에 배치된다.
    
 - JSP 파일
    - 배치 폴더'(/)'에 그대로 복사 된다.
    
 - 정적 Web resources file
    - .html, .css, .js, .jpeg, .png, .gif 등의 파일
    - 배치 폴더'(/)'에 그대로 복사 된다.
    
### 웹 애플리케이션 서버 ( Web Application Server)

#### 개요

 - JavaEE Spec.에 따라 만든 서버이다.
 - JavaEE 스펙에 따라 작성된 애플리케이션을 실행할 수 있다.
 - 보통 줄여서 'WAS' 라고 표현한다. 'AS'라고 할 때도 있다.
 
 
#### 구성 요소

 - HTTP Server
    - HTTP 프로토콜을 기반으로 웹브라우저의 요청을 받고 응답하는 서버
    - 보통 실무에서 운영할 때는 WAS에 내장된 HTTP Server를 사용하지 않고
     별도의 HTTP Server(ex. Apache, nginX 등)를 사용한다.
     
 - Servlet Container
    - JavaEE 스펙 중에서 웹 관련 스펙을 실행하는 서버
    - 서블릿, 필터, 리스너. JSP를 관리하고 실행하는 서버
   
 - EJB Container
    - JavaEE 스펙 중에서 EJB관련 스펙을 구현한 서버이다.
    - 현재는 EJB를 잘 사용하지 않는다.
    - POJO 기반의 Spring 프레임워크를 주로 사용하기 때문이다.
   
 - 대표 제품
   - JavaEE의 모든 스펙을 준수하는 서버 
       - Oracle의 WebLogic(유료)
       - TMaxSoft의 JEUS (유료)
       - RedHat의 JBOSS (유/무료)
       - IBM의 WebSphere (유료)
       - Oracle의 GlassFish (무료 , 개발용으로 사용됨)
       - ASF의 Geronemo (무료)
   - JavaEE의 스펙 중에서 웹 관련 스펙만 준수하는 서버
       - 보통 미니 HTTP Server와 Servlet Container를 갖추고 있다.
       - ASF의 Tomcat(무료)
       - Caucho Techonology의 Resin(유/무료)
       - Eclipse의 Jetty(무료)
        
### Java EE Spec.

- Java Enterpise Edition의 약자이다.
- 자바로 기업용 애플리케이션을 제작하는데 필요한 기술을 정의한 명세이다
- 기술 요소
    - 웹 애플리케이션 기술 : Servlet, JSP, JSF, Expression Language, WebSocket, JSTL등
    - 분산 컴포넌트 기술 : EJB, Message Service, Transaction, JavaMail 등 
    - 웹 서비스 기술 : JAX-RS, JAX-RPC, JAXR 등
    - 관리 및 보안 기술 : 
 - Java EE Implements(구현체)
    - Java EE 기술 명세에 따라서 동작하도록 만든 서버를 말한다.
    - Oracle에서 Java EE 명세를 제대로 구현했는지 검사하고 인증서를 발급한다.
    - 보통 WAS 라고 부르며 WebLogic, JBoss, JEUS 제품이 이에 해당한다.
    - Tomcat, Resin, Jetty는 Java EE 명세중 웹애플리케이션 기술만 구현한 서버
    
 - Java EE 버전
    - Java EE 는 여러 하위 기술로 구성된다.
    - Java EE 하위 기술 각각에 대해서도 버전이 부여되고 관리된다.
    - Java EE 버전 별로 각 하위 기술들의 버전이 지정된다.
    - 버전 예:
      - Java EE 8 : Servlet 4.0, JSP 2.3, JSTL 1.2, EJB 3.2 등
      - Java EE 7 : Servlet 3.1, JSP 2.3, JSTL 1.2, EJB 3.2 등
      - Java EE 6 : Servlet 3.0, JSP 2.2, JSTL 1.2, EJB 3.1 등
      - Java EE 5 : Servlet 2.5, JSP 2.1, JSTL 1.2, EJB 3.0 등
 
 - 웹 애플리케이션을 제작할 때 주의할 사항
    - WAS 제품의 버전을 확인 한다.
    - 그 제품이 Java EE 어떤 버전의 구현체인지 확인한다.
    - 그 버전에 맞춰서 Servlet/JSP 문법을 사용한다.
    
## Servlet/JSP 만들기

### Servlet 만들기

 Servlet은 클라이언트의 요청이 들어왔을 때 호출되는 자바 객체이다.
 
 - javax.servlet.Servlet 인터페이스에 따라 작성해야 한다.
 - @WebSErvlet 또는 web.xml 파일에 Servlet정보를 등록 한다.
 - 
 
     
        
   
   
  
  
      