package edu.wisc.my.messages.controller;

import edu.wisc.my.messages.model.User;
import edu.wisc.my.messages.service.MessagesService;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Understands what HTTP requests are asking about messages, queries the MessagesService
 * accordingly, and replies in JSON.
 */
@RestController
public class MessagesController {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  private MessagesService messagesService;
  private IsMemberOfHeaderParser isMemberOfHeaderParser;

  /**
   * Messages filtered to the context of the request. <p> Expects user group memberships as
   * isMemberOf header with semicolon-delimited values. Fails gracefully in absence of this header.
   * <p> The details of the filtering are NOT a semantically versioned aspect of this API. That is,
   * the microservice can get more sophisticated at filtering and this will be considered a MINOR
   * rather than MAJOR (breaking) change. <p> As currently implemented, EXCLUDES messages that are
   * ANY of <p> <ul> <li>premature per not-before metadata on the message</li> <li>expired per
   * not-after metadata on the message</li> <li>limited to groups none of which include the
   * requesting user</li> </ul>
   */
  @GetMapping("/messages")
  public String currentMessages(HttpServletRequest request) {

    String isMemberOfHeader = request.getHeader("isMemberOf");
    Set<String> groups =
      isMemberOfHeaderParser.groupsFromHeaderValue(isMemberOfHeader);
    User user = new User();
    user.setGroups(groups);

    return messagesService.filteredMessages(user).toString();
  }

  @GetMapping("/allMessages")
  public String messages() {
    return messagesService.allMessages().toString();
  }

  @RequestMapping("/")
  public void index(HttpServletResponse response) {
    try {
      JSONObject responseObj = new JSONObject();
      responseObj.put("status", "up");
      response.getWriter().write(responseObj.toString());
      response.setContentType("application/json");
      response.setStatus(HttpServletResponse.SC_OK);
    } catch (Exception e) {
      logger.error("Issues happened while trying to write Status", e);
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }

  @Autowired
  public void setMessagesService(MessagesService messagesService) {
    this.messagesService = messagesService;
  }

  @Autowired
  public void setIsMemberOfHeaderParser(
    IsMemberOfHeaderParser isMemberOfHeaderParser) {
    this.isMemberOfHeaderParser = isMemberOfHeaderParser;
  }

}
