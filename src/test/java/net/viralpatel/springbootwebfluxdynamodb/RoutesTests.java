package net.viralpatel.springbootwebfluxdynamodb;

import net.viralpatel.springbootwebfluxdynamodb.customer.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.concurrent.CompletableFuture;

import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@ContextConfiguration(initializers = RoutesTests.DynamoDBInitializer.class)
public class RoutesTests {

    private static final int DYNAMODB_PORT = 8000;

    @Autowired
    DynamoDbAsyncClient dynamoDbAsyncClient;

    @Container
    public static GenericContainer dynamodb =
            new GenericContainer<>("amazon/dynamodb-local:latest")
                .withExposedPorts(DYNAMODB_PORT);

    public static class DynamoDBInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext ctx) {

            TestPropertyValues.of(
                    String.format("application.dynamodb.endpoint: http://%s:%s",
                        dynamodb.getContainerIpAddress(), dynamodb.getMappedPort(DYNAMODB_PORT)))
                    .applyTo(ctx);
        }
    }

    @Autowired
    public WebTestClient webTestClient;

    @Test
    public void shouldCreateCustomerWhenCustomerAPIInvoked() {

        // Create customers table in DynamoDB
        CompletableFuture<CreateTableResponse> createTable = dynamoDbAsyncClient.createTable(CreateTableRequest.builder()
                .tableName("customers")
                .attributeDefinitions(AttributeDefinition.builder().attributeName("customerId").attributeType("S").build())
                .keySchema(KeySchemaElement.builder().attributeName("customerId").keyType(KeyType.HASH).build())
                .provisionedThroughput(ProvisionedThroughput.builder().readCapacityUnits(5l).writeCapacityUnits(5l).build())
                .build());

        Mono.fromFuture(createTable).block();

        Customer customer = new Customer();
        customer.setName("John");
        customer.setCity("Sydney");
        customer.setEmail("john@example.com");

        webTestClient
                .post()
                .uri("/customers")
                .bodyValue(customer)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectHeader().value("Location", is(not(blankOrNullString())));
    }
}
