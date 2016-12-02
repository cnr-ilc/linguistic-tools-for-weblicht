/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.tokenizer.app;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import it.cnr.ilc.tokenizer.service.resources.TokenizerBaseResource;
import my.org.weblicht.resources.IndexResource;
import my.org.weblicht.resources.TokSentencesResource;

/**
 *
 * @author Riccardo Del Gratta &lt;riccardo.delgratta@ilc.cnr.it&gt;
 */
public class TokenizerBaseServiceApp extends Application<TokenizerBaseServiceConf> {

    public static void main(String[] args) throws Exception {
        new TokenizerBaseServiceApp().run(args);
    }

    @Override
    public void initialize(Bootstrap<TokenizerBaseServiceConf> btstrp) {

    }

    @Override
    public void run(TokenizerBaseServiceConf t, Environment environment) throws Exception {

//        NamedEntitiesResource namedEntitiesResource = new NamedEntitiesResource();
//        ReferencesResource referencesResource = new ReferencesResource();
        IndexResource indexResource = new IndexResource();
        TokenizerBaseResource tokresource = new TokenizerBaseResource();
//        environment.jersey().register(namedEntitiesResource);
//        environment.jersey().register(referencesResource);
        environment.jersey().register(TokSentencesResource.class);
        environment.jersey().register(tokresource);
        environment.jersey().register(indexResource);
    }

}
