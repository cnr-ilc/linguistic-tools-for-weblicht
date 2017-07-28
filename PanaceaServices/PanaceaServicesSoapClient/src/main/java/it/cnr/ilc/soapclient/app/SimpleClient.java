/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.soapclient.app;

import it.cnr.ilc.consumer.Result;

import it.cnr.ilc.ilcioutils.IlcIOUtils;
import it.cnr.ilc.ilcioutils.IlcInputToFile;
import it.cnr.ilc.ilcutils.Format;
import it.cnr.ilc.ilcutils.Vars;
import it.cnr.ilc.panacea.service.filler.i.FillSimpleTypes;
import it.cnr.ilc.panacea.service.i.PanaceaService;
import it.cnr.ilc.provider.LinguisticProcessor;
import it.cnr.ilc.provider.Writer;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class explains hot to connect to a panacea service using a list of
 * parameters
 *
 * @author Riccardo Del Gratta &lt;riccardo.delgratta@ilc.cnr.it&gt;
 */
public class SimpleClient {

    public static final String CLASS_NAME = SimpleClient.class.getName();

    //private String find_mw = Format.FIND_MW;
    //private String find_ner = Format.FIND_NER;
    private String service = "";
    private String lang = "";
    private String otherInputs = "";
    private String iFile = "";
    private String oFile = "";
    private String format = Format.SERVICE_OUT_TAG;
    private String serviceOutputFormat = Format.SERVICE_OUT_TAG;
    private LinguisticProcessor lp = new LinguisticProcessor();

    /**
     * if false the output is read from a temporary file instead that from the
     * URL of the service use the swith -t in the list of parameters
     */
    private boolean readOutputFromUrl = true;

    Timestamp timestamp;// = new Timestamp(System.currentTimeMillis());

    public Theservice theservice = new Theservice();

    /**
     * This method does the following:
     * <ul>
     * <li>Reads the input from a file (or a URL) if the corresponding parameter
     * is sent to the program. Otherwise waits for an human input; </li>
     * <li>Prepares the factory to instantiate the correct panacea service and
     * filler;</li>
     * <li>Sets the inputs and run the service (using the Theservice class);
     * </li>
     * <li>Gets the output either from a file or the the service URL;</li>
     * <li>Executes the manageServiceOutput method to read from result and fill
     * the basic types;</li>
     * <li>Instantiates the writer;</li>
     * <li>Prints the result in a specific format.</li>
     * </ul>
     *
     * @param goahead if true, then run
     */
    public void init(boolean goahead) {
        PrintStream ps = System.out;
        BufferedReader br = null;
        boolean str = true;
        boolean useps = false;
        String input = "";
        String message = "", routine = "init";
        ServiceFactory factory = new ServiceFactory();
        String ts = "";
        lp.setLayer("text");
        Result result = new Result();

        Map inputs = new HashMap();

        File file;

        if (goahead) {
            if (getiFile().isEmpty()) {
                try {
                    br = new BufferedReader(new InputStreamReader(System.in));
                    while (str) {
                        input = br.readLine();
                        str = false;
                    }

                } catch (IOException e) {
                    message = String.format("IOException reading the stream in routine %s with message %s", routine, e.getMessage());
                    Logger.getLogger(CLASS_NAME).log(Level.SEVERE, message);
                    System.exit(-1);
                } finally {
                    if (br != null) {
                        try {
                            br.close();
                        } catch (IOException e) {

                            message = String.format("IOException closing the stream in routine %s with message %s", routine, e.getMessage());
                            //message = "IOException in closing the stream " + e.getMessage();
                            Logger
                                    .getLogger(CLASS_NAME).log(Level.SEVERE, message);

                            System.exit(-1);

                        }
                    }
                }

            } else {// read the input file
                try {
                    input = IlcIOUtils.readFileContent(getiFile());
                } catch (IOException e) {
                    message = String.format("IOException reading the stream in routine %s from file %s with message %s", routine, getiFile(), e.getMessage());
                    //message = "IOException in closing the stream " + e.getMessage();
                    Logger
                            .getLogger(CLASS_NAME).log(Level.SEVERE, message);

                    //theservice.printHelp();
                    System.exit(-1);
                }

            }

            //System.err.println("input " + input);
            // actual code from here
            PanaceaService s = factory.getService(getService());
            FillSimpleTypes t = factory.getFillSimpleType(service);
            //System.err.println("input in init "+s.getInputs());
            if (!getServiceOutputFormat().isEmpty()) {
                inputs.put("output_format", getServiceOutputFormat());
            }

            //theservice.setService(s);
            if (!getOtherInputs().isEmpty()) {
                inputs = fillOtherInputParams(getOtherInputs(), inputs);
            }
            timestamp = new Timestamp(System.currentTimeMillis());
            ts = s.getClass().getName() + "#" + timestamp.toString().replaceAll(" ", "T") + "Z";

            lp.getLps().add(ts);

            theservice.setService(s, input, inputs);
            theservice.run();

            // get the output
            if (!readOutputFromUrl) {
                file = IlcInputToFile.createAndWriteTempFileFromString(s.getOutputStream());
//                t.manageServiceOutput(t.getLinesFromFile(file), getServiceOutputFormat());
//                
//                for (String line : IlcIOUtils.readFromFile(file)) {
//                    System.err.println("line " + line);
//                }
//                for (String line : t.getLinesFromFile(file)) {
//                    System.err.println("line 1 " + line);
//                }
            } else { // from url
                file = IlcInputToFile.createAndWriteTempFileFromUrl(s.getOutputUrl());

//                //System.err.println("Tokens "+fillSimpleTypesFromFreelingIt.getTokens().toString());
//                //System.err.println("lemmas " + fillSimpleTypesFromFreelingIt.getLemmas());
//                for (IlcSimpleLemma lemma : t.getLemmas()) {
//                    System.err.println(lemma.toKaf());
//                }
            }

            t.manageServiceOutput(t.getLinesFromFile(file), getServiceOutputFormat());
            result.setLinguisticProcessor(lp);
            result.setInput(input);
            result.setLang(getLang());
            result.setSentences(t.createListOfSentences());

            // writer
            Writer writer = new Writer(result);
            writer.setFormat(getServiceOutputFormat());
            if (!t.getLemmas().isEmpty()) {
                result.setLemmas(t.getLemmas());
            }

            // set the printstream
            if (!getoFile().isEmpty()) {
                try {
                    ps = new PrintStream(getoFile());
                    useps = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                ps = System.out;
                useps = false;
            }

            // write the result
            if (getFormat().equals(Format.OUT_KAF)) {
                if (!useps) {
                    System.err.println(writer.toKaf());
                } else {
                    writer.toKaf(ps);
                }
            }

            if (getFormat().equals(Format.OUT_TAB)) {

                if (!useps) {
                    System.err.println(writer.toTab());
                } else {
                    writer.toTab(ps);
                }
            }

            if (getFormat().equals(Format.OUT_TCF)) {
                if (!useps) {
                    writer.toTcf();
                } else {
                    writer.toTcf(ps);
                }
            }

        } else {

            theservice.printHelp();
            //System.err.println("EXIT");
            System.exit(0);
        }

    }

    /**
     * This method does the following:
     * <ul>
     * <li>Reads the input from a file (or a URL) if the corresponding parameter
     * is sent to the program. Otherwise waits for an human input; </li>
     * <li>Prepares the factory to instantiate the correct panacea service and
     * filler;</li>
     * <li>Sets the inputs and run the service (using the Theservice class);
     * </li>
     * <li>Gets the output either from a file or the the service URL;</li>
     * <li>Executes the manageServiceOutput method to read from result and fill
     * the basic types;</li>
     * <li>Instantiates the writer;</li>
     * <li>Prints the result in a specific format.</li>
     * </ul>
     *
     * @param goahead if true, then run
     */
    public Result forservice(boolean goahead, String input) {
        PrintStream ps = System.out;
        BufferedReader br = null;

        boolean useps = false;

        String message = "", routine = "forservice";
        ServiceFactory factory = new ServiceFactory();
        String ts = "";
        lp.setLayer("text");
        Result result = new Result();

        Map inputs = new HashMap();

        File file;

        if (goahead) {


            //System.err.println("input " + input);
            // actual code from here
            PanaceaService s = factory.getService(getService());
            FillSimpleTypes t = factory.getFillSimpleType(service);
            //System.err.println("input in init "+s.getInputs());
            if (!getServiceOutputFormat().isEmpty()) {
                inputs.put("output_format", getServiceOutputFormat());
            }

            //theservice.setService(s);
            if (!getOtherInputs().isEmpty()) {
                inputs = fillOtherInputParams(getOtherInputs(), inputs);
            }
            timestamp = new Timestamp(System.currentTimeMillis());
            ts = s.getClass().getName() + "#" + timestamp.toString().replaceAll(" ", "T") + "Z";

            lp.getLps().add(ts);

            theservice.setService(s, input, inputs);
            theservice.run();

            // get the output
            if (!readOutputFromUrl) {
                file = IlcInputToFile.createAndWriteTempFileFromString(s.getOutputStream());
//              
            } else { // from url
                file = IlcInputToFile.createAndWriteTempFileFromUrl(s.getOutputUrl());

//              
            }

            t.manageServiceOutput(t.getLinesFromFile(file), getServiceOutputFormat());
            result.setLinguisticProcessor(lp);
            result.setInput(input);
            result.setLang(getLang());
            result.setSentences(t.createListOfSentences());

            if (!t.getLemmas().isEmpty()) {
                result.setLemmas(t.getLemmas());
            }

        } else {

            theservice.printHelp();
            //System.err.println("EXIT");
            System.exit(0);
        }
        return result;

    }

    private Map fillOtherInputParams(String paramList, Map inputs) throws IllegalArgumentException {
        String key, value;
        String[] params;
        String message;

        params = paramList.split(",");
        if (params.length == 0) {
            message = String.format("IllegalArgumentException in %s ", paramList);
            throw new IllegalArgumentException(message);
        }

        for (String param : params) {
            String[] p;
            p = param.split("=");
            if (p.length != 2) {
                message = String.format("IllegalArgumentException in %s ", param);
                throw new IllegalArgumentException(message);
            }
            key = p[0];
            value = p[1];

            inputs.put(key, value);
            //System.err.println("XXX inputs "+inputs);

        }
        return inputs;
    }

    public boolean checkArgsForHelp(String[] args) {

        for (String arg : args) {
            switch (arg) {
                case "-h":

                    return true;

            }
            //System.err.println("arg at " + i + "-" + arg + "-");

        }

        return false;
    }

    public void printTheHelp() {
        //tokenizerCli.printHelp();
        //System.err.println("PRINTHELP");
        theservice.printHelp();
    }

    private boolean checkServices(String service) {

        return Vars.services.contains(service);

    }

    private boolean checkServiceFormat(String serviceformat) {

        return Format.serviceFormats.contains(serviceformat);

    }

    public boolean checkArgs(String[] args) {
        boolean ret = false;
        int mandatory = 0;
        int i = 0;
        int l = 0;

        if (Arrays.asList(args).contains("-t")) {
            l = args.length - 1;
        } else {
            l = args.length;
        }
        if ((l % 2) != 0) {
            return false;
        }
        for (String arg : args) {
            switch (arg) {
                case "-s":

                    if (checkServices(args[i + 1])) {
                        setService(args[i + 1]);
                        mandatory = mandatory + 1;
                        break;
                    } else {
                        return false;
                    }
                case "-i":
                    setiFile(args[i + 1]);
                    break;
                case "-o":
                    setoFile(args[i + 1]);
                    break;
                case "-sf":
                    if (checkServiceFormat(args[i + 1])) {
                        setServiceOutputFormat(args[i + 1]);
                    } else {
                        setServiceOutputFormat(Format.SERVICE_OUT_TAG);
                    }

                    break;
                case "-f":
                    setFormat(args[i + 1]);
                    break;
                case "-m":
                    //System.err.println("STOQUI");
                    setOtherInputs(args[i + 1]);
                    break;
                case "-t":
                    setReadOutputFromUrl(false);
                    break;
                case "-l":
                    setLang(args[i + 1]);
                    mandatory = mandatory + 1;
                    break;

            }
            //System.err.println("arg at " + i + "- " + arg + " -");
            i++;
        }
        if (mandatory == 2) {
            ret = true;
        } else {
            ret = false;
        }
        return ret;
    }

    /**
     * @return the service
     */
    public String getService() {
        return service;
    }

    /**
     * @param service the service to set
     */
    public void setService(String service) {
        this.service = service;
    }

    /**
     * @return the iFile
     */
    public String getiFile() {
        return iFile;
    }

    /**
     * @param iFile the iFile to set
     */
    public void setiFile(String iFile) {
        this.iFile = iFile;
    }

    /**
     * @return the oFile
     */
    public String getoFile() {
        return oFile;
    }

    /**
     * @param oFile the oFile to set
     */
    public void setoFile(String oFile) {
        this.oFile = oFile;
    }

    /**
     * @return the otherInputs
     */
    public String getOtherInputs() {
        return otherInputs;
    }

    /**
     * @param otherInputs the otherInputs to set
     */
    public void setOtherInputs(String otherInputs) {
        this.otherInputs = otherInputs;
    }

    /**
     * @return the format
     */
    public String getFormat() {
        return format;
    }

    /**
     * @param format the format to set
     */
    public void setFormat(String format) {
        this.format = format;
    }

    /**
     * @return the serviceOutputFormat
     */
    public String getServiceOutputFormat() {
        return serviceOutputFormat;
    }

    /**
     * @param serviceOutputFormat the serviceOutputFormat to set
     */
    public void setServiceOutputFormat(String serviceOutputFormat) {
        this.serviceOutputFormat = serviceOutputFormat;
    }

    /**
     * @return the readOutputFromUrl
     */
    public boolean isReadOutputFromUrl() {
        return readOutputFromUrl;
    }

    /**
     * @param readOutputFromUrl the readOutputFromUrl to set
     */
    public void setReadOutputFromUrl(boolean readOutputFromUrl) {
        this.readOutputFromUrl = readOutputFromUrl;
    }

    /**
     * @return the lang
     */
    public String getLang() {
        return lang;
    }

    /**
     * @param lang the lang to set
     */
    public void setLang(String lang) {
        this.lang = lang;
    }
}
