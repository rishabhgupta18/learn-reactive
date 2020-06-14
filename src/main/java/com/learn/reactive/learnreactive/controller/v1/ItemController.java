package com.learn.reactive.learnreactive.controller.v1;

import com.learn.reactive.learnreactive.document.Item;
import com.learn.reactive.learnreactive.repository.ItemReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.learn.reactive.learnreactive.constants.ItemConstants.ITEM_ENDPOINT;

@RestController
@Slf4j
public class ItemController {

    @Autowired
    ItemReactiveRepository itemReactiveRepository;

    @GetMapping(ITEM_ENDPOINT)
    public Flux<Item> getAllItems(){
        return itemReactiveRepository.findAll();
    }

    @GetMapping(ITEM_ENDPOINT+"/{id}")
    public Mono<ResponseEntity<Item>> getItem(@PathVariable String id){
        return itemReactiveRepository.findById(id)
                .map(item-> new ResponseEntity<>(item, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND))
                ;
    }

    @PostMapping(ITEM_ENDPOINT)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Item> createItem(@RequestBody Item item){
        return itemReactiveRepository.save(item);
    }

    @DeleteMapping(ITEM_ENDPOINT+"/{id}")
    public Mono<Void> deleteItem(@PathVariable String id){
        return itemReactiveRepository.deleteById(id);
    }


    @PutMapping(ITEM_ENDPOINT+"/{id}")
    public Mono<ResponseEntity<Item>> updateItem(@PathVariable String id, @RequestBody Item updatedItem){
        return itemReactiveRepository.findById(id)
                .flatMap(currentItem -> {
                    currentItem.setPrice(updatedItem.getPrice());
                    currentItem.setDescription(updatedItem.getDescription());
                    currentItem.setPrice(updatedItem.getPrice());
                    return itemReactiveRepository.save(currentItem);
                })
                .map(item -> new ResponseEntity<>(item, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND))
                            ;
    }
}
