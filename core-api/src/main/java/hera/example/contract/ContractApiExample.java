/*
 * @copyright defined in LICENSE.txt
 */

package hera.example.contract;

import hera.api.model.Authentication;
import hera.api.model.ContractAddress;
import hera.api.model.ContractDefinition;
import hera.api.model.ContractResult;
import hera.api.model.ContractTxReceipt;
import hera.api.model.Fee;
import hera.api.model.Identity;
import hera.api.model.Time;
import hera.api.model.TryCountAndInterval;
import hera.api.model.TxHash;
import hera.client.AergoClient;
import hera.contract.ContractApi;
import hera.contract.ContractApiFactory;
import hera.example.AbstractExample;
import hera.key.AergoKey;
import hera.keystore.KeyStore;
import hera.keystore.KeyStores;
import hera.wallet.WalletApi;
import hera.wallet.WalletApiFactory;

public class ContractApiExample extends AbstractExample {

  public static void main(String[] args) throws Exception {
    // prepare objects
    AergoKey richKey = getRichKey();
    AergoClient client = getLocalClient();
    String contractPayload = loadResource("/contract_payload");

    // make a keystore
    KeyStore keyStore = KeyStores.newInMemoryKeyStore();
    Identity identity = richKey.getAddress();
    String password = "password";
    Authentication authentication = Authentication.of(identity, password);
    keyStore.save(authentication, richKey);

    // create a wallet api
    WalletApi walletApi = new WalletApiFactory().create(keyStore);

    // prepare keeps
    ContractAddress deployedContractAddress;

    /* Make */
    {
      // Deploy a contract.
      {
        // make a contract definition
        String encodedContract = contractPayload;
        ContractDefinition contractDefinition = ContractDefinition.newBuilder()
            .encodedContract(encodedContract)
            .build();

        // deploy contract
        walletApi.unlock(authentication);
        TxHash txHash = walletApi.with(client).transaction()
            .deploy(contractDefinition, Fee.INFINITY);
        walletApi.lock();

        // sleep
        Thread.sleep(2000L);

        // get ContractTxReceipt
        ContractTxReceipt contractTxReceipt = walletApi.with(client).query()
            .getContractTxReceipt(txHash);

        // get contract address
        ContractAddress contractAddress = contractTxReceipt.getContractAddress();
        System.out.println("Deployed contract address: " + contractAddress);

        // do not include in docs
        deployedContractAddress = contractAddress;
      }

      // Write an interface. Interface methods should matches with smart contract functions.
      {
        // CustomInterface1 definition

        // Data definition
      }

      // Make a contract api with implicit retry count and interval on nonce failure.
      {
        // create a contract api
        ContractAddress contractAddress = deployedContractAddress;
        ContractApi<CustomInterface1> contractApi = new ContractApiFactory()
            .create(contractAddress, CustomInterface1.class);
        System.out.println("ContractApi: " + contractApi);
      }

      // Make a contract api with explicit retry count and interval on nonce failure.
      {
        // create a contract api with retry count 5 and interval 1000ms
        ContractAddress contractAddress = deployedContractAddress;
        TryCountAndInterval tryCountAndInterval = TryCountAndInterval.of(5, Time.of(1000L));
        ContractApi<CustomInterface1> contractApi = new ContractApiFactory()
            .create(contractAddress, CustomInterface1.class, tryCountAndInterval);
        System.out.println("ContractApi: " + contractApi);
      }
    }

    /* Execute */
    {
      // With an aergo key.
      {
        // prepare an signer
        AergoKey signer = richKey;

        // create a contract api
        ContractAddress contractAddress = deployedContractAddress;
        ContractApi<CustomInterface1> contractApi = new ContractApiFactory()
            .create(contractAddress, CustomInterface1.class);

        // execute contract with a contract api
        TxHash executeTxHash = contractApi.with(client).execution(signer)
            .set("key", 123, "test", Fee.INFINITY);
        System.out.println("Execute tx hash: " + executeTxHash);
      }

      // sleep
      Thread.sleep(2000L);

      // With a wallet api.
      {
        // create a contract api
        ContractAddress contractAddress = deployedContractAddress;
        ContractApi<CustomInterface1> contractApi = new ContractApiFactory()
            .create(contractAddress, CustomInterface1.class);

        // execute contract with a contract api
        walletApi.unlock(authentication);
        TxHash executeTxHash = contractApi.with(client).execution(walletApi)
            .set("key", 123, "test", Fee.INFINITY);
        walletApi.lock();
        System.out.println("Execute tx hash: " + executeTxHash);
      }
    }

    // sleep
    Thread.sleep(2000L);

    /* Query */
    {
      // With a model binded.
      {
        // create a contract api
        ContractAddress contractAddress = deployedContractAddress;
        ContractApi<CustomInterface1> contractApi = new ContractApiFactory()
            .create(contractAddress, CustomInterface1.class);

        // query contract with a contract api
        Data data = contractApi.with(client).query().get("key");
        System.out.println("Queried data: " + data);
      }

      // Without binded model.
      {
        // create a contract api
        ContractAddress contractAddress = deployedContractAddress;
        ContractApi<CustomInterface2> contractApi = new ContractApiFactory()
            .create(contractAddress, CustomInterface2.class);

        // query contract with a contract api
        ContractResult contractResult = contractApi.with(client).query().get("key");
        System.out.println("Queried data: " + contractResult);
      }
    }

    client.close();
  }

  // interface for smart contract
  interface CustomInterface1 {

    /*
      Matches with

        function set(key, arg1, arg2)
          ...
        end

        ...

        abi.register(set)

      And it also uses provided fee when making transaction.
     */
    TxHash set(String key, int arg1, String args2, Fee fee);

    /*
      Matches with

        function set(key, arg1, arg2)
          ...
        end

        ...

        abi.register(set)

      And it also uses Fee.INFINITY when making transaction.
     */
    TxHash set(String key, int arg1, String args2);

    /*
      Matches with

        function get(key)
          ...
          -- returns lua table which can be binded with Data class
          return someVal
        end

        ...

        abi.register_view(get)
     */
    Data get(String key);

  }

  // interface for smart contract
  interface CustomInterface2 {

    /*
      Matches with

        function get(key)
          ...
          return someVal
        end

        ...

        abi.register_view(get)
     */
    ContractResult get(String key);

  }

}
