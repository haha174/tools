package com.wen.tools.cumumber.steps;

import cucumber.api.java.en.Given;
import cucumber.api.java.nl.En;
import cucumber.runtime.StepDefinition;

import java.lang.annotation.Annotation;

public class CommonSteps {
    @Given("I have {string} cukes in my belly")
    public void i_have_n_cukes_in_my_belly(String cukes) {
        System.out.println( "=============================="+cukes);
    }

}
