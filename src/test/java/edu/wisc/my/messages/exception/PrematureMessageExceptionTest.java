package edu.wisc.my.messages.exception;

import org.junit.Test;

public class PrematureMessageExceptionTest {

  @Test
  public void copesWithNullConstructorArguments() {
    new PrematureMessageException(null, null);
  }

}
