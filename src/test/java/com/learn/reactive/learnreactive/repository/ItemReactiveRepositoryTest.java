package com.learn.reactive.learnreactive.repository;

import com.learn.reactive.learnreactive.document.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

@DataMongoTest
@RunWith(SpringRunner.class)
@DirtiesContext
public class ItemReactiveRepositoryTest {

    @Autowired
    ItemReactiveRepository itemRepo;

    List<Item> itemList = Arrays.asList(new Item(null, "Samsung Tv", 400.0),
            new Item(null, "LG Tv", 420.0),
            new Item(null, "Nokia Tv", 299.99),
            new Item("oneplus", "Oneplus Tv", 149.9));

    @BeforeEach
    public void setup(){
        itemRepo.deleteAll()
                .thenMany(Flux.fromIterable(itemList))
                .flatMap(itemRepo::save)
                .doOnNext(item-> System.out.println("Item is ::" +item))
                .blockLast(); // Never use this in actual code
    }

    @Test
    public void getAllItems(){
        StepVerifier.create(itemRepo.findAll())
                    .expectSubscription()
                    .expectNextCount(4)
                    .verifyComplete();
    }


    @Test
    public void getItemById(){

        Item item =  new Item("oneplus", "Oneplus Tv", 149.9);
        StepVerifier.create(itemRepo.findById("oneplus"))
                .expectSubscription()
                .expectNext(item)
                .verifyComplete();
    }

    @Test
    public void getItemById_MatchWithDescription(){

        Item item =  new Item("oneplus", "Oneplus Tv", 149.9);
        StepVerifier.create(itemRepo.findById("oneplus"))
                .expectSubscription()
                .expectNextMatches(itm -> itm.getDescription().equals(item.getDescription()))
                .verifyComplete();
    }

    @Test
    public void getItemByDescription(){

        StepVerifier.create(itemRepo.findByDescription("Oneplus Tv").log())
                .expectSubscription()
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void saveItem(){

       Item item = new Item("google_mini", "Google Home Mini", 20.99);
       Mono<Item> mono = itemRepo.save(item);
       StepVerifier.create(mono.log())
                    .expectSubscription()
                    .expectNextMatches(savedItem -> savedItem.getId().equals("google_mini"))
                    .verifyComplete();
    }

    @Test
    public void updateItem(){

        Double newPrice = 520.99;
        Mono<Item> updatedItem = itemRepo.findByDescription("LG Tv")
                .flatMap(item -> {
                    item.setPrice(newPrice);
                    return itemRepo.save(item);
                });

        StepVerifier.create(updatedItem.log())
                    .expectSubscription()
                    .expectNextMatches(item -> item.getPrice().equals(newPrice))
                    .verifyComplete();
    }


    @Test
    public void deleteItem(){

        Double newPrice = 520.99;

        Mono<Void> deletedItem = itemRepo.findById("oneplus")
                .flatMap(item -> {
                    return itemRepo.deleteById(item.getId());
                });


        StepVerifier.create(deletedItem.log())
                .expectSubscription()
                .verifyComplete();

        //Verify delete

        StepVerifier.create(itemRepo.findById("oneplus"))
                    .expectSubscription()
                    .expectNextCount(0)
                    .verifyComplete();
    }

    @Test
    public void deleteItemByDescription(){

        Double newPrice = 520.99;

        Mono<Void> deletedItem = itemRepo.findByDescription("LG Tv")
                .flatMap(item -> {
                    return itemRepo.deleteById(item.getId());
                });


        StepVerifier.create(deletedItem.log())
                .expectSubscription()
                .verifyComplete();

        //Verify delete

        StepVerifier.create(itemRepo.findByDescription("LG Tv"))
                .expectSubscription()
                .expectNextCount(0)
                .verifyComplete();
    }
}
