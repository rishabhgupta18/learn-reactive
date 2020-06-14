package com.learn.reactive.learnreactive.initialize;

import com.learn.reactive.learnreactive.document.Item;
import com.learn.reactive.learnreactive.repository.ItemReactiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;

@Component
@Profile("!test")
public class ItemDataInitializer implements CommandLineRunner {

    @Autowired
    ItemReactiveRepository itemReactiveRepository;

    @Override
    public void run(String... args) throws Exception {
        initialDataSetup();
    }

    public static List<Item> data(){
        return Arrays.asList(new Item(null, "Samsung Tv", 400.0),
                new Item(null, "LG Tv", 420.0),
                new Item(null, "Nokia Tv", 299.99),
                new Item("Oneplus", "Oneplus Tv", 149.9));
    }

    private void initialDataSetup() {
        itemReactiveRepository.deleteAll()
                    .thenMany(Flux.fromIterable(data()))
                    .flatMap(itemReactiveRepository::save)
                    .thenMany(itemReactiveRepository.findAll())
                    .subscribe(item->{
                        System.out.println("Item inserted ::: " + item);
                    });

    }
}
