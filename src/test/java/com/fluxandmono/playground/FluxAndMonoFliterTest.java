package com.fluxandmono.playground;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FluxAndMonoFliterTest {

	private List<String> names = Arrays.asList("MVC", "Boot", "Reactive");
	
	@Test
	public void filterTest() {
		
		Flux<String> fluxString = Flux.fromIterable(names)
								.filter(s -> s.startsWith("B"))	
								.log();
		
		StepVerifier.create(fluxString)
					.expectNext("Boot")
					.verifyComplete();
		
	}
}
