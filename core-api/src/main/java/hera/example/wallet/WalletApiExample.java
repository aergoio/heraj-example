/*
 * @copyright defined in LICENSE.txt
 */

package hera.example.wallet;

import hera.api.model.Authentication;
import hera.api.model.BytesValue;
import hera.api.model.Signature;
import hera.example.AbstractExample;
import hera.key.AergoKey;
import hera.key.AergoKeyGenerator;
import hera.keystore.KeyStore;
import hera.keystore.KeyStores;
import hera.wallet.WalletApi;
import hera.wallet.WalletApiFactory;

public class WalletApiExample extends AbstractExample {

  public static void main(String[] args) {

    /* WalletApi */
    {
      /* Create */
      {
        // create a keystore
        KeyStore keyStore = KeyStores.newInMemoryKeyStore();

        // create a wallet api
        WalletApi walletApi = new WalletApiFactory().create(keyStore);
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
    }

  }

}
