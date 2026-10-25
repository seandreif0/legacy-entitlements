package com.meridian.ent.ws;

import java.io.BufferedReader;
import java.io.IOException;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meridian.ent.service.EntitlementService;

/**
 * SOAP endpoint used by the research portal and the index publishing tool.
 * Authentication is handled upstream by the F5.
 */
public class EntitlementLookupServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger(EntitlementLookupServlet.class);

    private EntitlementService entitlementService;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader r = req.getReader();
        String line;
        while ((line = r.readLine()) != null) {
            sb.append(line);
        }
        resp.setContentType("text/xml;charset=UTF-8");
        resp.getWriter().write(handle(sb.toString()));
    }

    String handle(String body) {
        String op = SoapEnvelopeUtil.operation(body);
        String userId = SoapEnvelopeUtil.extract(body, "userId");
        LOG.debug("op=" + op + " user=" + userId);
        if ("getEntitlements".equals(op)) {
            return SoapEnvelopeUtil.entitlementsResponse(userId, entitlementService.getEntitlements(userId));
        }
        if ("checkAccess".equals(op)) {
            String ds = SoapEnvelopeUtil.extract(body, "datasetCode");
            return SoapEnvelopeUtil.checkAccessResponse(entitlementService.checkAccess(userId, ds));
        }
        return SoapEnvelopeUtil.fault("Client", "Unknown operation");
    }

    void setEntitlementService(EntitlementService entitlementService) {
        this.entitlementService = entitlementService;
    }
}
