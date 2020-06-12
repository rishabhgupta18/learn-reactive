package com.learn.reactive.learnreactive.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
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
	
	@Test
	public void flux_approach3() {
		
		List<Integer> expectedIntegerList = Arrays.asList(1,2,3,4,5); 
		
		EntityExchangeResult<List<Integer>> entityResult = webTestClient.get().uri("/flux")
					 .accept(MediaType.APPLICATION_JSON)
					 .exchange()
					 .expectStatus().isOk()
					 .expectBodyList(Integer.class)
					 .returnResult();
		
		assertEquals(expectedIntegerList, entityResult.getResponseBody());
		
	}
	
	@Test
	public void flux_approach4() {
		
		List<Integer> expectedIntegerList = Arrays.asList(1,2,3,4,5); 
		
		webTestClient.get().uri("/flux")
					 .accept(MediaType.APPLICATION_JSON)
					 .exchange()
					 .expectStatus().isOk()
					 .expectBodyList(Integer.class)
					 .consumeWith(entityResult ->{
						 assertEquals(expectedIntegerList, entityResult.getResponseBody());
					 });
		
		
	}
	
	@Test
	public void fluxStream_approach1() {
		
		
		Flux<Long> longFlux = webTestClient.get().uri("/fluxStream")
					 .accept(MediaType.APPLICATION_STREAM_JSON)
					 .exchange()
					 .expectStatus().isOk()
					 .returnResult(Long.class)
					 .getResponseBody();
		
		StepVerifier.create(longFlux)
					.expectNext(0l)
					.expectNext(1l)
					.expectNext(2l)
					.thenCancel();
		
	}
	
	@Test
	public void mono_approach1() {
		
		Integer expected = new Integer(1);
		
		Integer response = webTestClient.get().uri("/mono")
							.exchange()
							.expectStatus().isOk()
							.expectBody(Integer.class)
							.returnResult()
							.getResponseBody();
		
		
		assertEquals(expected, response);
	}
	
	@Test
	public void mono_approach2() {
		
		Integer expected = new Integer(1);
		webTestClient.get().uri("/mono")
							.exchange()
							.expectStatus().isOk()
							.expectBody(Integer.class)
							.consumeWith(response->{
								assertEquals(expected, response.getResponseBody());
							});
		
		
	}

}


