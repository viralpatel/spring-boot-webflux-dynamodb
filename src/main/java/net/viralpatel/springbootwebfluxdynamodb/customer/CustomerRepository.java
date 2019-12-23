package net.viralpatel.springbootwebfluxdynamodb.customer;

import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class CustomerRepository {

    public Flux<Customer> listCustomers() {
        return null;
    }

    public Mono<Customer> createCustomer(Customer customer) {
        return null;
    }

    public Mono<Customer> deleteCustomer(String customerId) {
        return null;
    }
}
