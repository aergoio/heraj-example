/*
 * @copyright defined in LICENSE.txt
 */

package hera.example.client;

import static java.util.Arrays.asList;

import hera.api.model.AccountAddress;
import hera.api.model.AccountState;
import hera.api.model.AccountTotalVote;
import hera.api.model.Aer;
import hera.api.model.Aer.Unit;
import hera.api.model.ElectedCandidate;
import hera.api.model.Name;
import hera.api.model.StakeInfo;
import hera.api.model.TxHash;
import hera.api.transaction.NonceProvider;
import hera.api.transaction.SimpleNonceProvider;
import hera.client.AergoClient;
import hera.example.AbstractExample;
import hera.key.AergoKey;
import java.util.List;

public class AccountOperationExample extends AbstractExample {

  public static void main(String[] args) throws Exception {
    // prepare client and signer
    AergoClient client = getLocalClient();
    AergoKey richKey = getRichKey();

    // prepare nonce provider
    NonceProvider nonceProvider = new SimpleNonceProvider();
    AccountState state = client.getAccountOperation().getState(richKey.getAddress());
    nonceProvider.bindNonce(state);

    /* Get Account State */
    {
      AccountAddress accountAddress = AccountAddress
          .of("AmNrsAqkXhQfE6sGxTutQkf9ekaYowaJFLekEm8qvDr1RB1AnsiM");
      AccountState accountState = client.getAccountOperation().getState(accountAddress);
      System.out.println("AccountState: " + accountState);
    }

    /* Create Name */
    {
      // prepare a signer
      AergoKey signer = richKey;

      // make a naming transaction
      Name name = randomName();
      long nonce = nonceProvider.incrementAndGetNonce(signer.getAddress());
      TxHash txHash = client.getAccountOperation().createNameTx(signer, name, nonce);
      System.out.println("Create name tx hash: " + txHash);
    }

    Thread.sleep(2000L);

    /* Update Name */
    {
      // prepare a signer
      AergoKey signer = richKey;

      // create an name
      Name name = randomName();
      long nonce1 = nonceProvider.incrementAndGetNonce(signer.getAddress());
      client.getAccountOperation().createNameTx(signer, name, nonce1);

      // sleep
      Thread.sleep(2000L);

      // update an name
      AccountAddress nextOwner = AccountAddress
          .of("AmNrsAqkXhQfE6sGxTutQkf9ekaYowaJFLekEm8qvDr1RB1AnsiM");
      long nonce2 = nonceProvider.incrementAndGetNonce(signer.getAddress());
      TxHash txHash = client.getAccountOperation().updateNameTx(signer, name, nextOwner, nonce2);
      System.out.println("Update name tx hash: " + txHash);
    }

    Thread.sleep(2000L);

    /* Get Name Owner */
    {
      // At current block.
      {
        // get name owner at current block
        Name name = Name.of("samplename11");
        AccountAddress nameOwner = client.getAccountOperation().getNameOwner(name);
        System.out.println("Nonce owner: " + nameOwner);
      }

      // At specific block.
      {
        // get name owner at block 3
        Name name = Name.of("samplename11");
        AccountAddress nameOwner = client.getAccountOperation().getNameOwner(name, 3);
        System.out.println("Nonce owner: " + nameOwner);
      }
    }

    /* Stake */
    {
      // prepare a signer
      AergoKey signer = richKey;

      // stake 10000 aergo
      Aer amount = Aer.of("10000", Unit.AERGO);
      long nonce = nonceProvider.incrementAndGetNonce(signer.getAddress());
      TxHash txHash = client.getAccountOperation().stakeTx(signer, amount, nonce);
      System.out.println("Stake tx hash: " + txHash);
    }

    Thread.sleep(2000L);

    // commented since unstake is invalid just after staking
    /* Unstake */
    {
      // prepare a signer
      AergoKey signer = richKey;

      // unstake 10000 aergo
//      Aer amount = Aer.of("10000", Unit.AERGO);
//      long nonce = nonceProvider.incrementAndGetNonce(signer.getAddress());
//      TxHash txHash = client.getAccountOperation().unstakeTx(signer, amount, nonce);
//      System.out.println("Unstake tx hash: " + txHash);
    }

    /* Get Stake Info */
    {
      AccountAddress accountAddress = AccountAddress
          .of("AmNrsAqkXhQfE6sGxTutQkf9ekaYowaJFLekEm8qvDr1RB1AnsiM");
      StakeInfo stakeInfo = client.getAccountOperation().getStakingInfo(accountAddress);
      System.out.println("Stake info: " + stakeInfo);
    }

    /* Vote */
    {
      // prepare a signer
      AergoKey signer = richKey;

      // vote to "voteBP"
      List<String> candidates = asList("16Uiu2HAkwWbv8nKx7S6S5NMvUpTLNeXMVCPr3NTnrx6rBPYYiQ4K");
      long nonce = nonceProvider.incrementAndGetNonce(signer.getAddress());
      TxHash txHash = client.getAccountOperation().voteTx(signer, "voteBp", candidates, nonce);
      System.out.println("Vote tx hash: " + txHash);
    }

    Thread.sleep(2000L);

    /* Get Vote Info */
    {
      AccountAddress accountAddress = AccountAddress
          .of("AmNrsAqkXhQfE6sGxTutQkf9ekaYowaJFLekEm8qvDr1RB1AnsiM");
      AccountTotalVote voteInfo = client.getAccountOperation().getVotesOf(accountAddress);
      System.out.println("Vote info: " + voteInfo);
    }

    /* Get Vote Result */
    {
      // get vote result for vote id "voteBP" for top 23 candidates.
      List<ElectedCandidate> elected = client.getAccountOperation().listElected("voteBP", 23);
      System.out.println("Elected: " + elected);
    }

    client.close();
  }

}
