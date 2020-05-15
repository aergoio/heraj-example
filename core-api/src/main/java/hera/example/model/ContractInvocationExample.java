/*
 * @copyright defined in LICENSE.txt
 */

package hera.example.model;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

import hera.api.model.Aer;
import hera.api.model.ContractAddress;
import hera.api.model.ContractFunction;
import hera.api.model.ContractInterface;
import hera.api.model.ContractInvocation;
import hera.api.model.StateVariable;
import hera.example.AbstractExample;
import java.util.List;

public class ContractInvocationExample extends AbstractExample {

  public static void main(String[] args) {
    // prepare dummy contract interface
    ContractInterface contractInterfaceKeep = dummyContractInterface();

    /* Make */
    {
      // Without args.
      {
        // make a contract invocation
        ContractInterface contractInterface = contractInterfaceKeep;
        ContractInvocation contractInvocation = contractInterface.newInvocationBuilder()
            .function("set")
            .build();
        System.out.println("Contract invocation: " + contractInvocation);
      }

      // With args.
      {
        // make a contract invocation
        ContractInterface contractInterface = contractInterfaceKeep;
        ContractInvocation contractInvocation = contractInterface.newInvocationBuilder()
            .function("set")
            .args("key", 333, "test2")
            .build();
        System.out.println("Contract invocation: " + contractInvocation);
      }

      // With args and amount.
      {
        // make a contract invocation
        ContractInterface contractInterface = contractInterfaceKeep;
        ContractInvocation contractInvocation = contractInterface.newInvocationBuilder()
            .function("set")
            .args("key", 333, "test2")
            .amount(Aer.AERGO_ONE)
            .build();
        System.out.println("Contract invocation: " + contractInvocation);
      }

      // With args and fee delegation.
      {
        // make a contract invocation
        ContractInterface contractInterface = contractInterfaceKeep;
        ContractInvocation contractInvocation = contractInterface.newInvocationBuilder()
            .function("set")
            .args("key", 333, "test2")
            .delegateFee(true)
            .build();
        System.out.println("Contract invocation: " + contractInvocation);
      }
    }

  }

  protected static ContractInterface dummyContractInterface() {
    final ContractAddress address =
        ContractAddress.of("AmJaNDXoPbBRn9XHh9onKbDKuAzj88n5Bzt7KniYA78qUEc5EwBd");
    final String version = "v1";
    final String language = "lua";
    final ContractFunction set = new ContractFunction("set", emptyList(), true, false, true);
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
