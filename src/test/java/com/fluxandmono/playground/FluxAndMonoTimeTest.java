package com.fluxandmono.playground;

import java.time.Duration;

import org.junit.jupiter.api.Test;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FluxAndMonoTimeTest {

	
	@Test
	public void fluxIntervalSequenceTest() throws InterruptedException {

		Flux<Long> flux = Flux.interval(Duration.ofMillis(200))
							  .take(5)
							  .log();
		
		StepVerifier.create(flux)
					.expectSubscription()
					.expectNext(0L, 1L, 2L, 3L, 4L)
					.verifyComplete();

	}
	

	@Test
	public void fluxIntervalSequenceTest_WithSomeOperation() throws InterruptedException {

		Flux<Integer> flux = Flux.interval(Duration.ofMillis(200))
							  .map(ele -> ele.intValue())
							  .take(5)
							  .log();
		
		StepVerifier.create(flux)
					.expectSubscription()
					.expectNext(0, 1, 2, 3, 4)
					.verifyComplete();

	}
	
	@Test
	public void fluxIntervalSequenceTest_WithSomeOperationAndDelay() throws InterruptedException {

		Flux<Integer> flux = Flux.interval(Duration.ofSeconds(1))
							  .map(ele -> ele.intValue())
							  .take(5)
							  .log();
		
		StepVerifier.create(flux)
					.expectSubscription()
					.expectNext(0, 1, 2, 3, 4)
					.verifyComplete();

	}
}
