/*
 * @copyright defined in LICENSE.txt
 */

package hera.example.client;

import hera.client.AergoClient;
import hera.client.AergoClientBuilder;
import hera.example.AbstractExample;
import java.util.concurrent.TimeUnit;

public class AergoClientBuildExample extends AbstractExample {

  public static void main(String[] args) {

    /* Endpoint */
    // You can configure aergo node endpoint to connect.
    {
      // connect to 'localhost:7845'
      AergoClient aergoClient = new AergoClientBuilder()
          .withEndpoint("localhost:7845")
          .build();
    }

    /* Connect strategy */
    // You can configure a strategy to connect.
    {
      // Non-Blocking connection uses netty internally.
      {
        // connect to 'localhost:7845' with non-blocking connect
        AergoClient aergoClient = new AergoClientBuilder()
            .withEndpoint("localhost:7845")
            .withNonBlockingConnect()
            .build();
      }

      // Blocking connection uses okhttp internally..
      {
        // connect to 'localhost:7845' with blocking connect
        AergoClient aergoClient = new AergoClientBuilder()
            .withEndpoint("localhost:7845")
            .withBlockingConnect()
            .build();
      }
    }

    /* Retry */
    // You can configure retry count on any kind of failure.
    // It just retry the same request with an interval.
    {
      // retry 3 count with a 1000ms interval
      AergoClient aergoClient = new AergoClientBuilder()
          .withRetry(3, 1000L, TimeUnit.MILLISECONDS)
          .build();
    }

    /* Timeout */
    // You can configure timeout without any response for each request.
    {
      // set timeout as 5000ms for each request.
      AergoClient aergoClient = new AergoClientBuilder()
          .withTimeout(5000L, TimeUnit.MILLISECONDS)
          .build();
    }

    /* Close */
    // Close an aergo client. You have to close it to prevent memory leak.
    {
      // You can close aergo client by calling close method.
      {
        // create
        AergoClient aergoClient = new AergoClientBuilder()
            .withEndpoint("localhost:7845")
            .withBlockingConnect()
            .withTimeout(10000L, TimeUnit.MILLISECONDS)
            .build();

        // ... do some operations

        // close
        aergoClient.close();
      }

      // Since java 7, you can use try-with-resources block to close aergo client.
      {
        // try-with-resources block
        try (AergoClient aergoClient = new AergoClientBuilder()
            .withEndpoint("localhost:7845")
            .build()) {

          // ... do some operations
        }
      }
    }

  }

}
