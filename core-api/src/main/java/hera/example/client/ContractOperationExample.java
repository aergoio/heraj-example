/*
 * @copyright defined in LICENSE.txt
 */

package hera.example.client;

import hera.api.model.AccountState;
import hera.api.model.ContractAddress;
import hera.api.model.ContractDefinition;
import hera.api.model.ContractInterface;
import hera.api.model.ContractInvocation;
import hera.api.model.ContractResult;
import hera.api.model.ContractTxReceipt;
import hera.api.model.Event;
import hera.api.model.EventFilter;
import hera.api.model.Fee;
import hera.api.model.StreamObserver;
import hera.api.model.Subscription;
import hera.api.model.TxHash;
import hera.api.transaction.NonceProvider;
import hera.api.transaction.SimpleNonceProvider;
import hera.client.AergoClient;
import hera.example.AbstractExample;
import hera.key.AergoKey;
import java.util.List;

public class ContractOperationExample extends AbstractExample {

  public static void main(String[] args) throws Exception {
    // prepare client and signer
    AergoClient client = getLocalClient();
    AergoKey richKey = getRichKey();

    // prepare nonce provider
    NonceProvider nonceProvider = new SimpleNonceProvider();
    AccountState state = client.getAccountOperation().getState(richKey.getAddress());
    nonceProvider.bindNonce(state);

    // prepare contractPayload
    String contractPayload = loadResource("/contract_payload");

    // prepare keep variables
    ContractAddress contractAddressKeep;
    ContractInterface contractInterfaceKeep;

    /* Deploy */
    // Deploy -> Wait -> Get contract tx receipt -> Find a contract address -> Get contract interface.
    {
      AergoKey signer = richKey;

      // made by aergoluac --compiledContract {some_contract}.lua
      String encodedContract = contractPayload;

      // make a contract definition
      ContractDefinition contractDefinition = ContractDefinition.newBuilder()
          .encodedContract(encodedContract)
          .build();

      // deploy
      long nonce = nonceProvider.incrementAndGetNonce(signer.getAddress());
      TxHash txHash = client.getContractOperation().deployTx(signer, contractDefinition,
          nonce, Fee.ZERO);
      System.out.println("Contract deployment tx hash: " + txHash);

      // wait deploy contract to be confirmed
      Thread.sleep(2200L);

      // get contract tx receipt
      ContractTxReceipt contractTxReceipt = client.getContractOperation()
          .getContractTxReceipt(txHash);
      System.out.println("Contract tx receipt: " + contractTxReceipt);

      // find a contract address
      ContractAddress contractAddress = contractTxReceipt.getContractAddress();

      // get contract interface
      ContractInterface contractInterface = client.getContractOperation()
          .getContractInterface(contractAddress);
      System.out.println("Contract interface: " + contractInterface);

      // ignore in docs
      contractInterfaceKeep = contractInterface;
      contractAddressKeep = contractInterface.getAddress();
    }

    /* Re-Deploy */
    {
      // prepare signer
      AergoKey signer = richKey;

      // made by aergoluac --compiledContract {some_contract}.lua
      String encodedContract = contractPayload;

      // make a contract definition
      ContractDefinition newDefinition = ContractDefinition.newBuilder()
          .encodedContract(encodedContract)
          .build();

      // redeploy
      ContractAddress contractAddress = contractAddressKeep;
      long nonce = nonceProvider.incrementAndGetNonce(signer.getAddress());
      TxHash txHash = client.getContractOperation()
          .redeployTx(signer, contractAddress, newDefinition, nonce, Fee.ZERO);
      System.out.println("Redeploy tx hash: " + txHash);
    }

    /* Get Contract Tx Receipt */
    // Get contract tx receipt.
    {
      TxHash txHash = TxHash.of("EGXNDgjY2vQ6uuP3UF3dNXud54dF4FNVY181kaeQ26H9");
      ContractTxReceipt contractTxReceipt = client.getContractOperation()
          .getContractTxReceipt(txHash);
      System.out.println("ContractTxReceipt: " + contractTxReceipt);
    }

    /* Get Contract Interface */
    // Get contract interface.
    {
      ContractAddress contractAddress = ContractAddress
          .of("AmNrsAqkXhQfE6sGxTutQkf9ekaYowaJFLekEm8qvDr1RB1AnsiM");
      ContractInterface contractInterface = client.getContractOperation()
          .getContractInterface(contractAddress);
      System.out.println("ContractInterface: " + contractInterface);
    }

    /* Execute */
    // Execute contract function.
    {
      // prepare signer
      AergoKey signer = richKey;

      // make a contract invocation
      ContractInterface contractInterface = contractInterfaceKeep;
      ContractInvocation invocation = contractInterface.newInvocationBuilder()
          .function("set")
          .args("key", 333, "test2")
          .build();

      // execute
      long nonce = nonceProvider.incrementAndGetNonce(signer.getAddress());
      TxHash txHash = client.getContractOperation()
          .executeTx(signer, invocation, nonce, Fee.ZERO);
      System.out.println("Execute tx hash: " + txHash);
    }

    Thread.sleep(2000L);

    /* Query */
    {
      // make a contract invocation
      ContractInterface contractInterface = contractInterfaceKeep;
      ContractInvocation query = contractInterface.newInvocationBuilder()
          .function("get")
          .args("key")
          .build();

      // query contract
      ContractResult queryResult = client.getContractOperation().query(query);
      Data data = queryResult.bind(Data.class);
      System.out.println("Raw contract result: " + queryResult);
      System.out.println("Binded data: " + data);
    }

    /* List Event */
    {
      ContractAddress contractAddress = contractAddressKeep;
      EventFilter eventFilter = EventFilter.newBuilder(contractAddress)
          .eventName("set")
          .args("key")
          .recentBlockCount(1000)
          .build();
      List<Event> events = client.getContractOperation().listEvents(eventFilter);
      System.out.println("Events: " + events);
    }

    /* Event Subscription */
    {
      // prepare signer
      AergoKey signer = richKey;

      // subscribe event
      ContractAddress contractAddress = contractAddressKeep;
      EventFilter eventFilter = EventFilter.newBuilder(contractAddress)
          .recentBlockCount(1000)
          .build();
      Subscription<Event> subscription = client.getContractOperation()
          .subscribeEvent(eventFilter, new StreamObserver<Event>() {
            @Override
            public void onNext(Event value) {
              System.out.println("Next event: " + value);
            }

            @Override
            public void onError(Throwable t) {
            }

            @Override
            public void onCompleted() {
            }
          });

      // execute
      ContractInterface contractInterface = contractInterfaceKeep;
      ContractInvocation run = contractInterface.newInvocationBuilder()
          .function("set")
          .args("key", 333, "test2")
          .build();
      long nonce = nonceProvider.incrementAndGetNonce(signer.getAddress());
      client.getContractOperation().executeTx(signer, run, nonce, Fee.ZERO);
      Thread.sleep(2200L);

      // unsubscribe event
      subscription.unsubscribe();
    }

    client.close();
  }

}

