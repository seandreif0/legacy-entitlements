package com.meridian.ent.web;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.meridian.ent.model.Entitlement;
import com.meridian.ent.service.EntitlementService;

/**
 * REST/JSON surface alongside the SOAP endpoint at /services/EntitlementService.
 * Authentication is handled upstream by the F5; /api/v1/** must be covered by the same rule.
 */
@RestController
@RequestMapping("/api/v1/users/{userId}")
public class EntitlementRestController {

    private final EntitlementService entitlementService;

    public EntitlementRestController(EntitlementService entitlementService) {
        this.entitlementService = entitlementService;
    }

    @GetMapping("/entitlements")
    public List<Entitlement> getEntitlements(@PathVariable String userId) {
        return entitlementService.getEntitlements(userId);
    }

    @GetMapping("/access/{datasetCode}")
    public Map<String, Object> checkAccess(@PathVariable String userId, @PathVariable String datasetCode) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("userId", userId);
        body.put("datasetCode", datasetCode);
        body.put("allowed", entitlementService.checkAccess(userId, datasetCode));
        return body;
    }
}
