/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.openerserviceswrapper.app.json.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.cnr.ilc.openerserviceswrapper.app.json.pojo.InputData;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A basic parser of the pojo input data
 * @author Riccardo Del Gratta &lt;riccardo.delgratta@ilc.cnr.it&gt;
 */
public class InputDataParser {
    private static String CLASS_NAME = InputDataParser.class.getName();

    //private InputData id;
    /**
     * Parse the data
     * @param data the inputdata
     * @return the iputdata 
     * @throws NullPointerException 
     */
    public static InputData getDataFromInput(String data) throws NullPointerException{
        ObjectMapper mapper = new ObjectMapper();
        InputData id=null;
        String message;
        try {
            //id = mapper.readerFor(InputDataParser.class).readValue(data);
            id=mapper.readValue(data, InputData.class);
//            System.out.println("it.cnr.ilc.restclient.app.json.parser.InputDataParser.getDataFromInput() language "+ id.getLanguage());
//            System.out.println("it.cnr.ilc.restclient.app.json.parser.InputDataParser.getDataFromInput() format "+ id.getIformat());
//           System.out.println("it.cnr.ilc.restclient.app.json.parser.InputDataParser.getDataFromInput() file "+ id.getFile());
//           System.out.println("it.cnr.ilc.restclient.app.json.parser.InputDataParser.getDataFromInput() fromUrl "+ id.getFromUrl());
        } catch (Exception ex) {
           
                    message = String.format("Unable to parse the pojo data -%s-", data);
            Logger
                    .getLogger(CLASS_NAME).log(Level.SEVERE, message);
            throw new NullPointerException(message);
           
            
        }
        return id;

    }

}
