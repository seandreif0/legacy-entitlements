package com.meridian.ent.ws;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.meridian.ent.model.Entitlement;
import com.meridian.ent.service.EntitlementService;

public class EntitlementLookupServletTest {

    private EntitlementLookupServlet servlet() {
        EntitlementLookupServlet s = new EntitlementLookupServlet();
        s.setEntitlementService(new EntitlementService() {
            public List<Entitlement> getEntitlements(String userId) {
                return Arrays.asList(new Entitlement(userId, "REF_DATA", "READ"));
            }
            public boolean checkAccess(String userId, String datasetCode) {
                return "CREDIT_RAW".equals(datasetCode);
            }
        });
        return s;
    }

    private String envelope(String inner) {
        return "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ent=\""
            + SoapEnvelopeUtil.NS + "\"><soapenv:Header/><soapenv:Body>" + inner + "</soapenv:Body></soapenv:Envelope>";
    }

    @Test
    public void getEntitlements() {
        String out = servlet().handle(envelope("<ent:getEntitlements><userId>u10001</userId></ent:getEntitlements>"));
        assertTrue(out.contains("dataset=\"REF_DATA\""));
    }

    @Test
    public void checkAccess() {
        String out = servlet().handle(envelope(
            "<ent:checkAccess><userId>u10001</userId><datasetCode>CREDIT_RAW</datasetCode></ent:checkAccess>"));
        assertTrue(out.contains("<allowed>true</allowed>"));
    }

    @Test
    public void unknownOperationIsFault() {
        assertTrue(servlet().handle(envelope("<ent:deleteAll/>")).contains("Fault"));
    }
}
