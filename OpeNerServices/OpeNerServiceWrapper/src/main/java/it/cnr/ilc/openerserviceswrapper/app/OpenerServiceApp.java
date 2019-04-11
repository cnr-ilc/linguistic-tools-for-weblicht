/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.openerserviceswrapper.app;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import it.cnr.ilc.openerserviceswrapper.app.resources.OpenerServiceTokenizerResource;
import it.cnr.ilc.openerserviceswrapper.app.resources.ReadMeResource;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Riccardo Del Gratta &lt;riccardo.delgratta@ilc.cnr.it&gt;
 */
public class OpenerServiceApp extends Application<OpenerServiceConf> {

    private static String CLASS_NAME = OpenerServiceApp.class.getName();

    /**
     * You can execute as java -jar ABC.jar server [file]. file specify the
     * configuration file. If not provided the 8080 as port and the / as root
     * context are used.
     *
     * @param args the arguments provided. The first is server to start the
     * server. The second is optional and specifies the yaml configuration file.
     * Usually this last one is under src/assembly/conf
     * @throws Exception The possible exception thrown during the process
     */
    public static void main(String[] args) throws Exception {
        String message;
        message = String.format("Init App -%s-", "OpenerServiceApp");
        Logger
                .getLogger(CLASS_NAME).log(Level.INFO, message);

       new OpenerServiceApp().run(args);
    }

    @Override
    public void initialize(Bootstrap<OpenerServiceConf> btstrp) {

    }

    @Override
    public void run(OpenerServiceConf t, Environment environment) throws Exception {
        String message;
        message = String.format("Loading Resources -%s-", "");
        
        Logger.getLogger(CLASS_NAME).log(Level.INFO, "{0}  {1}", new Object[]{message, environment.getApplicationContext().toString()});
        ReadMeResource readmeResource = new ReadMeResource();
        OpenerServiceTokenizerResource tokenizer = new OpenerServiceTokenizerResource();
        environment.jersey().register(readmeResource);
        environment.jersey().register(tokenizer);
    }

}
