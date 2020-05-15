/*
 * @copyright defined in LICENSE.txt
 */

package hera.example.wallet;

import hera.api.model.Authentication;
import hera.api.model.BytesValue;
import hera.api.model.Signature;
import hera.api.model.Time;
import hera.api.model.TryCountAndInterval;
import hera.client.AergoClient;
import hera.client.AergoClientBuilder;
import hera.example.AbstractExample;
import hera.key.AergoKey;
import hera.key.AergoKeyGenerator;
import hera.keystore.KeyStore;
import hera.keystore.KeyStores;
import hera.wallet.QueryApi;
import hera.wallet.TransactionApi;
import hera.wallet.WalletApi;
import hera.wallet.WalletApiFactory;
import java.util.concurrent.TimeUnit;

public class WalletApiExample extends AbstractExample {

  public static void main(String[] args) {

    /* Create */
    {
      // With implicit retry count and interval on nonce failure.
      {
        // create a keystore
        KeyStore keyStore = KeyStores.newInMemoryKeyStore();

        // create a wallet api
        WalletApi walletApi = new WalletApiFactory().create(keyStore);
        System.out.println("WalletApi: " + walletApi);
      }

      // With explicit retry count and interval on nonce failure.
      {
        // create a keystore
        KeyStore keyStore = KeyStores.newInMemoryKeyStore();

        // create a wallet api with retry count 5 and interval 1s
        TryCountAndInterval tryCountAndInterval = TryCountAndInterval
            .of(5, Time.of(1L, TimeUnit.SECONDS));
        WalletApi walletApi = new WalletApiFactory().create(keyStore, tryCountAndInterval);
        System.out.println("WalletApi: " + walletApi);
      }
    }

    /* Unlock and Lock */
    {
      // create a keystore
      KeyStore keyStore = KeyStores.newInMemoryKeyStore();

      // store new key to keystore
      AergoKey aergoKey = new AergoKeyGenerator().create();
      Authentication authentication = Authentication.of(aergoKey.getAddress(), "password");
      keyStore.save(authentication, aergoKey);

      // create a wallet api
      WalletApi walletApi = new WalletApiFactory().create(keyStore);

      // unlock account
      boolean unlockResult = walletApi.unlock(authentication);
      System.out.println("Unlock result: " + unlockResult);
      System.out.println("Currently locked one: " + walletApi.getPrincipal());

      // do something..
      Signature signature = walletApi.signMessage(BytesValue.of("test".getBytes()));
      System.out.println("Signature: " + signature);

      // lock account
      boolean lockResult = walletApi.lock();
      System.out.println("Lock result: " + lockResult);
    }

    /* High Level Api */
    {
      // prepare client
      AergoClient aergoClient = new AergoClientBuilder().build();

      // create a keystore
      KeyStore keyStore = KeyStores.newInMemoryKeyStore();

      // create a wallet api
      WalletApi walletApi = new WalletApiFactory().create(keyStore);
      System.out.println("WalletApi: " + walletApi);

      // transaction api
      TransactionApi transactionApi = walletApi.with(aergoClient).transaction();
      System.out.println("Transaction Api: " + transactionApi);

      // query api
      QueryApi queryApi = walletApi.with(aergoClient).query();
      System.out.println("Query Api: " + queryApi);
    }

  }

}
