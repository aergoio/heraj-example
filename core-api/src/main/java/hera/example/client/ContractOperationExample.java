/*
 * @copyright defined in LICENSE.txt
 */

package hera.example.client;

import hera.api.model.Aer;
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
import hera.client.AergoClient;
import hera.example.AbstractExample;
import hera.key.AergoKey;
import java.util.List;

public class ContractOperationExample extends AbstractExample {

  public static void main(String[] args) throws Exception {
    AergoClient client = getLocalClient();
    AergoKey richKey = getRichKey();
    String payload = loadResource("/payload");
    ContractInterface contractInterfaceKeep;
    ContractAddress contractAddressKeep;

    /* Deployment */
    // Deploy contract.
    {
      // Without args
      {
        // prepare signer
        AergoKey signer = richKey;

        // made by aergoluac --payload {some_contract}.lua
        String encodedContract = payload;

        // make a contract definition
        ContractDefinition contractDefinition = ContractDefinition.newBuilder()
            .encodedContract(encodedContract)
            .build();
        TxHash txHash = client.getContractOperation()
            .deployTx(signer, contractDefinition, 1L, Fee.ZERO);
      }

      // With args
      {
        AergoKey signer = richKey;

        // made by aergoluac --payload {some_contract}.lua
        String encodedContract = payload;

        // make a contract definition
        ContractDefinition contractDefinition = ContractDefinition.newBuilder()
            .encodedContract(encodedContract)
            .constructorArgs("key", 123, "test")
            .build();
        TxHash txHash = client.getContractOperation()
            .deployTx(signer, contractDefinition, 2L, Fee.ZERO);
      }

      // With args and amount
      {
        AergoKey signer = richKey;
        Aer amount = Aer.AERGO_ONE;
        ContractDefinition contractDefinition = ContractDefinition.newBuilder()
            .encodedContract(
                "4coHBeUgiMBpiGmZ9McPhAXWt7Ymk8WoTipWvvHrMytjtLqVqwvKo2VqGjFYHLGT2vpvYCcB1AmMLGCbf9BPVE8KSfEqXTvc2TunE2Pp2ZjtErng1odLHttTrg2LBtT5CqWrLwwVEv8Wi8BQjvenrEvrVw3apJoJjGJFWP1eNX9hU7ahAtkMCYXDp3EaD3Vr6tpXwD61N3tvG8ejqQtUnkzNaHGauNUU8PrzXBXsa4a2WUrWAFzxV1e6mwk2tL8aZtzvBGm2LGrcXVaADyomk9a58FNG6YVKwLHwcttnxKi6CWRRe5ueCBZuAxxR1DQWRzCzUs4oEZY7MpEMmMdqgN5QLw4dWQDQfj36a94NpLN9DcbeXesQ8fmFVXZBJzRoZLuQg5DQGNsu5upKAq6fndvpJ3puurDkztYKawToTtDqBjbbAkwUd3FcSHjogeiY6odoxFQvVJcLjpbBwMVboSVsnn3p1XYdUFvZ3KpjJzLd8D3JbozGSoqED1X8Re7NsGJS5JZiYdrzSyPCHjkEd1SN1ssvniFVJBJVZkojTQ1PMgT6P5ZakQC2SEw42t52BdPujr4iVsgkqGKbry3ouXef8wA8c7tJGazqa7dZ2RBKY3Bxerz4LFJsVupF6SKpXnDjJwedHiuhgj1EvZ162SqkUrakk53JUwtdL5QPFG67nLar9ZHro41xc2k3MvH1rouDYpMnRbrHxz2ZkRvyQ75CxryKm1EyT2WEC51L41R8YA8DMhPMVpSUwcYr6zVLvEmvoXL2AGz1QZNyUdzRLeQ9sYqzkkSMkXaMLG3wuLP2nGZjVbKN8f4fssRLA8x2K5jkZW4TTHuZwM4j5bfuRnNp2XH9xUiK8wfiH7W8xr6j")
            .constructorArgs("key", 123, "test")
            .amount(amount)
            .build();
        TxHash txHash = client.getContractOperation()
            .deployTx(signer, contractDefinition, 3L, Fee.ZERO);
      }

      /* Deployment Process */
      // Deploy -> Wait -> Get contract tx receipt -> Find a contract address -> Get contract interface.
      {
        AergoKey signer = richKey;

        // deploy
        ContractDefinition contractDefinition = ContractDefinition.newBuilder()
            .encodedContract(
                "4coHBeUgiMBpiGmZ9McPhAXWt7Ymk8WoTipWvvHrMytjtLqVqwvKo2VqGjFYHLGT2vpvYCcB1AmMLGCbf9BPVE8KSfEqXTvc2TunE2Pp2ZjtErng1odLHttTrg2LBtT5CqWrLwwVEv8Wi8BQjvenrEvrVw3apJoJjGJFWP1eNX9hU7ahAtkMCYXDp3EaD3Vr6tpXwD61N3tvG8ejqQtUnkzNaHGauNUU8PrzXBXsa4a2WUrWAFzxV1e6mwk2tL8aZtzvBGm2LGrcXVaADyomk9a58FNG6YVKwLHwcttnxKi6CWRRe5ueCBZuAxxR1DQWRzCzUs4oEZY7MpEMmMdqgN5QLw4dWQDQfj36a94NpLN9DcbeXesQ8fmFVXZBJzRoZLuQg5DQGNsu5upKAq6fndvpJ3puurDkztYKawToTtDqBjbbAkwUd3FcSHjogeiY6odoxFQvVJcLjpbBwMVboSVsnn3p1XYdUFvZ3KpjJzLd8D3JbozGSoqED1X8Re7NsGJS5JZiYdrzSyPCHjkEd1SN1ssvniFVJBJVZkojTQ1PMgT6P5ZakQC2SEw42t52BdPujr4iVsgkqGKbry3ouXef8wA8c7tJGazqa7dZ2RBKY3Bxerz4LFJsVupF6SKpXnDjJwedHiuhgj1EvZ162SqkUrakk53JUwtdL5QPFG67nLar9ZHro41xc2k3MvH1rouDYpMnRbrHxz2ZkRvyQ75CxryKm1EyT2WEC51L41R8YA8DMhPMVpSUwcYr6zVLvEmvoXL2AGz1QZNyUdzRLeQ9sYqzkkSMkXaMLG3wuLP2nGZjVbKN8f4fssRLA8x2K5jkZW4TTHuZwM4j5bfuRnNp2XH9xUiK8wfiH7W8xr6j")
            .build();
        TxHash txHash = client.getContractOperation()
            .deployTx(signer, contractDefinition, 4L, Fee.ZERO);

        // wait deploy contract to be confirmed
        Thread.sleep(2200L);

        // get contract tx receipt
        ContractTxReceipt contractTxReceipt = client.getContractOperation()
            .getContractTxReceipt(txHash);

        // find a contract address
        ContractAddress contractAddress = contractTxReceipt.getContractAddress();

        // get contract interface
        ContractInterface contractInterface = client.getContractOperation()
            .getContractInterface(contractAddress);

        // ignore in docs
        contractInterfaceKeep = contractInterface;
        contractAddressKeep = contractInterface.getAddress();
      }
    }

    /* Get Contract Tx Receipt */
    // Get contract tx receipt.
    {
      TxHash txHash = TxHash.of("EGXNDgjY2vQ6uuP3UF3dNXud54dF4FNVY181kaeQ26H9");
      ContractTxReceipt contractTxReceipt = client.getContractOperation()
          .getContractTxReceipt(txHash);
    }

    /* Get Contract Interface */
    // Get contract interface.
    {
      ContractAddress contractAddress = ContractAddress
          .of("AmNrsAqkXhQfE6sGxTutQkf9ekaYowaJFLekEm8qvDr1RB1AnsiM");
      ContractInterface contractInterface = client.getContractOperation()
          .getContractInterface(contractAddress);
    }

    /* Execute */
    // Execute contract function.
    {
      {

      }
      AergoKey signer = richKey;
      ContractInterface contractInterface = contractInterfaceKeep;
      ContractInvocation execution = contractInterface.newInvocationBuilder()
          .function("set")
          .args("key", 333, "test2")
          .build();
      TxHash txHash = client.getContractOperation()
          .executeTx(signer, execution, 4L, Fee.ZERO);
    }

    Thread.sleep(2000L);

    /* Query */
    {
      class Data {

        protected int intVal;

        protected String stringVal;

        public int getIntVal() {
          return intVal;
        }

        public void setIntVal(int intVal) {
          this.intVal = intVal;
        }

        public String getStringVal() {
          return stringVal;
        }

        public void setStringVal(String stringVal) {
          this.stringVal = stringVal;
        }

        @Override
        public String toString() {
          return "Data [intVal=" + intVal + ", stringVal=" + stringVal + "]";
        }

      }

      ContractInterface contractInterface = contractInterfaceKeep;
      ContractInvocation query = contractInterface.newInvocationBuilder()
          .function("get")
          .args("key")
          .build();
      ContractResult queryResult = client.getContractOperation().query(query);
      Data data = queryResult.bind(Data.class);
    }

    /* List Event */
    {
      // By block number
      {
        // get event of specific address in block 1 ~ 10
        ContractAddress contractAddress = contractAddressKeep;
        EventFilter eventFilter = EventFilter.newBuilder(contractAddress)
            .fromBlockNumber(1L)
            .toBlockNumber(10L)
            .build();
        List<Event> events = client.getContractOperation().listEvents(eventFilter);
      }
      // Of recent block
      {
        // get event of specific address in recent 1000 block
        ContractAddress contractAddress = contractAddressKeep;
        EventFilter eventFilter = EventFilter.newBuilder(contractAddress)
            .eventName("set")
            .recentBlockCount(1000)
            .build();
        List<Event> events = client.getContractOperation().listEvents(eventFilter);
      }
      // By event name and args
      {
        // get event of specific address with name "set" and args "key" in recent 1000 block
        ContractAddress contractAddress = contractAddressKeep;
        EventFilter eventFilter = EventFilter.newBuilder(contractAddress)
            .eventName("set")
            .args("key")
            .recentBlockCount(1000)
            .build();
        List<Event> events = client.getContractOperation().listEvents(eventFilter);
      }
    }

    /* subscribe event */
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
      client.getContractOperation().execute(signer, run, 5L, Fee.ZERO);
      Thread.sleep(2200L);

      // unsubscribe event
      subscription.unsubscribe();
    }

    /* Re Deploy */
    {
      // prepare signer
      AergoKey signer = richKey;

      ContractAddress alreadyDeployed = contractAddressKeep;
      // made by aergoluac --payload {some_contract}.lua
      String encodedContract = payload;
      ContractDefinition newDefinition = ContractDefinition.newBuilder()
          .encodedContract(encodedContract)
          .build();
      TxHash txHash = client.getContractOperation()
          .redeployTx(signer, alreadyDeployed, newDefinition, 6L, Fee.ZERO);
    }

    client.close();
  }

}
