package net.viralpatel.springbootwebfluxdynamodb.customer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.Map;
import java.util.UUID;

@Repository
public class CustomerRepository {

    private DynamoDbAsyncClient dynamoDbAsyncClient;
    private String customerTable;

    public CustomerRepository(DynamoDbAsyncClient dynamoDbAsyncClient,
                              @Value("${application.dynamodb.customer_table}") String customerTable) {
        this.dynamoDbAsyncClient = dynamoDbAsyncClient;
        this.customerTable = customerTable;
    }

    public Flux<Customer> listCustomers() {

        ScanRequest scanRequest = ScanRequest.builder()
                .tableName(customerTable)
                .build();

        return Mono.fromCompletionStage(dynamoDbAsyncClient.scan(scanRequest))
                .map(scanResponse -> scanResponse.items())
                .map(CustomerMapper::fromList)
                .flatMapMany(Flux::fromIterable);
    }

    public Mono<Customer> createCustomer(Customer customer) {

        customer.setId(UUID.randomUUID().toString());

        PutItemRequest putItemRequest = PutItemRequest.builder()
                .tableName(customerTable)
                .item(CustomerMapper.toMap(customer))
                .build();

        return Mono.fromCompletionStage(dynamoDbAsyncClient.putItem(putItemRequest))
                .map(putItemResponse -> putItemResponse.attributes())
                .map(attributeValueMap -> customer);
    }

    public Mono<String> deleteCustomer(String customerId) {
        DeleteItemRequest deleteItemRequest = DeleteItemRequest.builder()
                .tableName(customerTable)
                .key(Map.of("customerId", AttributeValue.builder().s(customerId).build()))
                .build();

        return Mono.fromCompletionStage(dynamoDbAsyncClient.deleteItem(deleteItemRequest))
                .map(deleteItemResponse -> deleteItemResponse.attributes())
                .map(attributeValueMap -> customerId);
    }

    public Mono<Customer> getCustomer(String customerId) {
        GetItemRequest getItemRequest = GetItemRequest.builder()
                .tableName(customerTable)
                .key(Map.of("customerId", AttributeValue.builder().s(customerId).build()))
                .build();

        return Mono.fromCompletionStage(dynamoDbAsyncClient.getItem(getItemRequest))
                .map(getItemResponse -> getItemResponse.item())
                .map(CustomerMapper::fromMap);
    }

    public Mono<String> updateCustomer(String customerId, Customer customer) {

        customer.setId(customerId);
        PutItemRequest putItemRequest = PutItemRequest.builder()
                .tableName(customerTable)
                .item(CustomerMapper.toMap(customer))
                .build();

        return Mono.fromCompletionStage(dynamoDbAsyncClient.putItem(putItemRequest))
                .map(updateItemResponse -> customerId);
    }
}
