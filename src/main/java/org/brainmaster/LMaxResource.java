package org.brainmaster;

import java.util.Arrays;
import java.util.stream.Stream;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.lmax.disruptor.RingBuffer;

@Path("/hello")
public class LMaxResource {

  private static final Logger LOGGER = LoggerFactory.getLogger(LMaxResource.class);

  @Inject
  RingBuffer<ValueEvent> ringBuffer;

  @GET
  @Produces(MediaType.TEXT_PLAIN)
  public String hello(@QueryParam("value") int value) {
    long startTime = System.nanoTime();
    Integer data[] = new Integer[value];
    for (int i = 0; i < value; i++) {
      data[i] = i;
    }
    Stream<Integer> stream = Arrays.stream(data);
    // Multi.createFrom().items(stream).onItem().invoke(counter ->
    // publishMessage(counter.intValue())).subscribe().;
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
