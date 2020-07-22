package org.brainmaster;

import java.util.concurrent.atomic.AtomicInteger;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.eclipse.microprofile.context.ManagedExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.lmax.disruptor.RingBuffer;
import io.smallrye.mutiny.Multi;

@Path("/hello")
public class LMaxResource {

  private static final Logger LOGGER = LoggerFactory.getLogger(LMaxResource.class);

  @Inject
  RingBuffer<ValueEvent> ringBuffer;

  @Inject
  ManagedExecutor executor;

  @GET
  @Produces(MediaType.TEXT_PLAIN)
  public String hello(@QueryParam("value") int value) {
    long startTime = System.nanoTime();
    Multi.createBy().repeating()
        .supplier(() -> new AtomicInteger(), counter -> counter.getAndIncrement())
        .until(counterValue -> counterValue == value).runSubscriptionOn(executor).subscribe()
        .with(counterValue -> publishMessage(counterValue));
    return "request published in " + (System.nanoTime() - startTime) / 1_000_000 + " ms";
  }

  private void publishMessage(int value) {
    final long sequenceId = ringBuffer.next();
    LOGGER.info("value : {}, sequenceId : {}", value, sequenceId);
    final ValueEvent valueEvent = ringBuffer.get(sequenceId);
    valueEvent.setValue(value);
    ringBuffer.publish(sequenceId);
  }

}
