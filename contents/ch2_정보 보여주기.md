# 2. 정보 보여주기

## MVC

- 스프링 웹 애플리케이션에서는 데이터를 가져오고 처리하는 것이 컨트롤러의 일이다.
- 브라우저에 보여주는 데이터를 HTML로 나타내는 것은 뷰가 하는 일이다.

### 타코 디자인 페이지를 위한 컴포넌트

- 타코 식자재의 속성을 정의하는 도메인 클래스
- 식자재 정보를 가져와서 뷰에 전달하는 스프링 MVC 컨트롤러 클래스
- 식자재의 내역을 사용자의 브라우저에 보여주는 뷰 템플릿

### Model - 애플리케이션의 도메인

- 해당 애플리케이션의 이해에 필요한 개념을 다루는 영역
- 타코 클라우드에서의 도메인 > 고객이 선택한 타코 디자인, 디자인을 구성하는 식자재, 고객, 고객의 타코 주문

```java

@Data
//  Lombok 라이브러리에 포함된 어노테이션으로, final 속성들을 초기화하는 생성자와 각 속성의 게터와 세터를 런타임 시 생성
@RequiredArgsConstructor
public class Ingredient {

    private final String id;
    private final String name;
    private final Type type;

    public enum Type {
        WRAP, PROTEIN, VEGGIES, CHEESE, SAUCE
    }
}
```

```java

@Data
public class Taco {
    private String name;
    private List<String> ingredients;
}
```

### Controller

- HTTP 요청을 처리하고, 브라우저에 보여줄 HTML을 뷰에 요청하거나 REST 형태의 응답 body에 직접 데이터를 추가

#### 타코 클라우드 애플리케이션에 필요한 컨트롤러

- 요청 경로가 /design인 HTTP GET 요청 처리
- 식자재의 내역 생성
- 식자재 데이터의 HTML 작성을 뷰 템플릿에 요청하고, 작성된 HTML을 웹 브라우저에 전송

```java

@Controller
@RequestMapping("/design")
public class DesignTacoController {
    ...

    @GetMapping
    public String showDesignForm(Model model) {
        ...
        return "design";
    }
    ...
}
```

#### @Controller

- DesignTacoController 클래스가 컨트롤러로 식별되게 함
- 컴포넌트 검색을 해야 한다는 것을 나타 냄
- 스프링이 DesignTacoController 클래스를 찾은 후 스프링 애플리케이션 컨텍스트의 빈으로 이 클래스의 인스턴스를 자동 생성

#### @RequestMapping

- 클래스 수준으로 적용될 때는 해당 컨트롤러가 처리하는 요청의 종류를 나타냄
- DesignTaacoController에서 /design으로 시작하는 경로의 요청을 처리함을 나타냄

#### @GetMapping

- RequestMapping(method=RequestMethod.GET) 과 동일

| @RequestMapping | 다목적 요청 처리         | 
|-----------------|-------------------|
| @GetMapping     | HTTP GET 요청 처리    |
| @PostMapping    | HTTP POST 요청 처리   |
| @PutMapping     | HTTP PUT 요청 처리    |
| @DeleteMapping  | HTTP DELETE 요청 처리 |
| @PatchMapping   | HTTP PATCH 요청 처리  |

- Model은 컨트롤러와 데이터를 보여주는 뷰 사이에서 데이터를 운반하는 객체
- showDesignForm() 메서드는 마지막에 "design"을 반환하고 이 것은 모델 데이터를 브라우저에 나타내는 데 사용될 뷰의 논리적인 이름

### View

```html
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="EUC-KR">
    <title>Taco Cloud</title>
    <link rel="stylesheet" th:href="@{/styles.css}"/>
</head>

<body>
<h1>Design your taco!</h1>
<img th:src="@{/images/img.png}" alt="">

<form method="POST" th:object="${taco}">
		<span class="validationError"
              th:if="${#fields.hasErrors('ingredients')}"
              th:errors="*{ingredients}">Ingredient Error</span>
    ...
```

- 폼이 제출되면 브라우저가 폼의 모든 데이터를 모아 폼에 나타난 GET 요청과 같은 경로(/design)로 서버에 HTTP POST 요청을 전송
- 이 요청을 처리하는 컨트롤러의 메서드도 필요

```java
public class DesignTacoController {
    ...

    @PostMapping
    public String processDesign(
            @Valid Taco design,
            Errors errors,
            @ModelAttribute Order order
    ) {
        if (errors.hasErrors()) {
            return "design";
        }

        Taco saved = tacoRepository.save(design);
        order.addDesign(saved);

        return "redirect:/orders/current";
    }
}
```

- @PostMapping는 processDesign() 메서드가 /design 경로의 POST 요청을 처리함을 나타내고, 타코르르 디자인하는 사용자가 제출한 것을 이 메서드에서 처리
- return 되는 String은 사용자에게 보여주는 뷰를 나타냄
- 리디렉션(변경된 경로로 재접속) 뷰를 나타내는 "redirect:" 로 시작하는 "redirect:/orders/current" 값은 "/orders/current" 상대 경로로 재접속 되어야 함을 나타냄

```java

@Slf4j
@Controller
@RequestMapping("/orders")
public class OrderController {

    private OrderRepository orderRepository;

    public OrderController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @GetMapping("/current")
    public String orderForm(Model model) {
        model.addAttribute("order", new Order());
        return "orderForm";
    }

    @PostMapping
    public String processOrder(@Valid Order order, Errors errors, SessionStatus sessionStatus) {
        if (errors.hasErrors()) {
            return "orderForm";
        }

        orderRepository.save(order);
        sessionStatus.setComplete();

        return "redirect:/";
    }
}
```

- 클래스 수준의 @RequestMapping 어노테이션은 /orders 로 시작되는 경로의 요청을 이 컨트롤러의 요청 처리 메서드가 처리한다는 것을 알려주는 것
- 메서드 수준의 @GetMapping을 함께 지정하여 /orders/current 경로의 HTTP GET 요청을 orderForm() 메서드가 처리한다는 것을 알려줌

```html
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="EUC-KR">
    <title>Taco Cloud</title>
    <link rel="stylesheet" th:href="@{/styles.css}"/>
</head>
<body>

<form method="POST" th:action="@{/orders}" th:object="${order}">
    <h1>Order your taco creations!</h1>

    <img th:src="@{/images/img.png}" alt="">
    ...
```

- <form> 태그에 폼 액션이 지정되지 않은 경우 폼에 나타났던 것과 같은 URL로 <form> 태그의 method 로 호출이 되고, 액션이 지정된 경우 해당 경로로 method가 호출됨

### 유효성 검사

- 유효성 검사할 클래스에 검사 규칙 선언 > Model 클래스에
- 유효성 검사를 해야 하는 컨트롤러 메서드에 검사를 수행한다는 것을 지정 > DesignTacoController의 processDesign() 메서드와 OrderController의 processOrder()
  메서드에
- 검사 에러를 보여주도록 폼 뷰 수정

### 뷰 컨트롤러

- HomeController와 같이 모델 데이터나 사용자 입력을 처리하지 않는 간단한 컨트롤러의 경우 아래와 같이 WebMvcConfigurer 를 구현하여 컨트롤러 정의 가능

```java

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("home");
    }
}
```

- WebMvcConfigurer 인터페이스는 스프링 MVC를 구성하는 메서드를 정의하고 있고, 인터페이스임에도 불구하고 정의된 모든 메서드의 기본적인 구현 제공

## 서비스 디스커버링 (내용 보완 예정)

- 서비스를 호출할때 서비스의 위치 (즉 IP주소와 포트)를 알아낼 수 있는 기능

### Client side discovering

- service client가 service registry에서 서비스의 위치를 찾아 호출하는 방식
- service registry:

### Server side discovering

- 호출이 되는 서비스 앞에 일종의 proxy 서버 (로드밸런서)를 넣는 방식
- 서비스 클라이언트는 이 로드밸런서를 호출하면 로드밸런서가 Service registry로 부터 등록된 서비스의 위치를 리턴하고, 이를 기반으로 라우팅을 하는 방식

### 참고

- [조대협의 블로그](https://bcho.tistory.com/1252)
- [Browser에 www.google.com을 검색하면 일어나는 일](https://medium.com/@maneesha.wijesinghe1/what-happens-when-you-type-an-url-in-the-browser-and-press-enter-bb0aa2449c1a)
