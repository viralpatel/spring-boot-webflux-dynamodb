package net.viralpatel.springbootwebfluxdynamodb.customer;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    private CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Mono<ServerResponse> listCustomers(ServerRequest serverRequest) {
        return customerRepository.listCustomers()
                .collect(Collectors.toList())
                .flatMap(customers -> ServerResponse.ok().body(BodyInserters.fromValue(customers)));
    }

    public Mono<ServerResponse> createCustomer(ServerRequest serverRequest) {

        return serverRequest.bodyToMono(Customer.class)
                .flatMap(customer -> customerRepository.createCustomer(customer))
                .flatMap(customer -> ServerResponse.created(URI.create("/customers/" + customer.getId())).build());
    }

    public Mono<ServerResponse> deleteCustomer(ServerRequest serverRequest) {
        String customerId = serverRequest.pathVariable("customerId");

        return customerRepository.deleteCustomer(customerId)
                .flatMap(customer -> ServerResponse.ok().build());
    }

    public Mono<ServerResponse> getCustomer(ServerRequest serverRequest) {
        String customerId = serverRequest.pathVariable("customerId");

        return customerRepository.getCustomer(customerId)
                .flatMap(customer -> ServerResponse.ok().body(BodyInserters.fromValue(customer)));
    }

    public Mono<ServerResponse> updateCustomer(ServerRequest serverRequest) {
        String customerId = serverRequest.pathVariable("customerId");

        return serverRequest.bodyToMono(Customer.class)
                .flatMap(customer -> customerRepository.updateCustomer(customerId, customer))
                .flatMap(customer -> ServerResponse.ok().build());
    }
}
