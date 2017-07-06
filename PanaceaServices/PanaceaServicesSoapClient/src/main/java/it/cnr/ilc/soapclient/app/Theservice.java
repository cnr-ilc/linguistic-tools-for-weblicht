/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.soapclient.app;

/**
 *
 * @author Riccardo Del Gratta &lt;riccardo.delgratta@ilc.cnr.it&gt;
 */
public class Theservice {
    
    public void printHelp() {
        System.out.println("The service can be used in two ways");
        System.out.println("\n\n Services available:" +Vars.services);
        System.out.println("1) Usage echo \"text_to_analize\" | java -jar <code>.jar -s <service> [-o output_file] [-f <kaf|tcf>]");
        System.out.println("\twhere:");
        System.out.println("\t\t the -l parameter is mandatory and MUST be one out of <it|en|es|fr|de|nl>");
        System.out.println("\t\t the -o parameter is optional  and tells the program to write the output in file");
        System.out.println("\t\t the -f parameter is optional  and tells the program to write the output in the format. If no format is provided a tab output is rendered");

        System.out.println("2) Usage java -jar <code>.jar -l <it|en|es|fr|de|nl> -i input_file [-o output_file] [-f <kaf|tcf>]");
        System.out.println("\twhere:");
        System.out.println("\t\t the -l parameter is mandatory and MUST be one out of <it|en|es|fr|de|nl>");
        System.out.println("\t\t the -i parameter is mandatory and tells the program to read from input file");
        System.out.println("\t\t the -o parameter is optional  and tells the program to write the output in file");
        System.out.println("\t\t the -f parameter is optional  and tells the program to write the output in the format. If no format is provided a tab output is rendered");

        System.out.println("\n\n In case of 2) if no input_file is provided, the program waits for human input from keyboard");
    }
    
}
