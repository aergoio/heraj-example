/*
 * @copyright defined in LICENSE.txt
 */

package hera.example.model;

import hera.api.model.Aer;
import hera.api.model.ContractDefinition;
import hera.example.AbstractExample;

public class ContractDefinitionExample extends AbstractExample {

  public static void main(String[] args) {
    // prepare contractPayload
    String contractPayload = loadResource("/contract_payload");

    /* Make */
    {
      // Without args.
      {
        // made by aergoluac --compiledContract {some_contract}.lua
        String encodedContract = contractPayload;

        // make a contract definition
        ContractDefinition contractDefinition = ContractDefinition.newBuilder()
            .encodedContract(encodedContract)
            .build();
        System.out.println("Contract definition: " + contractDefinition);
      }

      // With args.
      {
        // made by aergoluac --compiledContract {some_contract}.lua
        String encodedContract = contractPayload;

        // make a contract definition
        ContractDefinition contractDefinition = ContractDefinition.newBuilder()
            .encodedContract(encodedContract)
            .constructorArgs("key", 123, "test")
            .build();
        System.out.println("Contract definition: " + contractDefinition);
      }

      // With args and amount.
      {
        // made by aergoluac --compiledContract {some_contract}.lua
        String encodedContract = contractPayload;

        // make a contract definition
        ContractDefinition contractDefinition = ContractDefinition.newBuilder()
            .encodedContract(encodedContract)
            .constructorArgs("key", 123, "test")
            .amount(Aer.AERGO_ONE)
            .build();
        System.out.println("Contract definition: " + contractDefinition);
      }
    }

  }
}
