package com.fluxandmono.playground;

import java.time.Duration;

import org.junit.jupiter.api.Test;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.test.scheduler.VirtualTimeScheduler;

public class VirtualTimeTest {

	@Test
	public void testWithVirtual() {
		
		VirtualTimeScheduler.getOrSet();
		
		Flux<Long> longFlux = Flux.interval(Duration.ofSeconds(1)).take(3);
		
		StepVerifier.withVirtualTime(()-> longFlux.log())
					.expectSubscription()
					.thenAwait(Duration.ofSeconds(3))//Seconds * total Enteries
					.expectNext(0l,1l,2l)
					.verifyComplete();
		
	}
}
