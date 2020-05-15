/*
 * @copyright defined in LICENSE.txt
 */

package hera.example.wallet;

import static java.util.Collections.emptyList;

import hera.api.model.AccountAddress;
import hera.api.model.AccountState;
import hera.api.model.AccountTotalVote;
import hera.api.model.Authentication;
import hera.api.model.Block;
import hera.api.model.BlockHash;
import hera.api.model.BlockMetadata;
import hera.api.model.BlockchainStatus;
import hera.api.model.ChainIdHash;
import hera.api.model.ChainInfo;
import hera.api.model.ChainStats;
import hera.api.model.ContractAddress;
import hera.api.model.ContractDefinition;
import hera.api.model.ContractInterface;
import hera.api.model.ContractInvocation;
import hera.api.model.ContractResult;
import hera.api.model.ContractTxReceipt;
import hera.api.model.ElectedCandidate;
import hera.api.model.Event;
import hera.api.model.EventFilter;
import hera.api.model.Fee;
import hera.api.model.Identity;
import hera.api.model.Name;
import hera.api.model.NodeStatus;
import hera.api.model.Peer;
import hera.api.model.PeerMetric;
import hera.api.model.ServerInfo;
import hera.api.model.StakeInfo;
import hera.api.model.StreamObserver;
import hera.api.model.Subscription;
import hera.api.model.Transaction;
import hera.api.model.TxHash;
import hera.api.model.TxReceipt;
import hera.client.AergoClient;
import hera.example.AbstractExample;
import hera.key.AergoKey;
import hera.keystore.KeyStore;
import hera.keystore.KeyStores;
import hera.wallet.WalletApi;
import hera.wallet.WalletApiFactory;
import java.util.List;

public class QueryApiExample extends AbstractExample {

  public static void main(String[] args) throws Exception {
    // prepare client and keystore
    AergoClient client = getLocalClient();
    AergoKey signer = getRichKey();
    KeyStore keyStore = KeyStores.newInMemoryKeyStore();
    Identity identity = signer.getAddress();
    String password = "password";
    Authentication authentication = Authentication.of(identity, password);
    keyStore.save(authentication, signer);

    // create a wallet api
    WalletApi walletApi = new WalletApiFactory().create(keyStore);

    // prepare contract keep variables
    walletApi.unlock(authentication);
    String contractPayload = loadResource("/contract_payload");
    ContractInterface contractInterfaceKeep = deployContract(walletApi, client, contractPayload);
    ContractAddress contractAddressKeep = contractInterfaceKeep.getAddress();
    walletApi.lock();

    /* Get Account State */
    {
      // get account state
      AccountAddress accountAddress = AccountAddress
          .of("AmNrsAqkXhQfE6sGxTutQkf9ekaYowaJFLekEm8qvDr1RB1AnsiM");
      AccountState state = walletApi.with(client).query()
          .getAccountState(accountAddress);
      System.out.println("Account state: " + state);
    }

    /* Get Name Owner */
    {
      // At current block.
      {
        // get name owner
        Name name = Name.of("namenamename");
        AccountAddress nameOwner = walletApi.with(client).query().getNameOwner(name);
        System.out.println("Name owner: " + nameOwner);
      }

      // At specific block.
      {
        // get name owner at block 10
        Name name = Name.of("namenamename");
        AccountAddress nameOwner = walletApi.with(client).query().getNameOwner(name, 10);
        System.out.println("Name owner: " + nameOwner);
      }
    }

    /* Get Stake Info */
    {
      // get stake info
      AccountAddress accountAddress = AccountAddress
          .of("AmNrsAqkXhQfE6sGxTutQkf9ekaYowaJFLekEm8qvDr1RB1AnsiM");
      StakeInfo stakeInfo = walletApi.with(client).query().getStakeInfo(accountAddress);
      System.out.println("Stake info: " + stakeInfo);
    }

    /* List Elected Bps */
    {
      // list elected bps
      List<ElectedCandidate> candidates = walletApi.with(client).query().listElectedBps(23);
      System.out.println("Elected bps: " + candidates);
    }

    /* List Elected */
    {
      // list elected for "voteBP"
      List<ElectedCandidate> candidates = walletApi.with(client).query()
          .listElected("voteBP", 23);
      System.out.println("Elected candidates: " + candidates);
    }

    /* Get Vote Info */
    {
      // get vote info
      AccountAddress accountAddress = AccountAddress
          .of("AmNrsAqkXhQfE6sGxTutQkf9ekaYowaJFLekEm8qvDr1RB1AnsiM");
      AccountTotalVote accountTotalVote = walletApi.with(client).query().getVotesOf(accountAddress);
      System.out.println("Account total vote: " + accountTotalVote);
    }

    /* Get Best Block Hash */
    {
      // get best block hash
      BlockHash blockHash = walletApi.with(client).query().getBestBlockHash();
      System.out.println("Best block hash: " + blockHash);
    }

    /* Get Best Block Height */
    {
      // get best block hash
      long blockHeight = walletApi.with(client).query().getBestBlockHeight();
      System.out.println("Best block height: " + blockHeight);
    }

    /* Get Chain Id Hash */
    {
      // get chain id hash
      ChainIdHash chainIdHash = walletApi.with(client).query().getChainIdHash();
      System.out.println("Chain id hash: " + chainIdHash);
    }

    /* Get Blockchain Status */
    {
      // get blockchain status
      BlockchainStatus blockchainStatus = walletApi.with(client).query().getBlockchainStatus();
      System.out.println("Blockchain status: " + blockchainStatus);
    }

    /* Get Chain Info */
    {
      // get chain info
      ChainInfo chainInfo = walletApi.with(client).query().getChainInfo();
      System.out.println("ChainInfo: " + chainInfo);
    }

    /* Get Chain Stats */
    {
      // get chain stats
      ChainStats chainStats = walletApi.with(client).query().getChainStats();
      System.out.println("ChainStats: " + chainStats);
    }

    /* List Peers */
    {
      // Filtering hidden peers and itself.
      {
        // list peers
        List<Peer> peers = walletApi.with(client).query().listPeers();
        System.out.println("Peers: " + peers);
      }

      // Not filtering hidden peers and itself.
      {
        // list peers
        List<Peer> peers = walletApi.with(client).query().listPeers(true, true);
        System.out.println("Peers: " + peers);
      }
    }

    /* List Peer Metrics */
    {
      // list peer metrics
      List<PeerMetric> peerMetrics = walletApi.with(client).query().listPeerMetrics();
      System.out.println("Peer metrics: " + peerMetrics);
    }

    /* Get Server Info */
    {
      // get server info
      List<String> categories = emptyList();
      ServerInfo serverInfo = walletApi.with(client).query().getServerInfo(categories);
      System.out.println("Server info: " + serverInfo);
    }

    /* Get Node Status */
    {
      // get node status
      NodeStatus nodeStatus = walletApi.with(client).query().getNodeStatus();
      System.out.println("Node status: " + nodeStatus);
    }

    /* Get Block Metadata */
    {
      // By hash.
      {
        // get block metadata
        BlockHash blockHash = BlockHash.of("DN9TvryaThbJneSpzaXp5ZsS4gE3UMzKfaXC4x8L5qR1");
        BlockMetadata blockMetadata = walletApi.with(client).query().getBlockMetadata(blockHash);
        System.out.println("Block metadata by hash: " + blockMetadata);
      }

      // By height.
      {
        // get block metadata
        long height = 27_066_653L;
        BlockMetadata blockMetadata = walletApi.with(client).query().getBlockMetadata(height);
        System.out.println("Block metadata by height: " + blockMetadata);
      }
    }

    /* List Block Metadata */
    {
      // By hash.
      {
        // block metadatas by from hash to previous 100 block
        BlockHash blockHash = BlockHash.of("DN9TvryaThbJneSpzaXp5ZsS4gE3UMzKfaXC4x8L5qR1");
        List<BlockMetadata> blockMetadatas = walletApi.with(client).query()
            .listBlockMetadatas(blockHash, 100);
        System.out.println("Block metadatas by hash: " + blockMetadatas);
      }

      // By height.
      {
        // block metadatas by from height to previous 100 block
        long height = 27_066_653L;
        List<BlockMetadata> blockMetadatas = walletApi.with(client).query()
            .listBlockMetadatas(height, 100);
        System.out.println("Block metadatas by height: " + blockMetadatas);
      }
    }

    /* Get Block */
    {
      // By hash.
      {
        // get block by hash
        BlockHash blockHash = BlockHash.of("DN9TvryaThbJneSpzaXp5ZsS4gE3UMzKfaXC4x8L5qR1");
        Block block = walletApi.with(client).query().getBlock(blockHash);
        System.out.println("Block by hash: " + block);
      }

      // By height.
      {
        // get block by height
        long height = 27_066_653L;
        Block block = walletApi.with(client).query().getBlock(height);
        System.out.println("Block by hash: " + block);
      }
    }

    /* Block Metadata Subscription */
    {
      // make a subscription
      Subscription<BlockMetadata> metadataSubscription = walletApi.with(client).query()
          .subscribeBlockMetadata(new StreamObserver<BlockMetadata>() {
            @Override
            public void onNext(BlockMetadata value) {
              System.out.println("Next block metadata: " + value);
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onCompleted() {
            }
          });

      // wait for a while
      Thread.sleep(2000L);

      // unsubscribe it
      metadataSubscription.unsubscribe();
    }

    /* Block Subscription */
    {
      // make a subscription
      Subscription<Block> subscription = walletApi.with(client).query()
          .subscribeBlock(new StreamObserver<Block>() {
            @Override
            public void onNext(Block value) {
              System.out.println("Next block: " + value);
            }

            @Override
            public void onError(Throwable t) {
            }

            @Override
            public void onCompleted() {
            }
          });

      // wait for a while
      Thread.sleep(2000L);

      // unsubscribe it
      subscription.unsubscribe();
    }

    /* Get Transaction */
    {
      // get transaction
      TxHash txHash = TxHash.of("39vLyMqsg1mTT9mF5NbADgNB2YUiRVsT6SUkDujBZme8");
      Transaction transaction = walletApi.with(client).query().getTransaction(txHash);
      System.out.println("Transaction: " + transaction);
    }

    /* Get Transaction Receipt */
    {
      // get tx receipt
      TxHash txHash = TxHash.of("39vLyMqsg1mTT9mF5NbADgNB2YUiRVsT6SUkDujBZme8");
      TxReceipt txReceipt = walletApi.with(client).query().getTxReceipt(txHash);
      System.out.println("Transaction receipt: " + txReceipt);
    }

    /* Get Contract Tx Receipt */
    {
      // get contract tx receipt
      TxHash txHash = TxHash.of("EGXNDgjY2vQ6uuP3UF3dNXud54dF4FNVY181kaeQ26H9");
      ContractTxReceipt contractTxReceipt = walletApi.with(client).query()
          .getContractTxReceipt(txHash);
      System.out.println("Contract tx receipt: " + contractTxReceipt);
    }

    /* Get Contract Interface */
    {
      // get contract interface
      ContractAddress contractAddress = ContractAddress
          .of("AmNrsAqkXhQfE6sGxTutQkf9ekaYowaJFLekEm8qvDr1RB1AnsiM");
      ContractInterface contractInterface = walletApi.with(client).query()
          .getContractInterface(contractAddress);
      System.out.println("ContractInterface: " + contractInterface);
    }

    /* Query Contract */
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
      // list events with a filter
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
      // subscribe event
      ContractAddress contractAddress = ContractAddress
          .of("AmNrsAqkXhQfE6sGxTutQkf9ekaYowaJFLekEm8qvDr1RB1AnsiM");
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

      Thread.sleep(2200L);

      // unsubscribe event
      subscription.unsubscribe();
    }

    client.close();
  }

  protected static ContractInterface deployContract(WalletApi walletApi, AergoClient client,
      String contractPayload) throws Exception {
    // make a contract definition
    String encodedContract = contractPayload;
    ContractDefinition contractDefinition = ContractDefinition.newBuilder()
        .encodedContract(encodedContract)
        .build();

    // deploy contract
    TxHash txHash = walletApi.with(client).transaction().deploy(contractDefinition, Fee.INFINITY);

    // sleep
    Thread.sleep(2000L);

    // get ContractTxReceipt
    ContractTxReceipt contractTxReceipt = walletApi.with(client).query()
        .getContractTxReceipt(txHash);

    // get contract interface
    ContractAddress contractAddress = contractTxReceipt.getContractAddress();
    ContractInterface contractInterface = walletApi.with(client).query()
        .getContractInterface(contractAddress);
    return contractInterface;
  }

}
