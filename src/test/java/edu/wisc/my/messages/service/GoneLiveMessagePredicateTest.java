package edu.wisc.my.messages.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import edu.wisc.my.messages.model.Message;
import edu.wisc.my.messages.model.MessageFilter;
import java.time.LocalDateTime;
import org.junit.Test;

public class GoneLiveMessagePredicateTest {

  @Test(expected = RuntimeException.class)
  public void throwsOnNullMessage() {
    GoneLiveMessagePredicate predicate = new GoneLiveMessagePredicate(LocalDateTime.now());
    predicate.test(null);
  }

  @Test
  public void messageWithoutGoLiveDateHasGoneLive() {
    GoneLiveMessagePredicate predicate = new GoneLiveMessagePredicate(LocalDateTime.now());

    Message message = new Message();
    MessageFilter filter = new MessageFilter();
    message.setFilter(filter);
    assertTrue(predicate.test(message));

    filter.setGoLiveDate("");
    assertTrue(predicate.test(message));

    filter.setGoLiveDate(null);
    assertTrue(predicate.test(message));
  }

  @Test
  public void messageWithGarbageGoLiveDateHasNotGoneLive() {
    GoneLiveMessagePredicate predicate = new GoneLiveMessagePredicate(LocalDateTime.now());

    Message message = new Message();
    MessageFilter messageFilter = new MessageFilter();
    message.setFilter(messageFilter);
    messageFilter.setGoLiveDate("Garbage");

    assertFalse(predicate.test(message));
  }

  @Test
  public void messageWithPastGoLiveDateHasGoneLive() {
    GoneLiveMessagePredicate predicate = new GoneLiveMessagePredicate(LocalDateTime.now());

    Message message = new Message();
    MessageFilter messageFilter = new MessageFilter();
    message.setFilter(messageFilter);
    messageFilter.setGoLiveDate("2000-01-01");

    assertTrue(predicate.test(message));

  }

  @Test
  public void messageWithPrecisePastGoLiveDateHasGoneLive() {
    GoneLiveMessagePredicate predicate = new GoneLiveMessagePredicate(LocalDateTime.now());

    Message message = new Message();
    MessageFilter messageFilter = new MessageFilter();
    message.setFilter(messageFilter);
    messageFilter.setGoLiveDate("2000-01-01T00:04:01");

    assertTrue(predicate.test(message));


  }

  @Test
  public void messageWithFutureGoLiveDateHasNotGoneLive() {
    GoneLiveMessagePredicate predicate =
      new GoneLiveMessagePredicate(LocalDateTime.parse("2000-01-01T00:00:00"));

    Message message = new Message();
    MessageFilter messageFilter = new MessageFilter();
    message.setFilter(messageFilter);
    messageFilter.setGoLiveDate("2010-01-01");

    assertFalse(predicate.test(message));
  }

  @Test
  public void messageWithPreciseFutureGoeLiveDatehasNotGoneLive() {

    GoneLiveMessagePredicate predicate =
      new GoneLiveMessagePredicate(LocalDateTime.parse("2000-01-01T00:00:00"));

    Message message = new Message();
    MessageFilter messageFilter = new MessageFilter();
    message.setFilter(messageFilter);
    messageFilter.setGoLiveDate("2010-01-01T01:01:01");

    assertFalse(predicate.test(message));

  }

  @Test
  public void messageWithNoFilterIsGoneLive() {
    GoneLiveMessagePredicate predicate = new GoneLiveMessagePredicate(LocalDateTime.now());

    Message messageWithNoFilter = new Message();

    assertTrue(predicate.test(messageWithNoFilter));
  }

}
