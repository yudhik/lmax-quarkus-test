package org.brainmaster;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import com.lmax.disruptor.RingBuffer;

@Path("/hello")
public class LMaxResource {

  @Inject
  RingBuffer<ValueEvent> ringBuffer;

  @GET
  @Produces(MediaType.TEXT_PLAIN)
  public String hello(@QueryParam("value") int value) {
    long startTime = System.nanoTime();
    for (int i = 0; i < value; i++) {
      final long sequenceId = ringBuffer.next();
      final ValueEvent valueEvent = ringBuffer.get(sequenceId);
      valueEvent.setValue(i);
      ringBuffer.publish(sequenceId);
    }
    return "request published in " + (System.nanoTime() - startTime) / 1_000_000 + " ms";
  }
}
