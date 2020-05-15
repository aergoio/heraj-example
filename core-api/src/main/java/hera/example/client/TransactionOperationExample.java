/*
 * @copyright defined in LICENSE.txt
 */

package hera.example.client;

import hera.api.model.AccountAddress;
import hera.api.model.AccountState;
import hera.api.model.Aer;
import hera.api.model.BytesValue;
import hera.api.model.ChainIdHash;
import hera.api.model.Fee;
import hera.api.model.Name;
import hera.api.model.RawTransaction;
import hera.api.model.Transaction;
import hera.api.model.TxHash;
import hera.api.model.TxReceipt;
import hera.api.transaction.NonceProvider;
import hera.api.transaction.SimpleNonceProvider;
import hera.client.AergoClient;
import hera.example.AbstractExample;
import hera.key.AergoKey;

public class TransactionOperationExample extends AbstractExample {

  public static void main(String[] args) throws Exception {
    // prepare client and signer
    AergoClient client = getLocalClient();
    AergoKey richKey = getRichKey();

    // prepare nonce provider
    NonceProvider nonceProvider = new SimpleNonceProvider();
    AccountState state = client.getAccountOperation().getState(richKey.getAddress());
    nonceProvider.bindNonce(state);

    /* Get Transaction */
    {
      TxHash txHash = TxHash.of("39vLyMqsg1mTT9mF5NbADgNB2YUiRVsT6SUkDujBZme8");
      Transaction transaction = client.getTransactionOperation().getTransaction(txHash);
      System.out.println("Transaction: " + transaction);
    }

    /* Get Transaction Receipt */
    {
      TxHash txHash = TxHash.of("39vLyMqsg1mTT9mF5NbADgNB2YUiRVsT6SUkDujBZme8");
      TxReceipt txReceipt = client.getTransactionOperation().getTxReceipt(txHash);
      System.out.println("Transaction receipt: " + txReceipt);
    }

    /* Commit */
    {
      // get chain id hash
      ChainIdHash chainIdHash = client.getBlockchainOperation().getChainIdHash();

      // prepare signer
      AergoKey signer = richKey;

      // make a transaction
      long nonce = nonceProvider.incrementAndGetNonce(signer.getAddress());
      RawTransaction rawTransaction = RawTransaction.newBuilder()
          .chainIdHash(chainIdHash)
          .from(signer.getAddress())
          .to(signer.getAddress())
          .amount(Aer.AERGO_ONE)
          .nonce(nonce)
          .fee(Fee.ZERO)
          .payload(BytesValue.of("contract_payload".getBytes()))
          .build();

      // sign raw transaction
      Transaction transaction = signer.sign(rawTransaction);

      // commit signed one
      TxHash txHash = client.getTransactionOperation().commit(transaction);
      System.out.println("Commit tx hash: " + txHash);
    }

    /* Send */
    {
      // By address.
      {
        // prepare signer
        AergoKey signer = richKey;

        // make a send transaction
        AccountAddress accountAddress = AccountAddress
            .of("AmNrsAqkXhQfE6sGxTutQkf9ekaYowaJFLekEm8qvDr1RB1AnsiM");
        long nonce = nonceProvider.incrementAndGetNonce(signer.getAddress());
        TxHash txHash = client.getTransactionOperation()
            .sendTx(signer, accountAddress, Aer.ONE, nonce, Fee.INFINITY, BytesValue.EMPTY);
        System.out.println("Send tx hash: " + txHash);
      }

      // By name.
      {
        // prepare signer
        AergoKey signer = richKey;

        // create an name
        Name name = randomName();
        long nonce1 = nonceProvider.incrementAndGetNonce(signer.getAddress());
        client.getAccountOperation().createNameTx(signer, name, nonce1);

        // sleep
        Thread.sleep(2000L);

        // make a send transaction
        long nonce2 = nonceProvider.incrementAndGetNonce(signer.getAddress());
        TxHash txHash = client.getTransactionOperation()
            .sendTx(signer, name, Aer.ONE, nonce2, Fee.INFINITY, BytesValue.EMPTY);
        System.out.println("Send tx hash: " + txHash);
      }
    }

    client.close();
  }

}
