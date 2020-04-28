package hello;

import org.junit.Test;

import static org.junit.Assert.*;

public class HelloWorldApplicationTest {
  @Test
  public void applicationContextLoaded() {}

  @Test
  public void applicationContextTest() {
    HelloWorldApplication.main(new String[] {});
  }
}
