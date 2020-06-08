package com.learn.reactive.learnreactive.controller;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@RunWith(SpringRunner.class)
@WebFluxTest
@AutoConfigureWebTestClient(timeout = "10000")
public class FluxAndMonoControllerTest {
	
	@Autowired
	WebTestClient webTestClient;
	
	
	@Test
	public void flux_approach1() {
		
		Flux<Integer> fluxInteger = webTestClient.get().uri("/flux")
					 .accept(MediaType.APPLICATION_JSON)
					 .exchange()
					 .expectStatus().isOk()
					 .returnResult(Integer.class)
					 .getResponseBody();
		
		StepVerifier.create(fluxInteger.log())
					.expectSubscription()
					.expectNext(1,2,3,4,5)
					.verifyComplete();
					 
		
	}
	
	@Test
	public void flux_approach2() {
		
		webTestClient.get().uri("/flux")
					 .accept(MediaType.APPLICATION_JSON)
					 .exchange()
					 .expectStatus().isOk()
					 .expectHeader().contentType(MediaType.APPLICATION_JSON)
					 .expectBodyList(Integer.class)
					 .hasSize(5);
		
	}

}
