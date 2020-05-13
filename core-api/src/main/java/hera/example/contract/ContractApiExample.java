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

    /* ContractApi */
    {
      // deploy contract

      // prepare signer
      AergoKey signer = richKey;

      // make a keystore
      KeyStore keyStore = KeyStores.newInMemoryKeyStore();
      Identity identity = signer.getAddress();
      String password = "password";
      Authentication authentication = Authentication.of(identity, password);
      keyStore.save(authentication, signer);

      // create a wallet api
      WalletApi walletApi = new WalletApiFactory().create(keyStore);

      // make a contract definition
      String encodedContract = contractPayload;
      ContractDefinition contractDefinition = ContractDefinition.newBuilder()
          .encodedContract(encodedContract)
          .build();

      // deploy contract
      walletApi.unlock(authentication);
      TxHash txHash = walletApi.with(client).transaction().deploy(contractDefinition, Fee.INFINITY);
      walletApi.lock();

      // sleep
      Thread.sleep(2000L);

      // get ContractTxReceipt
      ContractTxReceipt contractTxReceipt = walletApi.with(client).query()
          .getContractTxReceipt(txHash);

      // get contract address
      ContractAddress contractAddress = contractTxReceipt.getContractAddress();

      /* Execute */
      {
        // create a contract api
        ContractApi<CustomInterface1> contractApi = new ContractApiFactory()
            .create(contractAddress, CustomInterface1.class);

        // execute contract with a contract api
        walletApi.unlock(authentication);
        TxHash executeTxHash = contractApi.with(client).execution(walletApi)
            .set("key", 123, "test", Fee.INFINITY);
        walletApi.lock();
        System.out.println("Execute tx hash: " + executeTxHash);
      }

      // sleep
      Thread.sleep(2000L);

      /* Query */
      {
        // With model binded.
        {
          // create a contract api
          ContractApi<CustomInterface1> contractApi = new ContractApiFactory()
              .create(contractAddress, CustomInterface1.class);

          // query contract with a contract api
          Data data = contractApi.with(client).query().get("key");
          System.out.println("Queried data: " + data);
        }

        // Without binded model.
        {
          // create a contract api
          ContractApi<CustomInterface2> contractApi = new ContractApiFactory()
              .create(contractAddress, CustomInterface2.class);

          // query contract with a contract api
          ContractResult contractResult = contractApi.with(client).query().get("key");
          System.out.println("Queried data: " + contractResult);
        }
      }

    }

    client.close();
  }

  interface CustomInterface1 {

    TxHash set(String key, int arg1, String args2, Fee fee);

    Data get(String key);

  }

  interface CustomInterface2 {

    TxHash set(String key, int arg1, String args2, Fee fee);

    ContractResult get(String key);

  }

}
