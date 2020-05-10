/*
 * @copyright defined in LICENSE.txt
 */

package hera.example.stream.internal;

import hera.api.model.Block;
import hera.api.model.StreamObserver;
import hera.api.model.Subscription;
import hera.api.model.Transaction;
import hera.api.model.TxHash;
import hera.client.AergoClient;
import hera.example.stream.BlockStream;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
class BlockStreamImpl implements BlockStream {

  // WARN: be careful of memory leak by never removed one
  protected final Map<TxHash, CompletableFuture<TxHash>> hash2Submited = new ConcurrentHashMap<>();

  protected final Object lock = new Object();
  protected volatile Subscription<Block> subscription;

  @Autowired
  protected AergoClient client;

  @Override
  public CompletableFuture<TxHash> submit(TxHash txHash) {
    // make a subscription if it's in unsubscribed state
    if (null == subscription || subscription.isUnsubscribed()) {
      synchronized (lock) {
        if (null == subscription || subscription.isUnsubscribed()) {
          subscription = makeNewSubscription();
        }
      }
    }

    // make a non-completed future and keep it
    CompletableFuture<TxHash> future = new CompletableFuture<>();
    hash2Submited.put(txHash, future);
    return future;
  }

  @Override
  public boolean unsubmit(TxHash txHash) {
    return null != hash2Submited.remove(txHash);
  }

  private Subscription<Block> makeNewSubscription() {
    StreamObserver<Block> streamObserver = new StreamObserver<Block>() {

      @Override
      public void onNext(Block block) {
        System.out.println("New block: " + block.getHash());
        block.getTransactions().stream().map(Transaction::getHash)
            .forEach(hash -> {
              // it hash is submitted one, complete it
              CompletableFuture<TxHash> submitted = hash2Submited.get(hash);
              if (null != submitted) {
                System.out.println("Complete submitted hash: " + hash);
                submitted.complete(hash);
                hash2Submited.remove(hash);
              }
            });
      }

      @Override
      public void onError(Throwable t) {
        System.err.println("Error: " + t);
      }

      @Override
      public void onCompleted() {
        System.err.println("Complete");
      }
    };
    return client.getBlockOperation().subscribeBlock(streamObserver);
  }

}
