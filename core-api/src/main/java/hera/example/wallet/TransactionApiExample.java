/*
 * @copyright defined in LICENSE.txt
 */

package hera.example.wallet;

import static java.util.Arrays.asList;

import hera.api.model.AccountAddress;
import hera.api.model.AccountState;
import hera.api.model.Aer;
import hera.api.model.Aer.Unit;
import hera.api.model.Authentication;
import hera.api.model.BytesValue;
import hera.api.model.ChainIdHash;
import hera.api.model.ContractAddress;
import hera.api.model.ContractDefinition;
import hera.api.model.ContractInterface;
import hera.api.model.ContractInvocation;
import hera.api.model.ContractTxReceipt;
import hera.api.model.Fee;
import hera.api.model.Identity;
import hera.api.model.Name;
import hera.api.model.RawTransaction;
import hera.api.model.Transaction;
import hera.api.model.TxHash;
import hera.client.AergoClient;
import hera.example.AbstractExample;
import hera.key.AergoKey;
import hera.keystore.KeyStore;
import hera.keystore.KeyStores;
import hera.wallet.WalletApi;
import hera.wallet.WalletApiFactory;
import java.util.List;

public class TransactionApiExample extends AbstractExample {

  public static void main(String[] args) throws Exception {
    // prepare client and signer
    AergoClient client = getLocalClient();
    AergoKey signer = getRichKey();

    // prepare contractPayload
    String contractPayload = loadResource("/contract_payload");

    // prepare authentication and keystore
    KeyStore keyStore = KeyStores.newInMemoryKeyStore();
    Identity identity = signer.getAddress();
    String password = "password";
    Authentication authentication = Authentication.of(identity, password);
    keyStore.save(authentication, signer);

    // create a wallet api
    WalletApi walletApi = new WalletApiFactory().create(keyStore);

    // prepare keep variables
    ContractAddress contractAddressKeep;
    ContractInterface contractInterfaceKeep;

    /* Create Name */
    {
      // unlock specific account with authentication
      walletApi.unlock(authentication);

      Name name = randomName();
      TxHash txHash = walletApi.with(client).transaction().createName(name);
      System.out.println("Create name tx hash: " + txHash);

      // lock an account
      walletApi.lock();
    }

    Thread.sleep(2000L);

    /* Update Name */
    {
      // unlock specific account with authentication
      walletApi.unlock(authentication);

      // create an name
      Name name = randomName();
      walletApi.with(client).transaction().createName(name);

      // sleep
      Thread.sleep(2000L);

      // update an name
      AccountAddress nextOwner = AccountAddress
          .of("AmNrsAqkXhQfE6sGxTutQkf9ekaYowaJFLekEm8qvDr1RB1AnsiM");
      TxHash txHash = walletApi.with(client).transaction().updateName(name, nextOwner);
      System.out.println("Update name tx hash: " + txHash);

      // lock an account
      walletApi.lock();
    }

    Thread.sleep(2000L);

    /* Stake */
    {
      // unlock specific account with authentication
      walletApi.unlock(authentication);

      // stake
      TxHash txHash = walletApi.with(client).transaction().stake(Aer.of("10000", Unit.AERGO));
      System.out.println("Stake tx hash: " + txHash);

      // lock an account
      walletApi.lock();
    }

    Thread.sleep(2000L);

    // commented since unstake is invalid just after staking
    /* Unstake */
    {
      // unlock specific account with authentication
      walletApi.unlock(authentication);

      // unstake
//      TxHash txHash = walletApi.with(client).transaction().unstake(Aer.of("10000", Unit.AERGO));
//      System.out.println("Unstake tx hash: " + txHash);

      // lock an account
      walletApi.lock();
    }

    /* Vote */
    {
      // unlock specific account with authentication
      walletApi.unlock(authentication);

      // vote to "voteBP"
      List<String> candidates = asList("16Uiu2HAkwWbv8nKx7S6S5NMvUpTLNeXMVCPr3NTnrx6rBPYYiQ4K");
      TxHash txHash = walletApi.with(client).transaction().vote("voteBp", candidates);
      System.out.println("Vote tx hash: " + txHash);

      // lock an account
      walletApi.lock();
    }

    Thread.sleep(2000L);

    /* Send */
    {
      // Send without contract_payload to address.
      {
        // unlock specific account with authentication
        walletApi.unlock(authentication);

        // send
        AccountAddress accountAddress = AccountAddress
            .of("AmNrsAqkXhQfE6sGxTutQkf9ekaYowaJFLekEm8qvDr1RB1AnsiM");
        TxHash txHash = walletApi.with(client).transaction()
            .send(accountAddress, Aer.AERGO_ONE, Fee.INFINITY);
        System.out.println("Send tx hash: " + txHash);

        // lock an account
        walletApi.lock();
      }

      Thread.sleep(2000L);

      // Send with contract_payload to address.
      {
        // unlock specific account with authentication
        walletApi.unlock(authentication);

        // send
        AccountAddress accountAddress = AccountAddress
            .of("AmNrsAqkXhQfE6sGxTutQkf9ekaYowaJFLekEm8qvDr1RB1AnsiM");
        BytesValue payload = BytesValue.of("test".getBytes());
        TxHash txHash = walletApi.with(client).transaction()
            .send(accountAddress, Aer.AERGO_ONE, Fee.INFINITY, payload);
        System.out.println("Send tx hash: " + txHash);

        // lock an account
        walletApi.lock();
      }

      Thread.sleep(2000L);

      // Send without contract_payload to name.
      {
        // unlock specific account with authentication
        walletApi.unlock(authentication);

        // create an name
        Name name = randomName();
        walletApi.with(client).transaction().createName(name);

        // sleep
        Thread.sleep(2000L);

        // send
        TxHash txHash = walletApi.with(client).transaction()
            .send(name, Aer.AERGO_ONE, Fee.INFINITY);
        System.out.println("Send tx hash: " + txHash);

        // lock an account
        walletApi.lock();
      }

      Thread.sleep(2000L);

      // Send with contract_payload to name.
      {
        // unlock specific account with authentication
        walletApi.unlock(authentication);

        // create an name
        Name name = randomName();
        walletApi.with(client).transaction().createName(name);

        // sleep
        Thread.sleep(2000L);

        // send
        BytesValue payload = BytesValue.of("test".getBytes());
        TxHash txHash = walletApi.with(client).transaction()
            .send(name, Aer.AERGO_ONE, Fee.INFINITY, payload);
        System.out.println("Send tx hash: " + txHash);

        // lock an account
        walletApi.lock();
      }
    }

    Thread.sleep(2000L);

    /* Commit */
    {
      // Sign with unlocked one and commit it.
      {
        // unlock specific account with authentication
        walletApi.unlock(authentication);

        // create a raw transaction
        AccountAddress current = walletApi.getPrincipal();
        ChainIdHash chainIdHash = walletApi.with(client).query().getChainIdHash();
        AccountState currentState = walletApi.with(client).query().getAccountState(current);
        RawTransaction rawTransaction = RawTransaction.newBuilder()
            .chainIdHash(chainIdHash)
            .from(current)
            .to(current)
            .amount(Aer.AERGO_ONE)
            .nonce(currentState.getNonce() + 1L)
            .build();

        // commit
        TxHash txHash = walletApi.with(client).transaction().commit(rawTransaction);
        System.out.println("Commit tx hash: " + txHash);

        // lock an account
        walletApi.lock();
      }

      Thread.sleep(2000L);

      // Commit signed transaction.
      {
        // unlock specific account with authentication
        walletApi.unlock(authentication);

        // create a signed transaction
        AccountAddress current = walletApi.getPrincipal();
        ChainIdHash chainIdHash = walletApi.with(client).query().getChainIdHash();
        AccountState currentState = walletApi.with(client).query().getAccountState(current);
        RawTransaction rawTransaction = RawTransaction.newBuilder()
            .chainIdHash(chainIdHash)
            .from(current)
            .to(current)
            .amount(Aer.AERGO_ONE)
            .nonce(currentState.getNonce() + 1L)
            .build();
        Transaction signed = walletApi.sign(rawTransaction);

        // commit
        TxHash txHash = walletApi.with(client).transaction().commit(signed);
        System.out.println("Commit tx hash: " + txHash);

        // lock an account
        walletApi.lock();
      }
    }

    Thread.sleep(2000L);

    /* Deploy */
    {
      // unlock specific account with authentication
      walletApi.unlock(authentication);

      // make a contract definition
      String encodedContract = contractPayload;
      ContractDefinition contractDefinition = ContractDefinition.newBuilder()
          .encodedContract(encodedContract)
          .build();

      // deploy contract
      TxHash txHash = walletApi.with(client).transaction().deploy(contractDefinition, Fee.INFINITY);
      System.out.println("Deploy tx hash: " + txHash);

      // sleep
      Thread.sleep(2000L);

      // get ContractTxReceipt
      ContractTxReceipt contractTxReceipt = walletApi.with(client).query()
          .getContractTxReceipt(txHash);
      System.out.println("Deployed contract tx receipt: " + contractTxReceipt);

      // get contract interface
      ContractAddress contractAddress = contractTxReceipt.getContractAddress();
      ContractInterface contractInterface = walletApi.with(client).query()
          .getContractInterface(contractAddress);
      System.out.println("Deployed contract interface: " + contractInterface);

      // lock an account
      walletApi.lock();

      // keep (not include in docs)
      contractAddressKeep = contractAddress;
      contractInterfaceKeep = contractInterface;
    }

    Thread.sleep(2000L);

    /* Re-Deploy */
    {
      // unlock specific account with authentication
      walletApi.unlock(authentication);

      // made by aergoluac --payload {some_contract}.lua
      String encodedContract = contractPayload;

      // make a contract definition
      ContractDefinition newDefinition = ContractDefinition.newBuilder()
          .encodedContract(encodedContract)
          .build();

      // redeploy
      ContractAddress contractAddress = contractAddressKeep;
      TxHash txHash = walletApi.with(client).transaction()
          .redeploy(contractAddress, newDefinition, Fee.INFINITY);
      System.out.println("Redeploy tx hash: " + txHash);

      // lock an account
      walletApi.lock();
    }

    Thread.sleep(2000L);

    /* Execute */
    {
      // unlock specific account with authentication
      walletApi.unlock(authentication);

      // make a contract invocation
      ContractInterface contractInterface = contractInterfaceKeep;
      ContractInvocation contractInvocation = contractInterface.newInvocationBuilder()
          .function("set")
          .args("key", 333, "test2")
          .build();

      // execute
      TxHash txHash = walletApi.with(client).transaction()
          .execute(contractInvocation, Fee.INFINITY);
      System.out.println("Execute tx hash: " + txHash);

      // lock an account
      walletApi.lock();
    }

    client.close();
  }

}
