package com.capstone.ems;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class EmsApplicationTests {

    @LocalServerPort
    private int port;

    @Test
    public void contextLoads() {
    }

    @Test
    public void applicationStartsOnConfiguredPort() {
        assertTrue(port > 0 && port < 65536);
    }
}