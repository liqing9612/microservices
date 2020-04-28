package hello.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GreetingTest {
  private Greeting myBean;

  private Greeting myBean2;

  @Before
  public void setUp() throws Exception {
    myBean = new Greeting();
    myBean.setId(123L);
    myBean.setContent("Bean, James Bean");

    myBean2 = new Greeting(456, "Bean, Jack bean");
  }

  @After
  public void tearDown() throws Exception {}

  @Test
  public void getId() {
    assertEquals(myBean.getId(), 123L);
    assertEquals(myBean2.getId(), 456L);
  }

  @Test
  public void getContent() {
    assertEquals(myBean.getContent(), "Bean, James Bean");
    assertEquals(myBean2.getContent(), "Bean, Jack bean");
  }
}
