package com.learn.reactive.learnreactive.repository;

import com.learn.reactive.learnreactive.document.Item;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface ItemReactiveRepository extends ReactiveMongoRepository<Item, String> {

    public Mono<Item> findByDescription(String description);
}
