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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class explains hot to connect to a panacea service using a list of
 * parameters
 *
 * @author Riccardo Del Gratta &lt;riccardo.delgratta@ilc.cnr.it&gt;
 */
public class SimpleSoapClient {

    
    public static final String CLASS_NAME = SimpleSoapClient.class.getName();

    //private String find_mw = Format.FIND_MW;
    //private String find_ner = Format.FIND_NER;
    private String service = "";
    private String lang = "";
    private String otherInputs = "";
    private String iFile = "";
    private String oFile = "";
    private String format = Format.PANACEA_SERVICE_OUT_TAG;
    private String serviceOutputFormat = Format.PANACEA_SERVICE_OUT_TAG;
    private final String AVAIL_PANACEA_SERVICES = "AVAIL_PANACEA_SERVICES";
    private final String AVAIL_PANACEA_LANGS = "AVAIL_PANACEA_LANGS";
    private LinguisticProcessor lp = new LinguisticProcessor();

    /**
     * if false the output is read from a temporary file instead that from the
     * URL of the service use the swith -t in the list of parameters
     */
    private boolean readOutputFromUrl = true;

    Timestamp timestamp;// = new Timestamp(System.currentTimeMillis());

    private Theservice theservice = new Theservice();
    private List<String> availableServicesFromFile = null;
    private List<String> availablelanguagesFromFile = null;
    private Properties prop;
    private static List<String> PANACEA_SERVICES = new ArrayList<String>();
    private static List<String> PANACEA_LANGS = new ArrayList<String>();

    
    /**
     * Constructor 
     * @param prop property file
     */
    public SimpleSoapClient(Properties prop) {
        String message, routine = "SimpleSoapClient-constructor";
        message = String.format("Initialing class -%s- with routine -%s- ", CLASS_NAME, routine);
        //message = "IOException in closing the stream " + e.getMessage();
        Logger.getLogger(CLASS_NAME).log(Level.INFO, message);
        message = String.format("Getting services and languages routine -%s- ", routine);
        //message = "IOException in closing the stream " + e.getMessage();
        Logger.getLogger(CLASS_NAME).log(Level.INFO, message);
        availableServicesFromFile = getAvailablePanaceaServices(prop, AVAIL_PANACEA_SERVICES);
        availablelanguagesFromFile = getAvailablePanaceaLanguages(prop, AVAIL_PANACEA_LANGS);
        message = String.format("Got services -%s- and languages -%s- routine -%s- ", availableServicesFromFile, availablelanguagesFromFile, routine);
        //message = "IOException in closing the stream " + e.getMessage();
        Logger.getLogger(CLASS_NAME).log(Level.INFO, message);
        setAvailableServicesFromFile(availableServicesFromFile);
        setAvailablelanguagesFromFile(availablelanguagesFromFile);

        message = String.format("Initializing TheService routine -%s- ", routine);
        //message = "IOException in closing the stream " + e.getMessage();
        Logger.getLogger(CLASS_NAME).log(Level.INFO, message);
        Theservice theservice = new Theservice();
        theservice.setListOfLanguages(availablelanguagesFromFile);
        theservice.setListOfServices(availableServicesFromFile);
        setTheservice(theservice);
        setProp(prop);
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
            s.setProp(prop);
            FillSimpleTypes t = factory.getFillSimpleType(getService());
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

            getTheservice().setService(s, input, inputs);
            getTheservice().run();

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
            writer.setFormat(getFormat());
            writer.setServiceFormat(getServiceOutputFormat());
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

            getTheservice().printHelp();
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
     * @param input the string
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
            s.setProp(prop);
            FillSimpleTypes t = factory.getFillSimpleType(getService());
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

            getTheservice().setService(s, input, inputs);
            getTheservice().run();

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

            getTheservice().printHelp();
            //System.err.println("EXIT");
            System.exit(0);
        }
        return result;

    }

    /**
     * 
     * @param paramList The list of additional parameters
     * @param inputs The map to be filled
     * @return the input map with 
     * @throws IllegalArgumentException Exception 
     */
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

     /**
     * 
     * @param args arguments
     * @return true if -h is in the list
     */
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
        getTheservice().printHelp();
    }

    /**
     * 
     * @param service the service to check
     * @return true if service is in the list
     */
    private boolean checkServices(String service) {

        return Vars.panaceaServices.contains(service);

    }

    private boolean checkServiceFormat(String serviceformat) {

        return Format.serviceFormats.contains(serviceformat);

    }

     /**
     * Loops over args and sets values
     * @param args the arguments list
     * @return true if mandatory parameters are supplied
     */
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
                        setServiceOutputFormat(Format.PANACEA_SERVICE_OUT_TAG);
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
    
    /**
     * Reads the available services from the property file
     *
     * @param propFile the property file
     * @param propName the property to get
     * @return the list of available services
     */
    private List<String> getAvailablePanaceaServices(Properties propFile, String propName) {
        String message, routine = "getAvailableOpenerServices";
        message = String.format("Executing routine %s in class %s", routine, CLASS_NAME);
        //message = "IOException in closing the stream " + e.getMessage();
        Logger.getLogger(CLASS_NAME).log(Level.INFO, message);

        List<String> availableServicesFromFile = null;
        String panaceaServices = propFile.getProperty(propName);
        if (panaceaServices == null || panaceaServices.length() == 0) {
            availableServicesFromFile = Collections.unmodifiableList(Arrays.asList(""));;
        } else {

            if (panaceaServices.split(",").length == 1) {
                availableServicesFromFile = Collections.unmodifiableList(Arrays.asList(panaceaServices));
            } else {
                availableServicesFromFile = Collections.unmodifiableList(Arrays.asList(panaceaServices.split(",")));
            }
            //System.err.println("  getIlc4ClarinCorpora "+corporaFromFile+ " "+openCorpora);

            // sets available corpora. This filters out potential test corpora in the korp backend
            setPANACEA_SERVICES(availableServicesFromFile);
            message = String.format("Extracted services %s in routine %s", availableServicesFromFile, routine);
            //message = "IOException in closing the stream " + e.getMessage();
            Logger.getLogger(CLASS_NAME).log(Level.INFO, message);

        }
        return availableServicesFromFile;
    }
    
     /**
     * Reads the available languages from the property file
     *
     * @param propFile the property file
     * @param propName the property to get
     * @return the list of available languages
     */
    private List<String> getAvailablePanaceaLanguages(Properties propFile, String propName) {
        String message, routine = "getAvailableOpenerlanguages";
        message = String.format("Executing routine %s in class %s", routine, CLASS_NAME);
        //message = "IOException in closing the stream " + e.getMessage();
        Logger.getLogger(CLASS_NAME).log(Level.INFO, message);

        List<String> availableLanguagesFromFile = null;
        String panaceaLanguages = propFile.getProperty(propName);
        if (panaceaLanguages == null || panaceaLanguages.length() == 0) {
            availableLanguagesFromFile = Collections.unmodifiableList(Arrays.asList(""));;
        } else {

            if (panaceaLanguages.split(",").length == 1) {
                availableLanguagesFromFile = Collections.unmodifiableList(Arrays.asList(panaceaLanguages));
            } else {
                availableLanguagesFromFile = Collections.unmodifiableList(Arrays.asList(panaceaLanguages.split(",")));
            }
            //System.err.println("  getIlc4ClarinCorpora "+corporaFromFile+ " "+openCorpora);

            // sets available corpora. This filters out potential test corpora in the korp backend
            setPANACEA_LANGS(availableLanguagesFromFile);
            message = String.format("Extracted languages %s in routine %s", availableLanguagesFromFile, routine);
            //message = "IOException in closing the stream " + e.getMessage();
            Logger.getLogger(CLASS_NAME).log(Level.INFO, message);

        }
        return availableLanguagesFromFile;
    }

    /**
     * @return the PANACEA_LANGS
     */
    public static List<String> getPANACEA_LANGS() {
        return PANACEA_LANGS;
    }

    /**
     * @param aPANACEA_LANGS the PANACEA_LANGS to set
     */
    public static void setPANACEA_LANGS(List<String> aPANACEA_LANGS) {
        PANACEA_LANGS = aPANACEA_LANGS;
    }
    
    /**
     * @return the PANACEA_SERVICES
     */
    public static List<String> getPANACEA_SERVICES() {
        return PANACEA_SERVICES;
    }

    /**
     * @param aPANACEA_SERVICES the PANACEA_SERVICES to set
     */
    public static void setPANACEA_SERVICES(List<String> aPANACEA_SERVICES) {
        PANACEA_SERVICES = aPANACEA_SERVICES;
    }

    /**
     * @return the availableServicesFromFile
     */
    public List<String> getAvailableServicesFromFile() {
        return availableServicesFromFile;
    }

    /**
     * @param availableServicesFromFile the availableServicesFromFile to set
     */
    public void setAvailableServicesFromFile(List<String> availableServicesFromFile) {
        this.availableServicesFromFile = availableServicesFromFile;
    }

    /**
     * @return the availablelanguagesFromFile
     */
    public List<String> getAvailablelanguagesFromFile() {
        return availablelanguagesFromFile;
    }

    /**
     * @param availablelanguagesFromFile the availablelanguagesFromFile to set
     */
    public void setAvailablelanguagesFromFile(List<String> availablelanguagesFromFile) {
        this.availablelanguagesFromFile = availablelanguagesFromFile;
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
     * @return the theservice
     */
    public Theservice getTheservice() {
        return theservice;
    }

    /**
     * @param theservice the theservice to set
     */
    public void setTheservice(Theservice theservice) {
        this.theservice = theservice;
    }

}
