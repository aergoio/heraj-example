/*
 * @copyright defined in LICENSE.txt
 */

package hera.example.client;

import hera.client.AergoClient;
import hera.client.AergoClientBuilder;
import hera.example.AbstractExample;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

public class BuildExample extends AbstractExample {

  public static void main(String[] args) {

    /* Endpoint */
    {
      // connect to 'localhost:7845'
      AergoClient aergoClient = new AergoClientBuilder()
          .withEndpoint("localhost:7845")
          .build();
    }

    /* Connect Strategy */
    {
      // Non-Blocking connection uses netty internally.
      {
        // connect to 'localhost:7845' with non-blocking connect
        AergoClient aergoClient = new AergoClientBuilder()
            .withEndpoint("localhost:7845")
            .withNonBlockingConnect()
            .build();
      }

      // Blocking connection uses okhttp internally.
      {
        // connect to 'localhost:7845' with blocking connect
        AergoClient aergoClient = new AergoClientBuilder()
            .withEndpoint("localhost:7845")
            .withBlockingConnect()
            .build();
      }
    }

    /* Connect Type */
    {
      // Connect with plaintext. This is default behavior.
      {
        // connect with plain text
        AergoClient aergoClient = new AergoClientBuilder()
            .withEndpoint("localhost:7845")
            .withPlainText()
            .build();
      }

      // Connect with tls. Note that client key must be PKCS8 format
      {
        // prepare cert files
        InputStream serverCert = loadResourceAsStream("/cert/server.crt");
        InputStream clientCert = loadResourceAsStream("/cert/client.crt");
        InputStream clientKey = loadResourceAsStream("/cert/client.pem");

        // connect with plain text
        AergoClient aergoClient = new AergoClientBuilder()
            .withEndpoint("localhost:7845")
            .withTransportSecurity("servername", serverCert, clientCert, clientKey)
            .build();
      }
    }

    /* Retry */
    {
      // retry 3 count with a 1000ms interval
      AergoClient aergoClient = new AergoClientBuilder()
          .withRetry(3, 1000L, TimeUnit.MILLISECONDS)
          .build();
    }

    /* Timeout */
    {
      // set timeout as 5000ms for each request.
      AergoClient aergoClient = new AergoClientBuilder()
          .withTimeout(5000L, TimeUnit.MILLISECONDS)
          .build();
    }

    /* Close */
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
