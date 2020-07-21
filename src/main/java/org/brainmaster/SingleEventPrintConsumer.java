package org.brainmaster;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.lmax.disruptor.EventHandler;

public class SingleEventPrintConsumer {

  private static final Logger LOGGER = LoggerFactory.getLogger(SingleEventPrintConsumer.class);

  @SuppressWarnings("unchecked")
  public EventHandler<ValueEvent>[] getEventHandler() {
    EventHandler<ValueEvent>[] handlers = new EventHandler[1];
    for (int i = 0; i < 1; i++) {
      handlers[i] = (event, sequence, endOfBatch) -> print(event.getValue(), sequence);
    }
    return handlers;
  }

  private void print(int id, long sequenceId) {
    LOGGER.info("id : {}, sequence id that was used : {}", id, sequenceId);
  }

}
