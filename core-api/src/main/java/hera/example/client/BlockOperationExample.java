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

    /* Get Block Metadata */
    {
      // By hash.
      {
        BlockHash blockHash = BlockHash.of("DN9TvryaThbJneSpzaXp5ZsS4gE3UMzKfaXC4x8L5qR1");
        BlockMetadata blockMetadata = client.getBlockOperation().getBlockMetadata(blockHash);
        System.out.println("Block metadata by hash: " + blockMetadata);
      }

      // By height.
      {
        long height = 27_066_653L;
        BlockMetadata blockMetadata = client.getBlockOperation().getBlockMetadata(height);
        System.out.println("Block metadata by height: " + blockMetadata);
      }
    }

    /* List Block Metadatas */
    {
      // By hash.
      {
        // block metadatas by from hash to previous 100 block
        BlockHash blockHash = BlockHash.of("DN9TvryaThbJneSpzaXp5ZsS4gE3UMzKfaXC4x8L5qR1");
        List<BlockMetadata> blockMetadatas = client.getBlockOperation()
            .listBlockMetadatas(blockHash, 100);
        System.out.println("Block metadatas by hash: " + blockMetadatas);
      }

      // By height.
      {
        // block metadatas by from height to previous 100 block
        long height = 27_066_653L;
        List<BlockMetadata> blockMetadatas = client.getBlockOperation()
            .listBlockMetadatas(height, 100);
        System.out.println("Block metadatas by height: " + blockMetadatas);
      }
    }

    /* Get Block */
    {
      // By hash.
      {
        BlockHash blockHash = BlockHash.of("DN9TvryaThbJneSpzaXp5ZsS4gE3UMzKfaXC4x8L5qR1");
        Block block = client.getBlockOperation().getBlock(blockHash);
        System.out.println("Block by hash: " + block);
      }

      // By height.
      {
        long height = 27_066_653L;
        Block block = client.getBlockOperation().getBlock(height);
        System.out.println("Block by height: " + block);
      }
    }

    /* Block Metadata Subscription */
    {
      // make a subscription
      Subscription<BlockMetadata> subscription = client
          .getBlockOperation().subscribeBlockMetadata(new StreamObserver<BlockMetadata>() {
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
      subscription.unsubscribe();
    }

    /* Block Subscription */
    {
      // make a subscription
      Subscription<Block> subscription = client.getBlockOperation()
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

    client.close();
  }

}
