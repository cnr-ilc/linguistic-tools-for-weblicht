/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.panaceasoapclient;

import java.util.HashMap;
import java.util.Map;
import org.soaplab.clients.ClientConfig;
import org.soaplab.clients.ServiceLocator;
import org.soaplab.clients.SoaplabBaseClient;
import org.soaplab.share.SoaplabException;
import org.soaplab.share.SoaplabMap;

/**
 *
 * @author Riccardo Del Gratta &lt;riccardo.delgratta@ilc.cnr.it&gt;
 */
public class SimpleClient {
    static SoaplabBaseClient getClient(String endpoint) {
        ServiceLocator locator = new ServiceLocator();
        locator.setProtocol(ClientConfig.PROTOCOL_AXIS1);
        locator.setServiceEndpoint(endpoint);
        try {
            return new SoaplabBaseClient(locator);
        } catch (SoaplabException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }
 
    static void printCategories() {
        SoaplabBaseClient client = getClient("http://langtech3.ilc.cnr.it:8080/soaplab2-axis/services");
        String[] catgs = client.getAvailableCategories();
        for (String category : catgs) {
            System.out.println(" - " + category);
        }
    }
 
    public static void main(String[] arg) {
        printCategories();
        SoaplabBaseClient client = getClient("http://langtech3.ilc.cnr.it:8080/soaplab2-axis/services/panacea.freeling_it");
        Map inputs = new HashMap();
        inputs.put("input_url", "Mi chiamo Riccardo. Abito a Cascina in una cascina, mi piace il tacchino come a Giunone");
        inputs.put("language", "it.cfg");
        try {
            SoaplabMap results = client.runAndWaitFor(SoaplabMap
                    .fromMap(inputs));
            Map outputs = SoaplabMap.toMap(results);
            System.out.println("Result:\n " + outputs.get("std_output"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
