package net.viralpatel.springbootwebfluxdynamodb.customer;

import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CustomerMapper {

    public static List<Customer> fromList(List<Map<String, AttributeValue>> items) {
        return items.stream()
                .map(CustomerMapper::fromMap)
                .collect(Collectors.toList());
    }

    public static Customer fromMap(Map<String, AttributeValue> attributeValueMap) {
        Customer customer = new Customer();
        customer.setId(attributeValueMap.get("customerId").s());
        customer.setName(attributeValueMap.get("name").s());
        customer.setEmail(attributeValueMap.get("email").s());
        customer.setCity(attributeValueMap.get("city").s());
        return customer;
    }

    public static Map<String, AttributeValue> toMap(Customer customer) {
        return Map.of(
                "customerId", AttributeValue.builder().s(customer.getId()).build(),
                "name", AttributeValue.builder().s(customer.getName()).build(),
                "email", AttributeValue.builder().s(customer.getEmail()).build(),
                "city", AttributeValue.builder().s(customer.getCity()).build()
        );
    }
}
