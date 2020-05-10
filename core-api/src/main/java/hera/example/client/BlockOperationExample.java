/*
 * @copyright defined in LICENSE.txt
 */

package hera.example.client;

import hera.api.model.Block;
import hera.api.model.BlockHash;
import hera.api.model.BlockMetadata;
import hera.api.model.StreamObserver;
import hera.api.model.Subscription;
import hera.client.AergoClient;
import hera.example.AbstractExample;
import java.util.List;

public class BlockOperationExample extends AbstractExample {

  public static void main(String[] args) throws Exception {
    AergoClient client = getTestnetClient();

    /* Get Block */
    // Get block.
    {
      // By hash
      {
        BlockHash blockHash = BlockHash.of("DN9TvryaThbJneSpzaXp5ZsS4gE3UMzKfaXC4x8L5qR1");
        Block block = client.getBlockOperation().getBlock(blockHash);
        System.out.println("Block: " + block);
      }

      // By height
      {
        long height = 27_066_653L;
        Block block = client.getBlockOperation().getBlock(height);
        System.out.println("Block: " + block);
      }
    }

    /* Get Block Metadata */
    // Get block metadata.
    {
      // By hash.
      {
        BlockHash blockHash = BlockHash.of("DN9TvryaThbJneSpzaXp5ZsS4gE3UMzKfaXC4x8L5qR1");
        BlockMetadata blockMetadata = client.getBlockOperation().getBlockMetadata(blockHash);
        System.out.println("Block metadata: " + blockMetadata);
      }

      // By height.
      {
        long height = 27_066_653L;
        BlockMetadata blockMetadata = client.getBlockOperation().getBlockMetadata(height);
        System.out.println("Block metadata: " + blockMetadata);
      }
    }

    /* List Block Metadatas */
    // Get block metadatas.
    {
      // By hash.
      {
        // block metadatas by from hash to previous 100 block
        BlockHash blockHash = BlockHash.of("DN9TvryaThbJneSpzaXp5ZsS4gE3UMzKfaXC4x8L5qR1");
        List<BlockMetadata> blockMetadatas = client.getBlockOperation()
            .listBlockMetadatas(blockHash, 100);
        System.out.println(blockMetadatas);
      }

      // By height.
      {
        // block metadatas by from height to previous 100 block
        long height = 27_066_653L;
        List<BlockMetadata> blockMetadatas = client.getBlockOperation()
            .listBlockMetadatas(height, 100);
        System.out.println(blockMetadatas);
      }
    }

    /* Block Subscription */
    // Subscribe new generated block.
    {
      // make a subscription
      Subscription<Block> subscription = client.getBlockOperation()
          .subscribeBlock(new StreamObserver<Block>() {
            @Override
            public void onNext(Block value) {
              System.out.println("Next: " + value);
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

    /* Block Metadata Subscription */
    // Subscribe new generated block metadata.
    {
      // make a subscription
      Subscription<BlockMetadata> metadataSubscription = client
          .getBlockOperation().subscribeBlockMetadata(new StreamObserver<BlockMetadata>() {
            @Override
            public void onNext(BlockMetadata value) {
              System.out.println("Next: " + value);
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

    client.close();
  }

}
