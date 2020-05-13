/*
 * @copyright defined in LICENSE.txt
 */

package hera.example;

import static java.util.UUID.randomUUID;

import hera.api.model.AccountState;
import hera.api.model.Aer;
import hera.api.model.Aer.Unit;
import hera.api.model.BytesValue;
import hera.api.model.ContractAddress;
import hera.api.model.ContractDefinition;
import hera.api.model.ContractInterface;
import hera.api.model.ContractTxReceipt;
import hera.api.model.EncryptedPrivateKey;
import hera.api.model.Fee;
import hera.api.model.Name;
import hera.api.model.TxHash;
import hera.api.transaction.NonceProvider;
import hera.api.transaction.SimpleNonceProvider;
import hera.client.AergoClient;
import hera.client.AergoClientBuilder;
import hera.key.AergoKey;
import hera.key.AergoKeyGenerator;
import hera.wallet.WalletApi;
import java.io.InputStream;
import java.util.Scanner;

public abstract class AbstractExample {

  protected static AergoKey getRichKey() {
    // AmMHQur9ye5bBpUdAATpswhaeygJa9xf4wjnXz27hy2TppUWoMM9
    EncryptedPrivateKey encryptedPrivateKey = EncryptedPrivateKey
        .of("47VYaB9WJ4FoBaiZ1HAh3yDBkSFDVM3fVxgjTfAnmmFa8GqyAZUb4gqKKirkoRgdhMazRHzbR");
    AergoKey richKey = AergoKey.of(encryptedPrivateKey, "password");
    AergoKey aergoKey = new AergoKeyGenerator().create();
    try (AergoClient client = getLocalClient()) {
      AccountState state = client.getAccountOperation()
          .getState(richKey.getAddress());
      NonceProvider nonceProvider = new SimpleNonceProvider();
      nonceProvider.bindNonce(state);
      long nonce = nonceProvider.incrementAndGetNonce(richKey.getAddress());
      client.getTransactionOperation()
          .sendTx(richKey, aergoKey.getAddress(), Aer.of("50000", Unit.AERGO),
              nonce, Fee.INFINITY, BytesValue.EMPTY);
      Thread.sleep(2200L);
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
    return aergoKey;
  }

  // only 12-length character is valid
  protected static Name randomName() {
    return Name.of(randomUUID().toString().replace("-", "").substring(0, 12));
  }

  protected static AergoClient getLocalClient() {
    return new AergoClientBuilder()
        .withEndpoint("localhost:7845")
        .build();
  }

  protected static AergoClient getTestnetClient() {
    return new AergoClientBuilder()
        .withEndpoint("testnet-api.aergo.io:7845")
        .build();
  }

  protected static String loadResource(String name) {
    StringBuilder stringBuilder = new StringBuilder();
    InputStream inputStream = loadResourceAsStream(name);
    Scanner scanner = new Scanner(inputStream);
    while (scanner.hasNext()) {
      stringBuilder.append(scanner.next());
    }
    return stringBuilder.toString();
  }

  protected static InputStream loadResourceAsStream(String name) {
    return AbstractExample.class.getResourceAsStream(name);
  }

}
