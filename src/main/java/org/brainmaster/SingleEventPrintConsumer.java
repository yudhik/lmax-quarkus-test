package org.brainmaster;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.lmax.disruptor.EventHandler;

public class SingleEventPrintConsumer {

  private static final Logger LOGGER = LoggerFactory.getLogger(SingleEventPrintConsumer.class);

  @SuppressWarnings("unchecked")
  public EventHandler<ValueEvent>[] getEventHandler() {
    EventHandler<ValueEvent> handler =
        (event, sequence, endOfBatch) -> print(event.getValue(), sequence);
    return new EventHandler[] {handler};
  }

  private void print(int id, long sequenceId) {
    LOGGER.info("id : {}, sequence id that was used : {}", id, sequenceId);
  }

}
