package org.hcmr.reservationsystem.cucumber.stepdefs;

import org.hcmr.reservationsystem.ReservationsystemApp;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;

import org.springframework.boot.test.context.SpringBootTest;

@WebAppConfiguration
@SpringBootTest
@ContextConfiguration(classes = ReservationsystemApp.class)
public abstract class StepDefs {

    protected ResultActions actions;

}
