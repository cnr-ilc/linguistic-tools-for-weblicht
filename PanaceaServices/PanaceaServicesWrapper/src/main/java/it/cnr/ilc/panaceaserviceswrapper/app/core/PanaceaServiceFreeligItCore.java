/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.panaceaserviceswrapper.app.core;

import it.cnr.ilc.consumer.Result;
import it.cnr.ilc.soapclient.app.SimpleClient;
import java.io.File;

/**
 *
 * @author Riccardo Del Gratta &lt;riccardo.delgratta@ilc.cnr.it&gt;
 */
public class PanaceaServiceFreeligItCore {
    private Result result = new Result();
    
    public synchronized void process(String[] args, String str, File f) throws Exception {
        SimpleClient sc=new SimpleClient();
        sc.checkArgs(args);
        result=sc.forservice(true, str);

    }

    /**
     * @return the result
     */
    public Result getResult() {
        return result;
    }

    /**
     * @param result the result to set
     */
    public void setResult(Result result) {
        this.result = result;
    }
    
}
