package com.learn.reactive.learnreactive.router;

import com.learn.reactive.learnreactive.constants.ItemConstants;
import com.learn.reactive.learnreactive.handler.ItemsHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class ItemsRouter {

    @Bean
    public RouterFunction<ServerResponse> itemsRoute(ItemsHandler itemsHandler){
        return RouterFunctions
                .route(
                RequestPredicates.GET(ItemConstants.ITEM_ENDPOINT_FUNCTIONAL)
                .and(RequestPredicates.accept(MediaType.APPLICATION_JSON))
                , itemsHandler::getAllItems
        );
    }

    @Bean
    public RouterFunction<ServerResponse> exceptionRoute(ItemsHandler itemsHandler){
        return RouterFunctions
                .route(
                        RequestPredicates.GET("/functional/exception")
                                .and(RequestPredicates.accept(MediaType.APPLICATION_JSON))
                        , itemsHandler::exception
                );
    }
}
