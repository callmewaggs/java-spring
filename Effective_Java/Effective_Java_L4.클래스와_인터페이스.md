# 4장. 클래스와 인터페이스

[아이템 15. 클래스와 멤버의 접근 권한을 최소화하라.](#아이템-15.-클래스와-멤버의-접근-권한을-최소화하라.)

[아이템 16. public 클래스에서는 public 필드가 아닌 접근자 메서드를 사용하라.](#아이템-16.-public-클래스에서는-public-필드가-아닌-접근자-메서드를-사용하라.)

[아이템 17. 변경 가능성을 최소화하라.](#아이템-17.-변경-가능성을-최소화하라.)

[아이템 18. 상속보다는 컴포지션을 사용하라.](#아이템-18.-상속보다는-컴포지션을-사용하라.)

[아이템 19. 상속을 고려해 설계하고 문서화하라. 그러지 않았다면 상속을 금지하라.](#아이템-19.-상속을-고려해-설계하고-문서화하라.-그러지-않았다면-상속을-금지하라.)

[아이템 20. 추상 클래스보다는 인터페이스를 우선하라.](#아이템-20.-추상-클래스보다는-인터페이스를-우선하라.)

[아이템 21. 인터페이스는 구현하는 쪽을 생각해 설계하라.](#아이템-21.-인터페이스는-구현하는-쪽을-생각해-설계하라.)

[아이템 22. 인터페이스는 타입을 정의하는 용도로만 사용하라.](#아이템-22.-인터페이스는-타입을-정의하는-용도로만-사용하라.)

[아이템 23. 태그 달린 클래스보다는 클래스 계층구조를 활용하라.](#아이템-23.-태그-달린-클래스보다는-클래스-계층구조를-활용하라.)

[아이템 24. 멤버 클래스는 되도록 static으로 만들라.](#아이템-24.-멤버-클래스는-되도록-static으로-만들라.)

[아이템 25. 톱레벨 클래스는 한 파일에 하나만 담으라.](#아이템-25.-톱레벨-클래스는-한-파일에-하나만-담으라.)

---

## 아이템 15. 클래스와 멤버의 접근 권한을 최소화하라.

어설프게 설계된 컴포넌트와 잘 설계된 컴포넌트의 가장 큰 차이는 바로 클래스 내부 데이터와 내부 구현 정보를 외부 컴포넌트로부터 얼마나 잘 숨겼느냐의 차이다. 잘 설계된 컴포넌트는 모든 내부 구현을 완벽히 숨겨, 구현과 API를 깔끔히 분리한다.

정보 은닉의 장점은 다음과 같다.

 * __시스템 개발 속도를 높인다.__ 여러 컴포넌트를 병렬로 개발할 수 있기 때문이다.
 * __시스템 관리 비용을 낮춘다.__ 각 컴포넌트를 더 빨리 파악하여 디버깅할 수 있고, 다른 컴포넌트로 교체하는 부담도 적기 때문이다.
 * 정보 은닉 자체가 성능을 높여주지는 않지만, __성능 최적화에 도움을 준다.__ 완성된 시스템을 프로파일링해 최적화할 컴포넌트를 정한 다음, 다른 컴포넌트에 영향을 주지 않고 해당 컴포넌트만 최적화할 수 있기 때문이다.
 * __소프트웨어 재사용성을 높인다.__ 외부에 거의 의존하지 않고 독자적으로 동작할 수 있는 컴포넌트라면 그 컴포넌트와 함께 개발되지 않은 낯선 환경에서도 유용하게 쓰일 가능성이 크기 때문이다.
 * __큰 시스템을 제작하는 난이도를 낮춰준다.__ 시스템 전체가 아직 완성되지 않은 상태에서도 개별 컴포넌트의 동작을 검증할 수 있기 때문이다.

자바가 제공하는 정보 은닉을 위한 장치로 __클래스, 인터페이스, 멤버의 접근성(접근 허용 범위)__ 가 있다. 접근성은 접근 제한자를 통해 결정되는데, 이 __접근 제한자를 제대로 활용하는 것이 정보 은닉의 핵심이다.__

 * private : 멤버를 선언한 톱레벨 클래스에서만 접근할 수 있다.
 * package-private : 멤버가 소속된 패키지 안의 모든 클래스에서 접근할 수 있다. 접근 제한자를 명시하지 않았을 때 적용되는 디폴트 셋팅이다. 단, 인터페이스의 멤버는 public 이 디폴트.
 * protected : package-private 의 접근 범위를 포함하며, 이 멤버를 선언한 클래스의 하위 클래스에서도 접근할 수 있다.
 * public : 모든 곳에서 접근할 수 있다.

기본 원칙은 __모든 클래스와 멤버의 접근성을 가능한 한 좁혀야 한다__ 는 것이다. 

톱레벨(가장 바깥) 클래스나 인터페이스를 public으로 선언하면 공개 API가 되며, package-private 으로 선언하면 해당 패키지 안에서만 이용할 수 있다. 패키지를 외부에서 쓸 이유가 없다면 package-private으로 선언하자. 이렇게 하면 클라이언트에 아무런 피해 없이 다음 릴리스에서 수정, 교체, 제거할 수 있다.

한 클래스에서만 사용하는 package-private 톱레벨 클래스나 인터페이스의 경우, 이를 사용하는 클래스 안에 private static으로 중첩시켜보자. private static으로 중첩 시키면 바깥 클래스 하나에서만 접근할 수 있다.

public 일 필요가 없는 클래스의 접근 수준을 package-private 톱레벨 클래스로 좁히자. public 클래스는 그 패키지의 API인 반면, package-private 톱레벨 클래스는 내부 구현에 속하기 때문이다.

__public 클래스의 인스턴스 필드는 되도록 public이 아니어야 한다. public 필드와 관련된 모든 것은 불변식을 보장할 수 없고, thread-safe 하지 않다.__

한 가지 예외로 public static final 필드로 공개하는 상수의 경우, 관례상 대문자 알파벳과 각 단어 사이 언더바를 사용하여 네이밍한다. 이 필드는 반드시 기본 타입 값이나 불변 객체를 참조해야 한다.

__길이가 0이 아닌 배열은 모두 변경 가능하다. 따라서 클래스에서 publid static final 배열 필드를 두거나 이 필드를 반환하는 접근자 메서드를 제공해서는 안된다.__ 이에 대한 해결책으로 두 가지가 있다.

  * 앞 코드의 public 배열을 private 으로 만들고 public 불변 리스트를 추가한다.
  ```java
  private static final Thing[] PRIVATE_VALUE = { ... };
  public static final List<Thing> VALUES = 
  Collections.unmodifiableList(Arrays.asList(PRIVATE_VALUES));
  ```

  * 해당 배열을 private 으로 만들고 그 복사본을 반환하는 public 메서드를 제공한다(방어적 복사).
  ```java
  private static final Thing[] PRIVATE_VALUE = { ... };
  public static final Thing[] values() {
      return PRIVATE_VALUES.clone();
  }
  ```

자바 9부터 '모듈 시스템'이라는 개념이 도입되면서 두 가지 암묵적 접근 수준이 추가되었다. __패키지가 클래스들의 묶음이듯, 모듈은 패키지들의 묶음이다.__ 하지만 이 모듈 개념이 널리 받아들여질지는 미지수다. 따라서 꼭 필요할 경우가 아니라면 당분간 사용하지 않는 게 권장된다.

### 결론 : 프로그램 요소의 접근성은 가능한 한 최소한으로 하라. 꼭 필요한 것만 골라 최소한의 public API를 설계하자. public 클래스는 상수용 public static final 필드 외에는 어떠한 public 필드도 가져서는 안 된다. 이 필드의 경우 반드시 불변 객체만을 참조해야 한다.

---

## 아이템 16. public 클래스에서는 public 필드가 아닌 접근자 메서드를 사용하라.

전형적인 캡슐화가 적용된 클래스는 다음과 같다.

```java
class Point {
    private double x;
    private double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() { return x; }
    public double getY() { return y; }

    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }
}
```

public 클래스에선 위와 같이 함으로써 __패키지 바깥에서 접근할 수 있는 클래스라면 접근자를 제공하기 때문에 내부 표현 방식을 언제든 바꿀 수 있는 유연성을 얻을 수 있다.__

하지만 __package-private 클래스 혹은 private 중첩 클래스라면 데이터 필드를 노출한다 해도 문제가 없다.__ 잘 사용한다면 클래스 선언 면에서나 이를 사용하는 클라이언트 코드 면에서나 접근자 방식보다 깔끔하다.

public 클래스의 필드가 불변이라면 직접 노출할 때의 단점이 조금 줄어들지만, 여전히 결코 좋은 생각이 아니다. API를 변경하지 않고는 표현 방식을 바꿀 수 없고, 필드를 읽을 때 부수 작업을 수행할 수 없다는 단점은 여전하기 때문이다.

### 결론 : public 클래스는 절대 가변 필드를 직접 노출해서는 안 된다. 불변 필드라면 노출해도 덜 위험하지만 완전히 안심할 수는 없다.

---

## 아이템 17. 변경 가능성을 최소화하라.

불변 클래스란 간단히 말해 그 인스턴스의 내부 값을 수정할 수 없는 클래스다. 불변 인스턴스에 간직된 정보는 고정되어 객체가 파괴되는 순간까지 절대 달라지지 않는다.

__불변 클래스는 가변 클래스보다 설계하고 구현하고 사용하기 쉬우며, 오류가 생길 여지도 적고 훨씬 안전하다.__

클래스를 불변으로 만들려면 다음 다섯 가지 규칙을 따르면 된다.

 * 객체의 상태를 변경하는 메서드(변경자)를 제공하지 않는다.
 * 모든 필드를 final로 선언한다.
 * 모든 필드를 private으로 선언한다.
 * 자신 외에는 내부 가변 컴포넌트에 접근할 수 없도록 한다.

#### 불변 복소수 클래스
```java
public final class Complex {
    private final double re;
    private final double im;

    public Complex(double re, double im) {
        this.re = re;
        this.im = im;
    }

    public double realPart() { return re; }
    public double imaginaryPart() { return im; }

    public Complex plus(Complex c) {
        return new Complex(this.re + c.re, this.im + c.im);
    }
    public Complex minus(Complex c) {
        return new Complex(this.re - c.re, this.im - c.im);
    }

    ...

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof complex))
            return false;
        Complex c = (Complex) o;

        return Double.compare(c.re, re) == 0
            && Double.compare(c.im, im) == 0;
    }
    @Override
    public int hashCode() {
        return 31 * Double.hashCode(re) + Double.hashCode(im);
    }

    @Override
    public String toString() {
        return "(" + re + " + " + im + "i)";
    }
}
```

이 사칙연산 메서드들은 인스턴스 자신을 수정하지 않고 새로운 Complex 인스턴스를 만들어 반환하는데, 이처럼 피연산자에 함수를 적용해 그 결과를 반환하지만, 피연산자 자체는 그대로인 프로그래밍 패턴을 __함수형 프로그래밍__ 이라고 한다.

함수형 프로그래밍을 적용하면 코드에서 불변이 되는 영역의 비율이 높아지는 장점을 누릴 수 있다.

### 불변 객체의 장점

 * __불변 객체__ 는 단순하여 __생성된 시점의 상태가 파괴될 때까지 그대로 상태를 유지한다.__

 * __불변 객체는 근본적으로 thread-safe 하여 따로 동기화할 필요가 없다(이는 클래스를 thread-safe 하게 만드는 가장 쉬운 방법).__ 따라서 안심하고 공유할 수 있다. 그러므로 불변 클래스라면 한번 만든 인스턴스를 최대한 재활용하기를 권장한다.

    #### 불변 객체를 재활용 하는 방법 - 상수(public static final)로 제공
    ```java
    public static final Complex ZERO = new Complex(0, 0);
    public static final Complex ONE = new Complex(1, 0);
    public static final Complex I = new Complex(0, 1);
    ```

 * 또한, 불변 클래스는 자주 사용되는 인스턴스를 캐싱하여 같은 인스턴스를 중복 생성하지 않게 해주는 __팩터리__ 를 제공할 수 있다.

 * 불변 객체는 자유롭게 공유할 수 있음은 물론, __불변 객체끼리는 내부 데이터를 공유할 수 있다.__

 * __객체를 만들 때 다른 불변 객체들을 구성요소로 사용하면 이점이 많다.__ 불변식을 유지하기 수월하기 때문에 Map의 key 와 Set의 원소로 쓰기에 적합하다.

 * __불변 객체는 그 자체로 실패 원자성(메서드에서 예외가 발생한 후에도 그 객체는 여전히 호출 전과 동일한 유효한 상태여야 한다)을 제공한다.__ 상태가 절대 변하지 않으니 잠깐이라도 불일치 상태에 빠질 가능성이 없다.

### 불변 객체의 단점
 * 값이 다르면 반드시 독립된 객체로 만들어야 한다.

__클래스가 불변임을 보장하려면 자신을 상속하지 못하게 해야 한다.__ 따라서 final 클래스로 선언하는 것 보다 이를 유연하게 구현하는 방법으로 __모든 생성자를 private 혹은 package-private 으로 만들고 public static 팩터리를 제공하는 방법이 있다.__

#### 생성자 대신 정적 팩터리를 사용한 불변 클래스
```java
public class Complex {
    private final double re;
    private final double im;

    private Complex(double re, double im) {
        this.re = re;
        this.im = im;
    }
    public static Complex valueOf(double re, double im) {
        return new Complex(re, im);
    }

    ...
}
```

```BigInteger``` 와 ```BigDecimal``` 은 하위 호환성 이슈가 있어 이를 인수로 받는다면 주의해야 한다. __이를 가변이라 가정하고 방어적으로 복사해 사용하는것이 권장된다.__

```java
public static BigInteger safeInstance(BigInteger val) {
    return val.getClass() == BigInteger.class ? 
                val : new BigInteger(val.toByteArray());
}
```

### 결론
 * getter 가 있다고 해서 무조건 setter 를 만들지는 말자. __클래스는 꼭 필요한 경우가 아니라면 불변이어야 한다.__
 * 단순한 값 객체는 항상 불변으로 만들자.
 * 하지만 모든 클래스를 불변으로 만들 수는 없다. __불변으로 만들 수 없는 클래스라도 변경할 수 있는 부분을 최소한으로 줄이자.__
 * __다른 합당한 이유가 없다면 모든 필드는 private final 이어야 한다.__
 * __생성자는 불변식 설정이 모두 완료된, 초기화가 완벽히 끝난 상태의 객체를 생성해야 한다.__ 생성자와 static 팩터리 외에는 그 어떤 초기화 메서드도 제공해서는 안된다.

---

## 아이템 18. 상속보다는 컴포지션을 사용하라.

상속은 코드를 재사용하는 강력한 수단이지만, 항상 최선은 아니다. 일반적인 구현체 클래스를 패키지 경계를 넘어, 즉 다른 패키지의 구체 클래스를 상속하는 일은 위험하다.

__메서드 호출과 달리 상속은 캡슐화를 깨뜨린다.__ 상위 클래스가 어떻게 구현되느냐에 따라 하위 클래스의 동작에 이상이 생길 수 있다. 상위 클래스는 릴리스마다 내부 구현이 달라질 수 있으며, 그 여파로 하위 클래스가 오작동할 수 있다는 말이다.

#### 잘못된 예 - 상속을 잘못 사용했다.
```java
public class InstrumentedHashSet<E> extends HashSet<E> {
    // 추가된 원소의 수
    private int addCount = 0;

    @Override
    public boolean add(E e) {
        addCount++;
        return super.add(e);
    }

    ...

    @Override
    public boolean addAll(Collection<? extends E> c) {
        addCount += c.size();
        return super.addAll(c);
    }

    ...
}
```

이 클래스에서 만약 addAll() 메서드를 사용했다 가정하자.

```java
InstrumentedHashSet<String> s = new InstrumentedHashSet<>();
s.addAll(Arrays.asList("틱", "탁탁", "펑"));
```

이 경우 addCount 의 값이 3일 것을 기대하지만, 실제로는 6이 된다. __HashSet의 addAll 메서드가 add메서드를 사용해 구현되었기 때문이다.__ 따라서 __addCount에 값이 중복해서 더해져, 최종 값이 6으로 늘어난 것이다.__

이처럼 자신의 다른 부분을 사용하는 '자기사용(self-use)' 여부는 해당 클래스의 내부 구현 방식에 해당하며, 이는 언제까지 유지될지 알 수 없다.

만약 상위 클래스의 메서드인 addAll 메서드를 다시 구현한다 하더라도 이 방식은 어렵고 성능도 안좋아질 것이며 오류를 낼 가능성도 있다. 또한 하위 클래스에서 접근할 수 없는 private 필드를 써야하는 상황이라면 이 방식으로는 구현 자체가 불가능하다.

이렇게 __하위 클래스가 깨지기 쉬운 이유는 모두 메서드 재정의가 원인이다.__ 

이를 개선하는 방법으로 __기존 클래스를 확장하는 대신, 새로운 클래스를 만들고 private 필드로 기존 클래스의 인스턴스를 참조하게 하자.__ 기존 클래스가 새로운 클래스의 구성요소로 쓰인다는 뜻에서 이러한 설계를 __컴포지션(Composition; 구성) 이라 한다.__

__⭐️️️컴포지션 : extends 또는 implements 할 대상을 멤버 변수로 가지고 있고, 보여주고 싶은 메서드만을 재정의 하여 사용하는 개념. 상속을 하게 되면 부모가 가지고 있던 모든 메서드가 공개되기 때문에.__

만들어 지는 새로운 클래스의 인스턴스 메서드들은(private 필드를 참조하는) 기존 클래스의 대응하는 메서드를 호출해 그 결과를 반환한다. 이 방식을 __전달(forwarding)__ 이라 하며, 이렇게 __전달하는 기능을 하는 메서드들을 전달 메서드(forwarding method)라 부른다.__

#### 래퍼(wrapper) 클래스 - 상속 대신 컴포지션을 사용했다. 따라서 ForwardingSet 을 extends 한다.
```java
public class InstrumentedSet<E> extends ForwardingSet<E> {
    private int addCount = 0;

    @Override
    public boolean add(E e) {
        addCount++;
        return super.add(e);
    }

    ...

    @Override
    public boolean addAll(Collection<? extends E> c) {
        addCount += c.size();
        return super.addAll(c);
    }

    ...
}
```

#### 재사용할 수 있는 전달(forwarding) 클래스 - private 멤버의 메서드를 전달할 뿐이다.
```java
public class ForwardingSet<E> implements Set<E> {
    private final Set<E> s;

    ...

    public void clear() { s.clear() };
    public boolean contains(Object o) { return s.contains(o); }
    public boolean isEmpty() { return s.isEmpty(); }

    ...
}
```

전달 클래스에서는 Set 인터페이스를 구현(implements) 했고, Set의 인스턴스를 인수로 받는 생성자를 하나 제공한다. 이렇게 되면 __InstrumentedSet은 임의의 Set에 addCount 기능 하나만 덧씌워 새로운 Set으로 만들어 지게 되는 것이다.__

__상속__ 방식은 구체 클래스 각각을 따로 확장해야 하며, 지원하고 싶은 상위 클래스의 생성자 각각에 대응하는 생성자를 별도로 정의해줘야 한다.

__컴포지션__ 방식은 한 번만 구현해 두면 아래와 같이 어떠한 Set 구현체라도 계측할 수 있으며, 기존 생성자들과도 함께 사용할 수 있다.

```java
Set<Instant> times = new InstrumentedSet<>(new TreeSet<>(cmp));
Set<E> s = new InstrumentedSet<>(new HashSet<>(INIT_CAPACITY));
```

다른 Set 인스턴스를 감싸고(wrap) 있다는 뜻에서 InstrumentedSet 같은 클래스를 __래퍼 클래스__ 라 하며, 다른 Set에 addCount 기능을 덧씌운다는 뜻에서 __데코레이터 패턴(Decorator pattern)__ 이라고 한다.

컴포지션과 전달(forward) 의 조합은 넓은 의미로 __위임(delegation)__ 이라고 부른다.

래퍼 클래스의 단점으로는 __콜백(callback) 프레임워크와는 어울리지 않는다는 점__ 이 있다.

__내부 객체는 자신을 감싸고 있는 래퍼의 존재를 모르니 대신 자신(this)의 참조를 넘기고, 콜백 때는 래퍼가 아닌 내부 객체를 호출하게 된다. 이를 SELF문제 라고 한다.__

전달 메서드가 성능에 끼치는 영향이나 래퍼 객체가 메모리 사용량에 주는 영향은 __실전에서는 둘 다 별다른 영향이 없다 밝혀졌다.__

상속은 반드시 하위 클래스가 상위 클래스의 '진짜' 하위 타입인 상황에서만 쓰여야 한다.

컴포지션을 써야 할 상황에서 상속을 사용하는 건 __내부 구현을 불필요하게 노출__ 하는 꼴이다. 그렇게 되면 __클라이언트가 노출된 내부에 직접 접근할 수 있게 되고, 클라이언트에서 상위 클래스를 직접 수정하여 하위 클래스의 불변식을 해칠 수 있다.__

컴포지션으로는 확장하려는 클래스의 API에 결함이 있더라도 이런 결함을 숨기는 새로운 API를 설계할 수 있지만, 상속은 상위 클래스의 API를 그 결함까지도 그대로 승계한다.

### 결론 : 상속은 강력하지만 캡슐화를 해친다. 상속은 상위 클래스와 하위 클래스가 순수한 is-a 관계일 때만 써야 한다. 상속의 취약점을 피하려면 상속 대신 컴포지션과 전달을 사용하자. 특히 래퍼 클래스로 구현할 적당한 인터페이스가 있다면 더욱 그렇다. 래퍼 클래스는 하위 클래스보다 견고하고 강력하다.

---

## 아이템 19. 상속을 고려해 설계하고 문서화하라. 그러지 않았다면 상속을 금지하라.

### 상속을 고려한 설계와 문서화란
 * 메서드를 재정의하면 어떤 일이 일어나는지를 정확히 정리하여 문서로 남기는 것. 
   * 다시 말해, 상속용 클래스는 재정의할 수 있는 메서드들을 내부적으로 어떻게 이용하는지 문서로 남겨야 한다. 
   * 이는 상속이 캡슐화를 해치기 때문에 발생할 수 있는 상황들을 미리 인지하게끔 하기 위함이다.
 * 클래스의 내부 동작 과정 중간에 끼어들 수 있는 훅(hook)을 잘 선별하여 protected 메서드 형태로 공개해야 할 수도 있다.
 * 상속용 클래스를 설계할 땐, 직접 하위 클래스를 만들어보며 어떤 메서드를 protected로 노출해야 할지 등을 결정해야 한다. 상속용으로 설계한 클래스는 배포 전에 반드시 하위 클래스를 만들어 검증해야 한다.
 * 상속용 클래스의 생성자는 직접적으로든 간접적으로든 재정의 가능 메서드를 호출해서는 안 된다.
   * 상위 클래스의 생성자가 하위 클래스의 생성자보다 먼저 실행되므로 하위 클래스에서 재정의한 메서드가 하위 클래스의 생성자보다 먼저 호출되는 상황이 발생한다.

#### 상위 클래스의 생성자가 재정의 가능 메서드를 호출하는 예
```java
public class Super {
    // 잘못된 예 - 생성자가 재정의 가능 메서드를 호출한다.
    public Super() {
        overrideMe();
    }
    public void overrideMe() {

    }
}
```
#### 하위 클래스의 코드
```java
public final class Sub extends Super {
    // 초기화되지 않은 final 필드. 생성자에서 초기화한다.
    private final Instant instant;

    Sub() {
        instant = Instant.now();
    }

    // 재정의 가능 메서드. 상위 클래스의 생성자가 호출한다.
    @Override
    public void overrideMe() {
        System.out.println(instant);
    }

    public static void main(String[] args) {
        Sub sub = new Sub();
        sub.overrideMe();
    }
}
```

이 경우에 있어 __상위 클래스의 생성자는 하위 클래스의 생성자가 인스턴스 필드를 초기화하기도 전에 overrideMe를 호출하기 때문에__ instant가 두 번 출력되지 않고, 첫 번째는 null을 출력하게 된다.

 * clone과 readObject 모두 직접적으로든 간접적으로든 재정의 가능 메서드를 호출해서는 안 된다.
 * Serializable을 구현한 상속용 클래스가 readResolve나 writeReplace 메서드를 갖는다면 이 메서드들은 protected로 선언해야 한다.

이 외의 일반적인 구체 클래스는 __클래스에 변화가 생길 때마다 하위 클래스를 오작동하게 만들수 있기 때문에 위험하다.__

따라서 이런 문제를 해결하는 가장 좋은 방법은 __상속용으로 설계하지 않은 클래스는 상속을 금지하는 것이다.__

상속을 금지하는 방법으로는 아래 두 가지 방법이 있다.
 * 클래스를 final로 선언하는 방법
 * 모든 생성자를 private 이나 package-private으로 선언하고 public static 팩터리를 만들어 주는 방법

### 결론 : 상속용 클래스를 설계하기란 쉽지 않다. 클래스 내부에서 스스로를 어떻게 사용하는지 모두 문서로 남겨야, 그 내부 구현 방식을 믿고 활용하던 하위 클래스가 오작동을 피할 수 있다. 클래스를 확장해야 할 명확한 이유가 떠오르지 않으면 상속을 금지하는 편이 낫다. 상속을 금지하려면 클래스를 final로 선언하거나 생성자 모두를 외부에서 접근할 수 없도록 만들면 된다.

---

## 아이템 20. 추상 클래스보다는 인터페이스를 우선하라.

자바가 제공하는 다중 구현 메커니즘은 __인터페이스와 추상 클래스__ 이렇게 두 가지다. 

이 둘의 가장 큰 차이는 __추상 클래스가 정의한 타입을 구현하는 클래스는 반드시 추상 클래스의 하위 클래스가 되어야 한다는 점__ 이다.

반면 __인터페이스가 선언한 메서드를 모두 정의하고 그 일반 규약을 잘 지킨 클래스라면 다른 어떤 클래스를 상속했든 같은 타입으로 취급된다.__

기존 클래스에도 손쉽게 새로운 인터페이스를 구현해넣을 수 있다. 인터페이스가 요구하는 메서드를 추가하고, 클래스 선언에 implements 구문만 추가하면 끝이다.

__인터페이스는 믹스인(mixin) 정의에 안성맞춤이다.__ 믹스인이란 클래스가 구현할 수 있는 타입으로, 믹스인을 구현한 클래스에 원래의 '주된 타입' 외에도 특정 선택적 행위를 제공한다고 선언하는 효과를 준다. 그 예로 __Comparable__ 은 자신을 구현한 클래스의 인스턴스들끼리는 순서를 정할 수 있다고 선언하는 믹스인 인터페이스다. 이처럼 대상 타입의 주된 기능에 선택적 기능을 '혼합(mixed in)' 한다고 해서 믹스인이라 부른다.

__인터페이스로는 계층구조가 없는 타입 프레임워크를 만들 수 있다.__ 

```java
public interface Singer {
    AudioClip sing(Song s);
}
public interface Songwriter {
    Song compose(int chartPosition);
}
```
위 처럼 타입을 인터페이스로 정의하면 __가수 클래스가 Singer 와 Songwriter 모두를 구현해도 전혀 문제되지 않는다.__ 심지어 이 둘 모두를 확장하고 새로운 메서드까지 추가한 제3의 인터페이스를 정의할 수도 있다.

```java
public interface SingerSongwriter extends Singer, Songwriter {
    AudioClip strum();
    void actSensitive();
}
```

__인터페이스는 래퍼 클래스(아이템 18)와 함께 사용하면 그 기능을 향상시키는 안전하고 강력한 수단이 된다.__ 

인터페이스의 메서드 중 구현 방법이 명백한 것이 있다면, 그 구현을 디폴트 메서드로 제공할 수도 있다.

__인터페이스와 추상 골격 구현(skeletal implementation) 클래스를 함께 제공하는 식으로 인터페이스와 추상 클래스의 장점을 모두 취하는 방법도 있다.__ 인터페이스는 타입을 정의하고, 필요에 의해 디폴트 메스드도 함께 제공한다. 그리고 골격 구현 클래스는 나머지 메서드들까지 구현한다. 이것이 __템플릿 메서드 패턴__ 이다.

이 예시로 컬렉션 프레임워크의 ```AbstractCollection```, ```AbstractSet```, ```AbstractList```, ```AbstractMap``` 이 바로 핵심 컬렉션 인터페이스의 골격 구현이다.

골격 구현 클래스는 추상 클래스처럼 구현을 도와주는 동시에, 추상 클래스의 제약에서 자유롭다.

골격 구현 클래스를 작성하는 방법은 아래와 같다.
 * 인터페이스를 확인하여 다른 메서드들의 구현에 사용되는 기반 메서드들을 정한다. 이는 추상 메서드가 된다.
 * 기반 메서드들을 사용해 직접 구현할 수 있는 메서드를 모두 디폴트 메서드로 제공한다.
 * 기반 메서드나 디폴트 메서드로 만들지 못한 메서드가 남아 있다면, 이 인터페이스를 구현하는 골격 구현 클래스를 만들어 남은 메서드들을 작성해 넣는다.

골격 구현 클래스의 예로 ```AbstractMapEntry```가 있다.

```java
public abstract class AbstractMapEntry<K, V> implements Map.Entry<K, V> {
    // 변경 가능한 엔트리는 이 메서드를 반드시 재정의해야 한다.
    @Override
    public V setValue(V value) {
        throw new UnsupportedOverationException();
    }

    ...

}
```

__단순 구현(simple implementation)__ 은 골격 구현의 작은 변종으로, AbstractMap.SimpleEntry가 좋은 예다. 단순 구현도 골격 구현과 같이 상속을 위해 인터페이스를 구현한 것이지만, 추상 클래스가 아니다. 쉽게 말해 __동작하는 가장 단순한 구현__ 이다. 따라서 그대로 써도 되고 필요에 의해 확장해도 된다.

### 결론 : 일반적으로 다중 구현용 타입으로 인터페이스가 가장 적합하다. 복잡한 인터페이스라면 골격 구현을 함께 제공하는 방법을 고려하자.

   * 'interface SingerSongwriter extends Singer, Songwriter' 이거는 다중상속이 아닌것인지!? - 다중상속이 아니라 다중 구현. 인터페이스는 다중이 가능, 객체는 하나만.

---

## 아이템 21. 인터페이스는 구현하는 쪽을 생각해 설계하라.

자바 8에 와서 기존 인터페이스에 메서드를 추가할 수 있도록 디폴트 메서드를 소개했지만, 이는 위험 요소를 갖고있다.

디폴트 메서드를 선언하면, 그 인터페이스를 구현한 후 디폴트 메서드를 재정의하지 않은 모든 클래스에서 디폴트 구현이 쓰이게 된다.

자바 8에서는 핵심 컬렉션 인터페이스들에 다수의 디폴트 메서드가 추가되었다. 주로 '람다'를 활용하기 위해서다. 자바 라이브러리의 디폴트 메서드는 코드 품질이 높고 범용적이라 대부분 상황에서 잘 작동한다. __하지만 생각할 수 있는 모든 상황에서 불변식을 해치지 않는 디폴트 메서드를 작성하기란 어려운 법이다.__

자바 8의 Collection 메서드에 추가된 removeIf() 메서드를 예로 들면, __이 메서드는 주어진 boolean 함수(predicate) 가 true를 반환하는 모든 원소를 제거한다.__ 구현은 아래와 같다.

#### 자바 8의 Collection 인터페이스에 추가된 디폴트 메서드 removeIf
```java
default boolean removeIf(Predicate<? super E> filter) {
    Objects.requireNonNull(filter);
    boolean result = false;
    for (Iterator<E> it = iterator(); it.hasNext(); ) {
        if(filter.test(it.next())) {
            it.remove();
            result = true;
        }
    }
    return result;
}
```

그런데 이 디폴트 메서드는 모든 Collection 구현체와 어우러지지는 못한다. 대표적으로 아파치 커먼즈 라이브러리의 SynchronizedCollection 은 클라이언트가 제공한 객체로 락을 거는 능력을 추가로 제공한다. 즉, 모든 메서드에서 주어진 락 객체로 동기화한 후 내부 컬렉션 객체에 기능을 위임하는 래퍼 클래스다.

하지만 이 클래스는 removeIf 메서드를 재정의하지 않고 있다. __따라서 모든 메서드 호출을 알아서 동기화해주지 못한다. removeIf의 구현은 동기화에 관해 아무것도 모르므로 락 객체를 사용할 수 없다.__

자바 플랫폼 라이브러리에서는 이런 문제를 예방하기 위해 조치를 취했는데, __구현한 인터페이스의 디폴트 메서드를 재정의하고, 다른 메서드에서는 디폴트 메서드를 호출하기 전에 필요한 작업을 수행하도록__ 한 것이 그 예이다.

결과적으로, __디폴트 메서드는 컴파일에 성공하더라도 기존 구현체에 런타임 오류를 일으킬 수 있다.__

__따라서 기존 인터페이스에 디폴트 메서드로 새 메서드를 추가하는 일은 꼭 필요한 경우가 아니면 피해야 한다. 반면 새로운 인터페이스를 만드는 경우라면 표준적인 메서드 구현을 제공하는 데 아주 유용한 수단이며, 그 인터페이스를 더 쉽게 구현해 활용할 수 있게끔 해준다.__

### 결론 : 디폴트 메서드라는 도구가 생겼더라도 인터페이스를 설계할 때는 여전히 세심한 주의를 기울여야 한다.

---

## 아이템 22. 인터페이스는 타입을 정의하는 용도로만 사용하라.

__인퍼테이스는 자신을 구현한 클래스의 인스턴스를 참조할 수 있는 타입 역할을 한다. 달리 말해, 클래스가 어떤 인터페이스를 구현한다는 것은 자신의 인스턴스로 무엇을 할 수 있는지를 클라이언트에 얘기해주는 것이다.__

이에 반하는 예로 '상수 인터페이스' 라는 것이 있다. 상수 인터페이스란 메서드 없이, 상수를 뜻하는 static final 필드로만 가득 찬 인터페이스를 말한다.

#### 상수 인터페이스 안티패턴 - 사용금지
```java
public interface PhysicalConstants {
    // 아보가르도 수 (1/몰)
    static final double AVOGADROS_NUMBER = 6.022_140_857e23;

    // qhfcmaks tkdtn (J/K)
    static final double BOLTZMANN_CONSTANT = 1.380_648_52e-23;

    ...
}
```

위의 상수 인터페이스는 잘못 사용한 예다. __클래스 내부에서 사용하는 상수는 외부 인터페이스가 아니라 내부 구현에 해당된다. 따라서 상수 인터페이스를 구현하는 것은 이 내부 구현을 클래스의 API로 노출하는 행위다.__

상수를 공개할 목적이라면 다음과 같은 선택지가 있다.
 * 특정 클래스나 인터페이스와 강하게 연관된 상수라면 그 클래스나 인터페이스 자체에 추가해야 한다.
 * enum 타입으로 나타내기 적합한 상수라면 enum 타입으로 만들어 공개한다(아이템 34).
 * 인스턴스화 할 수 없는 유틸리티 클래스(아이템 4)에 담아 공개한다.

#### 상수 유틸리티 클래스
```java
public class PhysicalConstants {
    private PhysicalConstants() {}  // 인스턴스화 방지

    // 아보가르도 수 (1/몰)
    static final double AVOGADROS_NUMBER = 6.022_140_857e23;

    // qhfcmaks tkdtn (J/K)
    static final double BOLTZMANN_CONSTANT = 1.380_648_52e-23;

    ...
}
```

### 결론 : 인터페이스는 타입을 정의하는 용도로만 사용해야 한다. 상수 공개용 수단으로 사용하지 말자.

---

## 아이템 23. 태그 달린 클래스보다는 클래스 계층구조를 활용하라.

두 가지 이상의 의미를 표현할 수 있으며, 그 중 현재 표현하는 의미를 '태그' 값으로 알려주는 클래스들이 있다.

#### 태그 달린 클래스 - 클래스 계층구조보다 훨씬 나쁘다
```java
class Figure {
    enum Shape { RECTANGLE, CIRCLE };

    // 태그 필드 - 현재 모양을 나타낸다.
    final Shape shape;

    // 다음 필드들은 모양이 사각형(RECTANGLE)일 때만 쓰인다.
    double length;
    double width;

    // 다음 필드는 모양이 원(CIRCLE)일 때만 쓰인다.
    double radius;

    // 원 전용 생성자
    Figure(double radius) {
        shape = Shape.CIRCLE;
        this.radius = radius;
    }

    // 사각형 전용 생성자
    Figure(double length, double width) {
        shape = Shape.RECTANGLE;
        this.length = length;
        this.width = width;
    }

    double area() {
        switch(shape) {
            case RECTANGLE :
                return length * width;
            case CIRCLE :
                return Math.PI * radius * radius;
            default :
                throw new AssertionError(shape);
        }
    }
}
```

위와 같이 '태그 달린 클래스'는 다음과 같은 단점을 가지고 있다.
 * 불필요한 코드가 많다(enum 타입 선언, 태그 필드, switch문 등).
 * 여러 구현이 한 클래스에 혼재되어 가독성이 떨어진다.
 * 쓰지 않는 필드를 초기화하는 불필요한 코드가 늘어난다.
 * 또 다른 의미를 추가하려면 코드를 수정해야 한다.

위와 같은 이유 때문에 __태그 달린 클래스는 장황하고 오류를 내기 쉬우며 비효율적이다.__

이를 개선하는 방법으로 __클래스 계층구조를 활용하는 서브타이핑(subtyping)__ 이 있다.

태그 달린 클래스를 계층구조로 바꾸는 방법은 아래와 같다.
 * 루트가 될 추상 클래스를 정의한다.
 * 태그 값에 따라 동작이 달라지는 메서드들을 루트 클래스의 추상 메서드로 선언한다(예시의 area()).
 * 태그 값에 상관없이 동작이 일정한 메서드들을 루트 클래스에 일반 메서드로 추가한다.
 * 모든 하위 클래스에서 공통으로 사용하는 데이터 필드들도 전부 루트 클래스로 올린다.
 * 루트 클래스를 확장한 구체 클래스를 의미별로 하나씩 정의한다.

#### 태그 달린 클래스를 클래스 계층구조로 변환
```java
interface Figure {
    double area();

    int size();
}

abstract class Figure {
    private int a;
    abstract double area();

    int size() {
        return a* a;
    }
}

class Circle extends Figure {
    final double radius;

    Circle(double radius) { 
        this.radius = radius; 
    }

    @Override
    double area() {
        return Math.PI * (radius * radius);
    }
}

class Rectangle extends Figure {
    final double length;
    final double width;

    Rectangle(double length, double width) {
        this.length = length;
        this.width = width;
    }

    @Override double area() { 
        return length * width;
    }
}
```

이렇게 구현하면 switch 문을 사용할 필요도 없어진다. 또한 __루트 클래스의 코드를 건드리지 않고도 다른 프로그래머들이 독립적으로 계층구조를 확장하고 함께 사용할 수 있다.__

만약 직사각형에서 정사각형을 추가하려면 다음과 같이 __확장__ 하면 된다.

```java
class Square extends Rectangle {
    Square(double side) {
        super(side, side);
    }
}
```

### 결론 : 기존 클래스에 또는 새로운 클래스를 작성하는데 태그 필드가 등장한다면 태그를 없애고 계층구조로 대체(리팩토링)하는 방법을 생각해보자. 

---

## 아이템 24. 멤버 클래스는 되도록 static으로 만들라.

중첩 클래스(nested class)란 다른 클래스 안에 정의된 클래스를 말한다. 중첩 클래스의 종류는 다음과 같다.

 * 정적 멤버 클래스
 * (비정적) 멤버 클래스
 * 익명 클래스
 * 지역 클래스

각각의 중첩 클래스를 언제, 왜 사용해야 하는지 알아보자.

### 정적 멤버 클래스(static member class)

정적 멤버 클래스는 다른 클래스 안에 선언되고, 바깥 클래스의 private 멤버에도 접근할 수 있다는 점만 제외하고는 일반 클래스와 똑같다.

정적 멤버 클래스는 흔히 바깥 클래스와 함께 쓰일 때만 유용한 public 도우미 클래스로 쓰인다.

### 비정적 멤버 클래스(non-static member class)

단지 static 의 유무에 따른 차이지만, 비정적 멤버 클래스와 정적 멤버 클래스의 의미상 차이는 꽤 크다.

비정적 멤버 클래스의 인스턴스는 바깥 클래스의 인스턴스와 암묵적으로 연결된다. 그래서 비정적 멤버 클래스의 인스턴스 메서드에서 '정규화된 this'를 사용해 바깥 인스턴스의 메서드를 호출하거나 바깥 인스턴스의 참조를 가져올 수 있다. 정규화된 this란 ```클래스명.this```의 형태로 바깥 클래스의 이름을 명시하는 용법을 말한다.

따라서 개념상 중첩 클래스의 인스턴스가 바깥 인스턴스와 독립적으로 존재할 수 있다면 정적 멤버 클래스로 만들어야 한다. 비정적 멤버 클래스는 바깥 인스턴스 없이는 생성할 수 없기 때문이다.

비정적 멤버 클래스의 인스턴스와 바깥 인스턴스 사이의 관게는 멤버 클래스가 인스턴스화될 때 확립되며, 더 이상 변경할 수 없다. 이 관계 정보는 비정적 멤버 클래스의 인스턴스 안에 만들어져 메모리 공간을 차지하며, 생성 시간도 더 걸린다.

__비정적 멤버 클래스는 어댑터를 정의할 때 자주 쓰인다.__ 즉, 어떤 클래스의 인스턴스를 감싸 마치 다른 클래스의 인스턴스처럼 보이게 하는 '뷰'로 사용하는 것이다. 예컨대 Map 인터페이스의 구현체들은 보통 (keySet, entrySet, values 메서드가 반환하는) 자신의 컬렉션 뷰를 구현할 때 비정적 멤버 클래스를 사용한다.

#### 비정적 멤버 클래스의 흔한 쓰임 - 자신의 반복자 구현
```java
public class MySet<E> extends AbstractSet<E> {

    ...

    @Override public Iterator<E> iterator() {
        return new MyIterator();
    }
    private class MyIterator implements Iterator<E> {
        ...
    }
}
```

__멤버 클래스에서 바깥 인스턴스에 접근할 일이 없다면 무조건 static을 붙여서 정적 멤버 클래스로 만들자.__ static을 생략하면 바깥 인스턴스로의 숨은 외부 참조를 갖게 된다. __이 참조를 저장하려면 시간과 공간이 소비된다. 더 심각한 문제는 가비지 컬렉션이 바깥 클래스의 인스턴스를 수거하지 못하는 메모리 누수가 생길 수 있다. 참조가 눈에 보이지 않아 반드시 신경써야 한다.__ 

### private static 멤버 클래스

private static 멤버 클래스는 흔히 바깥 클래스가 표현하는 객체의 한 부분(구성요소)을 나타낼 때 쓴다. 

키와 값을 매핑시키는 Map 인스턴스를 생각해보면, 많은 Map 구현체는 각각의 키-값 쌍을 표현하는 엔트리(Entry) 객체들을 가지고 있다. 모든 엔트리가 맵과 연관되어 있지만 엔트리의 메서드들(getKey, getValue, setValue)은 맵을 직접 사용하지는 않는다. __따라서 엔트리를 비정적 멤버 클래스로 표현하는 것은 낭비고, private static 멤버 클래스가 가장 알맞다.__

실수로 static을 생략하게 된다면 __모든 엔트리가 바깥 맵으로의 참조를 갖게 되어 공간과 시간을 낭비하게 된다.__

멤버 클래스가 공개된 클래스의 public이나 protected 멤버라면, 멤버 클래스 역시 공개 API가 되기 때문에 정적이냐 아니냐에 따라 향후 릴리스에서 static을 붙이면 하위 호환성이 깨지는 상황을 초래할 수 있다.

### 익명 클래스

익명 클래스는 이름이 없다. 멤버와 달리 __쓰이는 시점에 선언과 동시에 인스턴스가 만들어진다.__ 그리고 오직 비정적인 문맥에서 사용될 때만 바깥 클래스의 인스턴스를 참조할 수 있다.

익명 클래스는 응용하는 데엔 다음과 같은 제약이 따른다. 제약이 많은 편이다. 
 * 선언한 시점에서만 인스턴스를 만들 수 있고, instanceof 검사나 클래스의 이름이 필요한 작업은 수행할 수 없다. 
 * 여러 인터페이스를 구현할 수 없고, 인터페이스를 구현하는 동시에 다른 클래스를 상속할 수도 없다.
 * 익명 클래스를 사용하는 클라이언트는 그 익명 클래스가 상위 타입에서 상속한 멤버 외에는 호출할 수 없다.
 * 짧지 않으면 가독성이 떨어진다.

익명 클래스는 람다식으로 많이 대체되었지만, __정적 팩터리 메서드를 구현할 때 자주 사용된다.__

### 지역 클래스

지역변수를 선언할 수 있는 곳이면 실질적으로 어디서든 선언할 수 있고, 유효 범위도 지역변수와 같다.

멤버 클래스처럼 이름이 있고 반복해서 사용할 수 있다. 익명 클래스처럼 비정적 문맥에서 사용될 때만 바깥 인스턴스를 참조할 수 있으며, 정적 멤버는 가질수 없고 가독성을 위해 짧게 작성해야 한다.

### 결론 : 중첩 클래스엔 네 가지가 있다. 메서드 밖에서도 사용해야 하거나 메서드 안에 정의하기엔 너무 길다면 멤버 클래스로 만든다. 멤버 클래스의 인스턴스 각각이 바깥 인스턴스를 참조한다면 비정적으로, 그렇지 않다면 정적으로 만든다. 중첩 클래스가 한 메서드 안에서만 쓰이면서 그 인스턴스를 생성하는 지점이 단 한 곳이고 해당 타입으로 쓰기에 적합한 클래스나 인터페이스가 이미 있다면 익명 클래스로 만들고, 그렇지 않다면 지역 클래스로 만들자.

---

## 아이템 25. 톱레벨 클래스는 한 파일에 하나만 담으라.

소스 파일 하나에 톱레벨 클래스를 여러 개 선언하더라도 자바 컴파일러는 불평하지 않는다. 하지만 그중 어느 것을 사용할지는 어느 소스 파일을 먼저 컴파일하냐에 따라 달라지기 때문에 심각한 문제를 초래할 수 있다.

#### 톱레벨 클래스를 여러 개 선언한 예 - 두 클래스가 한 파일(Utensil.java)에 정의되었다.
```java
class Utensil {
    static final String NAME = "pan";
}
class Dessert {
    static final String NAME = "cake";
}
```

이 경우 ```sout(Utensil.NAME + Dessert.NAME)``` 을 출력하면 pancake을 출력하겠지만, 이 두 클래스를 동일하게 담은 Dessert.java 가 만들어 졌다고 가정하면,  

#### 또 다른 톱레벨 클래스를 여러 개 선언한 java파일 - 두 클래스가 한 파일(Dessert.java)에 정의되었다.
```java
class Utensil {
    static final String NAME = "pot";
}
class Dessert {
    static final String NAME = "pid";
}
```

이를 ```javac Main.java```나 ```javac Main.java Utensil.java``` 명령으로 컴파일하면 pancake을 출력한다. 하지만 ```javac Dessert.java Main.java``` 명령으로 컴파일하면 potpie를 출력한다.

즉, __컴파일러에 어느 소스 파일을 먼저 건네느냐에 따라 동작이 달라지므로 반드시 바로 잡아야 할 문제가 된다.__

이는 단순히 톱레벨 클래스들을 다른 소스파일로 분리하면 해결이 된다. 굳이 한 파일에 담고자 한다면 '정적 멤버 클래스'를 사용해 보자.

#### 톱레벨 클래스들을 정적 멤버 클래스로 바꿔본 모습
```java
public class Test {
    public static void main(String[] args) {
        sout(Utensil.NAME + Dessert.NAME);
    }

    private static class Utensil {
        static final String NAME = "pan";
    }
    private static class Dessert {
        static final String NAME = "cake";
    }
}
```

이렇게 정적 멤버 클래스를 사용하여 톱레벨 클래스를 한 파일에 담게 되면 읽기 좋고, private 으로 선언할 경우 접근 범위도 최소로 관리할 수 있다.

### 결론 : 소스 파일 하나에는 반드시 톱레벨 클래스(혹은 톱레벨 인터페이스)를 하나만 담자.

---