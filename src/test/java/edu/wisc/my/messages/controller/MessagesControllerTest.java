package edu.wisc.my.messages.controller;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import edu.wisc.my.messages.data.MessagesFromTextFile;
import edu.wisc.my.messages.model.Message;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.mock.env.MockEnvironment;
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
   * Test that the data in the incoming text file is actually valid.
   */
  @Test
  public void dataIsValid() {

    MockEnvironment mockEnv = new MockEnvironment();

    // ToDo: It would be very cool is this value could automatically stay in sync
    //  with the actual value in the application.properties file.
    mockEnv.setProperty("message.source", "classpath:messages.json");

    ResourceLoader defaultLoader = new DefaultResourceLoader();

    MessagesFromTextFile messageReader = new MessagesFromTextFile();
    messageReader.setEnv(mockEnv);
    messageReader.setResourceLoader(defaultLoader);

    List<Message> allMessages = messageReader.allMessages();

    // use the Spring injected wired up MessageReader
    List<Message> anotherAllMessages = this.messageReader.allMessages();

    // assert that the manually wired and auto wired messageReader get same result
    assertEquals(allMessages, anotherAllMessages);
  }
}
