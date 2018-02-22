package edu.wisc.my.messages.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.net.URL;
import org.hamcrest.core.StringContains;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MessagesControllerIT {

  @LocalServerPort
  private int port;

  private URL base;

  @Autowired
  private TestRestTemplate template;

  @Before
  public void setUp() throws Exception {
    this.base = new URL("http://localhost:" + port + "/");
  }

  @Test
  public void siteIsUp() throws Exception {
    ResponseEntity<String> response = template.getForEntity(base.toString(),
      String.class);
    assertThat(response.getBody(), StringContains.containsString("status"));
  }

  @Test
  public void nonexistentPathYields404() throws Exception {
    ResponseEntity<String> response =
      template.getForEntity(base.toString() + "someGoofyPath", String.class);
    assertEquals("Missing path should yield 404 not found response.",
      404, response.getStatusCodeValue());
  }
}
