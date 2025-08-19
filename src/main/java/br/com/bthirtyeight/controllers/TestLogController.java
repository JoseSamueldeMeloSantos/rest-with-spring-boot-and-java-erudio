package br.com.bthirtyeight.controllers;

import br.com.bthirtyeight.services.PersonServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/api/test/v1")
public class TestLogController {

    //correto e imporr do slf4j
    private Logger logger = LoggerFactory.getLogger(TestLogController.class.getName());


    @GetMapping("/test")
    public String testLog() {
        //para retornar um log ao terminal
        logger.debug("this is an Debug log");
        logger.info("this is an Info log");
        logger.warn("this is an warn log");
        logger.error("this is an Error log");
        return "Logs generated successfully";
    }
}
