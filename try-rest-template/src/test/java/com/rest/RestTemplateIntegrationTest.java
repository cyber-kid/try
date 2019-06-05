package com.rest;

import com.rest.dto.DemoDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestWebConfig.class})
@Slf4j
public class RestTemplateIntegrationTest {
  @Autowired
  private RestTemplate restTemplate;

  @Test
  public void testRestEndpoint() {
    List<?> converters = restTemplate.getMessageConverters();
    String messageConverters = converters.stream().map(converter -> converter.getClass().getSimpleName()).collect(Collectors.joining(","));

    ResponseErrorHandler errorHandler = restTemplate.getErrorHandler();

    log.info("Message converters: " + messageConverters);
    log.info("Response error handler: " + errorHandler.getClass().getSimpleName());

    DemoDto response = restTemplate.getForObject("http://localhost:8080/api/getData", DemoDto.class);

    log.info("Response is: " + response);

    assertNotNull(response);
    assertNotNull(restTemplate);
  }
}
