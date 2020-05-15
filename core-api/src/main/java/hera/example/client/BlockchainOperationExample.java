/*
 * @copyright defined in LICENSE.txt
 */

package hera.example.client;

import static java.util.Collections.emptyList;

import hera.api.model.BlockchainStatus;
import hera.api.model.ChainIdHash;
import hera.api.model.ChainInfo;
import hera.api.model.ChainStats;
import hera.api.model.NodeStatus;
import hera.api.model.Peer;
import hera.api.model.PeerMetric;
import hera.api.model.ServerInfo;
import hera.client.AergoClient;
import hera.example.AbstractExample;
import java.util.List;

public class BlockchainOperationExample extends AbstractExample {

  public static void main(String[] args) {
    AergoClient client = getTestnetClient();

    /* Get Chain Id Hash */
    {
      ChainIdHash chainIdHash = client.getBlockchainOperation().getChainIdHash();
      System.out.println("Chain id hash: " + chainIdHash);
    }

    /* Get Blockchain Status */
    {
      BlockchainStatus blockchainStatus = client.getBlockchainOperation().getBlockchainStatus();
      System.out.println("Blockchain status: " + blockchainStatus);
    }

    /* Get Chain Info */
    {
      ChainInfo chainInfo = client.getBlockchainOperation().getChainInfo();
      System.out.println("Chain info: " + chainInfo);
    }

    /* Get Chain Stats */
    {
      ChainStats chainStats = client.getBlockchainOperation().getChainStats();
      System.out.println("Chain stats: " + chainStats);
    }

    /* List Peers */
    {
      // Filtering itself and hidden.
      {
        List<Peer> peers = client.getBlockchainOperation().listPeers(false, false);
        System.out.println("Peers: " + peers);
      }

      // Not filtering itself and hidden.
      {
        List<Peer> peers = client.getBlockchainOperation().listPeers(true, true);
        System.out.println("Peers: " + peers);
      }
    }

    /* List Peers Metrics */
    {
      List<PeerMetric> peerMetrics = client.getBlockchainOperation().listPeerMetrics();
      System.out.println("PeerMetrics: " + peerMetrics);
    }

    /* Get Server Info */
    {
      List<String> categories = emptyList();
      ServerInfo serverInfo = client.getBlockchainOperation().getServerInfo(categories);
      System.out.println("Server info: " + serverInfo);
    }

    /* Get Node Status */
    {
      NodeStatus nodeStatus = client.getBlockchainOperation().getNodeStatus();
      System.out.println("Node status: " + nodeStatus);
    }

    client.close();
  }

}
