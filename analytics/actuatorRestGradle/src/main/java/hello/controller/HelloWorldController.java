package hello.controller;

import java.util.concurrent.atomic.AtomicLong;

import hello.model.Greeting;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value = "/api", description = "Operations about pets")
@RequestMapping("/api")
public class HelloWorldController {

  private static final String template = "Hello, %s!";
  private final AtomicLong counter = new AtomicLong();

  @GetMapping("hello")
  @ApiOperation(
      value = "Get hello",
      notes = "This is swagger test of REST get service.",
      response = Greeting.class,
      responseContainer = "List")
  @ApiResponses(
      value = {
        @ApiResponse(code = 400, message = "Invalid name supplied"),
        @ApiResponse(code = 404, message = "Greeting not found")
      })
  public Greeting sayHello(
      @ApiParam(value = "name that need to pass in", required = false)
          @RequestParam(name = "name", required = false, defaultValue = "Stranger")
          String name) {
    return new Greeting(counter.incrementAndGet(), String.format(template, name));
  }
}
