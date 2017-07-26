/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.soapclient.app;

import it.cnr.ilc.ilcutils.Vars;
import it.cnr.ilc.panacea.service.i.PanaceaService;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.validator.routines.UrlValidator;

/**
 * This class wraps the input and executes the service
 *
 * @author Riccardo Del Gratta &lt;riccardo.delgratta@ilc.cnr.it&gt;
 */
public class Theservice {

    private PanaceaService service;
    private Map inputs;
    private String input;

    public static final String CLASS_NAME = Theservice.class.getName();

    /**
     * Printhelp
     */
    public void printHelp() {
        System.out.println("This tool wrappers the following Panacea services: " + Vars.services + " and can process both texts and URLs");
        System.out.println("In what follows, -sf and -m parameters depend on the service profile loaded. However, the program exits "
                + "if supplied parameter are not correct");
        System.out.println("\n\nTexts or URLs can be provided in two ways");

        System.out.println("1) Usage echo \"text_to_analize|URL\" | java -jar <code>.jar -l lang -s <service> [-o output_file] [-f <final_output>] [-sf <service_output>] [-m inputs]");
        System.out.println("\twhere:");
        System.out.println("\t\t the -l parameter is mandatory and specifies the language (in three characthers code)");
        System.out.println("\t\t the -s parameter is mandatory and MUST be one out of " + Vars.services);
        System.out.println("\t\t the -o parameter is optional  and tells the program to write the output in file");
        System.out.println("\t\t the -f parameter is optional  and tells the program to write the output in one of the following formats " + Vars.formats
                + " If no format is provided a tab output (which corresponds to " + Vars.OF_DEF + " is rendered. ");
        System.out.println("\t\t the -sf parameter is optional  and tells the program to write the output in passed format. "
                + "If -sf parameter is set and -f parameter is not set, then the final output will be the value passed with -sf");
        System.out.println("\t\t the -m parameter is optional  and tells the program which input parameter to use. The format is as follows:");
        System.out.println("\t\t\t -m\"multiword=true,ner=basic,\"");
        System.out.println("\t\t the -t parameter is optional  and tells the program to read from temporary file instead that from the service "
                + "URL which is the default");

        System.out.println("2) Usage java -jar <code>.jar -s <service> [-i input_file] [-o output_file] "
                + "[-f <final_output>] [-sf <service_output>] [-m inputs] -r");
        System.out.println("\twhere:");
        System.out.println("\t\t the -l parameter is mandatory and specifies the language (in three characthers code)");
        System.out.println("\t\t the -s parameter is mandatory and MUST be one out of " + Vars.services);
        System.out.println("\t\t the -i parameter is optional  and tells the program to read from the output in file");
        System.out.println("\t\t the -o parameter is optional  and tells the program to write the output in file");
        System.out.println("\t\t the -f parameter is optional  and tells the program to write the output in one of the following formats " + Vars.formats
                + " If no format is provided a tab output (which corresponds to " + Vars.OF_DEF + " is rendered. ");
        System.out.println("\t\t the -sf parameter is optional  and tells the program to write the output in passed format. "
                + "If -sf parameter is set and -f parameter is not set, then the final output will be the value passed with -sf");
        System.out.println("\t\t the -m parameter is optional  and tells the program which input parameter to use. The format is as follows:");
        System.out.println("\t\t\t -m\"multiword=true,ner=basic,output_format=token\"");

        System.out.println("\n\n In case of 2) if no input_file is provided, the program waits for human input from keyboard");
        System.out.println("\n\n ******************************************");
        System.out.println("Example (for mode 1, just to see invocation...)");
        System.out.println("echo \"Mi chiamo Alberto e abito a Pisa.\" | java -jar <code>.jar -l ita -s freeling_it -o /tmp/out -f kaf  "
                + "-sf token -m multiword=true,ner=basic -t");
        System.out.println("where:");
        System.out.println("\t -l ita specifies the 3 characters code language");
        System.out.println("\t -s freeling_it indicates that the service profile loaded is freeling_it");
        System.out.println("\t -o /tmp/out indicates that the result of the execution will be saved in a file");
        System.out.println("\t -f kaf indicates that the format of the result is in KAF");
        System.out.println("\t -sf token indicates that the service outputs its result as a list of tokens. "
                + "Since the -f parameter is specified, the list of tokens is rendered as a KAF document.");
        System.out.println("\t -m multiword=true, ner=basic indicates additional parameters");
        System.out.println("\t -t parameter is optional  and tells the program to read from temporary file instead that from the service "
                + "URL which is the default");
        System.out.println("\n\n ******************************************");
        //System.out.println("\t\t\t -m\"multiword=true,ner=basic,output_format=token\"");
    }

    /**
     * @return the service
     */
    public PanaceaService getService() {
        return service;
    }

    /**
     * @param service the service to set
     */
    public void setService(PanaceaService service) {
        this.service = service;
    }

    public void setService(PanaceaService service, String input, Map inputs) {
        this.service = service;
        this.setInput(input);
        this.setInputs(inputs);

    }

    /**
     * This method does the following:
     * <ul>
     * <li>Sets the input parameters as service inputs; </li>
     * <li>Checks if the input string is a URL. If so then executes the corresponding methods; </li>
     * <li>Executes the service. </li>
     * </ul>
     */
    public void run() {
        String message = "", routine = "run";
        boolean fromUrl = false;

        service.setInputs(inputs);

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

        // Map inputs = new HashMap();
        //inputs.put("output_format", "tagged");
        //freelingIt.setInputs(inputs);
        message = String.format("Calling service %s with fromUrl %s", service.getSERVICE_NAME(), fromUrl);
        Logger.getLogger(CLASS_NAME).log(Level.INFO, message);

        service.runService(input, inputs, fromUrl);
        if (service.getStatus() == 0 && !service.getOutputUrl().isEmpty()) {
            message = String.format("Executed service %s with status %s and output url %s",
                    service.getSERVICE_NAME(), service.getStatus(), service.getOutputUrl());
            Logger.getLogger(CLASS_NAME).log(Level.INFO, message);
        }

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

}
