package com.learn.reactive.learnreactive.handler;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureWebTestClient
@DirtiesContext
public class SampleHandlerFunctionTest {

	@Autowired
	WebTestClient webTestClient;
	
	
	@Test
	public void flux() {
		
		Flux<Integer> fluxInteger = webTestClient.get().uri("/functional/flux")
					 .accept(MediaType.APPLICATION_JSON)
					 .exchange()
					 .expectStatus().isOk()
					 .returnResult(Integer.class)
					 .getResponseBody();
		
		StepVerifier.create(fluxInteger.log())
					.expectSubscription()
					.expectNext(1,2,3,4,5);
 					 
		
	}
	
	@Test
	public void mono() {
		
		Integer expected = new Integer(1);
		webTestClient.get().uri("/functional/mono")
							.exchange()
							.expectStatus().isOk()
							.expectBody(Integer.class)
							.consumeWith(response->{
								assertEquals(expected, response.getResponseBody());
							});
		
		
	}
}
