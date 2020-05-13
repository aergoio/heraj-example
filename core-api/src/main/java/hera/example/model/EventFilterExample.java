/*
 * @copyright defined in LICENSE.txt
 */

package hera.example.model;

import hera.api.model.ContractAddress;
import hera.api.model.EventFilter;
import hera.example.AbstractExample;

public class EventFilterExample extends AbstractExample {

  public static void main(String[] args) throws Exception {

    /* Event Filter */
    {
      // With block bumber.
      {
        // set event filter for specific address in block 1 ~ 10
        ContractAddress contractAddress = ContractAddress
            .of("AmNrsAqkXhQfE6sGxTutQkf9ekaYowaJFLekEm8qvDr1RB1AnsiM");
        EventFilter eventFilter = EventFilter.newBuilder(contractAddress)
            .fromBlockNumber(1L)
            .toBlockNumber(10L)
            .build();
        System.out.println("Event filter: " + eventFilter);
      }

      // Of recent block
      {
        // set event filter for specific address in recent 1000 block
        ContractAddress contractAddress = ContractAddress
            .of("AmNrsAqkXhQfE6sGxTutQkf9ekaYowaJFLekEm8qvDr1RB1AnsiM");
        EventFilter eventFilter = EventFilter.newBuilder(contractAddress)
            .eventName("set")
            .recentBlockCount(1000)
            .build();
        System.out.println("Event filter: " + eventFilter);
      }

      // By event name and args
      {
        // set event filter for specific address with name "set" and args "key" in recent 1000 block
        ContractAddress contractAddress = ContractAddress
            .of("AmNrsAqkXhQfE6sGxTutQkf9ekaYowaJFLekEm8qvDr1RB1AnsiM");
        EventFilter eventFilter = EventFilter.newBuilder(contractAddress)
            .eventName("set")
            .args("key")
            .recentBlockCount(1000)
            .build();
        System.out.println("Event filter: " + eventFilter);
      }
    }

  }

}
