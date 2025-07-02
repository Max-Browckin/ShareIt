package ru.practicum;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ExampleService {
    private static final Logger log =
            LoggerFactory.getLogger(ExampleService.class);

    public int sum(int a, int b) {
        log.info("Got a={}, b={}", a, b);
        return a + b;
    }
}