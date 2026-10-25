package com.meridian.ent;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@ActiveProfiles("local")
@TestPropertySource(properties = "ent.jdbc.url=jdbc:h2:mem:entctx;MODE=Oracle;DB_CLOSE_DELAY=-1")
public class EntitlementServiceApplicationTest {

    @Test
    public void contextLoads() {
    }
}
