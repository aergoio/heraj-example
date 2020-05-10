/*
 * @copyright defined in LICENSE.txt
 */

package hera.example.client;

import hera.api.model.AccountAddress;
import hera.api.model.Aer;
import hera.api.model.BytesValue;
import hera.api.model.ChainIdHash;
import hera.api.model.Fee;
import hera.api.model.Name;
import hera.api.model.RawTransaction;
import hera.api.model.Transaction;
import hera.api.model.TxHash;
import hera.api.model.TxReceipt;
import hera.client.AergoClient;
import hera.example.AbstractExample;
import hera.key.AergoKey;

public class TransactionOperationExample extends AbstractExample {

  public static void main(String[] args) {
    AergoClient client = getTestnetClient();
    AergoKey richKey = getRichKey();

    /* Get Transaction */
    // Get transaction info.
    {
      TxHash txHash = TxHash.of("39vLyMqsg1mTT9mF5NbADgNB2YUiRVsT6SUkDujBZme8");
      Transaction transaction = client.getTransactionOperation().getTransaction(txHash);
      System.out.println("Transaction: " + transaction);
    }

    /* Get Transaction Receipt */
    // Get receipt of transaction.
    {
      TxHash txHash = TxHash.of("39vLyMqsg1mTT9mF5NbADgNB2YUiRVsT6SUkDujBZme8");
      TxReceipt txReceipt = client.getTransactionOperation().getTxReceipt(txHash);
      System.out.println("Transaction receipt: " + txReceipt);
    }

    client = getLocalClient();

    /* Commit */
    // Commit a signed transaction.
    {
      // get chain id hash
      ChainIdHash chainIdHash = client.getBlockchainOperation().getChainIdHash();

      // prepare signer
      AergoKey signer = richKey;

      // make a transaction
      RawTransaction rawTransaction = RawTransaction.newBuilder()
          .chainIdHash(chainIdHash)
          .from(signer.getAddress())
          .to(signer.getAddress())
          .amount(Aer.AERGO_ONE)
          .nonce(1L)
          .fee(Fee.ZERO)
          .payload(BytesValue.of("payload".getBytes()))
          .build();

      // sign raw transaction
      Transaction transaction = signer.sign(rawTransaction);
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
        TxHash txHash = client.getTransactionOperation()
            .sendTx(richKey, accountAddress, Aer.ONE, 2L, Fee.INFINITY, BytesValue.EMPTY);
        System.out.println("Send tx hash: " + txHash);
      }

      // By name.
      {
        // prepare signer
        AergoKey signer = richKey;

        // make a send transaction
        Name name = Name.of("samplename11");
        TxHash txHash = client.getTransactionOperation()
            .sendTx(richKey, name, Aer.ONE, 3L, Fee.INFINITY, BytesValue.EMPTY);
        System.out.println("Send tx hash: " + txHash);
      }
    }

    client.close();
  }

}
