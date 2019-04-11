/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.restclient.app;

import it.cnr.ilc.ilcutils.Vars;
import it.cnr.ilc.opener.service.i.OpenerService;

import java.util.List;

import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.validator.routines.UrlValidator;

/**
 * This class wraps the input and executes the service
 *
 * @author Riccardo Del Gratta &lt;riccardo.delgratta@ilc.cnr.it&gt;
 */
public class Theservice {

    

    private OpenerService service;
    private Map inputs;
    //
    private String input;
    private Properties prop;
    private List<String> listOfServices;
    private List<String> listOfLanguages;

    public static final String CLASS_NAME = Theservice.class.getName();

    /**
     * Printhelp
     */
    public void printHelp() {
        //TODO HELP IN A PROPER WAY
       
        System.out.println("This tool wraps the following Opener services: " + getListOfServices() + " and can process texts or URLs in different languages: " + getListOfLanguages());
        System.out.println("Languages can be either 3 or 2 chars long, according to ISO 639 ");
        System.out.println("\n\nTexts or URLs can be provided in two ways");

        System.out.println("1) Usage echo \"text_to_analize|URL\" | java -jar <code>.jar -l lang -s <service> -if raw [-o output_file] [-f <final_output>] [-sf <service_output>] [-m inputs]");
        System.out.println("\twhere:");
        System.out.println("\t\t the -l parameter is mandatory and specifies the language (in either three or two characthers code)");
        System.out.println("\t\t the -s parameter is mandatory and MUST be one out of " + getListOfServices());
        System.out.println("\t\t the -if parameter is mandatory and spefifies the input format. It MUST be one out of " + Vars.iFormats);
        System.out.println("\t\t\t If -if is "+Vars.IF_KAF + " the input format is KAF, otherwise a raw text is passed" );
        System.out.println("\t\t the -o parameter is optional  and tells the program to write the output in file");
        System.out.println("\t\t the -f parameter is optional  and tells the program to write the output in one of the following formats " + Vars.openerFormats
                + " If no format is provided a kaf output (which corresponds to " + Vars.OF_KAF + ") is rendered. ");
        System.out.println("\t\t the -sf parameter is optional  and tells the program to write the output in passed format. "
                + "If -sf parameter is set and -f parameter is not set, then the final output will be the value passed with -sf");
        
        

        System.out.println("2) Usage java -jar <code>.jar -s <service> -if kaf [-i input_file] [-o output_file] "
                + "[-f <final_output>] [-sf <service_output>] -r");
        System.out.println("\twhere:");
        System.out.println("\t\t the -l parameter is mandatory and specifies the language (in three characthers code)");
        System.out.println("\t\t the -s parameter is mandatory and MUST be one out of " + getListOfServices());
        System.out.println("\t\t the -if parameter is mandatory and spefifies the input format. It MUST be one out of " + Vars.iFormats);
        System.out.println("\t\t\t If -if is "+Vars.IF_KAF + " the input format is KAF, otherwise a raw text is passed" );
        System.out.println("\t\t the -i parameter is optional  and tells the program to read from the output in file");
        System.out.println("\t\t the -o parameter is optional  and tells the program to write the output in file");
        System.out.println("\t\t the -f parameter is optional  and tells the program to write the output in one of the following formats " + Vars.openerFormats
                + " If no format is provided a tab output (which corresponds to " + Vars.OF_JSON + " is rendered. ");
        System.out.println("\t\t the -sf parameter is optional  and tells the program to write the output in passed format. "
                + "If -sf parameter is set and -f parameter is not set, then the final output will be the value passed with -sf");
       

        System.out.println("\n\n In case of 2) if no input_file is provided, the program waits for human input from keyboard");
        System.out.println("\n\n ******************************************");
        System.out.println("Example (for mode 1, just to see invocation...)");
        System.out.println("echo \"Mi chiamo Alberto e abito a Pisa.\" | java -jar <code>.jar -l ita -s tokenizer -if raw -o /tmp/out -f kaf  ");
        System.out.println("where:");
        System.out.println("\t -l ita specifies the 3 characters code language");
        System.out.println("\t -s freeling_it indicates that the service profile loaded is freeling_it");
        System.out.println("\t -o /tmp/out indicates that the result of the execution will be saved in a file");
        System.out.println("\t -if kaf indicates that the input format is in KAF");
        System.out.println("\t -f kaf indicates that the format of the result is in KAF");
        System.out.println("\t -sf token indicates that the service outputs its result as a list of tokens. "
                + "Since the -f parameter is specified, the list of tokens is rendered as a KAF document.");
        
        System.out.println("\n\n ******************************************");
        //System.out.println("\t\t\t -m\"multiword=true,ner=basic,output_format=token\"");
    }

    /**
     * @return the service
     */
    public OpenerService getService() {
        return service;
    }

    /**
     * @param service the service to set
     */
    public void setService(OpenerService service) {
        this.service = service;
    }

    /**
     * set the service with additional parameters
     * @param service the service
     * @param input the input string
     * @param inputs other parameters
     */
    public void setService(OpenerService service, String input, Map inputs) {
        this.setService(service);
        this.setInput(input);
        this.setInputs(inputs);

    }

    /**
     * This method does the following:
     * <ul>
     * <li>Sets the input parameters as service inputs; </li>
     * <li>Checks if the input string is a URL. If so then executes the
     * corresponding methods; </li>
     * <li>Executes the service. </li>
     * </ul>
     */
    public void run() {
        String message = "", routine = "run";
        boolean fromUrl = false;

        
        UrlValidator defaultValidator = new UrlValidator(); // default schemes
        if (defaultValidator.isValid(input)) {
            fromUrl = true;
            message = String.format("Routine %s. The inputType supplied -%s- requires to be read from a URL. "
                    + "So fromUrl is set to %s", routine, input, fromUrl);
            Logger.getLogger(CLASS_NAME).log(Level.INFO, message);

        } else {
            fromUrl = false;
            message = String.format("The inputType supplied -%s- requires to be read from a String. So fromUrl is set to %s", input, fromUrl);
            Logger.getLogger(CLASS_NAME).log(Level.INFO, message);
        }

      
        message = String.format("Calling service %s with fromUrl %s", getService().getSERVICE_NAME(), fromUrl);
        Logger.getLogger(CLASS_NAME).log(Level.INFO, message);

        getService().runService(input, inputs, fromUrl);
//        if (getService().getStatus() == 0 && !service.getOutputUrl().isEmpty()) {
//            message = String.format("Executed service %s with status %s and output url %s",
//                    getService().getSERVICE_NAME(), getService().getStatus(), getService().getOutputUrl());
//            Logger.getLogger(CLASS_NAME).log(Level.INFO, message);
//        }

    }

   
    

    /**
     * @return the input
     */
    public String getInput() {
        return input;
    }

    /**
     * @param input the input to set
     */
    public void setInput(String input) {
        this.input = input;
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
    
    /**
     * @return the inputs
     */
    public Map getInputs() {
        return inputs;
    }

    /**
     * @param inputs the inputs to set
     */
    public void setInputs(Map inputs) {
        this.inputs = inputs;
    }

    /**
     * @return the listOfLanguages
     */
    public List<String> getListOfLanguages() {
        return listOfLanguages;
    }

    /**
     * @param listOfLanguages the listOfLanguages to set
     */
    public void setListOfLanguages(List<String> listOfLanguages) {
        this.listOfLanguages = listOfLanguages;
    }

    /**
     * @return the listOfServices
     */
    public List<String> getListOfServices() {
        return listOfServices;
    }

    /**
     * @param listOfServices the listOfServices to set
     */
    public void setListOfServices(List<String> listOfServices) {
        this.listOfServices = listOfServices;
    }

}
