package com.fluxandmono.playground;

import org.junit.jupiter.api.Test;

import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FluxAndMonoBackPressureTest {

	
	@Test
	public void backPressureTest() {
		
		Flux<Integer> finiteFlux = Flux.range(1, 10)
									   .log();
		
		
		StepVerifier.create(finiteFlux)
					.expectSubscription()
					.thenRequest(1)
					.expectNext(1)
					.thenRequest(1)
					.expectNext(2)
					.thenCancel()
					.verify();
		
	}
	
	@Test
	public void backPressureTest_UsingSubscription() {
		
		Flux<Integer> finiteFlux = Flux.range(1, 10)
									   .log();
		
		
		finiteFlux.subscribe(
				  ele->System.out.println("Element is : "+ele),
				  exception -> System.out.println("Exception is : "+exception),
				  ()->System.out.println("Process completed Successfully"),
				  subscriptionConsumer->subscriptionConsumer.request(2)
				);
		
	}
	
	@Test
	public void backPressureTest_UsingSubscription_Cancel() {
		
		Flux<Integer> finiteFlux = Flux.range(1, 10)
									   .log();
		
		
		finiteFlux.subscribe(
				  ele->System.out.println("Element is : "+ele),
				  exception -> System.out.println("Exception is : "+exception),
				  ()->System.out.println("Process completed Successfully"),
				  subscriptionConsumer->subscriptionConsumer.cancel()
				);
		
		
		
	}
	
	@Test
	public void customizedBackPressure() {
		
		Flux<Integer> finiteFlux = Flux.range(1, 10)
									   .log();
		
		
		finiteFlux.subscribe(new BaseSubscriber<Integer>() {
			@Override
			protected void hookOnNext(Integer value) {
				request(1);
				System.out.println("Value is : "+value);
				if(value == 5)
					cancel();
			}
		});
		
		
		
	}
	
}
