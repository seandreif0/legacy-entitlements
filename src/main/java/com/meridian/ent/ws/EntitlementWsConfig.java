package com.meridian.ent.ws;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.meridian.ent.service.EntitlementService;

@Configuration
public class EntitlementWsConfig {

    @Bean
    public ServletRegistrationBean<EntitlementLookupServlet> entitlementLookup(EntitlementService entitlementService) {
        EntitlementLookupServlet servlet = new EntitlementLookupServlet();
        servlet.setEntitlementService(entitlementService);
        ServletRegistrationBean<EntitlementLookupServlet> reg =
            new ServletRegistrationBean<>(servlet, "/services/EntitlementService");
        reg.setName("entitlementLookup");
        reg.setLoadOnStartup(1);
        return reg;
    }
}
