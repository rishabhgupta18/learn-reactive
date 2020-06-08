package com.fluxandmono.playground;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class FluxAndMonoFactoryTest {

	private List<String> names = Arrays.asList("MVC", "Boot", "Reactive");

	@Test
	public void fluxUsingIterable() {

		Flux<String> fluxString = Flux.fromIterable(names);

		StepVerifier.create(fluxString).expectNext("MVC", "Boot", "Reactive").verifyComplete();

	}

	@Test
	public void fluxUsingArray() {

		String[] s = names.toArray(new String[names.size()]);
		Flux<String> fluxString = Flux.fromArray(s);

		StepVerifier.create(fluxString).expectNext("MVC", "Boot", "Reactive").verifyComplete();

	}

	@Test
	public void fluxUsingStream() {

		Flux<String> fluxString = Flux.fromStream(names.stream());

		StepVerifier.create(fluxString).expectNext("MVC", "Boot", "Reactive").verifyComplete();

	}

	@Test
	public void monoUsingJustOrEmpty_Empty() {

		Mono<String> mono = Mono.justOrEmpty(null);

		StepVerifier.create(mono).verifyComplete();

	}

	@Test
	public void monoUsingJustOrEmpty_WithData() {

		Mono<String> mono = Mono.justOrEmpty("MVC");

		StepVerifier.create(mono)
					.expectNext("MVC")
					.verifyComplete();

	}
	
	@Test
	public void monoUsingSupplier() {

		Mono<String> mono = Mono.fromSupplier(()->"MVC").log();

		StepVerifier.create(mono)
					.expectNext("MVC")
					.verifyComplete();

	}
	
	@Test
	public void fluxUsingRange() {

		Flux<Integer> fluxRange = Flux.range(1, 5).log();

		StepVerifier.create(fluxRange)
					.expectNext(1,2,3,4,5)
					.verifyComplete();

	}
	
	@Test
	public void fluxUsingNameRange() {

		Flux<String> fluxRange = Flux.fromIterable(names)
									 .take(2)
									 .log();

		StepVerifier.create(fluxRange)
					.expectNext("MVC", "Boot")
					.verifyComplete();

	}
	
	

}
