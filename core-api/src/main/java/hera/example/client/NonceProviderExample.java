/*
 * @copyright defined in LICENSE.txt
 */

package hera.example.client;

import hera.api.model.AccountAddress;
import hera.api.model.AccountState;
import hera.api.model.Aer;
import hera.api.model.BytesValue;
import hera.api.model.Fee;
import hera.api.transaction.NonceProvider;
import hera.api.transaction.SimpleNonceProvider;
import hera.client.AergoClient;
import hera.example.AbstractExample;
import hera.key.AergoKey;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class NonceProviderExample extends AbstractExample {

  public static void main(String[] args) throws Exception {
    AergoClient client = getLocalClient();
    AergoKey richKey = getRichKey();

    /* Create */
    // Create a SimpleNonceProvider.
    {
      // With explicit capacity.
      {
        // create nonce provider with capacity 100
        NonceProvider nonceProvider = new SimpleNonceProvider(100);
      }

      // With implicit capacity.
      {
        // create nonce provider with capacity 1000
        NonceProvider nonceProvider = new SimpleNonceProvider();
      }
    }

    /* Bind */
    // Bind nonce for address.
    {
      // For address.
      {
        AccountAddress accountAddress = AccountAddress
            .of("AmNrsAqkXhQfE6sGxTutQkf9ekaYowaJFLekEm8qvDr1RB1AnsiM");
        NonceProvider nonceProvider = new SimpleNonceProvider();
        nonceProvider.bindNonce(accountAddress, 30L);
      }

      // Using account state. It binds nonce for corresponding state.
      {
        AccountAddress accountAddress = AccountAddress
            .of("AmNrsAqkXhQfE6sGxTutQkf9ekaYowaJFLekEm8qvDr1RB1AnsiM");
        AccountState accountState = client.getAccountOperation().getState(accountAddress);
        NonceProvider nonceProvider = new SimpleNonceProvider();
        nonceProvider.bindNonce(accountState);
      }
    }

    /* Use */
    {
      // Increment and get nonce. It's thread-safe
      {
        AergoKey signer = richKey;
        NonceProvider nonceProvider = new SimpleNonceProvider();
        long nonce = nonceProvider.incrementAndGetNonce(signer.getAddress());
      }

      // Get last used nonce.
      {
        AergoKey signer = richKey;
        NonceProvider nonceProvider = new SimpleNonceProvider();
        long nonce = nonceProvider.getLastUsedNonce(signer.getAddress());
      }
    }

    /* Example */
    {
      // prepare signer
      AergoKey signer = richKey;

      // create an nonce provider
      AccountState accountState = client.getAccountOperation().getState(signer.getAddress());
      NonceProvider nonceProvider = new SimpleNonceProvider();
      nonceProvider.bindNonce(accountState);

      // print current
      long currentNonce = nonceProvider.getLastUsedNonce(signer.getAddress());
      System.out.println("Current nonce: " + currentNonce);

      // request using thread pool
      AccountAddress accountAddress = AccountAddress
          .of("AmNrsAqkXhQfE6sGxTutQkf9ekaYowaJFLekEm8qvDr1RB1AnsiM");
      ExecutorService service = Executors.newCachedThreadPool();
      IntStream.range(0, 1000).forEach(i -> {
        service.submit(() -> {
          // get nonce to use
          long nonce = nonceProvider.incrementAndGetNonce(signer.getAddress());
          client.getTransactionOperation().sendTx(signer, accountAddress, Aer.ONE, nonce,
              Fee.INFINITY, BytesValue.EMPTY);
        });
      });

      // stop the service
      service.awaitTermination(3000L, TimeUnit.MILLISECONDS);
      service.shutdown();

      // print 1000
      long lastUsedNonce = nonceProvider.getLastUsedNonce(signer.getAddress());
      System.out.println("Nonce difference: " + (lastUsedNonce - currentNonce));
    }

    client.close();
  }

}
