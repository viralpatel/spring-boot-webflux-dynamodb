# Spring Boot Webflux + AWS DynamoDB example
A reference implementation for Spring Boot Webflux integration with AWS DynamoDB using AWS Async Client.

### Technologies
1. Java 11
2. Spring Boot 2.2.2
3. Gradle 6.0.1
4. AWS SDK 2.10.40

### Running AWS DynamoDB locally without Docker
Follow the steps at https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/DynamoDBLocal.DownloadingAndRunning.html

1. Download the AWS DynamoDB Local JAR from above link and unzip it

2. Run the local dynamodb jar
    
    ```
    java -Djava.library.path=./DynamoDBLocal_lib/ 
        -jar DynamoDBLocal.jar
    ```
   
3. Create customer table in dynamodb.

    ```
    aws dynamodb create-table 
     	--table-name customers 
     	--attribute-definitions AttributeName=customerId,AttributeType=S 
     	--key-schema AttributeName=customerId,KeyType=HASH 
     	--provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5 
     	--endpoint-url http://localhost:8000
    ```

4. Verify the table is created.     
    
    ```
     aws dynamodb list-tables 
       	--endpoint-url http://localhost:8000
    ```
    
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
[Spring Boot Webflux DynamoDB Tutorial](https://www.viralpatel.net/spring-boot-webflux-dynamodb/)
