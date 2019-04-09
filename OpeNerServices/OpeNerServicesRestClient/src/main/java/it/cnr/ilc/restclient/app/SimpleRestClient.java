/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.restclient.app;

import it.cnr.ilc.consumer.Result;

import it.cnr.ilc.opener.service.i.OpenerService;
import it.cnr.ilc.ilcioutils.IlcIOUtils;
import it.cnr.ilc.ilcioutils.IlcInputToFile;
import it.cnr.ilc.ilcutils.Format;
import it.cnr.ilc.ilcutils.Vars;
import it.cnr.ilc.opener.service.filler.i.FillSimpleTypes;

import it.cnr.ilc.provider.Writer;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
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
public class SimpleRestClient {

    public static final String CLASS_NAME = SimpleRestClient.class.getName();

    //private String find_mw = Format.FIND_MW;
    //private String find_ner = Format.FIND_NER;
    private String service = "";
    private String lang = "";
    private String otherInputs = "";
    private String iFile = "";
    private String oFile = "";
    private String format = Format.OPENER_SERVICE_OUT_TAG;
    private String inputFormat = Format.OPENER_SERVICE_IN_TAG;
    private final String AVAIL_OPENER_SERVICES = "AVAIL_OPENER_SERVICES";
    private final String AVAIL_OPENER_LANGS = "AVAIL_OPENER_LANGS";
    private String serviceOutputFormat = Format.OPENER_SERVICE_OUT_TAG;
    //private LinguisticProcessor lp = new LinguisticProcessor();
    private static List<String> OPENER_SERVICES = new ArrayList<String>();
    private Theservice theservice;
    private List<String> availableServicesFromFile = null;
    private List<String> availablelanguagesFromFile = null;
    private Properties prop;

    public SimpleRestClient(Properties prop) {

        String message, routine = "SimpleRestClient-constructor";
        message = String.format("Initialing class -%s- with routine -%s- ", CLASS_NAME, routine);
        //message = "IOException in closing the stream " + e.getMessage();
        Logger.getLogger(CLASS_NAME).log(Level.INFO, message);
        message = String.format("Getting services and languages routine -%s- ", routine);
        //message = "IOException in closing the stream " + e.getMessage();
        Logger.getLogger(CLASS_NAME).log(Level.INFO, message);
        availableServicesFromFile = getAvailableOpenerServices(prop, AVAIL_OPENER_SERVICES);
        availablelanguagesFromFile = getAvailableOpenerlanguages(prop, AVAIL_OPENER_LANGS);
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
     * if false the output is read from a temporary file instead that from the
     * URL of the service use the swith -t in the list of parameters
     */
    private boolean readOutputFromUrl = true;

    Timestamp timestamp;// = new Timestamp(System.currentTimeMillis());

    /**
     * Reads the available services from the property file
     *
     * @param propFile the property file
     * @param propName the property to get
     * @return the list of available services
     */
    private List<String> getAvailableOpenerServices(Properties propFile, String propName) {
        String message, routine = "getAvailableOpenerServices";
        message = String.format("Executing routine %s in class %s", routine, CLASS_NAME);
        //message = "IOException in closing the stream " + e.getMessage();
        Logger.getLogger(CLASS_NAME).log(Level.INFO, message);

        List<String> availableServicesFromFile = null;
        String openerServices = propFile.getProperty(propName);
        if (openerServices == null || openerServices.length() == 0) {
            availableServicesFromFile = Collections.unmodifiableList(Arrays.asList(""));;
        } else {

            if (openerServices.split(",").length == 1) {
                availableServicesFromFile = Collections.unmodifiableList(Arrays.asList(openerServices));
            } else {
                availableServicesFromFile = Collections.unmodifiableList(Arrays.asList(openerServices.split(",")));
            }
            //System.err.println("  getIlc4ClarinCorpora "+corporaFromFile+ " "+openCorpora);

            // sets available corpora. This filters out potential test corpora in the korp backend
            setOPENER_SERVICES(availableServicesFromFile);
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
    private List<String> getAvailableOpenerlanguages(Properties propFile, String propName) {
        String message, routine = "getAvailableOpenerlanguages";
        message = String.format("Executing routine %s in class %s", routine, CLASS_NAME);
        //message = "IOException in closing the stream " + e.getMessage();
        Logger.getLogger(CLASS_NAME).log(Level.INFO, message);

        List<String> availableLanguagesFromFile = null;
        String openerServices = propFile.getProperty(propName);
        if (openerServices == null || openerServices.length() == 0) {
            availableLanguagesFromFile = Collections.unmodifiableList(Arrays.asList(""));;
        } else {

            if (openerServices.split(",").length == 1) {
                availableLanguagesFromFile = Collections.unmodifiableList(Arrays.asList(openerServices));
            } else {
                availableLanguagesFromFile = Collections.unmodifiableList(Arrays.asList(openerServices.split(",")));
            }
            //System.err.println("  getIlc4ClarinCorpora "+corporaFromFile+ " "+openCorpora);

            // sets available corpora. This filters out potential test corpora in the korp backend
            setOPENER_SERVICES(availableLanguagesFromFile);
            message = String.format("Extracted languahes %s in routine %s", availableLanguagesFromFile, routine);
            //message = "IOException in closing the stream " + e.getMessage();
            Logger.getLogger(CLASS_NAME).log(Level.INFO, message);

        }
        return availableLanguagesFromFile;
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
        //lp.setLayer("text");
        Result result = new Result();
        File file;
        Map inputs = new HashMap();

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

            //System.err.println("input " + input + " -" + getFormat() + "- -"+getServiceOutputFormat()+"-");
            // actual code from here
            OpenerService s = factory.getService(getService());
            s.setProp(prop);
            FillSimpleTypes t = factory.getFillSimpleType(getService());


            inputs.put("language", getLang());
            inputs.put("kaf", String.valueOf(isInputFormatKaf(getInputFormat())));
            getTheservice().setService(s, input, inputs);
            getTheservice().run();
            file = IlcInputToFile.createAndWriteTempFileFromString(s.getOutputStream());

            t.manageServiceOutput(file);
            //System.exit(-1);

            // get the output
//            if (!readOutputFromUrl) {
                // XXX     file = IlcInputToFile.createAndWriteTempFileFromString(s.getOutputStream());
//                t.manageServiceOutput(t.getLinesFromFile(file), getServiceOutputFormat());
//                
//                for (String line : IlcIOUtils.readFromFile(file)) {
//                    System.err.println("line " + line);
//                }
//                for (String line : t.getLinesFromFile(file)) {
//                    System.err.println("line 1 " + line);
//                }
//            } else { // from url
                // XXX       file = IlcInputToFile.createAndWriteTempFileFromUrl(s.getOutputUrl());

//                //System.err.println("Tokens "+fillSimpleTypesFromFreelingIt.getTokens().toString());
//                //System.err.println("lemmas " + fillSimpleTypesFromFreelingIt.getLemmas());
//                for (IlcSimpleLemma lemma : t.getLemmas()) {
//                    System.err.println(lemma.toKaf());
//                }
//            }

            // XXX         t.manageServiceOutput(t.getLinesFromFile(file), getServiceOutputFormat());
            //result.setLinguisticProcessor(lp);
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
            if (getFormat().equals(Format.OPENER_SERVICE_OUT_TAG)) {
                if (!useps) {
                    System.err.println(s.getOutputStream());
                 //   System.err.println(writer.toKaf());
                } else {
                    try {
                        //writer.toKaf(ps);
                        ps.write(s.getOutputStream().getBytes("UTF-8"));
                        // ps.writeln(s.getOutputStream());
                    } catch (IOException ex) {
                        Logger.getLogger(SimpleRestClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
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
//    public Result forservice(boolean goahead, String input) {
//        PrintStream ps = System.out;
//        BufferedReader br = null;
//        
//        boolean useps = false;
//        
//        String message = "", routine = "forservice";
//        ServiceFactory factory = new ServiceFactory();
//        String ts = "";
//        //lp.setLayer("text");
//        Result result = new Result();
//        
//        Map inputs = new HashMap();
//        
//        File file;
//        
//        if (goahead) {
//
//            //System.err.println("input " + input);
//            // actual code from here
//            OpenerService s = factory.getService(getService());
//            FillSimpleTypes t = factory.getFillSimpleType(getService());
//            //System.err.println("input in init "+s.getInputs());
//            if (!getServiceOutputFormat().isEmpty()) {
//                inputs.put("output_format", getServiceOutputFormat());
//            }
//
//            //theservice.setService(s);
//            if (!getOtherInputs().isEmpty()) {
//                inputs = fillOtherInputParams(getOtherInputs(), inputs);
//            }
//            timestamp = new Timestamp(System.currentTimeMillis());
//            ts = s.getClass().getName() + "#" + timestamp.toString().replaceAll(" ", "T") + "Z";
//
//            //    lp.getLps().add(ts);
//            getTheservice().setService(s, input, inputs);
//            getTheservice().run();
//
//            // get the output
//            if (!readOutputFromUrl) {
//                file = IlcInputToFile.createAndWriteTempFileFromString(s.getOutputStream());
////              
//            } else { // from url
//                file = IlcInputToFile.createAndWriteTempFileFromUrl(s.getOutputUrl());
//
////              
//            }
//            
//            t.manageServiceOutput(t.getLinesFromFile(file), getServiceOutputFormat());
//            //  result.setLinguisticProcessor(lp);
//            result.setInput(input);
//            result.setLang(getLang());
//            result.setSentences(t.createListOfSentences());
//            
//            if (!t.getLemmas().isEmpty()) {
//                result.setLemmas(t.getLemmas());
//            }
//            
//        } else {
//            
//            getTheservice().printHelp();
//            //System.err.println("EXIT");
//            System.exit(0);
//        }
//        return result;
//        
//    }
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
        getTheservice().printHelp();
    }

    private boolean checkServices(String service) {
        boolean ret = getAvailableServicesFromFile().contains(service);
        //System.err.println("SERVICES RET " + ret);
        return ret;

    }

    private boolean checklanguages(String lang) {

        boolean ret = false;
        /* FIX 3 and 2 chars languages */

        if (lang.equalsIgnoreCase(Vars.ITA)) {

            lang = Vars.IT;
        } else if (lang.equalsIgnoreCase(Vars.DEU)) {
            lang = Vars.DE;
        } else if (lang.equalsIgnoreCase(Vars.FR)) {
            lang = Vars.FRA;
        } else if (lang.equalsIgnoreCase(Vars.ESP)) {
            lang = Vars.ES;
        } else if (lang.equalsIgnoreCase(Vars.NLD)) {
            lang = Vars.NL;
        } else if (lang.equalsIgnoreCase(Vars.ENG)) {
            lang = Vars.EN;
        }
        setLang(lang);
        ret = getAvailablelanguagesFromFile().contains(lang);
        //System.err.println("LANG RET " + ret);
        return ret;

    }

    private boolean checkServiceFormat(String serviceformat) {

        return Format.serviceFormats.contains(serviceformat);

    }

    private boolean checkInputFormat(String inputFormat) {

        boolean ret = Format.iFormats.contains(inputFormat);
        System.err.println("LANG IF " + ret);
        return ret;

    }

    public boolean checkArgs(String[] args) {
        boolean ret = false;
        int mandatory = 0;
        int i = 0;
        int l = args.length;

        if ((l % 2) != 0) {
            return false;
        }
        for (String arg : args) {
            if (i % 2 == 0) {
                System.err.println("arg at -" + i + "- is #" + arg + "# with value -" + args[i + 1] + "-");
            }
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
                        setServiceOutputFormat(Format.OPENER_SERVICE_OUT_TAG);
                    }

                    break;
                case "-if":
                    if (checkInputFormat(args[i + 1])) {
                        setInputFormat(args[i + 1]);
                        mandatory = mandatory + 1;
                    } else {
                        setInputFormat(Format.OPENER_SERVICE_IN_TAG);
                    }

                    break;
                case "-f":
                    setFormat(args[i + 1]);
                    break;

                case "-l":
                    if (checklanguages(args[i + 1])) {
                        //setLang(args[i + 1]);
                        mandatory = mandatory + 1;
                        break;
                    } else {
                        return false;
                    }

            }
            System.out.println("it.cnr.ilc.restclient.app.SimpleRestClient.checkArgs() mandatory " + mandatory);
            i++;
        }
        if (mandatory == 3) {
            ret = true;
        } else {
            ret = false;
        }
        return ret;
    }

    public boolean checkArgs1(String[] args) {
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
     * @return the OPENER_SERVICES
     */
    public static List<String> getOPENER_SERVICES() {
        return OPENER_SERVICES;
    }

    /**
     * @param aOPENER_SERVICES the OPENER_SERVICES to set
     */
    public static void setOPENER_SERVICES(List<String> aOPENER_SERVICES) {
        OPENER_SERVICES = aOPENER_SERVICES;
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
    private void setTheservice(Theservice theservice) {
        this.theservice = theservice;
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
    private void setAvailableServicesFromFile(List<String> availableServicesFromFile) {
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
    private void setAvailablelanguagesFromFile(List<String> availablelanguagesFromFile) {
        this.availablelanguagesFromFile = availablelanguagesFromFile;
    }

    /**
     * @return the inputFormat
     */
    public String getInputFormat() {
        return inputFormat;
    }

    public boolean isInputFormatKaf(String inputFormat) {
        if (inputFormat.equals(Vars.IF_RAW)) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * @param inputFormat the inputFormat to set
     */
    public void setInputFormat(String inputFormat) {
        this.inputFormat = inputFormat;
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
    public final void setProp(Properties prop) {
        this.prop = prop;
    }
}
