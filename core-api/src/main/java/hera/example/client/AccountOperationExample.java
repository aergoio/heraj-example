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
import hera.client.AergoClient;
import hera.example.AbstractExample;
import hera.key.AergoKey;
import java.util.List;

public class AccountOperationExample extends AbstractExample {

  public static void main(String[] args) throws Exception {
    AergoClient client = getLocalClient();
    AergoKey richKey = getRichKey();

    /* Get Account State */
    // Get state of account.
    {
      AccountAddress accountAddress = AccountAddress
          .of("AmNrsAqkXhQfE6sGxTutQkf9ekaYowaJFLekEm8qvDr1RB1AnsiM");
      AccountState accountState = client.getAccountOperation().getState(accountAddress);
      System.out.println("AccountState: " + accountState);
    }

    /* Create Name */
    // Create name which owns by a transaction signer.
    {
      // prepare a signer
      AergoKey signer = richKey;

      // make a naming transaction
      Name name = Name.of("samplename11");
      TxHash txHash = client.getAccountOperation().createNameTx(signer, name, 1L);
      System.out.println("Create name tx hash: " + txHash);
    }

    Thread.sleep(2000L);

    /* Update Name */
    // Update name owner to new account. It should be done by origin name owner.
    {
      // prepare a signer
      AergoKey signer = richKey;

      Name name = Name.of("samplename11");
      AccountAddress nextOwner = AccountAddress
          .of("AmNrsAqkXhQfE6sGxTutQkf9ekaYowaJFLekEm8qvDr1RB1AnsiM");
      TxHash txHash = client.getAccountOperation().updateNameTx(signer, name, nextOwner, 2L);
      System.out.println("Update name tx hash: " + txHash);
    }

    Thread.sleep(2000L);

    /* Get Name Owner */
    // Get name owner.
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
    // Stake an aergo.
    {
      // prepare a signer
      AergoKey signer = richKey;

      // stake 10000 aergo
      Aer amount = Aer.of("10000", Unit.AERGO);
      TxHash txHash = client.getAccountOperation().stakeTx(signer, amount, 3L);
      System.out.println("Stake tx hash: " + txHash);
    }

    Thread.sleep(2000L);

    // comment this block on execution
    /* Unstake */
    // UnStake an aergo.
//    {
//      // prepare a signer
//      AergoKey signer = richKey;
//
//      // unstake 10000 aergo
//      Aer amount = Aer.of("10000", Unit.AERGO);
//      TxHash txHash = client.getAccountOperation().unstakeTx(signer, amount, 4L);
//      System.out.println("Unstake tx hash: " + txHash);
//    }

    /* Get Stake Info */
    // Get stake info of an account.
    {
      AccountAddress accountAddress = AccountAddress
          .of("AmNrsAqkXhQfE6sGxTutQkf9ekaYowaJFLekEm8qvDr1RB1AnsiM");
      StakeInfo stakeInfo = client.getAccountOperation().getStakingInfo(accountAddress);
      System.out.println(stakeInfo);
      System.out.println("Stake info: " + stakeInfo);
    }

    /* Vote */
    // Vote candidate to a vote id.
    {
      // prepare a signer
      AergoKey signer = richKey;

      // vote to "voteBP" with candidate "test"
      List<String> candidates = asList("16Uiu2HAkwWbv8nKx7S6S5NMvUpTLNeXMVCPr3NTnrx6rBPYYiQ4K");
      TxHash txHash = client.getAccountOperation().voteTx(signer, "voteBp", candidates, 4L);
      System.out.println("Vote tx hash: " + txHash);
    }

    Thread.sleep(2000L);

    /* Get Vote Info */
    // Get vote info of an account.
    {
      AccountAddress accountAddress = AccountAddress
          .of("AmNrsAqkXhQfE6sGxTutQkf9ekaYowaJFLekEm8qvDr1RB1AnsiM");
      AccountTotalVote voteInfo = client.getAccountOperation().getVotesOf(accountAddress);
      System.out.println("Vote info: " + voteInfo);
    }

    /* Get Vote Result */
    // Get vote result for vote id.
    {
      // get vote result for vote id "voteBP" for top 23 candidates.
      List<ElectedCandidate> elected = client.getAccountOperation().listElected("voteBP", 23);
      System.out.println("Elected: " + elected);
    }

    client.close();
  }

}
