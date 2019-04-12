/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.openerserviceswrapper.app.core;

import it.cnr.ilc.consumer.Result;
import it.cnr.ilc.restclient.app.ReadExternalPropFiles;
import it.cnr.ilc.restclient.app.SimpleRestClient;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author Riccardo Del Gratta &lt;riccardo.delgratta@ilc.cnr.it&gt;
 */
public class OpenerServiceTokenizerCore {
    private Result result = new Result();
    Properties prop;
    private String outPutAsKaf="";
    
    /**
     * Process the request
     * @param args the list of arguments
     * @param f the file to read
     * @throws Exception Exception
     */
    public synchronized void process(String[] args, File f) throws Exception {
        try {
             prop = ReadExternalPropFiles.getPropertyFile("opener.properties");
        } catch (IOException e) {

            e.printStackTrace();
        }
        SimpleRestClient sc=new SimpleRestClient(prop);
        sc.checkArgs(args);
        result=sc.forservice(true);
        setOutPutAsKaf(sc.getOutPutAsKaf());
        
        

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
     * @return the outPutAsKaf
     */
    public String getOutPutAsKaf() {
        return outPutAsKaf;
    }

    /**
     * @param outPutAsKaf the outPutAsKaf to set
     */
    public void setOutPutAsKaf(String outPutAsKaf) {
        this.outPutAsKaf = outPutAsKaf;
    }
    
}
