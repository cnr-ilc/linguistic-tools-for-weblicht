/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.soapclient.app;

import it.cnr.ilc.ilcfillsimpletypes.basic.FillSimpleTypesFromFreelingIt;
import it.cnr.ilc.ilcfillsimpletypes.basic.i.FillSimpleTypes;
import it.cnr.ilc.ilcutils.Vars;
import it.cnr.ilc.soapclient.i.PanaceaService;
import it.cnr.ilc.soapclient.impl.FreelingIt;

/**
 *
 * @author Riccardo Del Gratta &lt;riccardo.delgratta@ilc.cnr.it&gt;
 */
public class ServiceFactory {

    public PanaceaService getService(String service) {
        if (service == null) {
            return null;
        }
        if (service.equalsIgnoreCase(Vars.FREELING_IT)) {
            return new FreelingIt();
        }
        return null;
    }
    
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
