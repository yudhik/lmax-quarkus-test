package org.brainmaster;

import com.lmax.disruptor.EventFactory;

public final class ValueEvent {

  @SuppressWarnings("rawtypes")
  public final static EventFactory EVENT_FACTORY = () -> new ValueEvent();

  private int value;

  public int getValue() {
    return value;
  }

  public void setValue(int value) {
    this.value = value;
  }

}
