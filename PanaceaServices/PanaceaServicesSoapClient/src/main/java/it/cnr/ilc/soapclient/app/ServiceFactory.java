/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.soapclient.app;


import it.cnr.ilc.ilcutils.Vars;
import it.cnr.ilc.panacea.service.filler.i.FillSimpleTypes;
import it.cnr.ilc.panacea.service.filler.impl.FillSimpleTypesFromFreelingIt;
import it.cnr.ilc.panacea.service.i.PanaceaService;
import it.cnr.ilc.panacea.service.impl.FreelingIt;



/**
 * Factory to instantiate the correct service according to the input
 * @author Riccardo Del Gratta &lt;riccardo.delgratta@ilc.cnr.it&gt;
 */
public class ServiceFactory {

    /**
     * Returns the panacea service
     * @param service the name of the service
     * @return the instantiated service
     */
    public PanaceaService getService(String service) {
        if (service == null) {
            return null;
        }
        if (service.equalsIgnoreCase(Vars.FREELING_IT)) {
            return new FreelingIt();
        }
        return null;
    }
    
    /**
     * Returns the class to read th input and fill basic types
     * @param service the name of the service
     * @return the instantiated class to read the service output 
     */
    public FillSimpleTypes getFillSimpleType(String service){
    if (service == null) {
            return null;
        }
        if (service.equalsIgnoreCase(Vars.FREELING_IT)) {
            return new FillSimpleTypesFromFreelingIt();
        }
        return null;
    }

}
