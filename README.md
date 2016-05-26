# Phorests SQS Events Sample

This is a sample Spring Boot Java App that demonstrates how to subscribe for SQS messages using JMS API.

Phorest's third party purchase API is integrated here to let to send you messages.
 
## How does it work

On startup it makes sure that SQS queue exist (with default name `external-events-test-queue`, it could be changed through )

## Prerequisites

*   You must have a valid Amazon Web Services developer account.
*   You must be signed up to use Amazon SQS. For more information on Amazon SQS, see [http://aws.amazon.com/sqs](http://aws.amazon.com/sqs).
*   You must hava Java 8 installed on your machine


## Configuration

Configuration is done through `src/main/resources/application.properties` file

### Required

* fill in your Access Key ID and Secret Access Key:

  ```
  amazon.credentials.accessKeyId=
  amazon.credentials.secretKey=
  ```

### Optional

* change AWS region (defaults to `eu-west-1`)
  ```
  aws.region=
  ```

* change SQS queue name (defaults to sqs-sample-events-queue)

    ```
    sqs.events-queue-name=
    ```

## Running the Sample

The basic steps for running the Amazon SQS sample are:

1.  Run the `./gradlew bootRun` command at the root project level and wait until it prints: `Tomcat started on port(s): 8080 (http)`

2.  Now you can trigger sending a sample Purchase Event to the SQS queue by sending `POST` request to the endpoint: `localhost:8080/sqs-sample/test/purchaseEvent`
 i.e.: `curl -X POST localhost:8080/sqs-sample/test/purchaseEvent`

 That will send sample event (`src/main/resources/samples/sample-event.json`) to the SQS queue.

6. In logs you should see that event was received by `PurchaseEventListener`, its content was logged, it was deserialized to the `Event<PurchaseData>` object and logged
