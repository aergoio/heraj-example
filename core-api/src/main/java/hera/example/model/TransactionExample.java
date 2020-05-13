/*
 * @copyright defined in LICENSE.txt
 */

package hera.example.model;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

import hera.api.model.AccountAddress;
import hera.api.model.Aer;
import hera.api.model.Aer.Unit;
import hera.api.model.BytesValue;
import hera.api.model.ChainIdHash;
import hera.api.model.ContractAddress;
import hera.api.model.ContractDefinition;
import hera.api.model.ContractFunction;
import hera.api.model.ContractInterface;
import hera.api.model.ContractInvocation;
import hera.api.model.Fee;
import hera.api.model.Name;
import hera.api.model.RawTransaction;
import hera.api.model.StateVariable;
import hera.api.model.Transaction;
import hera.api.transaction.ContractInvocationPayloadConverter;
import hera.api.transaction.PayloadConverter;
import hera.example.AbstractExample;
import hera.key.AergoKey;
import hera.key.AergoKeyGenerator;
import java.util.List;

public class TransactionExample extends AbstractExample {

  public static void main(String[] args) {

    /* Make a transaction */
    {
      /* Plain Transaction */
      {
        // make a plain transaction
        AergoKey aergoKey = new AergoKeyGenerator().create();
        ChainIdHash chainIdHash = ChainIdHash.of("6YCMGJu3UN66ULzUuS5R7GTxXLDsSjRdjWPB94EiqMJc");
        RawTransaction rawTransaction = RawTransaction.newBuilder()
            .chainIdHash(chainIdHash)
            .from(aergoKey.getAddress())
            .to(aergoKey.getAddress())
            .amount(Aer.AERGO_ONE)
            .nonce(1L)
            .fee(Fee.ZERO)
            .payload(BytesValue.of("contract_payload".getBytes()))
            .build();
        Transaction transaction = aergoKey.sign(rawTransaction);
        System.out.println("Plain transaction: " + transaction);
      }

      /* Deploy Contract Transaction */
      {
        // make a contract definition
        ContractDefinition definition = ContractDefinition.newBuilder()
            .encodedContract(
                "FppTEQaroys1N4P8RcAYYiEhHaQaRE9fzANUx4q2RHDXaRo6TYiTa61n25JcV19grEhpg8qdCWVdsDE2yVfuTKxxcdsTQA2B5zTfxA4GqeRqYGYgWJpj1geuLJAn1RjotdRRxSS1BFA6CAftxjcgiP6WUHacmgtNzoWViYESykhjqVLdmTfV12d44wfh9YAgQ57aRkLNCPkujbnJhdhHEtY1hrJYLCxUDBveqVcDhrrvcHtjDAUcZ5UMzbg6qR1kthGB1Lua6ymw1BmfySNtqb1b6Hp92UPMa7gi5FpAXF5XgpQtEbYDXMbtgu5XtXNhNejrtArcekmjrmPXRoTnMDGUQFcALtnNCrgSv2z5PiXP1coGEbHLTTbxkmJmJz6arEfsb6J1Dv7wnvgysDFVApcpABfwMjHLmnEGvUCLthRfHNBDGydx9jvJQvismqdpDfcEaNBCo5SRMCqGS1FtKtpXjRaHGGFGcTfo9axnsJgAGxLk")
            .amount(Aer.ZERO)
            .constructorArgs(1, 2)
            .build();

        // make a contract deployment transaction
        AergoKey aergoKey = new AergoKeyGenerator().create();
        ChainIdHash chainIdHash = ChainIdHash.of("6YCMGJu3UN66ULzUuS5R7GTxXLDsSjRdjWPB94EiqMJc");
        RawTransaction rawTransaction = RawTransaction.newDeployContractBuilder()
            .chainIdHash(chainIdHash)
            .from(aergoKey.getAddress())
            .definition(definition)
            .nonce(1L)
            .fee(Fee.ZERO)
            .build();
        Transaction transaction = aergoKey.sign(rawTransaction);
        System.out.println("Contract deployment transaction: " + transaction);
      }

      /* Invoke Contract Transaction */
      {
        // make a contract invocation
        ContractInterface contractInterface = dummyContractInterface();
        ContractInvocation invocation = contractInterface.newInvocationBuilder()
            .function("set")
            .args("key", "123")
            .delegateFee(false)
            .build();

        // make a contract invocation transaction
        AergoKey aergoKey = new AergoKeyGenerator().create();
        ChainIdHash chainIdHash = ChainIdHash.of("6YCMGJu3UN66ULzUuS5R7GTxXLDsSjRdjWPB94EiqMJc");
        RawTransaction rawTransaction = RawTransaction.newInvokeContractBuilder()
            .chainIdHash(chainIdHash)
            .from(aergoKey.getAddress())
            .invocation(invocation)
            .nonce(1L)
            .fee(Fee.ZERO)
            .build();
        Transaction transaction = aergoKey.sign(rawTransaction);
        System.out.println("Invoke contract transaction: " + transaction);
      }

      /* Redeploy Contract */
      {
        // make an new contract definition
        ContractDefinition reDeployTarget = ContractDefinition.newBuilder()
            .encodedContract(
                "FppTEQaroys1N4P8RcAYYiEhHaQaRE9fzANUx4q2RHDXaRo6TYiTa61n25JcV19grEhpg8qdCWVdsDE2yVfuTKxxcdsTQA2B5zTfxA4GqeRqYGYgWJpj1geuLJAn1RjotdRRxSS1BFA6CAftxjcgiP6WUHacmgtNzoWViYESykhjqVLdmTfV12d44wfh9YAgQ57aRkLNCPkujbnJhdhHEtY1hrJYLCxUDBveqVcDhrrvcHtjDAUcZ5UMzbg6qR1kthGB1Lua6ymw1BmfySNtqb1b6Hp92UPMa7gi5FpAXF5XgpQtEbYDXMbtgu5XtXNhNejrtArcekmjrmPXRoTnMDGUQFcALtnNCrgSv2z5PiXP1coGEbHLTTbxkmJmJz6arEfsb6J1Dv7wnvgysDFVApcpABfwMjHLmnEGvUCLthRfHNBDGydx9jvJQvismqdpDfcEaNBCo5SRMCqGS1FtKtpXjRaHGGFGcTfo9axnsJgAGxLk")
            .amount(Aer.ZERO)
            .constructorArgs(1, 2)
            .build();

        // make a contract redeployment transaction
        AergoKey aergoKey = new AergoKeyGenerator().create();
        ChainIdHash chainIdHash = ChainIdHash.of("6YCMGJu3UN66ULzUuS5R7GTxXLDsSjRdjWPB94EiqMJc");
        RawTransaction rawTransaction = RawTransaction.newReDeployContractBuilder()
            .chainIdHash(chainIdHash)
            .creator(aergoKey.getAddress()) // must be creator
            .contractAddress(
                ContractAddress.of("AmJaNDXoPbBRn9XHh9onKbDKuAzj88n5Bzt7KniYA78qUEc5EwBd"))
            .definition(reDeployTarget)
            .nonce(1L)
            .fee(Fee.ZERO)
            .build();
        Transaction transaction = aergoKey.sign(rawTransaction);
        System.out.println("Contarct redeployment transaction: " + transaction);
      }

      /* Create Name */
      {
        // make an name creation transaction
        AergoKey aergoKey = new AergoKeyGenerator().create();
        ChainIdHash chainIdHash = ChainIdHash.of("6YCMGJu3UN66ULzUuS5R7GTxXLDsSjRdjWPB94EiqMJc");
        RawTransaction rawTransaction = RawTransaction.newCreateNameTxBuilder()
            .chainIdHash(chainIdHash)
            .from(aergoKey.getAddress())
            .name(Name.of("namenamename"))
            .nonce(1L)
            .build();
        Transaction transaction = aergoKey.sign(rawTransaction);
        System.out.println("Create name transaction: " + transaction);
      }

      /* Update Name */
      {
        // make an name update transaction
        AergoKey aergoKey = new AergoKeyGenerator().create();
        ChainIdHash chainIdHash = ChainIdHash.of("6YCMGJu3UN66ULzUuS5R7GTxXLDsSjRdjWPB94EiqMJc");
        RawTransaction rawTransaction = RawTransaction.newUpdateNameTxBuilder()
            .chainIdHash(chainIdHash)
            .from(aergoKey.getAddress())
            .name("namenamename")
            .nextOwner(AccountAddress.of("AmgVbUZiReUVFXdYb4UVMru4ZqyicSsFPqumRx8LfwMKLFk66SNw"))
            .nonce(1L)
            .build();
        Transaction transaction = aergoKey.sign(rawTransaction);
        System.out.println("Update name transaction: " + transaction);
      }

      /* Stake */
      {
        // make a stake transaction
        AergoKey aergoKey = new AergoKeyGenerator().create();
        ChainIdHash chainIdHash = ChainIdHash.of("6YCMGJu3UN66ULzUuS5R7GTxXLDsSjRdjWPB94EiqMJc");
        RawTransaction rawTransaction = RawTransaction.newStakeTxBuilder()
            .chainIdHash(chainIdHash)
            .from(aergoKey.getAddress())
            .amount(Aer.of("10000", Unit.AERGO))
            .nonce(1L)
            .build();
        Transaction transaction = aergoKey.sign(rawTransaction);
        System.out.println("Stake transaction: " + transaction);
      }

      /* Unstake */
      {
        // make a unstake transaction
        AergoKey aergoKey = new AergoKeyGenerator().create();
        ChainIdHash chainIdHash = ChainIdHash.of("6YCMGJu3UN66ULzUuS5R7GTxXLDsSjRdjWPB94EiqMJc");
        RawTransaction rawTransaction = RawTransaction.newUnstakeTxBuilder()
            .chainIdHash(chainIdHash)
            .from(aergoKey.getAddress())
            .amount(Aer.of("10000", Unit.AERGO))
            .nonce(1L)
            .build();
        Transaction transaction = aergoKey.sign(rawTransaction);
        System.out.println("Unstake transaction: " + transaction);
      }

      /* Vote */
      {
        // make a vote transaction
        AergoKey aergoKey = new AergoKeyGenerator().create();
        ChainIdHash chainIdHash = ChainIdHash.of("6YCMGJu3UN66ULzUuS5R7GTxXLDsSjRdjWPB94EiqMJc");
        RawTransaction rawTransaction = RawTransaction.newVoteTxBuilder()
            .chainIdHash(chainIdHash)
            .from(aergoKey.getAddress())
            .voteId("voteBP")
            .candidates(asList("123", "456"))
            .nonce(1L)
            .build();
        Transaction transaction = aergoKey.sign(rawTransaction);
        System.out.println("Vote transaction: " + transaction);
      }
    }

    /* Parse contract_payload to model */
    {
      /* Contract Invocation */
      {
        // make a contract invocation
        ContractInterface contractInterface = dummyContractInterface();
        ContractInvocation invocation = contractInterface.newInvocationBuilder()
            .function("set")
            .args("key", "123")
            .delegateFee(false)
            .build();

        // make a contract invocation transaction
        AergoKey aergoKey = new AergoKeyGenerator().create();
        ChainIdHash chainIdHash = ChainIdHash.of("6YCMGJu3UN66ULzUuS5R7GTxXLDsSjRdjWPB94EiqMJc");
        RawTransaction rawTransaction = RawTransaction.newInvokeContractBuilder()
            .chainIdHash(chainIdHash)
            .from(aergoKey.getAddress())
            .invocation(invocation)
            .nonce(1L)
            .fee(Fee.ZERO)
            .build();

        // parse contract invocation info
        PayloadConverter<ContractInvocation> invocationConverter =
            new ContractInvocationPayloadConverter();
        ContractInvocation parsedInvocation = invocationConverter
            .parseToModel(rawTransaction.getPayload());
        System.out.println("Parsed contract invocation: " + parsedInvocation.getAddress());
      }
    }

  }

  protected static ContractInterface dummyContractInterface() {
    final ContractAddress address =
        ContractAddress.of("AmJaNDXoPbBRn9XHh9onKbDKuAzj88n5Bzt7KniYA78qUEc5EwBd");
    final String version = "v1";
    final String language = "lua";
    final ContractFunction set = new ContractFunction("set", emptyList(), false, false, true);
    final ContractFunction get = new ContractFunction("get", emptyList(), false, true, false);
    final List<ContractFunction> functions = asList(set, get);
    final List<StateVariable> stateVariables = emptyList();
    return ContractInterface.newBuilder()
        .address(address)
        .version(version)
        .language(language)
        .functions(functions)
        .stateVariables(stateVariables)
        .build();
  }

}
