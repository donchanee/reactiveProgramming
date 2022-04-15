package com.codestates.bta;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Locale;

public class FirstAssignmentTests {

    @Test
    @DisplayName("1. [\"Blenders\", \"Old\", \"Johnnie\"] 와 \"[Pride\", \"Monk\", \"Walker”] 를 순서대로 하나의 스트림으로 처리되는 로직 검증")
    public void concatWithDelay() {
        Flux<String> names1$ = Flux.just("Blenders", "Old", "Johnnie")
                .delayElements(Duration.ofMillis(100));
        Flux<String> names2$ = Flux.just("Pride", "Monk", "Walker")
                .delayElements(Duration.ofMillis(100));
        // concat 오퍼레이션을 통해 두개의 플럭스를 합친다.
        Flux<String> names$ = Flux.concat(names1$, names2$)
                .log();

        StepVerifier.create(names$)
                .expectSubscription()
                .expectNext("Blenders", "Old", "Johnnie", "Pride", "Monk", "Walker")
                .verifyComplete();
    }

    @Test
    @DisplayName("2. 1~100 까지의 자연수 중 짝수만 출력하는 로직 검증")
    public void filterEvenNumber() {
        Flux<Integer> numbers = Flux.range(1, 100)
                .filter(num -> num % 2 == 0);

        ArrayList<Integer> verifyList = new ArrayList<>();
        for (int i = 2; i <= 100; i += 2) {
            verifyList.add(i);
        }

        StepVerifier.create(numbers)
                .expectSubscription()
                .expectNextSequence(verifyList)
                .verifyComplete();
    }

    @Test
    @DisplayName("3. “hello”, “there” 를 순차적으로 publish하여 순서대로 나오는지 검증")
    public void publishHelloThere() {
        Flux<String> helloThere = Flux.just("hello", "there")
                .delayElements(Duration.ofMillis(100))
                .log();

        StepVerifier.create(helloThere)
                .expectSubscription()
                .expectNext("hello")
                .expectNext("there")
                .verifyComplete();
    }

    @Test
    @DisplayName("4. 아래와 같은 객체가 전달될 때 “JOHN”, “JACK” 등 이름이 대문자로 변환되어 출력되는 로직 검증")
    public void mapUpperCase() {
        Person John = new Person("John", "[john@gmail.com](mailto:john@gmail.com)", "12345678");
        Person Jack = new Person("Jack", "[john@gmail.com](mailto:john@gmail.com)", "12345678");

        Flux<Person> people = Flux.just(John, Jack)
                .map(Person::toUpper)
                .log();

        StepVerifier.create(people)
                .expectSubscription()
                .expectNextMatches(person -> person.getName().equals("JOHN"))
                .expectNextMatches(person -> person.getName().equals("JACK"))
                .verifyComplete();
    }

    @Test
    @DisplayName("5. [\"Blenders\", \"Old\", \"Johnnie\"] 와 \"[Pride\", \"Monk\", \"Walker”]를 압축하여 스트림으로 처리 검증")
    public void zipFlux() {
        Flux<String> firstFlux = Flux.just("Blenders", "Old", "Johnnie")
                .delayElements(Duration.ofMillis(100));
        Flux<String> secondFlux = Flux.just("Pride", "Monk", "Walker")
                .delayElements(Duration.ofMillis(100));

        Flux<String> zippedFlux = Flux.zip(firstFlux, secondFlux, (f, s) -> f + " " + s)
                .log();

        StepVerifier.create(zippedFlux)
                .expectSubscription()
                .expectNext("Blenders Pride")
                .expectNext("Old Monk")
                .expectNext("Johnnie Walker")
                .verifyComplete();
    }

    @Test
    @DisplayName("6. [\"google\", \"abc\", \"fb\", \"stackoverflow\"] 의 문자열 중 5자 이상 되는 문자열만 대문자로 비동기로 치환하여 1번 반복하는 스트림으로 처리하는 로직 검증")
    public void flatMapRepeat() {
        Flux<String> flatMapRepeat = Flux.just("google", "abc", "fb", "stackoverflow")
                .filter(s -> s.length() > 4)
                .flatMap(s -> Mono.just(s.toUpperCase(Locale.ROOT)))
                .repeat(1)
                .log();

        StepVerifier.create(flatMapRepeat)
                .expectSubscription()
                .expectNext("GOOGLE", "STACKOVERFLOW", "GOOGLE", "STACKOVERFLOW")
                .verifyComplete();
    }
}
