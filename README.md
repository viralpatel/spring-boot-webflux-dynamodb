# Spring Boot Webflux + AWS DynamoDB example
A reference implementation for Spring Boot Webflux integration with AWS DynamoDB using AWS Async Client.

### Technologies
1. Java 11
2. Spring Boot 2.2.2
3. Gradle 6.0.1
4. AWS SDK 2.10.40

### Running AWS DynamoDB locally without Docker
1. Download the AWS DynamoDB Local JAR
2. Run the local dynamodb
    
    `$ java -jar `
3. Create customer table in dynamodb.

    `$ aws dynamodb create-table `

4. Verify the table is created.     
    
    `aws dynamodb list-tables --endpoint-url http://localhost:8000`
    
    Output:
    ``` 
    {
          "TableNames": [
              "customers"
          ]
    }
    ``` 

    
### Build and run

`./gradlew bootRun`


## Tutorial
[Spring Boot Webflux DynamoDB Tutorial](https://www.viralpatel.net/spring-boot-webflux-dynamodb-example/)
