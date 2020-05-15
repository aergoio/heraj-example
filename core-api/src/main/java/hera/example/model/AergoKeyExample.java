/*
 * @copyright defined in LICENSE.txt
 */

package hera.example.model;

import hera.api.model.Aer;
import hera.api.model.BytesValue;
import hera.api.model.ChainIdHash;
import hera.api.model.EncryptedPrivateKey;
import hera.api.model.Hash;
import hera.api.model.KeyFormat;
import hera.api.model.RawTransaction;
import hera.api.model.Signature;
import hera.api.model.Transaction;
import hera.example.AbstractExample;
import hera.key.AergoKey;
import hera.key.AergoKeyGenerator;
import hera.key.AergoSignVerifier;
import hera.key.Verifier;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AergoKeyExample extends AbstractExample {

  public static void main(String[] args) throws NoSuchAlgorithmException {

    /* New */
    {
      AergoKeyGenerator aergoKeyGenerator = new AergoKeyGenerator();
      AergoKey aergoKey = aergoKeyGenerator.create();
      System.out.println("Created key: " + aergoKey);
    }

    /* Export */
    {
      // Export as wallet import format.
      {
        AergoKey aergoKey = new AergoKeyGenerator().create();
        EncryptedPrivateKey wif = aergoKey.exportAsWif("password");
        System.out.println("Wallet Import Format: " + wif);
      }

      // Export as keyformat.
      {
        AergoKey aergoKey = new AergoKeyGenerator().create();
        KeyFormat keyFormat = aergoKey.exportAsKeyFormat("password");
        System.out.println("KeyFormat: " + keyFormat);
      }
    }

    /* Import */
    {
      // Import with wif.
      {
        EncryptedPrivateKey importedWif = EncryptedPrivateKey
            .of("47btMyQmmWddJmEigUp8HjUPam94Jjtf6eG6SW74r61YmbcJGyoxhwTBa8XhVBQ9wYm468DED");
        AergoKey imported = AergoKey.of(importedWif, "password");
        System.out.println("Imported from wif: " + imported);
      }

      // Import with keyformat.
      {
        String keystore = loadResource(
            "/AmPo7xZJoKNfZXg4NMt9n2saXpKRSkMXwEzqEAfzbVWC71HQL3hn__keystore.txt");
        KeyFormat keyFormat = KeyFormat.of(BytesValue.of(keystore.getBytes()));
        AergoKey imported = AergoKey.of(keyFormat, "password");
        System.out.println("Imported from keyformat: " + imported);
      }
    }

    /* Sign and Verify */
    {
      // On transaction.
      {
        // prepare aergo key
        AergoKey aergoKey = new AergoKeyGenerator().create();

        // sign transaction
        RawTransaction rawTransaction = RawTransaction.newBuilder(ChainIdHash.of(BytesValue.EMPTY))
            .from(aergoKey.getAddress())
            .to(aergoKey.getAddress())
            .amount(Aer.AERGO_ONE)
            .nonce(1L)
            .build();
        Transaction transaction = aergoKey.sign(rawTransaction);
        System.out.println("Signed transaction: " + transaction);

        // verify transaction
        Verifier verifier = new AergoSignVerifier();
        boolean result = verifier.verify(transaction);
        System.out.println("Verify result: " + result);
      }

      // On plain message.
      {
        // prepare aergo key
        AergoKey aergoKey = new AergoKeyGenerator().create();

        // sign message
        BytesValue plainMessage = BytesValue.of("test".getBytes());
        Signature signature = aergoKey.signMessage(plainMessage);
        System.out.println("Signature: " + signature);

        // verify signature
        Verifier verifier = new AergoSignVerifier();
        boolean result = verifier.verify(aergoKey.getAddress(), plainMessage, signature);
        System.out.println("Verify result: " + result);
      }

      // On hashed message.
      {
        // prepare aergo key
        AergoKey aergoKey = new AergoKeyGenerator().create();

        // sign sha-256 hashed message
        BytesValue plainMessage = BytesValue.of("test".getBytes());
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        byte[] rawHashed = messageDigest.digest(plainMessage.getValue());
        Hash hashedMessage = Hash.of(BytesValue.of(rawHashed));
        Signature signature = aergoKey.signMessage(hashedMessage);
        System.out.println("Signature: " + signature);

        // verify signature
        Verifier verifier = new AergoSignVerifier();
        boolean result = verifier.verify(aergoKey.getAddress(), hashedMessage, signature);
        System.out.println("Verify result: " + result);
      }
    }

  }

}
