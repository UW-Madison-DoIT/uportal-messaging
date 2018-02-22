package edu.wisc.my.messages.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

  @Test
  public void siteIsUp() throws Exception {
    mvc.perform(MockMvcRequestBuilders.get("/").accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(content().contentTypeCompatibleWith("application/json"))
      .andExpect(content().json("{\"status\":\"up\"}"));
  }

  @Test
  public void filteredMessagesIncludesAMessage() throws Exception {

    String expectedJson = "{\n"
      + "  \"messages\": [\n"
      + "    {\n"
      + "      \"data\": {},\n"
      + "      \"messageType\": \"announcement\",\n"
      + "      \"moreInfoButton\": {\n"
      + "        \"label\": \"More info\",\n"
      + "        \"url\": \"https://www.apereo.org/content/2018-open-apereo-montreal-quebec\"\n"
      + "      },\n"
      + "      \"actionButton\": {\n"
      + "        \"label\": \"Add to home\",\n"
      + "        \"url\": \"addToHome/open-apereo\"\n"
      + "      },\n"
      + "      \"description\": \"This announcement is not filtered by groups.\",\n"
      + "      \"titleShort\": \"Not filtered by audience\",\n"
      + "      \"id\": \"has-no-audience-filter\",\n"
      + "      \"descriptionShort\": \"Not filtered by groups.\",\n"
      + "      \"title\": \"An announcement lacking an audience filter.\"\n"
      + "    }\n"
      + "  ]\n"
      + "}";

    mvc.perform(MockMvcRequestBuilders.get("/messages").accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(content().contentTypeCompatibleWith("application/json"))
      .andExpect(content().json(expectedJson));
  }
}
