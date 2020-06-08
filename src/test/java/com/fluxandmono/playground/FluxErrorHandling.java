package com.fluxandmono.playground;

import java.time.Duration;

import org.junit.jupiter.api.Test;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.util.retry.Retry;

public class FluxErrorHandling {
	
	

	@Test
	public void fluxErrorHandling_OnErrorResume() {

		Flux<String> flux = Flux.just("A", "B", "C")
								 .concatWith(Flux.error(new RuntimeException("Custom error")))
								 .concatWith(Flux.just("D"))
								 .onErrorResume(exception -> {
									 System.out.println(exception.getMessage());
									 return Flux.just("default", "default1");
								 })
				;

		StepVerifier.create(flux.log())
					.expectSubscription()//merge uses subscribe. Hence we need to test it 
					.expectNext("A", "B", "C")
					.expectNext("default", "default1")
					.verifyComplete();

	}

	
	
	@Test
	public void fluxErrorHandling_OnErrorReturn() {

		Flux<String> flux = Flux.just("A", "B", "C")
								 .concatWith(Flux.error(new RuntimeException("Custom error")))
								 .concatWith(Flux.just("D"))
								 .onErrorReturn("default");

		StepVerifier.create(flux.log())
					.expectSubscription()//merge uses subscribe. Hence we need to test it 
					.expectNext("A", "B", "C")
					.expectNext("default")
					.verifyComplete();

	}
	
	
	@Test
	public void fluxErrorHandling_OnErrorMap() {

		Flux<String> flux = Flux.just("A", "B", "C")
								 .concatWith(Flux.error(new RuntimeException("Custom error")))
								 .concatWith(Flux.just("D"))
								 .onErrorMap(ex -> new CustomException(ex));

		StepVerifier.create(flux.log())
					.expectSubscription()//merge uses subscribe. Hence we need to test it 
					.expectNext("A", "B", "C")
					.expectError(CustomException.class)
					.verify();

	}
	
	@Test
	public void fluxErrorHandling_OnErrorMap_withRetry() {

		Flux<String> flux = Flux.just("A", "B", "C")
								 .concatWith(Flux.error(new RuntimeException("Custom error")))
								 .concatWith(Flux.just("D"))
								 .onErrorMap(ex -> new CustomException(ex))
								 .retry(2);

		StepVerifier.create(flux.log())
					.expectSubscription()//merge uses subscribe. Hence we need to test it 
					.expectNext("A", "B", "C")
					.expectNext("A", "B", "C")//retry1
					.expectNext("A", "B", "C")//retry2
					.expectError(CustomException.class)
					.verify();

	}
	
	@Test
	public void fluxErrorHandling_OnErrorMap_withRetryDelay() {

		Flux<String> flux = Flux.just("A", "B", "C")
								 .concatWith(Flux.error(new RuntimeException("Custom error")))
								 .concatWith(Flux.just("D"))
								 .onErrorMap(ex -> new CustomException(ex))
								 .retryWhen(Retry.backoff(2, Duration.ofSeconds(5)));

		StepVerifier.create(flux.log())
					.expectSubscription()//merge uses subscribe. Hence we need to test it 
					.expectNext("A", "B", "C")
					.expectNext("A", "B", "C")//retry1
					.expectNext("A", "B", "C")//retry2
					.expectError(IllegalStateException.class)
					.verify();

	}

}
