package com.home;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static org.junit.Assert.assertEquals;

public class StepDefs{
  private String today;
  private String expectedAnswer;

  @Given("today is \"([^\"]*)\"$")
  public void given(String today) {
    this.today = today;
  }

  @When("I ask whether it's Friday yet")
  public void when() {
    this.expectedAnswer = TestMock.isItFriday(today);
  }

  @Then("I should be told \"([^\"]*)\"$")
  public void then(String answer) {
    assertEquals(answer, expectedAnswer);
  }
}
