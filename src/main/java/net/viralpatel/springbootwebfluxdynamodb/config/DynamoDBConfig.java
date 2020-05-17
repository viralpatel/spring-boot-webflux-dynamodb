package net.viralpatel.springbootwebfluxdynamodb.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;

import java.net.URI;

@Configuration
public class DynamoDBConfig {

    @Bean
    public DynamoDbAsyncClient dynamoDbAsyncClient(
            @Value("${application.region}") String awsRegion,
            @Value("${application.dynamodb.endpoint}") String dynamoDBEndpoint) {
        return DynamoDbAsyncClient.builder()
                .region(Region.of(awsRegion))
                .endpointOverride(URI.create(dynamoDBEndpoint))
                .credentialsProvider(DefaultCredentialsProvider.builder().build())
                .build();
    }
}
