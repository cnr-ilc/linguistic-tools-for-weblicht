/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.tokenizer;

import it.cnr.ilc.tokenizer.types.Result;
import it.cnr.ilc.tokenizer.utils.Utilities;
import it.cnr.ilc.tokenizer.utils.Vars;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Riccardo Del Gratta &lt;riccardo.delgratta@ilc.cnr.it&gt;
 */
public class Main {

    private String lang = "";
    private String iFile = "";
    private String oFile = "";
    private String format = "";

    public TokenizerCli tokenizerCli = new TokenizerCli();

    /**
     * ADD A JAVADOC
     */
     public static void main(String[] args) {
        //System.out.println("eccomi " + args.toString());

        //run("it");
        boolean goahead = true;

        Main m = new Main();

        if (m.checkArgsForHelp(args)) {
            m.printTheHelp();
            System.exit(0);
        }
        goahead = m.checkArgs(args);

        m.init(goahead);

    }

    public void printTheHelp() {
        tokenizerCli.printHelp();
    }

    public void init(boolean goahead) {
        PrintStream ps = System.out;
        BufferedReader br = null;
        boolean str = true;
        String input = "";
        String message = "";

        if (goahead) {
            if (getiFile().isEmpty()) {
                try {
                    br = new BufferedReader(new InputStreamReader(System.in));

                    while (str) {

                        //System.out.print("Enter something : ");
                        input = br.readLine();
                        str = false;

                    }

                } catch (IOException e) {
                    //tokenizerCli.printHelp();
                    message = "IOException in reading the stream " + e.getMessage();
                    Logger
                            .getLogger(Main.class
                                    .getName()).log(Level.SEVERE, message);

                    System.exit(-1);

                } finally {
                    if (br != null) {
                        try {
                            br.close();
                        } catch (IOException e) {
                           // tokenizerCli.printHelp();

                            message = "IOException in closing the stream " + e.getMessage();
                            Logger
                                    .getLogger(Main.class
                                            .getName()).log(Level.SEVERE, message);

                            System.exit(-1);

                        }
                    }
                }

            } else {// read the input file
                try {
                    input = Utilities.readFileContent(getiFile());
                } catch (IOException e) {
                    //tokenizerCli.printHelp();
                    System.exit(-1);
                }

            }

            Result r = tokenizerCli.run(lang, input);
            // check output stream
            if (!getoFile().isEmpty()) {
                try {
                    ps = new PrintStream(new File(getoFile()));
                } catch (Exception e) {
                }
            }
            if (!getFormat().isEmpty()) {
                r.toKaf(ps);
            } else {
                r.toTab(ps);
            }
        } else {

            tokenizerCli.printHelp();
            //System.err.println("EXIT");
            System.exit(0);
        }
    }

    private boolean checkArgs(String[] args) {
        boolean ret = true;
        int i = 0;
        if ((args.length % 2) != 0) {
            return false;
        }
        for (String arg : args) {
            switch (arg) {
                case "-l":

                    if (checkLanguages(args[i + 1])) {
                        setLang(args[i + 1]);
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
                case "-f":
                    setFormat(args[i + 1]);
                    break;

            }
            //System.err.println("arg at " + i + "-" + arg + "-");
            i++;
        }

        return true;
    }

    private boolean checkArgsForHelp(String[] args) {

        for (String arg : args) {
            switch (arg) {
                case "-h":

                    return true;

            }
            //System.err.println("arg at " + i + "-" + arg + "-");

        }

        return false;
    }

    private boolean checkLanguages(String lang) {
        List<String> langs = new ArrayList<>();
        return Vars.langs.contains(lang);

    }

    /**
     * @return the lang
     */
    public String getLang() {
        return lang;
    }

    /**
     * @return the iFile
     */
    public String getiFile() {
        return iFile;
    }

    /**
     * @return the oFile
     */
    public String getoFile() {
        return oFile;
    }

    /**
     * @return the format
     */
    public String getFormat() {
        return format;
    }

    /**
     * @param lang the lang to set
     */
    public void setLang(String lang) {
        this.lang = lang;
    }

    /**
     * @param iFile the iFile to set
     */
    public void setiFile(String iFile) {
        this.iFile = iFile;
    }

    /**
     * @param oFile the oFile to set
     */
    public void setoFile(String oFile) {
        this.oFile = oFile;
    }

    /**
     * @param format the format to set
     */
    public void setFormat(String format) {
        this.format = format;
    }

}
