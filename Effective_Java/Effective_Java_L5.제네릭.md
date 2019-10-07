# 5장. 제네릭

[아이템 26. 로 타입은 사용하지 말라.](#아이템-26.-로-타입은-사용하지-말라.)

[아이템 27. 비검사 경고를 제거하라.](#아이템-27.-비검사-경고를-제거하라.)

[아이템 28. 배열보다는 리스트를 사용하라.](#아이템-28.-배열보다는-리스트를-사용하라.)

[아이템 29. 이왕이면 제네릭 타입으로 만들라.](#아이템-29.-이왕이면-제네릭-타입으로-만들라.)

[아이템 30. 이왕이면 제네릭 메서드로 만들라.](#아이템-30.-이왕이면-제네릭-메서드로-만들라.)

[아이템 31. 한정적 와일드카드를 사용해 API 유연성을 높이라.](#아이템-31.-한정적-와일드카드를-사용해-API-유연성을-높이라.)

[아이템 32. 제네릭과 가변인수를 함께 쓸 때는 신중하라.](#아이템-32.-제네릭과-가변인수를-함께-쓸-때는-신중하라.)

[아이템 33. 타입 안전 이종 컨테이너를 고려하라.](#아이템-33.-타입-안전-이종-컨테이너를-고려하라.)

---

## 아이템 26. 로 타입은 사용하지 말라.

__클래스와 인터페이스 선언에 타입 매개변수(type parameter)가 쓰이면, 이를 제네릭 클래스 혹은 제네릭 인터페이스 라 한다.__ 제네릭 클래스와 제네릭 인터페이스를 통틀어 __제네릭 타입(generic type)__ 이라 한다.

각각의 제네릭 타입은 일련의 __매개변수화 타입(parameterized type)__ 을 정의한다. 먼저 클래스(또는 인터페이스) 이름이 나오고, 이어서 꺾쇠로 실제 타입 매개변수들을 나열한다.

예를 들어, ```List<String>``` 은 원소의 타입이 String인 리스트를 뜻하는 매개변수화 타입이다. 여기서 ```String```이 정규(formal) 타입 매개변수 E에 해당하는 실제(actual) 타입 매개변수다.

제네릭 타입을 정의하면, 그에 딸린 '로 타입(raw type)'도 함께 정의된다. __로 타입이란 제네릭 타입에서 타입 매개변수를 전혀 사용하지 않을 때를 말한다.__ 예를 들어 ```List<E>``` 의 로 타입은 List다.

#### 컬렉션의 로 타입
```java
// Stamp 인스턴스만 취급한다.
private final Collection stamps = ...;
```

이 코드를 사용하면 Stamp 인스턴스 대신 전혀 무관한 Coin 인스턴스를 넣어도 아무 오류 없이 컴파일되고 실행된다.

#### 로 타입 사용 예
```java
// 실수로 Coin 인스턴스를 넣는다.
stamps.add(new Coin(...));

for (Iterator i = stamps.iterator(); i.hasNext(); ) {
    Stamp stamp = (Stamp) i.next();     // ClassCastException
    stamp.cancel();
}
```

이렇게 사용하면 __런타임 에서야__ 문제를 알아챌 수 있어 매우 위험한 상황이 발생한다.

반대로 __제네릭을 사용하면 이 정보가 주석이 아닌 타입 선언 자체에 녹아든다.__

```java
private final Collection<Stamp> stamps = ...;
```

이렇게 선언하면 __컴파일러는 stamps에는 Stamp의 인스턴스만 넣어야 함을 컴파일러가 인지하게 된다.__ 그렇기 때문에 아무런 경고 없이 컴파일 된다면 의도대로 동작할 것임이 보장된다.

컴파일러는 컬렉션에서 원소를 꺼내는 모든 곳에 보이지 않는 형변환을 추가하여 절대 실패하지 않음을 보장한다.

요약하자면, __로 타입을 쓰면 제네릭이 안겨주는 안전성과 표현력을 모두 잃게 된다.__ 로 타입(타입 매개변수가 없는 제네릭 타입)을 쓰는 걸 언어 차원에서 마이그레이션 호환성을 위해 막아 놓지는 않았지만 절대로 써서는 안 된다.

List 같은 로 타입은 사용해서는 안 되지만, ```List<Object>``` 처럼 임의 객체를 허용하는 매개변수화 타입은 괜찮다.

__제네릭 타입을 쓰고 싶지만 실제 타입 매개변수가 무엇인지 신경 쓰고 싶지 않다면 물음표(?) 를 사용하자.__ 예컨대 제네릭 타입인 Set<E>의 비한정적 와일드카드 타입은 Set<?> 다. 이것이 어떤 타입이라도 담을 수 있는 가장 범용적인 매개변수화 Set 타입이다.

#### 비한정적 와일드카드 타입을 사용하라. - type safe 하며 유연하다.
```java
static int numElementsInCommon(Set<?> s1, Set<?> s2) { ... }
```

이 물음표(?) 는 와일드카드 타입인데, 이는 type safe하여 안전하다.

로 타입을 써야만 하는 상황은 다음과 같다.
 * class 리터럴에는 로 타입을 써야 한다. 예를 들어 ```List.class```, ```String[].class``` 등
 * instanceof 연산자를 사용할 때.

#### 로 타입을 써도 좋은 예 - instanceof 연산자
```java
if (o instanceof Set) {     // 로 타입
    Set<?> s = (Set<?>) o;  // 와일드카드 타입
    ...
}
```

### 결론 : 로 타입을 사용하면 런타임에 예외가 일어날 수 있으니 사용하면 안 된다. 로 타입은 이전 코드와의 호환성을 위해 제공될 뿐이다.

---

## 아이템 27. 비검사 경고를 제거하라.

제네릭을 사용하기 시작하면 수많은 컴파일러 경고를 보게 된다. 그 중, 비검사 경고는 쉽게 제거할 수 있는 편인데, __가능한 한 모든 비검사 경고를 제거하라.__ 비검사 경고를 모두 제거한다면 그 코드는 타입 안전성이 보장된다.

__경고를 제거할 수 는 없지만 타입 안전하다고 확신할 수 있다면 ```@Suppress Warnings("unchecked")``` 애너테이션을 달아 경고를 숨기자.__ 단, 타입 안전함을 검증하지 않은 채 경고를 숨기면 스스로에게 잘못된 보안 인식을 심어주는 꼴이다. 그 코드는 경고 없이 컴파일되겠지만, 런타임에는 여전히 ClassCastException을 던질 수 있다. 또한 안전하다고 검증된 비검사 경고를 숨기지 않은 채 그대로 두면, 진짜 문제를 알리는 새로운 경고가 나와도 눈치채지 못할 수 있다. 제거하지 않은 수많은 거짓 경고 속에 새로운 경고가 파묻히게 되기 때문이다.

@SuppressWarnings 애너테이션은 개별 지역변수 선언부터 클래스 전체까지 어떤 선언에도 달 수 있다. 하지만 __@SuppressWarnings 애너테이션은 항상 간으한 한 좁은 범위에 적용하자.__

#### ArrayList 의 toArray 메서드
```java
public <T> T[] toArray(T[] a) {
    if (a.length < size)
        return (T[]) Arrays.copyOf(elements, size, a.getClass());
    System.arraycopy(elements, 0, a, 0, size);
    if (a.length > size)
        a[size] = null;
    return a;
}
```

이 ArrayList를 컴파일 하면 다음과 같은 경고가 발생한다.

```
ArrayList.java:305: warning: [unchecked] unchecked cast
    return (T[]) Arrays.copyOf(elements, size, a.getClass());
                               ^
  required : T[]
  found:     Object[]
```

애너테이션은 선언에만 달 수 있기 때문에 return문에는 @SuppressWarnings 를 다는데 불가능하다. 따라서, __return 값을 담을 지역변수를 하나 선언하고 그 변수에 애너테이션을 달아주자.__

#### 지역변수를 추가해 @SuppressWarnings의 범위를 좁힌다.
```java
public <T> T[] toArray(T[] a) {
    if (a.length < size) {
        // 생성한 배열과 매개변수로 받은 배열의 타입이 모두 T[]로 같으므로 올바른 형변환이다.
        @SuppressWarnings("unchecked")
        T[] result = (T[]) Arrays.copyOf(elements, size, a.getClass());
        return result;
    }
    System.arraycopy(elements, 0, a, 0, size);
    if (a.length > size)
        a[size] = null;
    return a;
}
```

__@SuppressWarnings("unchecked") 애너테이션을 사용할 때면 그 경고를 무시해도 안전한 이유를 항상 주석으로 남겨야 한다.__ 이는 다른 사람이 그 코드를 이해하는 데 도움이 되며, 그 코드를 잘못 수정하여 타입 안전성을 잃는 상황을 줄여준다.

### 결론 : 비검사 경고는 중요하니 무시하지 말자. 모든 비검사 경고는 런타임에 ClassCastException을 일으킬 수 있는 잠재적 가능성을 뜻하니 최선을 다해 제거하라. 경고를 없앨 방법을 찾지 못하겠다면, 그 코드가 타입 안전함을 증명하고 가능한 한 범위를 좁혀 @SuppressWarnings("unchecked") 애너테이션으로 경고를 숨겨라. 그리고 숨기기로 한 근거를 주석으로 남겨라.

---

## 아이템 28. 배열보다는 리스트를 사용하라.

배열과 제네릭 타입에는 중요한 차이가 두 가지 있다.

### 첫 번째, 배열은 공변(covariant)이다. 즉, 함께 변한다는 뜻. 반면 제네릭은 불공변(invariant)이다.

배열의 경우 공변이기 때문에 예를 들어, Sub가 Super의 하위 타입이라면 배열 Sub[]는 배열 Super[]의 하위 타입이 된다.

제네릭의 경우 불공변이기 때문에 서로 다른 타입 Type1과 Type2가 있을 때, List<Type1>은 List<Type2>의 하위 타입도, 상위 타입도 아니다. 

위와 같은 차이 때문에 다음과 같이 각기 다른 시점에 에러를 낸다.

#### 배열 - 런타임에 실패한다.
```java
Object[] objectArray = new Long[1];
objectArray[0] = "타입이 달라 넣을 수 없다.";   // ArrayStoreException을 던진다.
```

#### 제네릭 - 컴파일되지 않는다.
```java
List<Object> ol = new ArrayList<Long>();  // 호환되지 않는 타입이다.
ol.add("타입이 달라 넣을 수 없다.");
```

위의 두 경우 모두 Long 타입의 저장소에 String을 넣을 수 는 없다. 다만 __배열에서는 그 실수를 런타임에야 알게 되지만, 리스트를 사용하면 컴파일할 때 바로 알 수 있다.__

### 두 번째, 배열은 실체화(reify)된다. 배열은 런타임에도 자신이 담기로 한 원소의 타입을 인지하고 확인한다. 반면 제네릭은 타입 정보가 런타임에는 소거(erasure)된다.

따라서 Long 배열에 String을 넣으려 하면 ArrayStoreException 이 발생한다. 

반면 제네릭은 타입 정보가 런타임에는 소거되기 때문에, 원소 타입을 컴파일 타임에만 검사하며 런타임에는 알 수조차 없게 된다.

소거는 제네릭이 지원되기 전의 레거시 코드와 제네릭 타입을 함께 사용할 수 있게 해주는 메커니즘이다.

이런 차이로 인해 배열과 제네릭은 잘 어우러지지 못한다. 예를 들어 배열은 제네릭 타입, 매개변수화 타입, 타입 매개변수로 사용할 수 없다.

__제네릭 배열을 만들지 못하게 막은 이유는 type-safe 하지 않기 때문이다.__ 이를 허용한다면 컴파일러가 자동 생성한 형변환 코드에서 런타임에 ClassCastException이 발생할 수 있다.

```E```, ```List<E>```, ```List<String>``` 같은 타입을 실체화 불가 타입(non-reifiable type)이라 한다. 실체화되지 않아서 런타임에는 컴파일타임보다 타입 정보를 적게 가지는 타입이다. 소거 메커니즘 때문에 매개변수화 타입 가운데 실체화될 수 있는 타입은 List<?>와 Map<?, ?> 같은 비한정적 와일드 카드 타입뿐이다.

배열로 형변환할 때 제네릭 배열 생성 오류나 비검사 형변환 경고가 뜨는 경우 대부분은 배열인 ```E[]``` 대신 컬렉션인 ```List<E>``` 를 사용하면 해결된다. 코드가 조금 복잡해지고 성능이 살짝 나빠질 수도 있지만, 그 대신 타입 안전성과 상호운용성은 좋아진다.

#### 제네릭을 적용하지 않은 Chooser
```java
public class Chooser {
    private final Object[] choiceArray;

    public Chooser(Collection choices) {
        choiceArray = choices.toArray();
    }
    
    public Object choose() {
        Random rnd = ThreadLocalRandom.current();
        return choiceArray[rnd.nextInt(choiceArray.length)];
    }
}
```

이 클래스를 사용하려면 choose 메서드를 호출할 때마다 반환된 Object를 원하는 타입으로 형변환해야 한다. 혹시나 타입이 다른 원소가 들어있었다면 런타임에 형변환 오류가 나게 된다.

따라서 type-safe 한 리스트 기반으로 변경하면 아래와 같다.

#### 리스트 기반 Chooser - 타입 안전성 확보
```java
public class Chooser {
    private final List<T> choiceList;

    public Chooser(Collection<T> choices) {
        choiceArray = new ArrayList<>(choices);
    }
    
    public Object choose() {
        Random rnd = ThreadLocalRandom.current();
        return choiceList.get(rnd.nextInt(choiceList.size()));
    }
}
```

이렇게 하면 코드양이 조금 늘었고, 조금 더 느릴 수 있지만, 런타임에 ClassCastException을 만날 일은 없으니 그만한 가치가 있다.

### 결론 : 배열과 제네릭에는 매우 다른 타입 규칙이 적용된다. 배열은 공변이고 실체화되는 반면, 제네릭은 불공변이고 타입 정보가 소거된다. 그 결과 배열은 런타임에는 type-safe 하지만, 컴파일 타임에는 type-safe 하지 않다. 반면, 제네릭은 반대다. 그래서 배열과 제네릭을 섞어 쓰기란 쉽지 않다. 둘을 섞어 쓰다가 컴파일 오류나 경고를 만나면 가장 먼저 배열을 리스트로 대체하는 방법을 적용해보자.

---

## 아이템 29. 이왕이면 제네릭 타입으로 만들라.

제네릭 타입을 새로 만드는 방법에 대해 알아본다.

#### Object 기반 스택 - 제네릭이 절실한 후보
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
        if (size == 0)
            throw new EmptyStackException();
        Object result = elements[--size];
        elements[size] = null;  //  다 쓴 참조 해제
        return result;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private void ensureCapacity() {
        if (elements.length == size)
            elements = Arrays.copyOf(elements, 2 * size + 1);
    }
}
```

일반 클래스를 제네릭 클래스로 만드는 첫 단계는 클래스 선언에 타입 매개변수를 추가하는 일이다. 이때 타입 이름으로는 보통 E를 사용한다.


#### 제네릭 스택으로 가는 첫 단계 - 컴파일 되지 않는다.
```java
public class Stack<E> {
    private E[] elements;
    private int size = 0;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    public Stack() {
        elements = new E[DEFAULT_INITIAL_CAPACITY];
    }

    public void push(E e) {
        ensureCapacity();
        elements[size++] = e;
    }

    public E pop() {
        if (size == 0)
            throw new EmptyStackException();
        E result = elements[--size];
        elements[size] = null;  //  다 쓴 참조 해제
        return result;
    }

    ...
}
```

Object를 적절한 타입 매개변수로 바꾸고 컴파일 해보면 대개 하나 이상의 오류나 경고가 뜬다. 위의 코드를 컴파일 해보려 하면 다음과 같은 오류가 발생한다.

```
Stack.java:8: generic array creation
    elements = new E[DEFAULT_INITIAL_CAPACITY];
               ^
```

아이템 28에서 다룬것 처럼 __E와 같은 실체화 불가 타입으로는 배열을 만들 수 없다.__

배열을 사용하는 코드를 제네릭으로 만들려 할 때 이 문제가 항상 발목을 잡게 되는데 이에 대한 해결첵은 두 가지다.

### 첫 번째, 제네릭 배열 생성을 금지하는 제약을 대놓고 우회하는 방법 : Object 배열을 생성한 다음 제네릭 배열로 형변환한다. 일반적으로 type-safe 하지 않다.

이 방법으로 코드를 고쳐보면 컴파일러는 오류 대신 경고를 내보내게 된다.

```
Stack.java:8: warning: [unchecked] unchecked cast
found: Object[], required: E[]
        elements = (E[]) new Object[DEFAULT_INITIAL_CAPACITY];
                       ^
```

컴파일러가 이 프로그램이 type-safe 한지 증명할 방법이 없기 때문에 우리 스스로 이 비검사 형변환이 type-safe 를 해치지 않음을 확인해야 한다.

비검사 형변환이 안전함을 확신한다면 범위를 최소로 좁혀 @SuppressWarnings 애너테이션으로 해당 경고를 숨긴다(아이템 27).

#### 배열을 사용한 코드를 제네릭으로 만드는 방법 첫 번째
```java
// 배열 elements는 push(E)로 넘어온 E 인스턴스만 담는다.
// 따라서 타입 안전성을 보장하지만,
// 이 배열의 런타임 타입은 E[]가 아닌 Object[] 다.
@SuppressWarnings("unchecked")
public Stack() {
    elements = (E[]) new Object[DEFAULT_INITIAL_CAPACITY];
}
```

### 두 번째, elements 필드의 타입을 E[]에서 Object[]로 바꾼다.

이렇게 하면 첫 번째와는 다른 오류가 발생한다.
```
Stack.java:19: incompatible types
found: Object, required: E
        E result = elements[--size];
                           ^
```

배열이 반환한 원소를 E로 형변환하면 오류 대신 경고가 뜬다.

```
Stack.java:19: [unchecked] unchecked cast
found: Object, required: E
        E result = (E) elements[--size];
                               ^
```

이번에도 마찬가지로 스스로 type-safe 한지 증명하고 경고를 숨기면 된다.

#### 배열을 사용한 코드를 제네릭으로 만드는 방법 두 번째
```java
// 비검사 경고를 적절히 숨긴다.
public E pop() {
    if (size == 0)
        throw new EmptyStackException();
    
    // push에서 E 타임만 허용하므로 이 형변환은 안전하다.
    @SuppressWarnings("unchecked") E result = (E) elements[--size];

    elements[size] = null; // 다 쓴 참조 해제
    return result;
}
```

위 두 가지 방법중 첫 번째 방법이 더 선호되는 방식이다.

앞서 아이템 28에서 '배열보다는 리스트를 우선하라'고 다뤘기 때문에 위의 예시에서 배열을 리스트로 교체하지 않는것에 대해 모순처럼 보일 수 있다.

하지만 __자바가 리스트를 기본 타입으로 제공하지 않으므로 ArrayList 같은 제네릭 타입도 결국은 기본 타입인 배열을 사용해 구현해야 한다. 또한 HashMap 같은 제네릭 타입은 성능을 높일 목적으로 배열을 사용하기도 한다.__

위 Stack 예시처럼 대다수의 제네릭 타입은 타입 매개변수에 아무런 제약을 두지 않는다. 하지만 __기본 타입은 사용할 수 없다.__ ```Stack<int>``` 과 같이 사용하려 하면 컴파일 오류가 난다. 이는 박싱된 기본 타입(아이템 61)을 사용해 우회할 수 있다.

타입 배개변수에 제약을 두는 제네릭 타입도 있다. 예컨대 java.util.concurrent.DelayQueue 는 다음처럼 선언되어 있다.

```java
class DelayQueue<E extends Delayed> implements BlockingQueue<E>
```

타입 매개변수 목록인 ```<E extends Delayed>``` 는 java.util.concurrent.Delayed 의 하위 타입만 받는다는 뜻이다. 이렇게 하여 이를 사용하는 클라이언트는 DelayQueue의 원소에서 형변환 없이 곧바로 Delayed 클래스의 메서드를 호출할 수 있다. 이러한 타입 매개변수 E를 __한정적 타입 매개변수(bounded type parameter)라 한다.__

### 결론 : 클라이언트에서 직접 형변환해야 하는 타입보다 제네릭 타입이 더 안전하고 쓰기 편하다. 그러니 새로운 타입을 설계할 때는 형변환 없이도 사용할 수 있도록 하라.

---

## 아이템 30. 이왕이면 제네릭 메서드로 만들라.

클래스와 마찬가지로, __메서드도 제네릭으로 만들 수 있다.__ 매개변수화 타입을 받는 정적 유틸리티 메서드는 보통 제네릭이다(ex. Collections의 '알고리즘' 메서드 binarySearch, sort 등).

#### 경고가 발생하는 메서드
```java
public static Set union(Set s1, Set s2) {
    Set result = new HashSet(s1);
    result.addAll(s2);
    return result;
}
```

위 메서드는 두 집합의 합집합을 리턴하는 union 메서드인데, 컴파일은 되지만 경고가 발생한다. 이는 이 메서드가 type-safe 하지 않기 떄문으로, __메서드 선언에서의 세 집합(입력 2개, 반환 1개)의 원소 타입을 타입 매개변수로 명시하고, 메서드 안에서도 이 타입 매개변수만 사용하게 수정하면 된다.__

이 때, __(타입 매개변수들을 선언하는) 타입 매개변수 목록은 메서드의 제한자와 리턴 타입 사이에 온다.__

#### 제네릭 메서드
```java
public static <E> Set<E> union(Set<E> s1, Set<E> s2) {
    Set<E> result = new HashSet(s1);
    result.addAll(s2);
    return result;
}
```

위 제네릭을 이용하여 변경된 메서드는 __한정적 와일드카드 타입(아이템 31)을 사용하여 더 유연하게 개선할 수 있다.__

때때로 불변 객체를 여러 타입으로 활용할 수 있게 만들어야 할 때가 있다. 제네리근 런타임에 타입 정보가 소거되므로(아이템 28) 하나의 객체를 어떤 타입으로든 매개변수화할 수 있다.

항등함수(identity function)를 담은 클래스의 경우 이 객체는 상태가 없으니 요청할 때마다 새로 생성하는 것은 낭비다. 제네릭은 소거 방식을 사용하기 때문에 __제네릭 싱글턴__ 하나면 충분하다.

#### 제네릭 싱글턴 팩터리 패턴
```java
private static UnaryOperator<Object> IDENTITY_FN = (t) -> t;

@SuppressWarnings("unchecked")
public static <T> UnaryOperator<T> identityFunction() {
    return (UnaryOperator<T>) IDENTITY_FN;
}
```

위의 경우 비검사 형변환 경고를 잡기위해 ```@SuppressWarnings("unchecked")``` 를 사용하였다. 경고가 발생하는 이유는 T가 어떤 타입이든 ```UnaryOperator<Object>``` 는 ```UnaryOperator<T>```가 아니기 때문이다. 하지만 __항등함수란 입력 값을 수정 없이 그대로 반환하는 함수이므로, T가 어떤 타입이든 ```UnaryOperator<T>``` 를 사용해도 type-safe 하다.

__재귀적 타입 한정(recursive type bound)__ 은 자기 자신이 들어간 표현식을 사용하여 타입 매개변수의 허용 범위를 한정하는 것이다. 재귀적 타입 한정은 주로 타입의 자연적 순서를 정하는 Comparable 인터페이스(아이템 14)와 함께 쓰인다.

#### Comparable 인터페이스
```java
public interface Comparable<T> {
    int compareTo(T o);
}
```

#### 재귀적 타입 한정을 이용해 상호 비교할 수 있음을 표현
```java
public static <E extends Comparable<E>> E max(Collection<E> c);
```

타입 한정인 ```<E extends Comparable<E>>``` 는 "모든 타입 E는 자신과 비교할 수 있다" 라고 읽을 수 있다.


### 결론 : 제네릭 타입과 마찬가지로, 클라이언트에서 입력 매개변수와 반환값을 명시적으로 형변환해야 하는 메서드보다 제네릭 메서드가 더 안전하며 사용하기도 쉽다. 메서드도 형변환 없이 사용할 수 있는 편이 좋으며, 많은 경우 그렇게 하려면 제네릭 메서드가 되어야 한다. 형변환을 해줘야 하는 기존 메서드는 제네릭하게 만들자.

---

## 아이템 31. 한정적 와일드카드를 사용해 API 유연성을 높이라.

매개변수화 타입은 불공변(invariant)이다. 즉, 서로 다른 타입 Type1 과 Type2 가 있을 때, ```List<Type1>```은 ```List<Type2>```의 하위 타입도, 상위 타입도 아니다. 따라서 ```List<String>```은 ```List<Object>```의 하위 타입이 아닌게 된다(리스코프 치환 원칙에 위배).

스택에 일련의 원소를 넣는 메서드를 다음과 같이 구현하면 결함이 발생한다.

#### 와일드카드 타입을 사용하지 않은 pushAll 메서드 - 결함 존재
```java
public void pushAll(Iterable<E> src) {
    for (E e : src)
        push(e);
}
```

Iterable src 의 원소 타입이 스택의 원소 타입과 일치한다면 잘 작동하겠지만, __Stack<Number>로 선언한 후 pushAll(intVal)을 호출하는 경우__ 문제가 발생한다. Integer는 Number의 하위 타입이니 잘 동작할 것 같지만 그렇지 않다.

```
StackTest.java:7: error: incompatible types: Iterable<Integer> cannot be converted to Iterable<Number>
    numberStack.pushAll(integers);
                        ^
```

이는 __매개변수화 타입이 불공변이기 때문이다.__ 자바는 이런 상황에 대처하도록 __한정적 와일드카드 타입__ 이라는 특별한 매개변수화 타입을 지원한다.

#### E 생산자(producer) 매개변수에 와일드카드 타입 적용
```java
public void pushAll(Iterable<? extends E> src) {
    for(E e : src)
        push(e);
}
```

같은 맥락으로 Stack 안의 모든 원소를 주어진 컬렉션으로 옮겨 담는 popAll 메서드의 경우 같은 문제가 발생한다.

#### 와일드카드 타입을 사용하지 않은 popAll 메서드 - 결함 존재
```java
public void popAll(Collection<E> dst) {
    while (!isEmpty())
        dst.add(pop());
}
```

이를 와읻르카드 타입을 사용하면 아래와 같이 적용된다.

#### E 소비자(consumer) 매개변수에 와일드카드 타입 적용
```java
public void popAll(Collection<? super E> dst) {
    while (!isEmpty())
        dst.add(pop());
}
```

따라서 __유연성을 극대화하려면 원소의 생산자나 소비자용 입력 매개변수에 와일드카드 타입을 사용하라.__ 하지만, 입력 매개변수가 생산자와 소비자 역할을 동시에 한다면 와일드카드 타입을 쓰면 오히려 악효과가 날 수 있다. 클라이언트가 직접 casting 해야하는 수고가 발생할 여지가 있음. 같은 맥락으로 리턴 타입에는 한정적 와일드카드 타입을 사용하면 안됨.

### PECS : Producer-Extends, Consumer-Super

즉, __매개변수화 타입 T가 생산자라면 <? extends T>를 사용하고, 소비자라면 <? super T>를 사용하라.__ Stack의 예시에서 pushAll의 src 매개변수는 Stack이 사용할 E 인스턴스를 __생산__ 하고, popAll의 dst 매개변수는 Stack 으로부터 E 인스턴스를 __소비__ 하고있다.

클라이언트가 와일드카드 타입을 신경 써야 한다면 그 API에 문제가 있을 가능성이 크다.

#### 참고 : 매개변수(parameter) - 메서드 선언에 정의한 변수, 인수(argument) - 메서드 호출 시 넘기는 '실제 값'.

```java
public statkc <E extends Comparable<E>> E max(List<E> list)
```

이 경우 PECS 공식을 적용하여 변환하면, 생산자와 소비자가 공존하게 된다.

```java
public static <E extends Comparable<? super E>> E max(List<? extends E> list)
```

이렇게 수정할 경우 매우 복잡한 형태를 띄게 됨에도 불구하고 __Comparable(혹은 Comparator)을 직접 구현하지 않고, 직접 구현한 다른 타입을 확장한 타입을 지원하기 위해 와일드카드가 필요하다.__ 

__Comparable, Comparator는 언제나 소비자다.__

타입 매개변수와 와일드카드에는 공통되는 부분이 있어서, 메서드를 정의할 때 둘 중 어느것을 사용할지 선택이 필요하다. __메서드 선언에 타입 매개변수가 한 번만 나오면 와일드카드가 좋다.__

### 결론 : 와일드카드 타입을 적용하면 API가 훨씬 유연해진다. 따라서 널리 쓰일 라이브러리를 작성한다면 반드시 와일드카드 타입을 적절히 사용해야 한다. PECS - Producer는 extends를, Consumer는 super를. Comparable과 Comparator는 모두 소비자다.

---

## 아이템 32. 제네릭과 가변인수를 함께 쓸 때는 신중하라.

가변인수는 메서드에 넘기는 인수의 개수를 클라이언트가 조절할 수 있게 해주었는데, 구현 방식에 허점이 있다. 가변인수 메서드를 호출하면 이를 담기 위한 배열이 자동으로 만들어지는데, 이 배열이 클라이언트에 노출된다. 그 결과 varargs 매개변수에 제네릭이나 매개변수화 타입이 포함되면 컴파일 경고가 발생한다.

매개변수화 타입의 변수가 타입이 다른 객체를 참조하면 힙 오염(heap pollution)이 발생한다.

#### 제네릭과 varargs 를 혼용하면 타입 안전성이 깨진다.
```java
static void dangerous(List<String>... stringLists) {
    List<Integer> intList = List.of(42);
    Object[] objects = stringLists;
    objects[0] = intList;               // 힙 오염 발생
    String s = stringLists[0].get(0);   // ClassCastException
}
```

따라서 __제네릭 varargs 배열 매개변수에 값을 저장하는 것은 안전하지 않다.__

하지만 __메서드가 안전한 게 확실하다면 @SafeVarargs 애너테이션을 달아 그 메서드가 type-safe 함을 보장할 수 있다.__

다음 두 조건을 모두 만족하는 경우에만 제네릭 varargs 메서드가 안전하다고 볼 수 있다.
 * varargs 매개변수 배열에 아무것도 저장하지 않는다.
 * 그 배열(혹은 복제본)을 신뢰할 수 없는 코드에 노출하지 않는다.

@SafeVarargs 애너테이션을 다는 방법 외에도 (실체는 배열인) varargs 매개변수를 List 매개변수로 바꿀 수도 있다.

#### 제네릭 varargs 매개변수를 List로 대체한 예 - type-safe 하다.
```java
static <T> List<T> flatten(List<List<? extends T>> lists) {
    List<T> result = new ArrayList<>();
    for (List<? extends T> list : lists)
        result.addAll(list);
    return result;
}
```

### 결론 : 가변인수와 제네릭은 궁합이 좋지 않아 같이 쓰는게 위험하다. 가변인수 기능은 배열을 노출하여 추상화가 완벽하지 못하고, 배열과 제네릭의 타입 규칙이 서로 다르기 때문이다. 메서드에 제네릭 varargs 매개변수를 사용하고자 한다면, 그 메서드가 type-safe 함을 확인하고 @SafeVarargs 애너테이션을 달아주자.

 * varargs : 가변인수.

---

## !!아이템 33. 타입 안전 이종 컨테이너를 고려하라.!!

제네릭은 ```Set<E>``` 등의 컬렉션과 ```ThreadLocak<T>``` 등의 단일원소 컨테이너에도 흔히 쓰인다. 여기서 __(원소가 아닌)매개변수화되는 대상은 컨테이너 자신이다.__

하지만 예를 들어 데이터베이스의 행(row)은 임의 개수의 열(column) 을 가질 수 있는데, 모든 열을 type-safe 하게 이용하고 싶다면 더 유연한 수단이 필요하다. 이 경우에 __컨테이너 대신 키를 매개변수화한 다음, 컨테이너에 값을 넣거나 뺄 때 매개변수화한 키를 함께 제공하면 된다.__

이렇게 하면 제네릭 타입 시스템이, 값의 타입이 키와 같음을 보장해 주게 되는데 이러한 설계 방식을 __타입 안전 이종 컨테이너 패턴(type safe heterogeneous container pattern)__ 이라 한다.

#### type-safe 이종 컨테이너 패턴 - API
```java
public class Favorites { 
    public <T> void putFavorite(Class<T> type, T instance);
    public <T> T getFavorite(Class<T> type);
}
```

#### type-safe 이종 컨테이너 패턴을 사용하는 클라이언트
```java
public static void main(String[] args) {
    Favorites f = new Favorites();

    f.putFavorite(String.class, "Java");
    f.putFavorite(Integer.class, 0xbabeface);
    f.putFavorite(Class.class, Favorites.class);

    String favoriteString = f.getFavorite(String.class);
    int favoriteInteger = f.getFavorite(Integer.class);
    Class<?> favoriteClass = f.getFavorite(Class.class);

    ...
}
```

#### type-safe 이종 컨테이너 패턴 - 구현
```java
public class Favorites { 
    private Map<Class<?>, Object> favorites = new HashMap<>();

    public <T> void putFavorite(Class<T> type, T instance) {
        favorites.put(Objects.requireNonNull(type), instance);
    }
    public <T> T getFavorite(Class<T> type) {
        return type.cast(favorites.get(type));
    }
}
```

__이렇게 하면 favorites 맵 안의 값은 해당 키의 타입과 항상 일치함을 보장할 수 있다.__

하지만 위 Favorites 클래스에는 다음과 같은 문제점이 존재하게 된다.
 * 악의적인 클라이언트가 Class 객체를 (제네릭이 아닌) 로 타입(아이템 26)으로 넘기면 Favorites 인스턴스의 타입 안전성이 쉽게 깨진다.
 * 실체화 불가 타입(아이템 28)에는 사용할 수 없다.

첫 번째 문제점을 개선하려면 __런타임시 type-safe를 얻어야 한다.__ 이는 아래처럼 __동적 형변환__ 으로 확보할 수 있다.

#### 동적 형변환으로 런타임 타입 안전성 확보
```java
public <T> void putFavorite(Class<T> type, T instance) {
    favorites.put(Objects.requireNonNull(type), type.cast(instance));
}
```

두 번째 문제점은 다시 말해 String 이나 String[]은 저장할 수 있어도, ```List<String>```은 저장할 수 없다는 것이다. 이는 __슈퍼 타입 토큰(super type token)__ 을 사용하여 해결이 가능하다.

#### 슈퍼 타입 토큰을 사용한 개선 방법
```java
Favorites f = new Favorites();

List<String> pets = Arrays.asList("개", "고양이", "앵무새");

f.putFavorite(new TypeRef<List<String>>(){}, pets);
List<String> listOfStrings = f.getFavorite(new TypeRef<List<String>>(){});
```

### 결론 : 컬렉션 API로 대표되는 일반적인 제네릭 형태에서는 한 컨테이너가 다룰 수 있는 타입 매개변수의 수가 고정되어 있다. 하지만 컨테이너 자체가 아닌 키를 타입 매개변수로 바꾸면 이런 제약이 없는 'type-safe 이종 컨테이너'를 만들 수 있다. type-safe 이종 컨테이너는 'Class'를 '키'로 쓰는 형태이다. 이런 식으로 쓰이는 Class 객체를 '타입 토큰'이라 한다.

---