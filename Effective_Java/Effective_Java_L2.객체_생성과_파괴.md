# 2장. 객체 생성과 파괴

[아이템 1. 생성자 대신 정적 팩터리 메서드를 고려하라.](#아이템-1.-생성자-대신-정적-팩터리-메서드를-고려하라.)

[아이템 2. 생성자에 매개변수가 많다면 빌더를 고려하라.](#아이템-2.-생성자에-매개변수가-많다면-빌더를-고려하라.)

[아이템 3. private 생성자나 열거 타입으로 싱글턴임을 보증하라.](#아이템-3.-private-생성자나-열거-타입으로-싱글턴임을-보증하라.)

[아이템 4. 인스턴스화를 막으려거든 private 생성자를 사용하라.](#아이템-4.-인스턴스화를-막으려거든-private-생성자를-사용하라.)

[아이템 5. 자원을 직접 명시하지 말고 의존 객체 주입을 사용하라.](#아이템-5.-자원을-직접-명시하지-말고-의존-객체-주입을-사용하라.)

[아이템 6. 불필요한 객체 생성을 피하라.](#-아이템-6.-불필요한-객체-생성을-피하라.)

[아이템 7. 다 쓴 객체 참조를 해제하라.](#-아이템-7.-다-쓴-객체-참조를-해제하라.)

[아이템 8. finalizer와 cleaner 사용을 피하라.](#-아이템-8.-finalizer와-cleaner-사용을-피하라.)

[아이템 9. try-finally 보다는 try-with-resources를 사용하라.](#-아이템-9.-try-finally-보다는-try-with-resources를-사용하라.)



## 아이템 1. 생성자 대신 정적 팩터리 메서드를 고려하라.

__정적 팩토리 메서드 : 클래스의 인스턴스를 반환하는 단순한 static 메서드.__

### 정적 팩토리 메서드가 생성자보다 좋은 장점
 * 이름을 가질 수 있다. 그래서 반환될 객체의 특성을 쉽게 묘사할 수 있다.
 * 호출될 때마다 인스턴스를 새로 생성하지는 않아도 된다. 
   * 따라서 같은 객체가 자주 요청되는 상황에서의 퍼포먼스를 증가시킬 수 있다.
   * 반복되는 요청에 같은 객체를 반환하는 식으로 정적 팩토리 방식의 클래스는 언제 어느 인스턴스를 살아 있게 할지를 통제할 수 있다 - 인스턴스 통제(instance-controlled)
 * 리턴 타입의 하위 타입 객체를 반환할 수 있다. - 유연성
 * 입력 매개변수에 따라 매번 다른 클래스의 객체를 반환할 수 있다.
 * 정적 팩토리 메서드를 작성하는 시점에는 반환할 객체의 클래스가 존재하지 않아도 된다. - 유연성

### 정적 팩토리 메서드의 단점
 * 상속을 하려면 public이나 protected 생성자가 필요하니 정적 팩토리 메서드만 제공하면 하위 클래스를 만들 수 없다.
 * 정적 팩토리 메서드는 프로그래머가 찾기 어렵다.
 
```java
    public static Member createWithPrivacy(String memberId, String memberPw, String memberName, String memberPhoneNumber, String memberEmail, String memberAddress) {
        return new Member(memberId, memberPw, memberName, memberPhoneNumber, memberEmail, memberAddress);
    }

    public static Member createWithoutPrivacy(String memberId, String memberPw, String memberName) {
        return new Member(memberId, memberPw, memberName, null, null, null);
    }

    public static Member createInitialMember() {
        return new Member(null, null, null, null, null, null);
    }
```

### 결론 : 정적 팩토리 메서드와 public 생성자는 각자의 장단점을 이해하고 사용해야 한다. 그럼에도 불구하고 정적 팩토리를 사용하는 게 유리한 경우가 더 많다.

   * 팩토리 - 디자인 패턴의 내용. 그냥 이름을 팩토리라 붙인 것. "인스턴스를 생성하는 역할을 담당하는 대상", BoardFactory 에서 처럼 여러 메소드로 인스턴스를 만들어 반환해줌.
   * 인스턴스 : new 해서 생성한 객체(클래스), 클래스로부터 생성되었을 때 인스턴스라 부름.

   * interface vs abstract : abstract 는 변수를 가질 수 있음. 인터페이스는 메서드만 가질 수 있음.

   * implements vs extends 는 별로 구분이 중요하지 않음.

---

## 아이템 2. 생성자에 매개변수가 많다면 빌더를 고려하라.

클래스 내에 여러 멤버변수(필수 + 선택)가 있는 경우 __점층적 생성자 패턴(파라미터가 다른 여러 생성자를 오버로딩)__ 은 클라이언트 코드를 작성하거나 읽기 어렵다.

__자바빈즈 패턴(JavaBeans pattern) -매개변수가 없는 생성자로 객체를 만든 후, setter 들을 호출해 매개변수의 값을 설정하는 방식-__ 은 객체 하나를 만들려면 메서드를 여러 개 호출해야 하고, 객체가 완전히 생성되기 전까지는 일관성(consistency)이 무너진 상태에 놓이게 된다는 단점이 있다.

__빌더 패턴(Builder pattern)__ 을 사용하게 되면 위의 문제를 모두 해결할 수 있음. 필수 매개변수만으로 생성자를 호출해 빌더 객체를 얻고, 빌더 객체가 제공하는 일종의 세터 메서드들로 원하는 선택 매개변수들을 설정한다.

빌더 패턴의 단점 : 객체 생성에 앞서 빌더를 먼저 만들어 줘야한다. 성능 이슈가 있을 수 있음.

#### NutritionFacts.class - Builder pattern
```java
public class NutritionFacts {
    
    private final int servingSize;
    private final int servings;
    private final int calories;
    private final int fat;
    private final int sodium;
    private final int carbohydrate;


    public NutritionFacts(Builder builder) {
        this.servingSize = builder.servingSize;
        this.servings = builder.servings;
        this.calories = builder.calories;
        this.fat = builder.fat;
        this.sodium = builder.sodium;
        this.carbohydrate = builder.carbohydrate;
    }

    public static class Builder {
        private final int servingSize;
        private final int servings;

        private int calories = 0;
        private int fat = 0;
        private int sodium = 0;
        private int carbohydrate = 0;

        public Builder(int servingSize, int servings) {
            this.servingSize = servingSize;
            this.servings = servings;
        }

        public Builder calories(int val) {
            this.calories = val;
            return this;
        }

        public Builder fat(int val) {
            this.fat = val;
            return this;
        }

        public Builder sodium(int val) {
            this.sodium = val;
            return this;
        }

        public Builder carbohydrate(int val) {
            this.carbohydrate = val;
            return this;
        }

        public NutritionFacts build() {
            return new NutritionFacts(this);
        }
    }

    public int getServingSize() {
        return servingSize;
    }

    public int getServings() {
        return servings;
    }

    public int getCalories() {
        return calories;
    }

    public int getFat() {
        return fat;
    }

    public int getSodium() {
        return sodium;
    }

    public int getCarbohydrate() {
        return carbohydrate;
    }
    
}
```

#### NutritionFactsTest.class - Client side
```java
NutritionFacts cocaCola = new NutritionFacts.Builder(240, 8)
        .calories(100)
        .sodium(35)
        .carbohydrate(27)
        .build();
```

### 결론 : 생성자나 정적 팩토리가 처리해야 할 매개변수가 많다면 빌더 패턴을 선택하는 게 더 낫다.

---

## 아이템 3. private 생성자나 enum 타입으로 싱글턴임을 보증하라.

__싱글톤(singleton) : 인스턴스를 오직 하나만 생성할 수 있는 클래스.__

클래스를 싱글톤으로 만들면 싱글톤 인스턴스를 mock 으로 대체할 수 없어 이를 사용하는 클라이언트를 테스트하기가 어려워 진다.

### 싱글톤을 만드는 방식

#### public static final 필드 방식의 싱글톤
```java
public class Elvis {
    public static final Elvis INSTANCE = new Elvis();
    private Elvis() { ... }
    
    public void leaveTheBuilding() { ... }
}
```
 * 장점
    * 해당 클래스가 싱글톤임이 API에 명백히 드러남.
    * 간결함.
 * 예외
    * 권한이 있는 클라이언트는 리플렉션 API인 ```AccessibleObject.setAccessible``` 을 사용해 private 생성자를 호출할 수 있다. 따라서 인스턴스가 '오직 하나'임이 보장되지 않을 수 있다. - 공격 위험, 두 번째 객체가 생성되려 할 때 예외를 던져 방어 가능.

<br>

#### 정적 팩토리 방식의 싱글톤
```java
public class Elvis {
    private static final Elvis INSTANCE = new Elvis();
    private Elvis() { ... }
    public static Elvis getInstance() { return INSTANCE; };
    
    public void leaveTheBuilding() { ... }
}
```
 * 장점
   * (필요에 의해)API를 바꾸지 않고도 싱글톤이 아니게 변경할 수 있다.
   * 정적 팩토리를 제네릭 싱글톤 팩토리로 만들 수 있다.
   * 정적 팩토리의 메서드 참조를 공급자(supplier)로 사용할 수 있다.
   
```java 
Elvis::getInstance -> Supplier<Elvis>
```

위의 정적 팩토리 방식의 장점들이 필요하지 않다면 public 필드 방식이 더 추천됨.

<br>

위의 두 방식 모두 아래의 단점을 가진다.
 * 단점
    * __직렬화(Serializable)에 있어 모든 인스턴스 필드를 transient라 선언하고 readResolve 메서드를 제공해야 한다.__ 이렇게 하지 않으면 직렬화된 인스턴스를 역직렬화할 때마다 새로운 인스턴스가 만들어짐.

따라서 이를 보완하고자 싱글톤임을 보장해주는 readResolve 메서드를 아래처럼 구현한다.

#### 싱글톤임을 보장해주는 readResolve 메서드
```java 
// 싱글톤임을 보장해주는 readResolve 메서드
private Object readResolve() {
    // '진짜' Elvis를 반환하고, 가짜 'Elvis'는 가비지 컬렉터에 맡긴다.
    return INSTANCE;
}
```

<br>

#### enum 타입 방식의 싱글톤 - 추천
```java
public enum Elvis {
    INSTANCE;
    
    public void leaveTheBuilding() { ... }
}
```
 * 장점
   * public 필드 방식보다 더 간결.
   * 추가적인 작업 없이 직렬화 가능
   * 직렬화 상황이나 리플렉션 공격에도 인스턴스가 '오직 하나' 임을 보장.

 * 단점
   * 만들려는 싱글톤이 enum 외의 클래스를 상속해야 할 경우 사용 불가.

### 결론 : 싱글톤을 만들 때 '원소가 하나인 enum 타입' 방식의 싱글톤이 가장 바람직한 방법이다.

---

## 아이템 4. 인스턴스화를 막으려거든 private 생성자를 사용하라.

### '정적 메서드와 정적 필드'만을 담고 있는 클래스
 * java.lang.Math 나 java.util.Arrays 처럼 기본 타입 값이나 배열 관련 메서드들을 모아놓는 클래스
 * java.util.Collections 처럼 특정 인터페이스를 구현하는 객체를 생성해주는 정적 메서드(팩토리)를 모아놓을때
 * final 클래스와 관련한 메서드들을 모아놓을 때

어떤 경우에 있어 __인스턴스로 만들어 쓰려고 정적 멤버만 담아놓은게 아닐 수 있다.__ 하지만 생성자를 명시하지 않으면 컴파일러가 자동으로 public 기본 생성자를 만들어 인스턴스화할 수 있게 된다.

추상 클래스로 만드는 것으로는 인스턴스화를 막을 수 없다.

따라서 __private 생성자를 추가하여 클래스의 인스턴스화를 막는다.__

#### 인스턴스를 만들 수 없는 유틸리티 클래스
```java
public class UtilityClass {
    // 기본 생성자가 만들어지는 것을 막는다(인스턴스화 방지용).
    private UtilityClass() {
        throw new AssertionError();
    }
    ...
}
```

명시적 생성자가 private 이라서 클래스 바깥에서는 접근할 수 없고, 클래스 안에서 실수로라도 생성자를 호출하면 AssertionError() 를 던진다.

이 방식은 상속을 불가능하게 하는 효과도 있다.

---

## 아이템 5. 자원을 직접 명시하지 말고 의존 객체 주입을 사용하라.

많은 클래스가 하나 이상의 자원에 의존한다. 아래의 예시는 '맞춤법 검사기(SpellChecker)'가 사전(dictionary)에 의존하는 예 이다.

#### 정적 유틸리티를 잘못 사용한 예 - 유연하지 않고 테스트하기 어렵다.
```java
public class SpellChecker {
    private static final Lexicon dictionary = ...;
    
    private SpellChecker() {}   // 객체 생성 방지

    public static boolean isValid(String word) { ... }
    public static List<String> suggestions(String typo) { ... }
}
```

#### 싱글턴을 잘못 사용한 예 - 유연하지 않고 테스트하기 어렵다.
```java
public class SpellChecker {
    private final Lexicon dictionary = ...;

    private SpellChecker(...) {}
    public static SpellChecker INSTANCE = new SpellChecker(...);

    public Boolean isValid(String word) { ... }
    public List<String> suggestions(String typo) { ... }
}
```

SpellChecker 가 여러 사전(언어별, 특수 어휘용, 테스트용 등..) 을 사용하는 경우 정적 유틸리티 방법이나 싱글턴 방법 모두 예외의 여지가 있다.

즉, __사용하는 자원에 따라 동작이 달라지는 클래스에는 정적 유틸리티 클래스나 싱글턴 방식이 적합하지 않다.__

대신 __클래스(SpellChecker)가 여러 자원 인스턴스를 지원하고, 클라이언트가 원하는 자원(dictionary)을 사용할 수 있는 '인스턴스를 생성할 때 생성자에 필요한 자원을 넘겨주는 방식' 을 사용하면 된다(의존 객체 주입의 한 형태).__

#### 의존 객체 주입 방식 - 유연성과 테스트 용이성을 높여준다.
```java
public class SpellChecker {
    private final Lexicon dictionary;
    
    public SpellChecker(Lexicon dictionary) {
        this.dictionary = Objects.requireNonNull(dictionary);
    }
    
    public boolean isValid(String word) { ... }
    public List<String> suggestions(String typo) { ... }
}
```
cf) ```Objects.requireNonNull(dictionary)```
```java
 * Checks that the specified object reference is not {@code null}. This
 * method is designed primarily for doing parameter validation in methods
 * and constructors, as demonstrated below:
 * <blockquote><pre>
 * public Foo(Bar bar) {
 *     this.bar = Objects.requireNonNull(bar);
 * }
 * </pre></blockquote>
```

이렇게 의존 객체 주입 방식을 사용하면 __자원이 몇 개든 의존 관계가 어떻든 상관없이 잘 동작하고, 불변을 보장하여 같은 자원에 대한 공유가 가능하다.__

이 방식의 변형으로 __'생성자에 자원 팩토리를 넘겨주는 방식'__ 이 있다.

#### 팩토리 : 호출할 때마다 특정 타입의 인스턴스를 반복해서 만들어주는 객체를 말한다. 즉, '팩토리 메서드 패턴'을 구현한 것이다.

자바 8에서 추가된 ```Supplier<T>``` 인터페이스가 팩토리를 표현한 완벽한 예인데, 이를 사용하면 클라이언트는 자신이 명시한 타입의 하위 타입이라면 무엇이든 생성할 수 있는 팩토리를 넘길 수 있다.

#### 클라이언트가 제공한 팩토리가 생성한 타일(Tile)들로 구성된 Mosaic 을 만드는 메서드
```java
Mosaic create(Supplier<? extends Tile> tileFactory) { ... }
```

### 결론 : 클래스가 내부적으로 하나 이상의 자원에 의존하고, 그 자원이 클래스 동작에 영향을 준다면 '싱글톤'과 '정적 유틸리티 클래스'를 사용하는 대신, 필요한 자원(또는 그 자원을 만들어주는 팩토리)을 생성자(또는 정적 팩토리나 빌더)에 넘겨주는 '의존 객체 주입' 방식을 사용한다.

---

## 아이템 6. 불필요한 객체 생성을 피하라.

__똑같은 기능의 객체를 매번 생성하기보다 객체 하나를 재사용하는 편이 나을 때가 많다.__ 특히 불변 객체는 언제든 재사용할 수 있다.

#### 절대 하지 말아야 할 예시
```java
String s = new String("bikini");
```
위 예시는 실행될 때마다 String 인스턴스를 새로 만든다. 생성자에 넘겨진 "bikini" 자체가 String 이 만들어져 넘어가는데, 만들어 내려는 타입 String 과 동일하다.

#### 개선된 예시
```java
String s = "bikini";
```
위 예시는 새로운 인스턴스를 매번 만드는 대신 하나의 String 인스턴스를 사용한다.

또한 __생성자는 호출할 때마다 새로운 객체를 만들지만, 팩토리 메서드는 그렇지 않다(재사용 한다).__

생성 비용이 아주 비싼 객체는 캐싱하여 재사용하길 권장한다.

#### 성능을 많이 소비하는 코드
```java
static boolean isRomanNumeral(String s) {
    return s.matches("^(?=.)M*(C[MD]|D?C{0,3}" 
    + "(X[CL]|L?X{0,3})(I[XV]|V?I{0,3})$");
}
```

위 방식에서 String.matches 메서드는 정규 표현식으로 문자열 형태를 확인하는 가장 쉬운 방법이지만, 비용이 비싸다.
내부적으로 정규표현식용 Pattern 인스턴스를 만드는데, 이는 __한번 쓰고 버려져서 곧바로 GC 대상이 된다.__ Pattern은 입력받은 정규표현식에 해당하는 유한 상태 머신(finite state machine)을 만들기 때문에 인스턴스 생성 비용이 높다.

__따라서 정규 표현식을 표현하는 Pattern 인스턴스를 클래스 초기화(정적 초기화) 과정에서 직접 생성해 캐싱해두고, 해당 인스턴스를 필요로 하는 메서드가 호출될 때마다 재사용 한다.__

#### 개선된 코드 - 값비싼 객체를 재사용해 성능을 개선
```java
public class RomanNumerals {
    private static final Pattern ROMAN = Pattern.compile(
        "^(?=.)M*(C[MD]|D?C{0,3}" 
        + "(X[CL]|L?X{0,3})(I[XV]|V?I{0,3})$");

    static boolean isRomanNumeral(String s) {
        return ROMAN.matcher(s).matches();
    }
}
```

__객체가 불변이라면 재사용해도 안전함이 명백하다.__

불필요한 객체를 만들어내는 또 다른 예로 __오토박싱(auto boxing)__ 이 있다.

오토박싱은 기본 타입과 그에 대응하는 박싱된 기본 타입의 구분을 흐려주지만, 완전히 없애주는 것은 아니다.

가령 long 이 아닌 Long 으로 변수를 선언하게 되면 불필요한 Long 인스턴스가 약 231개나 만들어지게 된다.

따라서 __박싱된 기본 타입보다는 기본 타입을 사용하고, 의도치 않은 오토박싱이 숨어들지 않도록 주의하자.__

### 결론 : 객체 생성에 있어 성능을 항상 염두하여 '재사용'을 고려하자.

---

## 아이템 7. 다 쓴 객체 참조를 해제하라.

자바의 가비지 컬렉터는 생각보다 똑똑하지 않을 수 있다. __'메모리 누수'__ 를 개발자가 직접 꼼꼼하게 처리하는 것 만큼 완벽하게 관리하지 못한다.

#### 메모리 누수가 일어나는 Stack 클래스
```java
public class Stack {
    private Object[] elements;
    private int size = 0;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    public Stack() {
        elements = new Object[DEFAULT_INITIAL_CAPACITY];
    }
    public void push(Object e) {
        ensureCapacity();
        elements[size++] = e;
    }
    public Object pop() {
        if(size == 0)
            throw new EmptyStackException();
        return elements[--size];
    }

    // 원소를 위한 공간을 적어도 하나 이상 확보한다.
    // 배열 크기를 늘려야 할 때마다 대략 두 배씩 늘린다.
    private void ensureCapacity() {
        if(elements.length == size) 
            elements = Arrays.copyOf(elements, 2 * size + 1);
    }
}
```

위 Stack 클래스에서는 __스택이 커졌다 줄어들 때 스택에서 꺼내진 객체들을 GC가 회수하지 않는다.__ 따라서 그 객체들의 다 쓴 참조(obsolete reference)를 여전히 가지고 있게 된다. 따라서 이는 __'메모리 누수'__ 에 해당한다.

이를 해결하기 위해 __해당 참조를 다 썼을 때 null 처리(참조 해제)하면 된다.

#### 제대로 구현한 pop 메서드
```java
public Object pop() {
    if (size == 0)
        throw new EmptyStackException();
    Object result = element[--size];
    element[size] = null;   // 다 쓴 참조 해제
    return result;
}
```

이렇게 하면 null 처리한 참조를 사용하려 할 경우 NullPointerException 을 던지며 종료되게 된다.

위 코드에서의 스택이 메모리 누수에 취약한 이유는 '스택이 자기 메모리를 직접 관리하기 때문'이다. 객체 자체가 아니라 객체 참조를 담는 elements 배열로 저장소 풀을 만들어 원소들을 관리하기 때문에 __가비지 컬랙터의 메모리 회수 대상이 아니다.__ GC 입장에서는 비활성 영역에서의 참조 객체도 똑같이 유효한 객체이다. 

따라서 비활성 영역이 되는 순간 null 처리해서 해당 객체를 더이상 쓰지 않을 것임을 GC에게 알려야 한다.

__자기 메모리를 직접 관리하는 클래스라면 항시 메모리 누수에 주의해야 한다.__

__캐시 역시 메모리 누수를 일으키는 주범이다.__ 따라서 캐시 외부에서 key를 참조하는 동안만 엔트리가 살아있는 캐시가 필요한 상황이라면 'WeakHashMap' 을 사용해 캐시를 만들자. 이렇게 하면 다 쓴 엔트리는 즉시 자동으로 제거된다.

LinkedHashMap 은 remove EldestEntry 메서드를 써서 캐시에 새 엔트리를 추가할 때 부수 작업으로 쓰지 않는 엔트리를 제거할 수 있다.

또 다른 '메모리 누수'의 주범으로 __리스너(listener) 혹은 콜백(callback) 이 있다.__ 클라이언트가 콜백을 등록만 하고 명확히 해지하지 않는다면, 콜백은 계속 쌓여가게 된다.

이럴 때에는 콜백을 __약한 참조(weak reference)__ 로 저장하면 GC가 즉시 수거해간다. 예를 들어 WeakHashMap에 key로 저장하는 식이다.

### 결론 : '메모리 누수'는 겉으로 잘 드러나지 않아 그 예방법을 익혀두는 것이 매우 중요하다.

 * 38쪽 하단
    : 원래는 하나라도 참조하고 있다면 가비지 컬렉터가 지우지 않는것이 원칙. 참고 http://blog.breakingthat.com/2018/08/26/java-collection-map-weakhashmap/

---

## 아이템 8. finalizer와 cleaner 사용을 피하라.

### 자바의 객체 소멸자 2가지
 * __finalizer__ : 예측할 수 없고, 상황에 따라 위험할 수 있어 사용 권장 x
 * __cleaner__ : finalizer 에 대한 대안으로 자바 9부터 제시되었으나 여전히 예측할 수 없고, 느리고, 불필요하다.

### 그럼 자바는 비메모리 자원을 어떻게 회수할까? 
 * try-with-resources
 * try-finally

이 두 가지를 이용한다.

### finalizer와 cleaner의 단점

 * __finalizer와 cleaner 는 즉시 수행된다는 보장이 없어, 제때 실행되어야 하는 작업은 절대 할 수 없다.__
   * finalizer 스레드는 다른 애플리케이션 스레드보다 우선순위가 낮아 실행될 기회를 제대로 얻지도 못한다. cleaner는 자신을 수행할 스레드를 제어할 수 는 있지만, 여전히 백그라운드에서 수행되며 GC의 통제하에 있으니 즉각 수행되리라는 보장은 없다.

 * 또한 __상태를 영구적으로 수정하는 작업에서는 절대 finalizer나 cleaner에 의존해서는 안 된다.__ 예를 들어, 데이터베이스 같은 공유 자원의 영구 락(lock)의 해제를 finalizer나 cleaner에 맡겨놓으면 절대 안된다.

 * __finalizer와 cleaner는 심각한 성능 문제__ 도 동반하는데, AutoCloseable 객체를 생성하고 GC가 수거하기까지 12ns 가 걸린 작업이 finalizer를 사용하면 550ns, cleaner를 사용하면 500ns 가 소요된다. GC의 효율을 떨어뜨리기 때문.

 * __finalizer를 사용한 클래스는 finalizer 공격에 노출되어 심각한 보안 문제를 일으킬 수도 있다.__

### 그럼 파일이나 쓰레드를 종료해야 할 자원을 담고 있는 객체의 클래스에서 finalizer나 cleaner를 대신해줄 묘안은?
 * AutoCloseable을 구현해주고, 클라이언트에서 인스턴스를 다 쓰고 나면 close 메서드를 호출한다. 이 때, 일반적으로 예외가 발생해도 제대로 종료되도록 __try-with-resources__ 를 사용한다.
 * 이 때, 각 인스턴스는 자신이 닫혔는지를 추적하는 것이 좋다. close 메서드에서 이 객체는 더 이상 유효하지 않음을 필드에 기록하고, 객체가 닫힌 후에 불렸다면 IllegalStateException을 던진다.

### cleaner와 finalizer 는 그럼 도대체 어디에 쓰이지?
 * 자원의 소유자가 close 메서드를 호출하지 않는 것에 대비한 안전망 역할
   * 클라이언트가 하지 않은 자원 회수를 늦게라도 해 주기 위해
   * 하지만 자바 라이브러리의 일부 클래스는 FileInputStream, FileOutputStream, ThreadPoolExecutor 와 같은 안전망 역할의 finalizer 를 제공하므로, 직접 작성할만한 가치가 있는지 숙고해야한다.
 * 네이티브 피어(native peer)와 연결된 객체일 경우
   * 네이티브 피어란 일반 자바 객체가 네이티브 메서드를 통해 기능을 위임한 네이티브 객체
   * 따라서 네이티브 피어는 자바 객체가 아니기 때문에 GC가 그 존재를 알지 못한다. 따라서 회수 불가하다.

### 결론 : cleaner, finalizer는 안전망 역할이나 중요하지 않은 네이티브 자원 회수 용도로만 사용하자.

---

## 아이템 9. try-finally 보다는 try-with-resources를 사용하라.

자바 라이브러리에는 InputStream, OutputStream, java.sql.Connection 등 close 메서드를 호출해 직접 닫아줘야 하는 자원이 많다.

이 경우 try-finally 를 사용하는 경우와 try-with-resources 를 사용하는 경우를 비교해 보자.

#### try-finally 사용 예 : 비추
```java
static String firstLineOfFile(String path) throws IOException {
    BufferedReader br = new BufferedReader(new FileReader(path));
    try {
        return br.readLine();
    } finally {
        br.close();
    }
}
```

리소스를 하나 사용할 경우 큰 차이가 없는데, 두 개 이상부터는 확연한 차이를 보인다. 또한 scope 측면에서도 어려움이 있을 수 있음.


#### try-finally 사용 예 : 비추
```java
static void copy(String src, String dst) throws IOException {
    InputStream in = new FileInputStream(src);

    try {
        OutputStream out = new FileOutputStream(dst);
        try {
            byte[] buf = new byte[BUFFER_SIZE];
            int n;
            while (( n = in.read(buf)) >= 0)
                out.write(buf, 0, n);
        } finally {
            out.close();
        }
    } finally {
        in.close();
    }
}
```

__이렇게 nested 하게 작성하는 경우 가독성도 떨어질 뿐 아니라, 두 번째 예외가 첫 번째 예외를 집어삼켜 버릴 수 있다.__

따라서 __try-with-resources__ 를 사용하자. 단지 AutoCloseable 인터페이스를 구현하면 된다.

#### try-with-resources 사용 예 : 강추
```java
static String firstLineOfFile(String path) throws IOException {
    try (BufferedReader br = new BufferedReader(new FileReader(path))) {
        return br.readLine();
    }
}
```

#### try-with-resources 사용 예 : 강추
```java
static void copy(String src, String dst) throws IOException {
    try(InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst)) {
        byte[] buf = new byte[BUFFER_SIZE];
        int n;
        while((n = in.read(buf)) >= 0)
            out.write(buf, 0, n);
    } 
}

```

try-with-resources 를 사용할 경우 숨겨진 예외들도 스택 추적 내역에 '숨겨뒀다(suppressed)' 는 꼬리표를 달고 출력된다.

또한 try-with-resources 에서도 catch 절을 쓸 수 있어 nested try 문의 발생을 막을 수 있다.


#### try-with-resources 사용 예 : 강추
```java
static String firstLineOfFile(String path) throws IOException {
    try (BufferedReader br = new BufferedReader(new FileReader(path))) {
        return br.readLine();
    } catch (IOException e) {
        return defaultVal;
    }
}
```

### 결론 : 꼭 회수해야 하는 자원을 다룰 땐, try-finally 가 아니라 try-with-resources를 사용하자.

---
