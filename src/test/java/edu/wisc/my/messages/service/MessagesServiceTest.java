package edu.wisc.my.messages.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

import edu.wisc.my.messages.data.MessagesFromTextFile;
import edu.wisc.my.messages.model.Message;
import edu.wisc.my.messages.model.MessageFilter;
import edu.wisc.my.messages.model.User;

public class MessagesServiceTest {


  /**
   * Test that passes along all messages from repository.
   */
  @Test
  public void handsAlongAllMessagesFromRepository() {
    MessagesService service = new MessagesService();

    Message firstMessage = new Message();
    firstMessage.setId("uniqueMessageId-1");

    Message secondMessage = new Message();
    secondMessage.setId("anotherMessageId-2");

    List<Message> messagesFromRepository = new ArrayList<>();
    messagesFromRepository.add(firstMessage);
    messagesFromRepository.add(secondMessage);

    MessagesFromTextFile messageSource = mock(MessagesFromTextFile.class);
    when(messageSource.allMessages()).thenReturn(messagesFromRepository);

    service.setMessageSource(messageSource);

    List<Message> result = service.allMessages();

    // result should be a message array containing both messages

    assertNotNull(result);

    assertEquals(2, result.size());

    Message firstResultMessage = result.get(0);
    assertEquals("uniqueMessageId-1", firstResultMessage.getId());

    Message secondResultMessage = result.get(1);
    assertEquals("anotherMessageId-2", secondResultMessage.getId());
  }


  /**
   * Test that filters away messages with AudienceFilters reporting no match.
   */
  @Test
  public void includesOnlyMessagesMatchingAudienceFilters() {
    MessagesService service = new MessagesService();

    MessageFilter yesFilter = mock(MessageFilter.class);
    when(yesFilter.test(any())).thenReturn(true);
    Message matchingMessage = new Message();
    matchingMessage.setFilter(yesFilter);
    matchingMessage.setId("uniqueMessageId");

    MessageFilter noFilter = mock(MessageFilter.class);
    when(noFilter.test(any())).thenReturn(false);
    Message unmatchingMessage = new Message();
    unmatchingMessage.setFilter(noFilter);

    List<Message> unfilteredMessages = new ArrayList<>();
    unfilteredMessages.add(matchingMessage);
    unfilteredMessages.add(unmatchingMessage);

    MessagesFromTextFile messageSource = mock(MessagesFromTextFile.class);
    when(messageSource.allMessages()).thenReturn(unfilteredMessages);

    service.setMessageSource(messageSource);

    User user = new User();

    List<Message> result = service.filteredMessages(user);

    assertNotNull(result);

    assertEquals(1, result.size());

    Message resultMessage = result.get(0);

    assertEquals("uniqueMessageId", resultMessage.getId());
  }

  @Test
  public void excludesExpiredMessages() {
    MessagesService service = new MessagesService();

    Message expiredMessage = new Message();
    MessageFilter expiredFilter = new MessageFilter();
    expiredMessage.setFilter(expiredFilter);
    String longAgoDate = "1999-12-31";
    expiredFilter.setExpireDate(longAgoDate);

    Message preciselyExpiredMessage = new Message();
    MessageFilter preciselyExpiredMessageFilter = new MessageFilter();
    String preciseLongAgoDate = "1999-12-31T13:21:14";
    preciselyExpiredMessageFilter.setExpireDate(preciseLongAgoDate);

    List<Message> unfilteredMessages = new ArrayList<>();
    unfilteredMessages.add(expiredMessage);
    unfilteredMessages.add(preciselyExpiredMessage);

    MessagesFromTextFile messageSource = mock(MessagesFromTextFile.class);
    when(messageSource.allMessages()).thenReturn(unfilteredMessages);

    service.setMessageSource(messageSource);

    User user = new User();

    List<Message> result = service.filteredMessages(user);

    assertNotNull(result);

    assertTrue(result.isEmpty());
  }

  @Test
  public void includesUnExpiredMessages() {
    MessagesService service = new MessagesService();

    Message unexpiredMessage = new Message();
    MessageFilter unexpiredMessageFilter = new MessageFilter();
    String longFutureDate = "2999-12-31";
    unexpiredMessageFilter.setExpireDate(longFutureDate);
    unexpiredMessage.setFilter(unexpiredMessageFilter);

    Message preciselyUnexpiredMessage = new Message();
    MessageFilter preciselyUnexpiredMessageFilter = new MessageFilter();
    String preciseFutureDate = "2999-12-31T12:21:21";
    preciselyUnexpiredMessageFilter.setExpireDate(preciseFutureDate);
    preciselyUnexpiredMessage.setFilter(preciselyUnexpiredMessageFilter);

    List<Message> unfilteredMessages = new ArrayList<>();
    unfilteredMessages.add(unexpiredMessage);
    unfilteredMessages.add(preciselyUnexpiredMessage);

    MessagesFromTextFile messageSource = mock(MessagesFromTextFile.class);
    when(messageSource.allMessages()).thenReturn(unfilteredMessages);

    service.setMessageSource(messageSource);

    User user = new User();

    List<Message> result = service.filteredMessages(user);

    assertNotNull(result);

    assertEquals(2, result.size());
  }

}
