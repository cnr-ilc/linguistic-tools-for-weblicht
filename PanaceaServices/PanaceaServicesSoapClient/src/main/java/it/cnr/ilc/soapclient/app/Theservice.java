/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.soapclient.app;

import it.cnr.ilc.soapclient.i.PanaceaService;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.validator.routines.UrlValidator;

/**
 *
 * @author Riccardo Del Gratta &lt;riccardo.delgratta@ilc.cnr.it&gt;
 */
public class Theservice {

    private PanaceaService service;
    private Map inputs;
    private String input;

    public static final String CLASS_NAME = Theservice.class.getName();

    public void printHelp() {
        System.out.println("This tool wrappers the following Panacea services: " + Vars.services + " and can process both texts and URLs");
        System.out.println("Texts or URLs can be provided in two ways");

        System.out.println("1) Usage echo \"text_to_analize|URL\" | java -jar <code>.jar -s <service> [-o output_file] [-f <final_output>] [-sf <service_output] [-m inputs]");
        System.out.println("\twhere:");
        System.out.println("\t\t the -s parameter is mandatory and MUST be one out of " + Vars.services);
        System.out.println("\t\t the -o parameter is optional  and tells the program to write the output in file");
        System.out.println("\t\t the -f parameter is optional  and tells the program to write the output in one of the following formats " + Vars.formats
                + " If no format is provided a tab output (which corresponds to " + Vars.OF_DEF + " is rendered. ");
        System.out.println("\t\t the -sf parameter is optional  and tells the program to write the output in passed format. "
                + "If -sf parameter is set and -f parameter is not set, then the final output will be the value passed with -sf");

        System.out.println("2) Usage java -jar <code>.jar -s <service> [-i input_file] [-o output_file] [-f <final_output>] [-sf <service_output] [-m inputs]");
        System.out.println("\twhere:");
        System.out.println("\t\t the -s parameter is mandatory and MUST be one out of " + Vars.services);
        System.out.println("\t\t the -i parameter is optional  and tells the program to read from the output in file");
        System.out.println("\t\t the -o parameter is optional  and tells the program to write the output in file");
        System.out.println("\t\t the -f parameter is optional  and tells the program to write the output in one of the following formats " + Vars.formats
                + " If no format is provided a tab output (which corresponds to " + Vars.OF_DEF + " is rendered. ");
        System.out.println("\t\t the -sf parameter is optional  and tells the program to write the output in passed format. "
                + "If -sf parameter is set and -f parameter is not set, then the final output will be the value passed with -sf");

        System.out.println("\n\n In case of 2) if no input_file is provided, the program waits for human input from keyboard");
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

    public void run() {
        String message = "";
        boolean fromUrl = false;

        inputs = service.getInputs();

        //s.setInputForService("false", "basic", "tagged");
        inputs = service.getInputs();

        service.setInputs(inputs);
        inputs = service.getInputs();

        //FillSimpleTypesFromFreelingIt fillSimpleTypesFromFreelingIt = new FillSimpleTypesFromFreelingIt();
        // Get an UrlValidator
        UrlValidator defaultValidator = new UrlValidator(); // default schemes
        if (defaultValidator.isValid(input)) {
            fromUrl = true;
            message = String.format("The inputType supplied -%s- requires to be execued reading from a URL. So fromUrl is set to %s", input, fromUrl);
            Logger.getLogger(CLASS_NAME).log(Level.INFO, message);

        } else {
            fromUrl = false;
            message = String.format("The inputType supplied -%s- requires to be execued reading from a String. So fromUrl is set to %s", input, fromUrl);
            Logger.getLogger(CLASS_NAME).log(Level.INFO, message);
        }

        // Map inputs = new HashMap();
        //inputs.put("output_format", "tagged");
        //freelingIt.setInputs(inputs);
        message = String.format("Calling service Freeling_It with fromUrl %s", fromUrl);
        Logger.getLogger(CLASS_NAME).log(Level.INFO, message);

        service.runService(input, fromUrl);
        if (service.getStatus() == 0 && !service.getOutputUrl().isEmpty()) {
            System.err.println("Hi well done: ");
        }
        System.err.println("\t Status: " + service.getStatus());
        System.err.println("\t outputUrl: " + service.getOutputUrl());

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
