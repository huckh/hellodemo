package com.example.hellodemo;

import java.net.InetAddress;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

@RestController
@CrossOrigin
public class HelloController {

    private static final Logger logger = LoggerFactory.getLogger(HelloController.class);

    @Value("${info.app.name}")
    private String componentName;

    @Autowired
    protected MeterRegistry meterRegistry;

    private Counter metricsCounter;

    @PostConstruct
    public void init() {
        metricsCounter = meterRegistry.counter("app.hello.count"); // initialize
    }

    @GetMapping(value = "/hello")
    // @Timed(value = "greeting.time", description = "Time taken to return
    // greeting", percentiles = {0.5, 0.90})
    @Timed(value = "greeting.time", description = "Time taken to return greeting")
    public String greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        metricsCounter.increment(); // increment api call counter
        StringBuilder msg = new StringBuilder("Hello ");
        msg.append(name);
        msg.append("! from " + componentName);
        try {
            long start1 = System.currentTimeMillis();
            Thread.sleep((long) (Math.random() * 5000));
            long end1 = System.currentTimeMillis();
            long duration = end1 - start1;
            if (duration > 4000) {
                logger.error("This message took way to long: {} milliseconds", duration);
            } else if (duration > 3000) {
                logger.warn("This message took a long time: {} milliseconds", duration);
            } else if (duration > 2000) {
                logger.info("This message was slow: {} milliseconds", duration);
            }
            logger.debug("This message took {} milliseconds", duration);
            msg.append(" Running on: " + InetAddress.getLocalHost().getHostAddress());
            msg.append(" (took " + duration + " milliseconds)");
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
        if (logger.isDebugEnabled()) {
            logger.debug(msg.toString());
        }
        return msg.toString();
    }

}
