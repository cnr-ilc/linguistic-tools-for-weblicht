/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.panaceaserviceswrapper.app.core;

import it.cnr.ilc.consumer.Result;
import it.cnr.ilc.soapclient.app.ReadExternalPropFiles;
import it.cnr.ilc.soapclient.app.SimpleSoapClient;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author Riccardo Del Gratta &lt;riccardo.delgratta@ilc.cnr.it&gt;
 */
public class PanaceaServiceFreeligItCore {
    private Result result = new Result();
    private Properties prop;
    
    /**
     * Process the request
     * @param args the list of arguments
     * @param str the input string to process
     * @param f the file to read
     * @throws Exception Exception
     */
    public synchronized void process(String[] args, String str, File f) throws Exception {
//        try {
//             prop = ReadExternalPropFiles.getPropertyFile("panacea.properties");
//        } catch (IOException e) {
//
//            e.printStackTrace();
//        }
        SimpleSoapClient sc=new SimpleSoapClient(getProp());
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

    /**
     * @return the prop
     */
    public Properties getProp() {
        return prop;
    }

    /**
     * @param prop the prop to set
     */
    public void setProp(Properties prop) {
        this.prop = prop;
    }
    
}
