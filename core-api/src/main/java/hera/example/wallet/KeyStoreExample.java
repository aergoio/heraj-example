/*
 * @copyright defined in LICENSE.txt
 */

package hera.example.wallet;

import static java.util.UUID.randomUUID;

import hera.api.model.Authentication;
import hera.api.model.EncryptedPrivateKey;
import hera.api.model.Identity;
import hera.example.AbstractExample;
import hera.key.AergoKey;
import hera.key.AergoKeyGenerator;
import hera.keystore.KeyStore;
import hera.keystore.KeyStores;
import hera.model.KeyAlias;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;

public class KeyStoreExample extends AbstractExample {

  public static void main(String[] args) throws Exception {
    String someDir = new File(KeyStoreExample.class.getResource("/contract_payload").toURI())
        .getParent();
    System.out.println(someDir);

    /* Create */
    {
      /* InMemoryKeystore */
      {
        // make a keystore
        KeyStore keyStore = KeyStores.newInMemoryKeyStore();
        System.out.println("InMemoryKeystore: " + keyStore);
        System.out.println("Stored keys: " + keyStore.listIdentities());
      }

      /* JavaKeystore */
      {
        // create a java keystore
        java.security.KeyStore delegate = java.security.KeyStore.getInstance("PKCS12");
        delegate.load(new FileInputStream(someDir + "/keystore.p12"), "password".toCharArray());

        // make a keystore
        KeyStore keyStore = KeyStores.newJavaKeyStore(delegate);
        System.out.println("JavaKeyStore: " + keyStore);
        System.out.println("Stored keys: " + keyStore.listIdentities());
      }

      /* AergoKeystore */
      {
        // make a keystore
        String root = someDir + "/aergo_keystore";
        KeyStore keyStore = KeyStores.newAergoKeyStore(root);
        System.out.println("AergoKeyStore: " + keyStore);
        System.out.println("Stored keys: " + keyStore.listIdentities());
      }
    }

    /* Save and Load */
    {
      // Using alias.
      {
        // create a keystore
        KeyStore keyStore = KeyStores.newInMemoryKeyStore();

        // create an new key
        AergoKey key = new AergoKeyGenerator().create();

        // save
        Authentication authentication = Authentication.of(KeyAlias.of("myalias"), "password");
        keyStore.save(authentication, key);
      }

      // Using address itself.
      {
        // create an keystore
        KeyStore keyStore = KeyStores.newInMemoryKeyStore();

        // create an new key
        AergoKey key = new AergoKeyGenerator().create();

        // save
        Authentication authentication = Authentication.of(key.getAddress(), "password");
        keyStore.save(authentication, key);
      }
    }

    /* Remove */
    {
      // Using alias.
      {
        // create a keystore
        KeyStore keyStore = KeyStores.newInMemoryKeyStore();

        // create an new key
        AergoKey key = new AergoKeyGenerator().create();

        // save
        Authentication authentication = Authentication.of(KeyAlias.of("myalias"), "password");
        keyStore.save(authentication, key);

        // remove
        System.out.println("Before remove: " + keyStore.listIdentities());
        keyStore.remove(authentication);
        System.out.println("After remove: " + keyStore.listIdentities());
      }

      // Using address itself.
      {
        // create a keystore
        KeyStore keyStore = KeyStores.newInMemoryKeyStore();

        // create an new key
        AergoKey key = new AergoKeyGenerator().create();

        // save
        Authentication authentication = Authentication.of(key.getAddress(), "password");
        keyStore.save(authentication, key);

        // remove
        System.out.println("Before remove: " + keyStore.listIdentities());
        keyStore.remove(authentication);
        System.out.println("After remove: " + keyStore.listIdentities());
      }
    }

    /* Export */
    {
      // Using alias.
      {
        // create a keystore
        KeyStore keyStore = KeyStores.newInMemoryKeyStore();

        // create an new key
        AergoKey key = new AergoKeyGenerator().create();

        // save
        Authentication authentication = Authentication.of(KeyAlias.of("myalias"), "password");
        keyStore.save(authentication, key);

        // export
        EncryptedPrivateKey exported = keyStore.export(authentication, "newpassword");
        System.out.println("Exported: " + exported);
      }

      // Using address itself.
      {
        // create a keystore
        KeyStore keyStore = KeyStores.newInMemoryKeyStore();

        // create an new key
        AergoKey key = new AergoKeyGenerator().create();

        // save
        Authentication authentication = Authentication.of(key.getAddress(), "password");
        keyStore.save(authentication, key);

        // export
        EncryptedPrivateKey exported = keyStore.export(authentication, "newpassword");
        System.out.println("Exported: " + exported);
      }
    }

    /* List Stored Identities */
    {
      // create a keystore
      KeyStore keyStore = KeyStores.newInMemoryKeyStore();

      // create an new key
      AergoKey key = new AergoKeyGenerator().create();

      // save
      Authentication authentication = Authentication.of(KeyAlias.of("myalias"), "password");
      keyStore.save(authentication, key);

      // list
      List<Identity> identities = keyStore.listIdentities();
      System.out.println("Stored identities: " + identities);
    }

    someDir = System.getProperty("java.io.tmpdir");

    /* Store */
    {
      // prepare a java keystore
      java.security.KeyStore delegate = java.security.KeyStore.getInstance("PKCS12");
      delegate.load(null, null);

      // create a java keystore
      KeyStore keyStore = KeyStores.newJavaKeyStore(delegate);

      // store
      String path = someDir + "/" + randomUUID().toString();
      keyStore.store(path, "password".toCharArray());
    }

  }

}
