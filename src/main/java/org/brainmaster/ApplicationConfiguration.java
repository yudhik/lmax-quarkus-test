package org.brainmaster;

import java.util.concurrent.ThreadFactory;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import com.lmax.disruptor.BusySpinWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;

@ApplicationScoped
public class ApplicationConfiguration {

  @Produces
  public RingBuffer<ValueEvent> createValueEventRingBuffer() {
    final ThreadFactory threadFactory = DaemonThreadFactory.INSTANCE;
    @SuppressWarnings("unchecked")
    Disruptor<ValueEvent> disruptor = new Disruptor<>(ValueEvent.EVENT_FACTORY, 16, threadFactory,
        ProducerType.SINGLE, new BusySpinWaitStrategy());
    disruptor.handleEventsWith(new SingleEventPrintConsumer().getEventHandler());
    return disruptor.start();
  }

}
