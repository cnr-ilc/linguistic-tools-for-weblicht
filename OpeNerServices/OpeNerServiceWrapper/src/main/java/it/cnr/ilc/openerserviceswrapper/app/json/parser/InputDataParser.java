/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.openerserviceswrapper.app.json.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.cnr.ilc.openerserviceswrapper.app.json.pojo.InputData;

/**
 *
 * @author Riccardo Del Gratta &lt;riccardo.delgratta@ilc.cnr.it&gt;
 */
public class InputDataParser {

    //private InputData id; 
    public static InputData getDataFromInput(String data) throws NullPointerException{
        ObjectMapper mapper = new ObjectMapper();
        InputData id=null;
        try {
            //id = mapper.readerFor(InputDataParser.class).readValue(data);
            id=mapper.readValue(data, InputData.class);
//            System.out.println("it.cnr.ilc.restclient.app.json.parser.InputDataParser.getDataFromInput() language "+ id.getLanguage());
//            System.out.println("it.cnr.ilc.restclient.app.json.parser.InputDataParser.getDataFromInput() format "+ id.getIformat());
//            System.out.println("it.cnr.ilc.restclient.app.json.parser.InputDataParser.getDataFromInput() file "+ id.getFile());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return id;

    }

}
