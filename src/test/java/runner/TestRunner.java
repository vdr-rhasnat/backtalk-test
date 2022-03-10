package runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions (
        features = "src/test/resources/features",
        plugin = {"pretty", "html:target/cucumber/cucumber-pretty.html",
          "json:target/cucumber/cucumber.json","junit:target/cucumber/cucumber.xml"},
        glue = "steps"
)
public class TestRunner {
}
