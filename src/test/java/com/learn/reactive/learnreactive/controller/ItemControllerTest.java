package com.learn.reactive.learnreactive.controller;

import com.learn.reactive.learnreactive.constants.ItemConstants;
import com.learn.reactive.learnreactive.document.Item;
import com.learn.reactive.learnreactive.repository.ItemReactiveRepository;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
@DirtiesContext
@AutoConfigureWebTestClient
@ActiveProfiles("test")
public class ItemControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    ItemReactiveRepository itemReactiveRepository;


    List<Item> data = Arrays.asList(new Item(null, "Samsung Tv", 400.0),
            new Item(null, "LG Tv", 420.0),
            new Item(null, "Nokia Tv", 299.99),
            new Item("Oneplus", "Oneplus Tv", 149.9));

    @BeforeEach
    public void setup(){
        itemReactiveRepository.deleteAll()
                .thenMany(Flux.fromIterable(data))
                .flatMap(itemReactiveRepository::save)
                .doOnNext(item -> {
                    System.out.println("Item is :: "+item);
                }).blockLast();

    }

    @Test
    public void getAllItems(){
        webTestClient.get().uri(ItemConstants.ITEM_ENDPOINT)
                    .exchange()
                    .expectStatus().isOk()
                    .expectHeader().contentType(MediaType.APPLICATION_JSON)
                    .expectBodyList(Item.class)
                    .hasSize(4);
    }

    @Test
    public void getAllItems_approach2(){
        webTestClient.get().uri(ItemConstants.ITEM_ENDPOINT)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Item.class)
                .hasSize(4)
                .consumeWith(response -> {
                    response.getResponseBody()
                            .forEach(item->{
                                Assert.assertTrue(item.getId()!=null);
                            });
                });
    }

    @Test
    public void getAllItems_approach3(){
        Flux<Item> flux = webTestClient.get().uri(ItemConstants.ITEM_ENDPOINT)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .returnResult(Item.class)
                .getResponseBody()
                ;
        StepVerifier.create(flux.log("Item events"))
                .expectSubscription()
                .expectNextCount(4)
                .verifyComplete();
    }

    @Test
    public void getItemById(){
        webTestClient.get().uri(ItemConstants.ITEM_ENDPOINT.concat("/{id}"), "Oneplus")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.price", 149.9)
                ;
    }

    @Test
    public void getItemById_NotFound(){
        webTestClient.get().uri(ItemConstants.ITEM_ENDPOINT.concat("/{id}"), "XYZ")
                .exchange()
                .expectStatus().isNotFound()
        ;
    }
    @Test
    public void createItem(){
        Item item = new Item(null, "Iphone X", 999.9);
        webTestClient.post().uri(ItemConstants.ITEM_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(item), Item.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.description").isEqualTo(item.getDescription())
                .jsonPath("$.price").isEqualTo(item.getPrice())
        ;
    }

    @Test
    public void deleteItem(){
        webTestClient.delete().uri(ItemConstants.ITEM_ENDPOINT.concat("/{id}"), "Oneplus")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Void.class)
        ;
    }

    @Test
    public void updateItem(){
        Item item = new Item(null, "Oneplus Tv", 999.9);
        webTestClient.put().uri(ItemConstants.ITEM_ENDPOINT.concat("/{id}"), "Oneplus")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(item), Item.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.price", item.getPrice())
        ;
    }

    @Test
    public void updateItem_NotFound(){
        Item item = new Item(null, "Oneplus Tv", 999.9);
        webTestClient.put().uri(ItemConstants.ITEM_ENDPOINT.concat("/{id}"), "sss")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(item), Item.class)
                .exchange()
                .expectStatus().isNotFound()
        ;
    }


}
