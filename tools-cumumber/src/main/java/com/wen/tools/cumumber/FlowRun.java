package com.wen.tools.cumumber;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"pretty", "html:target/cucumber"}
                    ,features="src/test/resources/features/test.feature"
                    ,tags ={"@billing"}
                    ,glue = {"com.wen.tools.cumumber.steps"})
public class FlowRun {
}
