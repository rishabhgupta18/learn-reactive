package com.fluxandmono.playground;

import java.time.Duration;

import org.junit.jupiter.api.Test;

import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;

public class ColdAndHotPublisherTest {

	/**
	 * cold - emits the value from the beginning
	 * for every subscribe, it start emitting the value from beginning.
	 * @throws InterruptedException 
	 */
	@Test
	public void coldPublisherTest() throws InterruptedException {

		Flux<String> fluxString = Flux.just("A","B","C","D","E","F")
									  .delayElements(Duration.ofSeconds(1));
		fluxString.subscribe(element -> System.out.println("Subscriber 1 :: "+ element));
		Thread.sleep(2000);
		
		fluxString.subscribe(element -> System.out.println("Subscriber 2 :: "+ element));
		Thread.sleep(4000);
	}
	
	/**
	 * hot - doesn't emits the value from the beginning
	 * for every subscribe, it start emitting the value from last emitted value.
	 */
	@Test
	public void hotPublisherTest() throws InterruptedException {

		Flux<String> fluxString = Flux.just("A","B","C","D","E","F")
									  .delayElements(Duration.ofSeconds(1));
		
		ConnectableFlux<String> connectableFlux = fluxString.publish();
		connectableFlux.connect();
		
		connectableFlux.subscribe(element -> System.out.println("Subscriber 1 :: "+ element));
		Thread.sleep(2000);
		
		connectableFlux.subscribe(element -> System.out.println("Subscriber 2 :: "+ element));
		Thread.sleep(4000);
		
		
	}

}
