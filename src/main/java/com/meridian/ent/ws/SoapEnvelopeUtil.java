package com.meridian.ent.ws;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.meridian.ent.model.Entitlement;

/**
 * Minimal SOAP 1.1 handling. We looked at Axis2 but it was too heavy.
 */
public final class SoapEnvelopeUtil {

    public static final String NS = "http://ent.meridian.example/ws/1.0";

    private SoapEnvelopeUtil() {
    }

    public static String extract(String body, String element) {
        if (body == null) {
            return null;
        }
        Matcher m = Pattern.compile("<(?:\\w+:)?" + element + ">\\s*([^<]*?)\\s*</(?:\\w+:)?" + element + ">").matcher(body);
        return m.find() ? m.group(1) : null;
    }

    public static String operation(String body) {
        if (body == null) {
            return null;
        }
        Matcher m = Pattern.compile("<(?:\\w+:)?Body[^>]*>\\s*<(?:\\w+:)?(\\w+)").matcher(body);
        return m.find() ? m.group(1) : null;
    }

    public static String wrap(String payload) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
            + "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ent=\"" + NS + "\">"
            + "<soapenv:Body>" + payload + "</soapenv:Body></soapenv:Envelope>";
    }

    public static String entitlementsResponse(String userId, List<Entitlement> ents) {
        StringBuilder sb = new StringBuilder();
        sb.append("<ent:getEntitlementsResponse><userId>").append(userId).append("</userId><entitlements>");
        for (Entitlement e : ents) {
            sb.append("<entitlement dataset=\"").append(e.getDatasetCode())
              .append("\" level=\"").append(e.getAccessLevel())
              .append("\" source=\"").append(e.getSource()).append("\"/>");
        }
        sb.append("</entitlements></ent:getEntitlementsResponse>");
        return wrap(sb.toString());
    }

    public static String checkAccessResponse(boolean allowed) {
        return wrap("<ent:checkAccessResponse><allowed>" + allowed + "</allowed></ent:checkAccessResponse>");
    }

    public static String fault(String code, String msg) {
        return wrap("<soapenv:Fault><faultcode>soapenv:" + code + "</faultcode><faultstring>" + msg + "</faultstring></soapenv:Fault>");
    }
}
