 package com.fluxandmono.playground;

import java.time.Duration;

import org.junit.jupiter.api.Test;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FluxAndMonoCombineTest {

	
	@Test
	public void combineUsingMerge() {

		Flux<String> flux1 = Flux.just("A", "B", "C");
		Flux<String> flux2 = Flux.just("D", "E", "F");

		Flux<String> mergeFlux = Flux.merge(flux1, flux2);
		
		StepVerifier.create(mergeFlux)
					.expectSubscription()//merge uses subscribe. Hence we need to test it 
					.expectNext("A", "B", "C","D", "E", "F")
					.verifyComplete();
		

	}
	
	/**
	 * delay with merge does not maintain order
	 * Faster than concat
	 */
	@Test
	public void combineUsingMerge_withDelay() {

		Flux<String> flux1 = Flux.just("A", "B", "C").delayElements(Duration.ofSeconds(1));
		Flux<String> flux2 = Flux.just("D", "E", "F").delayElements(Duration.ofSeconds(1));

		//Run in parallel
		//Does not maintain order
		Flux<String> mergeFlux = Flux.merge(flux1, flux2);
		
		StepVerifier.create(mergeFlux.log())
					.expectSubscription()//merge uses subscribe. Hence we need to test it 
					.expectNextCount(6)
//					.expectNext("A", "B", "C","D", "E", "F")
					.verifyComplete();
		

	}
	
	@Test
	public void combineUsingConcat() {

		Flux<String> flux1 = Flux.just("A", "B", "C");
		Flux<String> flux2 = Flux.just("D", "E", "F");

		Flux<String> mergeFlux = Flux.concat(flux1, flux2);
		
		StepVerifier.create(mergeFlux)
					.expectSubscription()//merge uses subscribe. Hence we need to test it 
					.expectNext("A", "B", "C","D", "E", "F")
					.verifyComplete();
		

	}
	
	/**
	 * delay with concat maintains order
	 * Slower than merge
	 */
	@Test
	public void combineUsingConcat_withDelay() {

		Flux<String> flux1 = Flux.just("A", "B", "C").delayElements(Duration.ofSeconds(1));
		Flux<String> flux2 = Flux.just("D", "E", "F").delayElements(Duration.ofSeconds(1));

		//Run in parallel
		//Maintain order
		Flux<String> mergeFlux = Flux.concat(flux1, flux2);
		
		StepVerifier.create(mergeFlux.log())
					.expectSubscription()//merge uses subscribe. Hence we need to test it 
					.expectNext("A", "B", "C","D", "E", "F")
					.verifyComplete();
		

	}
	
	
	
	@Test
	public void combineUsingZip() {

		Flux<String> flux1 = Flux.just("A", "B", "C");
		Flux<String> flux2 = Flux.just("D", "E", "F");

		Flux<String> mergeFlux = Flux.zip(flux1, flux2, (d1,d2)-> d1.concat(d2));
		
		StepVerifier.create(mergeFlux.log())
					.expectSubscription()//merge uses subscribe. Hence we need to test it 
					.expectNext("AD", "BE", "CF")
					.verifyComplete();
		

	}
}
