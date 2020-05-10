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
    // Get chain id hash of current node.
    {
      ChainIdHash chainIdHash = client.getBlockchainOperation().getChainIdHash();
      System.out.println("Chain id hash: " + chainIdHash);
    }

    /* Get Blockchain Status */
    // Get blockchain status of current node.
    {
      BlockchainStatus blockchainStatus = client.getBlockchainOperation().getBlockchainStatus();
      System.out.println("Blockchain status: " + blockchainStatus);
    }

    /* Get Chain Info */
    // Get chain info of current node.
    {
      ChainInfo chainInfo = client.getBlockchainOperation().getChainInfo();
      System.out.println("Chain info: " + chainInfo);
    }

    /* Get Chain Stats */
    // Get chain statistics of current node.
    {
      ChainStats chainStats = client.getBlockchainOperation().getChainStats();
      System.out.println("Chain stats: " + chainStats);
    }

    /* Get Node Status */
    // Get node status of current node.
    {
      NodeStatus nodeStatus = client.getBlockchainOperation().getNodeStatus();
      System.out.println("Node status: " + nodeStatus);
    }

    /* Get Server Info */
    // Get server info of current node. Category is not implemented yet.
    {
      List<String> categories = emptyList();
      ServerInfo serverInfo = client.getBlockchainOperation().getServerInfo(categories);
      System.out.println("Server info: " + serverInfo);
    }

    /* List Peers */
    // List peers of current node.
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
    // List peers metrics of current node.
    {
      List<PeerMetric> peerMetrics = client.getBlockchainOperation().listPeerMetrics();
      System.out.println("PeerMetrics: " + peerMetrics);
    }

    client.close();
  }

}
