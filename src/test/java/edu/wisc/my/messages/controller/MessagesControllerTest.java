package edu.wisc.my.messages.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import edu.wisc.my.messages.data.MessagesFromTextFile;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MessagesControllerTest {

  @Autowired
  private MockMvc mvc;

  @Autowired
  private MessagesFromTextFile messageReader;

  @Test
  public void siteIsUp() throws Exception {
    mvc.perform(MockMvcRequestBuilders.get("/").accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(content().contentTypeCompatibleWith("application/json"))
      .andExpect(content().json("{\"status\":\"up\"}"));
  }

  /**
   * Test that the autowired MessageReader successfully reads messages. This is an essential
   * building block towards richer tests of the application-as-running.
   */
  @Test
  public void dataIsValid() {
    this.messageReader.allMessages();
  }


  /**
   * Test the /admin/message/{id} path reading a message.
   *
   * @throws Exception as an unexpected test failure modality
   */
  @Test
  public void adminMessageById() throws Exception {
    String expectedJson = "{\n"
      + "  \"id\": \"demo-high-priority-valid-group-no-date\",\n"
      + "  \"title\": \"Valid group. No date. High priority.\",\n"
      + "  \"titleShort\": \"Valid group. No date. High priority.\",\n"
      + "  \"titleUrl\": null,\n"
      + "  \"description\": \"Valid group. No date. High priority.\",\n"
      + "  \"descriptionShort\": \"Valid group. No date. High priority.\",\n"
      + "  \"messageType\": \"notification\",\n"
      + "  \"featureImageUrl\": null,\n"
      + "  \"priority\": \"high\",\n"
      + "  \"recurrence\": null,\n"
      + "  \"dismissible\": null,\n"
      + "  \"filter\": {\n"
      + "    \"goLiveDate\": null,\n"
      + "    \"expireDate\": null,\n"
      + "    \"groups\": [\n"
      + "      \"Portal Administrators\"\n"
      + "    ]\n"
      + "  },\n"
      + "  \"data\": {\n"
      + "    \"dataUrl\": null,\n"
      + "    \"dataObject\": null,\n"
      + "    \"dataArrayFilter\": null,\n"
      + "    \"dataMessageTitle\": null,\n"
      + "    \"dataMessageMoreInfoUrl\": null\n"
      + "  },\n"
      + "  \"actionButton\": {\n"
      + "    \"label\": \"Go\",\n"
      + "    \"url\": \"http://www.google.com\"\n"
      + "  },\n"
      + "  \"moreInfoButton\": null,\n"
      + "  \"confirmButton\": null\n"
      + "}";

    mvc.perform(MockMvcRequestBuilders.get("/admin/message/demo-high-priority-valid-group-no-date")
      .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(content().contentTypeCompatibleWith("application/json"))
      .andExpect(content().json(expectedJson));
  }

  /**
   * Test that looking for a message by an ID that does not match yields a 404 NOT FOUND.
   */
  @Test
  public void adminNotFoundMessageYields404() throws Exception {
    mvc.perform(MockMvcRequestBuilders.get("/admin/message/no-such-message")
      .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isNotFound());
  }

  /**
   * Test the /admin/message/{id} path reading a message.
   *
   * @throws Exception as an unexpected test failure modality
   */
  @Test
  public void messageById() throws Exception {
    String expectedJson = "{\n"
      + "  \"id\": \"has-no-audience-filter\",\n"
      + "  \"title\": \"An announcement lacking an audience filter.\",\n"
      + "  \"titleShort\": \"Not filtered by audience\",\n"
      + "  \"titleUrl\": null,\n"
      + "  \"description\": \"This announcement is not filtered by groups.\",\n"
      + "  \"descriptionShort\": \"Not filtered by groups.\",\n"
      + "  \"messageType\": \"announcement\",\n"
      + "  \"featureImageUrl\": null,\n"
      + "  \"priority\": null,\n"
      + "  \"recurrence\": null,\n"
      + "  \"dismissible\": null,\n"
      + "  \"filter\": null,\n"
      + "  \"data\": {\n"
      + "    \"dataUrl\": null,\n"
      + "    \"dataObject\": null,\n"
      + "    \"dataArrayFilter\": null,\n"
      + "    \"dataMessageTitle\": null,\n"
      + "    \"dataMessageMoreInfoUrl\": null\n"
      + "  },\n"
      + "  \"actionButton\": {\n"
      + "    \"label\": \"Add to home\",\n"
      + "    \"url\": \"addToHome/open-apereo\"\n"
      + "  },\n"
      + "  \"moreInfoButton\": {\n"
      + "    \"label\": \"More info\",\n"
      + "    \"url\": \"https://www.apereo.org/content/2018-open-apereo-montreal-quebec\"\n"
      + "  },\n"
      + "  \"confirmButton\": null\n"
      + "}";

    mvc.perform(MockMvcRequestBuilders.get("/message/has-no-audience-filter")
      .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(content().contentTypeCompatibleWith("application/json"))
      .andExpect(content().json(expectedJson));
  }

  /**
   * Attempting to get a message you are not in the audience of yields 403 FORBIDDEN.
   */
  @Test
  public void notInAudienceMessageByIdYieldsError() throws Exception {
    mvc.perform(MockMvcRequestBuilders.get("/message/1")
      .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isForbidden());
  }

  /**
   * Attempting to get an expired message yields 403 FORBIDDEN.
   */
  @Test
  public void expiredMessageByIdYieldsError() throws Exception {
    mvc.perform(MockMvcRequestBuilders.get("/message/expired")
      .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isForbidden());
  }

  /**
   * Attempting to get premature message yields 403 FORBIDDEN.
   */
  @Test
  public void prematureMessageByIdYieldsError() throws Exception {
    mvc.perform(MockMvcRequestBuilders.get("/message/premature")
      .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isForbidden());
  }

  /**
   * Test that looking for a message by an ID that does not match yields a 404 NOT FOUND.
   */
  @Test
  public void notFoundMessageYields404() throws Exception {
    mvc.perform(MockMvcRequestBuilders.get("/admin/message/no-such-message")
      .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isNotFound());
  }
}
