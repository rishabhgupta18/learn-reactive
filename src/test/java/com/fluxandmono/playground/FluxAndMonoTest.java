package com.fluxandmono.playground;

import org.junit.jupiter.api.Test;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class FluxAndMonoTest {

	public void fluxTest() {

		Flux<String> fluxString = Flux.just("MVC", "Boot", "Reactive")
				.log();
		fluxString.subscribe(System.out::println, 
				e -> System.err.println(e),
				()->System.out.println("Process completed"));

	}
	
	@Test
	public void fluxTestElements_WithoutError() {
		System.out.println("Flux Test Without Error");
		Flux<String> fluxString = Flux.just("MVC", "Boot", "Reactive")
				.log();
		StepVerifier.create(fluxString)
					.expectNext("MVC")
					.expectNext("Boot")
					.expectNext("Reactive")
					.verifyComplete();
		

	}
	
	@Test
	public void fluxTestElements_WithError() {
		Flux<String> fluxString = Flux.just("MVC", "Boot", "Reactive")
									.concatWith(Flux.error(new RuntimeException("Custom error")))
									.log();
		StepVerifier.create(fluxString)
					.expectNext("MVC")
					.expectNext("Boot")
					.expectNext("Reactive")
					.expectError(RuntimeException.class)
					.verify();
		

	}
	
	
	@Test
	public void fluxTestElements_WithError_IncludingMessage() {
		Flux<String> fluxString = Flux.just("MVC", "Boot", "Reactive")
									.concatWith(Flux.error(new RuntimeException("Custom error")))
									.log();
		StepVerifier.create(fluxString)
					.expectNext("MVC")
					.expectNext("Boot")
					.expectNext("Reactive")
					.expectErrorMessage("Custom error")
					.verify();
		

	}
	
	
	@Test
	public void fluxTestElementsCounts_WithError() {
		Flux<String> fluxString = Flux.just("MVC", "Boot", "Reactive")
									.concatWith(Flux.error(new RuntimeException("Custom error")))
									.log();
		StepVerifier.create(fluxString)
					.expectNextCount(3)
					.expectErrorMessage("Custom error")
					.verify();
		

	}
	
	
	@Test
	public void monoTest_WithoutError() {
		Mono<String> monoString = Mono.just("MVC")
									.log();
		StepVerifier.create(monoString)
					.expectNext("MVC")
					.verifyComplete();
		

	}
	
	@Test
	public void monoTest_WithError() {
		Mono<Object> monoObj = Mono.error(new RuntimeException("Custom Error"))
									.log();
		StepVerifier.create(monoObj)
					.expectError(RuntimeException.class)
					.verify();
		

	}

}
