package com.meridian.ent.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.meridian.ent.model.Entitlement;
import com.meridian.ent.service.EntitlementService;

public class EntitlementRestControllerTest {

    private final MockMvc mvc = MockMvcBuilders.standaloneSetup(new EntitlementRestController(new EntitlementService() {
        public List<Entitlement> getEntitlements(String userId) {
            return Arrays.asList(new Entitlement(userId, "REF_DATA", "READ"));
        }
        public boolean checkAccess(String userId, String datasetCode) {
            return "CREDIT_RAW".equals(datasetCode);
        }
    })).build();

    @Test
    public void getEntitlements() throws Exception {
        mvc.perform(get("/api/v1/users/u10001/entitlements"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].userId").value("u10001"))
            .andExpect(jsonPath("$[0].datasetCode").value("REF_DATA"))
            .andExpect(jsonPath("$[0].accessLevel").value("READ"));
    }

    @Test
    public void checkAccess() throws Exception {
        mvc.perform(get("/api/v1/users/u10001/access/CREDIT_RAW"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.allowed").value(true));
    }
}
