package com.example.project;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles; // <--- 1. NEW IMPORT

@SpringBootTest
@ActiveProfiles("test") // <--- 2. NEW ANNOTATION: Activates the H2 configuration
class ProjecApplicationTests {

    @Test
    void contextLoads() {
        // This test now uses the stable 'test' profile configuration
    }

}
