/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.tokenizer.app;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import it.cnr.ilc.tokenizer.service.resources.ReadMeResource;
import it.cnr.ilc.tokenizer.service.resources.TokenizerBaseResource;
import it.cnr.ilc.tokenizer.service.resources.TokenizerKafResource;
import it.cnr.ilc.tokenizer.service.resources.TokenizerTabResource;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class extends io.dropwizard.Application and uses
 * TokenizerBaseServiceConf as main configuration factory
 *
 * @author Riccardo Del Gratta &lt;riccardo.delgratta@ilc.cnr.it&gt;
 */
public class TokenizerBaseServiceApp extends Application<TokenizerBaseServiceConf> {

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
    private static String CLASS_NAME = TokenizerBaseServiceApp.class
            .getName();

    public static void main(String[] args) throws Exception {
        String message;
        message = String.format("Init App -%s-", "TokenizerBaseServiceApp");
        Logger
                .getLogger(CLASS_NAME).log(Level.INFO, message);

        new TokenizerBaseServiceApp().run(args);
    }

    @Override
    public void initialize(Bootstrap<TokenizerBaseServiceConf> btstrp) {

    }

    /**
     * You can use this method to register your resources. You can do it in two
     * ways:
     * <ol>
     * <li> register a single instance when the server starts up.</li>
     * <li> register a new instance each time the service is invoked
     * (myclass.class)</li>
     * </ol>
     * You should take care of thread safe[ln]ess in your code, essentially
     * synchronizing the process method
     */
    @Override
    public void run(TokenizerBaseServiceConf t, Environment environment) throws Exception {
        
        String message;
        message = String.format("Loading Resources -%s-", "");
        Logger
                .getLogger(CLASS_NAME).log(Level.INFO, message+"  "+environment.getApplicationContext().toString());

//        NamedEntitiesResource namedEntitiesResource = new NamedEntitiesResource();
//        ReferencesResource referencesResource = new ReferencesResource();
        ReadMeResource readmeResource = new ReadMeResource();
        TokenizerBaseResource tokresource = new TokenizerBaseResource();
        TokenizerKafResource kafresource = new TokenizerKafResource();
        TokenizerTabResource tabresource = new TokenizerTabResource();
//        environment.jersey().register(namedEntitiesResource);
//        environment.jersey().register(referencesResource);
        //environment.jersey().register(TokSentencesResource.class);
        environment.jersey().register(tokresource);
        environment.jersey().register(kafresource);
        environment.jersey().register(readmeResource);
        environment.jersey().register(tabresource);
    }

}
