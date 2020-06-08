package com.fluxandmono.playground;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

public class FluxAndMonoTransformTest {

	private List<String> names = Arrays.asList("MVC", "Boot", "Reactive");

	@Test
	public void transformUsingMap() {

		Flux<String> namesFlux = Flux.fromIterable(names).map(s -> s.toUpperCase()).log();
		StepVerifier.create(namesFlux).expectNext("MVC", "BOOT", "REACTIVE").verifyComplete();

	}

	@Test
	public void transformUsingMap_WithRepeat() {

		Flux<String> namesFlux = Flux.fromIterable(names).map(s -> s.toUpperCase()).repeat(1).log();
		StepVerifier.create(namesFlux).expectNext("MVC", "BOOT", "REACTIVE", "MVC", "BOOT", "REACTIVE")
				.verifyComplete();

	}

	@Test
	public void transformUsingFlatMap_Syncronous() {

		Flux<String> namesFlux = Flux.fromIterable(Arrays.asList("A", "B", "C", "D"))
				.flatMap(s -> Flux.fromIterable(someDBMethodReturningListFromId(s)))
				.log();
		StepVerifier.create(namesFlux).expectNextCount(8).verifyComplete();

	}

	@Test
	public void transformUsingFlatMap_AsyncronousWithoutWindow() {

		Flux<String> namesFlux = Flux.fromIterable(Arrays.asList("A", "B", "C", "D"))
				.map(this::someDBMethodReturningListFromId)
				.subscribeOn(Schedulers.parallel())
				.flatMap(Flux::fromIterable).log();

		StepVerifier.create(namesFlux).expectNextCount(8).verifyComplete();

	}

	@Test
	public void transformUsingFlatMap_AsyncronousWithWindow() {

		Flux<String> namesFlux = Flux.fromIterable(Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H")).window(4)
				.flatMap(window -> 
						window.map(this::someDBMethodReturningListFromId).subscribeOn(Schedulers.parallel())
						.flatMap(Flux::fromIterable))
				.log();

		StepVerifier.create(namesFlux).expectNextCount(16).verifyComplete();

	}
	
	
	@Test
	public void transformUsingFlatMap_AsyncronousWithWindow_MaintainOrder() {

		Flux<String> namesFlux = Flux.fromIterable(Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H"))
				.window(2)
				.flatMapSequential(window -> 
						window.map(this::someDBMethodReturningListFromId)
						.subscribeOn(Schedulers.parallel())
						.flatMap(Flux::fromIterable))
				.log();

		StepVerifier.create(namesFlux).expectNextCount(16).verifyComplete();

	}

	private List<String> someDBMethodReturningListFromId(String id) {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return Arrays.asList(id, id + " fetched successfully");
	}

}
