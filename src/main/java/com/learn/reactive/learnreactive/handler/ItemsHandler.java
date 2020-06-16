package com.learn.reactive.learnreactive.handler;

import com.learn.reactive.learnreactive.document.Item;
import com.learn.reactive.learnreactive.repository.ItemReactiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class ItemsHandler {

    @Autowired
    ItemReactiveRepository itemReactiveRepository;

    public Mono<ServerResponse> getAllItems(ServerRequest serverRequest){
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(itemReactiveRepository.findAll(), Item.class)
                ;
    }

    public Mono<ServerResponse> exception(ServerRequest serverRequest) {
        throw new RuntimeException("Custom Exception Occurred");
    }
}
