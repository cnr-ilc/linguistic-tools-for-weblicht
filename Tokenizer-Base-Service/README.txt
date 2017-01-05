NR-ILC Tokenizer services

Offered Services
CNR-ILC provides two distinct web services to perform tokenization on texts in the following languages: it, fr, de, en, es, nl. The application arises an Unsupported language exception if the language provided is not on the list.
The services are essentially the same, but while one consumes a plain text to produce a valid  TCF document, the second both consumes and produces TCF documents.

This page explains how to use the offered services.

The endpoints are the following:
        
	1 wl/tokenizer/plain (use it to tokenize plain texts)
        2 wl/tokenizer/tcf (use it to tokenize TCF documents)
        
 The language is provided as a parameter: 
        
	1 wl/tokenizer/plain?lang=XX
        2 wl/tokenizer/tcf?lang=XX
        
You can test the service endpoints using curl or wget as follows:

    
Send the input file to the endpoints for processing:
With curl:
        
	1 curl -H 'content-type: text/plain' --data-binary @plain-file.txt -X POST THEURL/wl/tokenizer/plain?lang=it
        
        2 curl -H 'content-type: text/tcf+xml' --data-binary @tcf-file.txt -X POST THEURL/wl/tokenizer/tcf?lang=it
        
Or wget:
        
        1 wget --post-file=plain-file.txt --header='Content-Type: text/plain' THEURL/wl/tokenizer/plain?lang=it
        
        2 wget --post-file=tcf-file.txt --header='Content-Type: text/tcf+xml' THEURL/wl/tokenizer/tcf?lang=it
    

       

As for plain text you can use Mi chiamo Riccardo. Abito a Roma
                
As for TCF text you can use 
<?xml version="1.0" encoding="UTF-8"?>
<?xml-model href="http://de.clarin.eu/images/weblicht-tutorials/resources/tcf-04/schemas/latest/d-spin_0_4.rnc" type="application/relax-ng-compact-syntax"?>
    <D-Spin xmlns="http://www.dspin.de/data" version="0.4">
        <md:MetaData xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:cmd="http://www.clarin.eu/cmd/" 
            xmlns:md="http://www.dspin.de/data/metadata" 
            xsi:schemaLocation="http://www.clarin.eu/cmd/ http://catalog.clarin.eu/ds/ComponentRegistry/rest/registry/profiles/clarin.eu:cr1:p_1320657629623/xsd">
        </md:MetaData>
            <tc:TextCorpus xmlns:tc="http://www.dspin.de/data/textcorpus" lang="it">
                <tc:text>
                    Mi chiamo Alfredo. Abito a Roma.
                </tc:text>
            </tc:TextCorpus>
    </D-Spin>

Usage
You can execute as java -jar ABC.jar server [file].
	file specify the configuration file. If not provided the 8080 as port and the / as root context are used. 

