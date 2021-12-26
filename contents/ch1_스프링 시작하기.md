# 1. 스프링 시작하기

애플리케이션은 애플리케이션 전체 기능 중 일부를 담당하는 많은 컴포넌트로 구성되며, 각 컴포넌트는 다른 애플리케이션 구성 요소와 협력해서 작업을 처리한다. 애플리케이션이 실행될 때는 각 컴포넌트가 어떻게든 생성되어야 하고 상호 간에 알 수 있어야 한다.

## 스프링 애플리케이션 컨텍스트
- "스프링 애플리케이션 컨텍스트" 라는 컨테이너를 제공하는데, 이것은 애플리케이션 컴포넌트들을 생성하고 관리한다.
- 애플리케이션 컴포넌트 또는 빈들은 애플리케이션 컨텍스트 내부에서서로 연결되어 완전한 애플리케이션을 만든다.

## 빈의 상호연결은 의존성 주입이라고 알려진 패턴을 기반으로 수행된다.
- 애플리케이션 컴포넌트에서 의존하는 다른 빈의 생성과 관리를 자체적으로 하는 대신 별도의 개체(컨테이너)가 해주며, 이 개채에서는 모든 컴포넌트를 생성, 관리하고 해당 컴포넌트를 필요로 하는 빈에 주입한다.
  일반적으로 생성자 인자 혹은 속성의 접근자 메서드를 통해 처리된다.

## @Configuration annotaion  
각 빈을 스프링 애플리케이션 컨텍스트에 제공하는 구성 클래스라는 것을 스프링에게 알려준다. 구성 클래스의 메서드에는 @Bean 이 지정되어 있고, 각 메서드에서 반환되는 객체가 애플리케이션 컨텍스트의 빈으로 추가되어야 한다는 것을 나타낸다.

## 자동-구성(autoconfiguration)  
자동 연결(autowiring)과 컴포넌트 검색(component scanning)이라는 스프링 기법을 기반으로 한다. 컴포넌트 검색을 사용하여 스프링은 자동으로 애플리케이션의 classpath에 지정된 컴포넌트를 찾은 후 스프링 애플리케이션 컨텍스트의 빈으로 생성할 수 있다. 또한, 스프링은 자동 연결을 사용하여 의존 관계가 있는 컴포넌트를 자동으로 다른 빈에 주입한다.

## 스프링 부트 스타터 의존성
- 자체적으로 라이브러리 코드를 갖지 않고 다른 라이브러리의 것을 사용
### 장점
- 필요로 하는 모든 라이브러리의 의존성을 선언하지 않아도 되서 빌드 파일이 더 작고 관리가 쉬워진다.
- 라이브러리 이름이 아닌 기능을 관점으로 의존성을 생각할 수 있다. (웹 애플리케이션을 작성할 수 있게 해주는 라이브러리를 일일이 지정하지 않고 웹 스타터 의존성만 추가하면 된다.)
- 라이브러리들의 버전을 걱정하지 않아도 된다. (스프링 부트에 포함되는 라이브러리의 버전은 호환이 보장되기 때문에 스프링 부트의 버전만 신경쓰면 된다.)

## @SpringBootApplication
 ```groovy
@SpringBootApplication
class TacoCloudApplication {

    static void main(String[] args) {
        SpringApplication.run(TacoCloudApplication, args)
    }
}
```
- 아래 세 가지 annotation 을 결합한 것
	- @SpringBootConfiguration: TacoCloudApplication 을 구성 클래스로 지정, @Configuration annotaion 의 특화된 형태
	- @EnableAutoConfiguration: 필요로 하는 컴포넌트들을 자동으로 구성하도록 스프링 부트에 알려주는 역할
	- @CompoenetScan: @Component, @Controller, @Service 등의 annotation과 함께 클래스가 선언된 경우 자동으로 그런 클래스를 찾아 스프링 어플리케이션 컨텍스트에 컴포넌트로 등록하는 기능(컴포넌트 검색) 활성화

- main() : JAR 파일이 실행될 때 호출되어 실행되는 메서드

## @Contorller
```java
@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "home";
    }
}

```
- 스프링 MVC라고 하는 웹 프레임워크의 중심에 있는 컨트롤러는 웹 요청과 응답을 처리하는 컴포넌트다.
- @Controller 는 컴포넌트 검색 시 HomeContorller 클래스가 컴포넌트로 식별되게 하는 것이 주 목적이다.
- 스프링의 컴포넌트 검색에서는 자동으로 HomeController 클래스를 찾은 후 스프링 애플리케이션 컨텍스트의 빈(bean)으로 HomeController의 인스턴스 생성
- home(): 루트 경로인 /의 HTTP GET 요청이 수신되면 이 메서드가 해당 요청을 처리한다.

## @WebMvcTest
```java
package com.example.tacocloud.tacos;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(HomeController.class)
class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testHomePage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("home")).andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("Welcome to...")));

    }
}
```
- @SpringBootTest 대신 스프링 부트에서 제공하는 특별한 테스트 annotation인 @WebMvcTest 를 사용한다. 스프링 MVC 애플리케이션의 형태로 테스트가 실행되도록 하여 HomeController가 스프링 MVC에 등록되어 스프링 MVC에 웹 요청을 보낼 수 있다.

## DevTools
- 제공하는 기능
	- 코드가 변경될 때 자동으로 애플리케이션을 다시 시작시킨다.
	- 브라우저로 전송되는 리소스가 변경될 때 자동으로 브라우저를 새로고침한다.
	- 템플릿 캐시를 자동으로 비활성화 한다.
		- 개발 시점에 템플릿 캐싱이 적용되면 템플릿 변경을 새로고침 후 캐싱된 템플릿으로 확인하기 때문에 적용된 내용을 바로 확인할 수 없게 해서 다시 시작해야만 변경된 결과를 볼 수 있다.
	- 만일 H2 데이터베이스가 사용중이라면 자동으로 H2 콘솔을 활성화한다.
- 각종 IDE의 플러그인이 아니어서 어떤 IDE 에서든 잘 동작한다.
- DevTools를 사용 중일 때 애플리케이션은 JVM에서 두 개의 클래스 로더에 의해 로드 된다. 하나는 자바코드, 속성파일, 프로젝트의 src/main/ 경로에 있는 모든 것과 함께 로드된다. 나머지 클래스 로더는 자주 변경되지 않는 의존성 라이브러리와 함께 로드된다.
- 변경이 감지되는 경우 DevTools는 프로젝트 코드를 포함하는 클래스 로더만 다시 로드하고 스프링 애플리케이션 컨텍스트를 다시 시작시킨다.
- 위 방식의 단점은 애플리케이션이 자동으로 다시 시작될 때 의존성 변경이 적용될 수 없다는 것이다. 빌드 명세에 의존성을 추가, 변경, 삭제할 때는 애플리케이션을 새로 시작해야만 그러한 변경의 효과를 볼 수 있다.

## 스프링 살펴보기
### 핵심 스프링 프레임워크
- 스프링 MVC
- 데이터 퍼시스턴스 지원
- 리액티브 프로그래밍 (WebFlux)

### 스프링 부트
- 자동-구성
- Actuator 는 애플리케이션의 내부 작동을 런타임 시에 살펴볼 수 있는 기능을 제공하며, metric, 스레드 덤프 정보, 애플리케이션의 상태, 애플리케이션에서 사용할 수 있는 환경 속성 포함
- 환경 속성의 명세
- 핵심 프리엠워크에 추가되는 테스트 지원

### 스프링 데이터
- 간단한 자바 인터페이스로 애플리케이션의 데이터 리퍼지터리를 정의
- 서로 다른 종류의 데이터베이스와 함께 사용 가능

### 스프링 시큐리티
- authentication(인증), authorization(허가), API 보안을 포함하는 폭 넓은 범위의 애플리케이션 보안 요구를 다룸

### 스프링 통합과 배치
- 다른 애플리케이션 또는 같은 애플리케이션의 서로 다른 컴포넌트를 통합할 필요가 생길 때를 위한 몇 가지 애플리케이션 통합 패턴이 있음
- 스프링 통합과 배치는 스프링 기반 애플리케이션의패턴 구현을 제공

### 스프링 클라우드
- 스프링 마이크로서비스 코딩 공작소 책 참고

## 요약
- 웹 애플리케이션 생성, 데이터베이스 사용, 애플리케이션 보안, 마이크로서비스 등에서 개발자의 노력을 덜어주는 것이 스프링으 목표
- 스프링 부트는 손쉬운 의존ㄴ성 관리, 자동-구성, 런타임 시의 애플리케이션 내부 작동 파악을 스프링에서 할 수 있게 한다.
- 스프링 애플리케이션은 스프링 Initializer를 사용해서 초기 설정
- 빈이라고 하는 컴포넌트는 스프링 애플리케이션 컨텍스트에서 자바나 XML 로 선언할 수 있으며, 컴포넌트 탐색으로 찾거나 스프링 부트 자동-구성에서 자동으로 구성
