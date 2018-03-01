package edu.wisc.my.messages.exception;

import org.junit.Test;

public class ExpiredMessageExceptionTest {

  @Test
  public void copesWithNullConstructorArguments() {
    new ExpiredMessageException(null, null);
  }

}
