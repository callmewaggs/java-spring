# 3장. 모든 객체의 공통 메서드

Object 에서 __final 이 아닌 메서드들인 equals, hashCode, toString, clone, finalize__ 는 모두 overriding 을 염두에 두고 설계된 것이다. 따라서 이 메서드들을 __언제 어떻게 재정의(overriding) 해야 하는지를__ 다룬다.

[아이템 10. equals는 일반 규약을 지켜 재정의하라.](#아이템-10.-equals는-일반-규약을-지켜-재정의하라.)

[아이템 11. equals를 재정의하려거든 hashCode도 재정의하라.](#아이템-11.-equals를-재정의하려거든-hashCode도-재정의하라.)

[아이템 12. toString을 항상 재정의하라.](#아이템-12.-toString을-항상-재정의하라.)

[아이템 13. clone 재정의는 주의해서 진행하라.](#아이템-13.-clone-재정의는-주의해서-진행하라.)

[아이템 14. Comparable을 구현할지 고려하라.](#아이템-14.-Comparable을-구현할지-고려하라.)

## 아이템 10. equals는 일반 규약을 지켜 재정의하라.

equals 의 경우 가장 최선의 선택은 __재정의(overriding)하지 않는 것이다.__ 따라서 아래의 상황중 하나라도 해당한다면 재정의 하지 않는것을 고려한다.

 * 각 인스턴스가 본질적으로 고유하다.
 * 인스턴스의 '논리적 동치성(logical equality)'을 검사할 일이 없다.
 * 상위 클래스에서 재정의한 equals가 하위 클래스에도 잘 들어맞는다.
 * 클래스가 private 이거나 package-private 이고 equals 메서드를 호출할 일이 없다.

__equals 를 정의해야 하는 상황__ 에서는 다음과 같은 조건들을 만족해야 한다.

### equals 메서드의 일반 규약

equals 메서드는 __동치관계(equivalence relation)__ 를 구현하며, 다음을 만족한다.

 * 반사성(reflexivity) : null이 아닌 모든 참조 값 x에 대해, x.equals(x)는 true다.
 * 대칭성(symmetry) : null이 아닌 모든 참조 값 x, y에 대해, x.equals(y)가 true면 y.equals(x)도 true다.
 * 추이성(transitivity) : null이 아닌 모든 참조 값 x, y, z에 대해, x.equalx(y)가 true이고 y.equals(z)도 true면 x.equals(z)도 true다.
 * 일관성(consistency) : null이 아닌 모든 참조 값 x, y에 대해, x.equals(y)를 반복해서 호출하면 항상 true를 반환하거나 항상 false를 반환한다.
 * not-null : null이 아닌 모든 참조 값 x에 대해, x.equals(null) 은 false다.


일반적으로 '구체 클래스를 확장해 새로운 값을 추가하면서 equals 규약을 만족시킬 방법은 존재하지 않는다.'

하지만 '상속 대신 컴포지션을 사용하여' 이를 우회할 수 있다.

#### equals 규약을 지키면서 값 추가하기
```java
public class ColorPoint {
    private final Point point;
    private final Color color;

    public ColorPoint(int x, int y, Color color) {
        point = new Point(x, y);
        this.color = Objects.requireNonNull(color);
    }

    // 이 ColorPoint 의 뷰를 반환한다.
    public Point asPoint() {
        return point;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ColorPoint))
            return false;
        ColorPoint cp = (ColorPoint) o;
        return cp.point.equals(point) && cp.color.equals(color);
    }
    ...
}
```

equals 는 항상 메모리에 존재하는 객체만을 사용한 결정적(deterministic) 계산만 수행해야 한다.

따라서 __양질의 equals 메서드를 구현하는 방법__ 은 다음과 같다.
 * == 연산자를 사용해 입력이 자기 자신의 참조인지 확인한다.
 * instanceof 연산자로 입력이 올바른 타입인지 확인한다.
 * 입력을 올바른 타입으로 형변환한다.
 * 입력 객체와 자기 자신의 대응되는 '핵심' 필드들이 모두 일치하는지 하나씩 검사한다.
 * equals를 재정의할 땐 hashCode 도 반드시 재정의하자.
 * 너무 복잡하게 해결하려 들지 말자.

#### 전형적인 equals 메서드의 예
```java
@Override
public boolean equals(Object o) {
    if (o == this)
        return true;
    if (!(o instanceof PhoneNumber))
        return false;
    PhoneNumber pn = (PhoneNumber)o;
    return pn.lineNum == lineNum && pn.prefix == prefix 
    && pn.areaCode == areaCode;
}
```

#### 잘못된 equals - 입력 타입은 반드시 Object여야 한다.
```java
public boolean equals(MyClass o) {
    ...
}
```

이렇게 까다로운 equals(hashCode 포함)를 작성하고 테스트하는 일은 번거로울 수 있다. 이를 구글이 만든 __AutoValue__ 프레임워크에서 자동으로 제공해준다. 애노테이션 하나만으로 AutoValue 가 이 메서드들을 알아서 작성해준다.

### 결론 : 꼭 필요한 경우가 아니라면 equals 를 재정의하지 말자. 재정의할 경우 다섯 가지 규약을 반드시 만족해야 한다.

---

## 아이템 11. equals를 재정의하려거든 hashCode도 재정의하라.

__equals를 재정의한 클래스 모두에서 hashCode도 재정의해야 한다.__ 그렇지 않으면 hashCode 일반 규약을 어기게 되어 해당 클래스의 인스턴스를 HashMap 이나 HashSet 같은 컬렉션의 원소로 사용할 때 문제를 일으키게 된다.

다음은 Object 명세에서 발췌한 규약이다.
 * equals 비교에 사용되는 정보가 변경되지 않았다면, 애플리케이션이 실행되는 동안 그 객체의 hashCode 메서드는 몇 번을 호출해도 일관되게 항상 같은 값을 반환해야 한다. 단, 애플리케이션을 다시 실행한다면 이 값이 달라져도 상관없다.
 * equals(Object)가 두 객체를 같다고 판단했다면, 두 객체의 hashCode는 똑같은 값을 반환해야 한다.
 * equals(Object)가 두 객체를 다르다고 판단했더라도, 두 객체의 hashCode가 서로 다른 값을 반환할 필요는 없다. 단, 다른 객체에 대해서는 다른 값을 반환해야 해시테이블의 성능이 좋아진다.

__hashCode 오버라이딩시 "논리적으로 같은 객체는 같은 해시코드를 반환해야 한다." equals는 물리적으로 다른 두 객체를 논리적으로는 같다고 할 수 있다. 하지만 Object의 기본 hashCode 메서드는 이 둘이 전혀 다르다고 판단하여, 서로 다른 값을 반환할 수 있다.__

```java
Map<PhoneNumber, String> m = new HashMap<>();
m.put(new PhoneNumber(707, 867, 5309), "제니");
```
이 코드 다음에
```java
m.get(new PhoneNumber(707, 867, 5309));
```
를 실행하면 "제니" 가 나올 것을 기대하지만, __실제로는 null 을 리턴__ 한다.

위 예시에서 2개의 PhoneNumber 인스턴스가 사용되었는데(HashMap에 넣을때, 꺼낼때), __PhoneNumber 클래스는 hashCode 를 재정의하지 않았기 때문에 논리적 동치인 두 객체가 서로 다른 해시코드를 반환하게 되어 두 번째 규약을 지키지 못한다.__ 따라서 hashCode 를 재정의 해 주면 위 상황에서의 문제는 해결된다.

좋은 해시 함수라면 서로 다른 인스턴스에 다른 해시코드를 반환한다. 이상적인 해시 함수는 주어진 (서로 다른) 인스턴스들을 32비트 정수 범위에 균일하게 분배해야 한다.

해시 함수에 있어 ```result = 31 * result + c``` 와 같이 계산함으로써 클래스에 비슷한 필드가 여러 개일 때, 해시 효과를 크게 높여준다. 곱할 숫자를 31로 정한 이유는 31이 홀수이면서 소수(prime)이기 때문이다. 2를 곱하는 것은 시프트 연산과 같은 결과를 내기 때문이고, 소수를 곱하는 이유는 일종의 관례이다. 결과적으로 31을 이용하면, 이 곱셈을 시프트 연산과 뺄셈으로 대체해 최적화할 수 있다(31 * i 는 (i << 5) - i 와 같다).

#### 전형적인 hashCode 메서드
```java
@Override
public int hashCode() {
    int result = Short.hashCode(areaCode);
    result = 31 * result + Short.hashCode(prefix);
    result = 31 * result + Short.hashCode(lineNum);
    return result;
}
```

Object 클래스는 임의의 개수만큼 객체를 받아 해시코드를 계산해주는 정적 메서드인 hash를 제공한다. 따라서 hashCode 함수를 단 한줄로 작성할 수 있지만 속도는 직접 작성한 것에 비해 더 느리다.

#### 한 줄짜리 hashCode 메서드 - 성능은 살짝 느리다.
```java
@Override
public int hashCode() {
    return Objects.hash(lineNum, prefix, areaCode);
}
```

### 결론 : equals를 재정의할 때는 hashCode도 반드시 재정의해야 한다. 재정의한 hashCode는 Object의 API 문서에 기술된 일반 규약을 따라야 하며, 서로 다른 인스턴스라면 되도록 해시코드도 서로 다르게 구현해야 한다. AutoValue 프레임워크를 사용하면 이러한 equals 와 hashCode 를 자동으로 만들어준다. - lombok 사용 추천

---

## 아이템 12. toString을 항상 재정의하라.

Object의 기본 toString 메서드를 사용할 경우 ```PhoneNumber@adbbd``` 처럼 단순히 ```클래스_이름@16진수로_표시한_해시코드``` 를 반환한다. 

따라서 toString 을 오버라이딩 해 준다면 디버깅에 용이하며 이용하기 편리하다. 제대로 오버라이딩 하지 않으면 쓸모없는 메시지만 로그에 남게 된다.

__toString은 그 객체가 가진 주요 정보 모두를 반환하는게 좋다.__ 이상적으로는 스스로를 완벽히 설명하는 문자열이어야 한다.

__toString이 반환한 값에 포함된 정보를 얻어올 수 있는 API를 제공해야 한다.__ 그렇지 않으면 클라이언트는 toString의 반환값을 파싱해야만 할 수도 있다.

내부에서 개발용으로 toString() 을 사용하는것이 맞는것이고, 클라이언트 등의 외부로 이걸 보여주는 것은 권장되지 않음.

### 결론 : 모든 구체 클래스에서 Object의 toString을 재정의하자. 구글의 AutoValue 프레임워크는 toString도 생성해준다.

---

## 아이템 13. clone 재정의는 주의해서 진행하라.

Cloneable은 문제가 많다. 복제해도 되는 클래스임을 명시하는 용도의 mixin interface 이지만, clone 메서드가 선언된 곳이 Cloneable이 아닌 Object 이고, 그마저도 protected 이다.

메서드가 하나도 없는 Cloneable 인터페이스는 __Object의 protected 메서드인 clone의 동작 방식을 결정한다.__

배열을 복제할 때는 배열의 clone 메서드를 사용하라고 권장한다.

여러 문제점들을 고려했을 때, __super.clone() 과 깊은 복사(deep copy)를 사용__ 해서 완벽히 분리된 새로운 객체를 제공할 수 있어야 한다.

Cloneable을 구현하는 모든 클래스는 clone을 재정의해야 한다. 이때 접근 제한자는 public 으로, 반환 타입은 클래스 자신으로 변경한다. 그 객체의 내부 '깊은 구조'에 숨어 있는 모든 가변 객체를 복사하고, 복제본이 가진 객체 참조 모두가 복사된 객체들을 가리키게 해야 한다.

하지만 Cloneable 을 이미 구현한 클래스를 확장한다면 어쩔 수 없이 위 과정을 거쳐야 하지만, 그렇지 않다면 __'복사 생성자'와 '복사 팩터리' 를 사용__ 하여 더 나은 객체 복사 방식을 제공할 수 있다.

#### 복사 생성자
```java
public Yum(Yum yum)
{
    ...
};
```
#### 복사 팩터리
```java
public static Yum newInstance(Yum yum)
{
    ...
};
```

복사 생성자와 그 변형인 복사 팩터리는 Cloneable/clone 방식보다 나은 면이 많다. 

### 복사 생성자와 그 변형인 복사 팩터리 -변환 생성자(conversion constructor), 변환 팩터리(conversion factory)- 의 장점
 * 언어 모순적이고 위험한 '객체 생성 메커니즘'을 사용하지 않음
 * 문서화된 규약에 의존하지 않음
 * final 필드 용법과도 충돌하지 않음
 * 불필요한 검사 예외를 던지지 않음
 * 형변환 필요하지 않음
 * __해당 클래스가 구현한 '인터페이스' 타입의 인스턴스를 인수로 받을 수 있음.__

### 결론 : Cloneable 은 많은 문제점을 가지고 있기 때문에, 새로운 인터페이스를 만들 땐 절대 Cloneable 을 확장해서는 안되며, 이를 구현해서도 안된다. 복제 기능은 '복제 생성자와 복제 팩터리'를 이용하는게 가장 최선이다.

 * super - 부모의 메서드나 생성자를 호출할 때 사용.

---

## 아이템 14. Comparable을 구현할지 고려하라.

Comparable 인터페이스의 유일한 메서드인 compareTo는 Object의 equals 와 구분하여 사용할 수 있어야 한다.

compareTo :  단순 동치성 비교 가능, 순서 비교 가능, 제네릭.

Compareable 을 구현했다는 것은 그 클래스의 인스턴스들에 자연적 순서(natural order)가 있음을 뜻한다. 그렇기 때문에 ```Arrays.sort(a);``` 와 같이 손쉽게 정렬이 가능하다.

따라서 __알파벳, 숫자, 연대 같이 순서가 명확한 값 클래스를 작성한다면 반드시 Comparable 인터페이스를 구현하자.__

### compareTo 메서드의 일반 규약

이 객체와 주어진 객체의 순서를 비교한다. __이 객체가 주어진 객체보다 작으면 음의 정수를, 같으면 0을, 크면 양의 정수를 반환한다.__ 이 객체와 비교할 수 없는 타입의 객체가 주어지면 ClassCastException을 던진다.

비교시 -, 0, + 만을 비교하기 때문에 그 숫자는 의미가 없음.

  다음 설명에서 sgn(표현식) 표기는 수학에서 말하는 부호 함수(signum function)를 뜻하며, 표현식의 값이 음수, 0, 양수 일 때 -1, 0, 1을 반환하도록 정의했다.

 * Comparable을 구현한 클래스는 모든 x, y에 대해 sgn(x.compareTo(y)) == -sgn(y.compareTo(x))여야 한다. 따라서 x.compareTo(y)는 y.compareTo(x) 가 예외를 던질 때에 한해 예외를 던져야 한다.

 * Comparable을 구현한 클래스는 추이성을 보장해야 한다. 즉, x.compareTo(y) > 0 && y.compareTo(z) > 0 이면 x.compareTo(z) > 0 이다.

 * Comparable을 구현한 클래스는 모든 z에 대해 x.compareTo(y) == 0 이면 sgn(x.compareTo(z)) == sgn(y.compareTo(z)) 이다.
 
 * (x.compareTo(y)) == 0) == (x.equals(y)) 여야 한다. - 필수는 아니지만 권장

compareTo 는 equals 메서드와 달리, 타입이 다른 객체를 신경 쓰지 않아도 된다. 다를 경우 예외를 던지면 됨. 

비교에 있어서, 핵심 필드가 여러 개라면 어느 것을 먼저 비교하느냐가 중요해진다. 가장 핵심적인 필드부터 비교해나가자. 비교 결과가 0이 아니라면 곧장 그 결과를 반환하고, 0이라면 똑같지 않은 핵심 필드를 찾을 때까지 그 다음 중요도에 따른 비교를 해나간다.

#### 기본 타입 필드가 여럿일 때의 비교자
```java
public int compareTo(PhoneNumber pn) {
    int result = Short.compare(areaCode, pn.areaCode);      // 가장 중요한 필드
    if (result == 0) {
        result = Short.compare(prefix, pn.prefix);          // 두 번째로 중요한 필드
        if (result == 0) {
            result = Short.compare(lineNum, pn.lineNum);    // 세 번째로 중요한 필드
        }
    }
    return result;
}
```

자바 8에서는 Comparator 인터페이스가 '비교자 생성 메서드(comparator construction method)'와 함께 method chaining 방식으로도 비교자를 생성할 수 있게 되었다. 단, 약간의 성능 저하가 뒤따른다.

#### 비교자 생성 메서드를 활용한 비교자
```java
private static final Comparator<PhoneNumber> COMPARATOR = 
        comparingInt((PhoneNumber pn) -> pn.areaCode)
                    .thenComparingInt(pn -> pn.prefix)
                    .thenComparingInt(pn -> pn.lineNum);

public int compareTo(PhoneNumber pn) {
    return COMPARATOR.compare(this, pn);
}
```

### 결론 : 순서를 고려해야 하는 값 클래스를 작성한다면 꼭 Comparable 인터페이스를 구현하여 그 인스턴스들을 쉽게 정렬하고, 검색하고, 비교 기능을 제공하는 컬렉션과 어우러지도록 해야 한다.

 * '뷰' 메서드 : 상태 값을 보여주는 view. getter 같은 역할의 메서드.

---