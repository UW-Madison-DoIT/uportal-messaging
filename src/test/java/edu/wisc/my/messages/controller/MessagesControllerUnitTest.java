package edu.wisc.my.messages.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import edu.wisc.my.messages.model.Message;
import edu.wisc.my.messages.model.User;
import edu.wisc.my.messages.service.MessagesService;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class MessagesControllerUnitTest {


  /**
   * Test that the controller reads isMemberOf header from the request, asking its
   * IsMemberOfHeaderParser about it.
   */
  @Test
  public void usesIsMemberOfHeader() {

    MessagesService mockService = mock(MessagesService.class);
    IsMemberOfHeaderParser mockParser = mock(IsMemberOfHeaderParser.class);

    MessagesController controller = new MessagesController();
    controller.setIsMemberOfHeaderParser(mockParser);
    controller.setMessagesService(mockService);

    HttpServletRequest mockRequest = mock(HttpServletRequest.class);
    List<Message> messages = new ArrayList<>();

    when(mockRequest.getHeader("isMemberOf")).thenReturn("group1;group2;");
    when(mockService.filteredMessages(any())).thenReturn(messages);

    controller.currentMessages(mockRequest);

    verify(mockRequest).getHeader("isMemberOf");
    verify(mockParser).groupsFromHeaderValue("group1;group2;");

  }

  /**
   * Test that the controller passes on to the Service the groups it learned from its
   * IsMemberOfHeaderParser.
   */
  @Test
  public void passesGroupsToService() {

    MessagesService mockService = mock(MessagesService.class);
    IsMemberOfHeaderParser mockParser = mock(IsMemberOfHeaderParser.class);

    MessagesController controller = new MessagesController();
    controller.setIsMemberOfHeaderParser(mockParser);
    controller.setMessagesService(mockService);

    HttpServletRequest mockRequest = mock(HttpServletRequest.class);

    List<Message> messages = new ArrayList<>();

    when(mockService.filteredMessages(any())).thenReturn(messages);
    Set<String> groups = new HashSet<>();
    groups.add("someGroup");
    groups.add("someOtherGroup");
    groups.add("yetAnotherGroup");
    when(mockParser.groupsFromHeaderValue(anyString())).thenReturn(groups);

    controller.currentMessages(mockRequest);

    ArgumentCaptor<User> argument = ArgumentCaptor.forClass(User.class);
    verify(mockService).filteredMessages(argument.capture());
    assertEquals(groups, argument.getValue().getGroups());
  }

  /**
   * Test that the controller hands over the messages from the Service to SpringWebMVC for JSON
   * rendering.
   */
  @Test
  public void passesAllMessagesToView() {
    MessagesService mockService = mock(MessagesService.class);

    MessagesController controller = new MessagesController();
    controller.setMessagesService(mockService);

    HttpServletRequest mockRequest = mock(HttpServletRequest.class);

    List<Message> messages = new ArrayList<>();

    when(mockService.allMessages()).thenReturn(messages);

    Map<String, Object> result = controller.messages();

    assertSame(messages, result.get("messages"));
  }

}
