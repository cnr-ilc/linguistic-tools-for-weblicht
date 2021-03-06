Linguistic services
This is the Java porting of the perl-based tokenizer developed within the opener project and available  
at https://github.com/opener-project/tokenizer-base
Current Software version 0.1, released on 28/06/2017
ILC4CLARIN provides three sets of distinct web services to perform tokenization on texts for the following languages: 
ita, fra, deu, eng, esp, nld

The application arises an Unsupported Language Exception if the language provided is not in the list.

Offered services perform the same operation (tokenization), but, according with the endpoints, valid TCF,KAF or tabbed files can be produced.

The service that produces TCF can read from both a plain text or a valid TCF document. The mimetype is set accordingly. 

This page explains how to invoke the offered services.
The endpoints are the following:

wl/tokenizer/plain (POST service to tokenize plain text and to produce a TCF valid document)
wl/tokenizer/lrs (GET service to tokenize a text retrieved from URL  and to produce a TCF valid document)
wl/tokenizer/tcf (POST service to tokenize TCF document and to produce a TCF valid document)
            
kaf/tokenizer/plain (POST service to tokenize plain texts and to produce a KAF valid document)
kaf/tokenizer/lrs (GET service to tokenize a text retrieved from URL  and to produce a KAF valid document)
            
tab/tokenizer/plain (POST service to tokenize plain texts and to produce a tabbed document)
tab/tokenizer/lrs (GET service to tokenize a text retrieved from URL  and to produce a tabbed document)
        
The language is provided as a parameter: 

wl/tokenizer/plain?lang=iso_3_codes_lang
wl/tokenizer/tcf?lang=iso_3_codes_lang
            
kaf/tokenizer/plain?lang=iso_3_codes_lang
            
tab/tokenizer/plain?lang=iso_3_codes_lang


        
For Language Resource Switchboard (please note the lrs in the path) we added three additional endpoints
The endpoints are the following:
 
wl/tokenizer/lrs (GET service to tokenize a text retrieved from URL  and to produce a TCF valid document)
            
kaf/tokenizer/lrs (GET service to tokenize a text retrieved from URL  and to produce a KAF valid document)
            
tab/tokenizer/lrs (GET service to tokenize a text retrieved from URL  and to produce a tabbed document)

 
Both the language and the url are provided as a parameters: 
 
wl/tokenizer/lrs?lang=iso_3_codes_lang&amp;url=URL
            
kaf/tokenizer/lrs?lang=iso_3_codes_lang&amp;url=URL
            
tab/tokenizer/lrs?lang=iso_3_codes_lang&amp;url=URL

This because the integration of services in Language Resource Switchboard requires the URL passed as an input parameter.

You can test the service endpoints using curl or wget as follows:
        


You car run the services locally or in a dedicated machine by executing:
java -jar Tokenizer-Base-Service-1.0-SNAPSHOT.jar server (to run the services in the default 8080 port and in the root context /)

Or using a specific port <PORT> and context <CONTEXT> in a configuration file.
If you see the service-config.yaml example file, you should change:
applicationContextPath: WRITE CONTEXT
port: WRITE PORT
Since the config file is external to the application, you can change the values and restart the server. The commando in the following

java -jar Tokenizer-Base-Service-1.0-SNAPSHOT.jar server service-config.yaml

The list of available services and a short howto is available at:

http://HOST:PORT/CONTEXT/readme

Send the input file to the endpoints for processing:
                
With curl:
                
curl -H 'content-type: text/plain' --data-binary @plain-file.txt -X POST http://HOST:PORT/CONTEXT/wl/tokenizer/plain?lang=ita
                
curl -H 'content-type: text/tcf+xml' --data-binary @tcf-file.txt -X POST http://HOST:PORT/CONTEXT/wl/tokenizer/tcf?lang=ita
                
curl -H 'content-type: text/plain' --data-binary @plain-file.txt -X POST http://HOST:PORT/CONTEXT/kaf/tokenizer/plain?lang=ita
                
curl -H 'content-type: text/plain' --data-binary @plain-file.txt -X POST http://HOST:PORT/CONTEXT/tab/tokenizer/plain?lang=it

                
Or using wget:
                
wget --post-file=plain-file.txt --header='Content-Type: text/plain' http://HOST:PORT/CONTEXT/wl/tokenizer/plain?lang=ita
                
wget --post-file=tcf-file.txt --header='Content-Type: text/tcf+xml' http://HOST:PORT/CONTEXT/wl/tokenizer/tcf?lang=ita
                
wget --post-file=plain-file.txt --header='Content-Type: text/plain' http://HOST:PORT/CONTEXT/kaf/tokenizer/plain?lang=ita
                
wget --post-file=plain-file.txt --header='Content-Type: text/plain' http://HOST:PORT/CONTEXT/tab/tokenizer/plain?lang=ita


To test the services for language Resource Switchboard:
With curl:

curl -X GET  "http://HOST:PORT/CONTEXT/wl/tokenizer/lrs?lang=ita&url=https://raw.githubusercontent.com/clarin-eric/LRS-Hackathon/master/samples/resources/txt/hermes-it.txt"
                
                
curl -X GET  "http://HOST:PORT/CONTEXT/kaf/tokenizer/lrs?lang=ita&url=https://raw.githubusercontent.com/clarin-eric/LRS-Hackathon/master/samples/resources/txt/hermes-it.txt"
                
                
curl -X GET  "http://HOST:PORT/CONTEXT/tab/tokenizer/lrs?lang=ita&url=https://raw.githubusercontent.com/clarin-eric/LRS-Hackathon/master/samples/resources/txt/hermes-it.txt"
                

Or using wget:
                
wget  "http://HOST:PORT/CONTEXT/wl/tokenizer/lrs?lang=ita&url=https://raw.githubusercontent.com/clarin-eric/LRS-Hackathon/master/samples/resources/txt/hermes-it.txt" [-O out_file]
                
wget  "http://HOST:PORT/CONTEXT/kaf/tokenizer/lrs?lang=ita&url=https://raw.githubusercontent.com/clarin-eric/LRS-Hackathon/master/samples/resources/txt/hermes-it.txt" [-O out_file]

wget  "http://HOST:PORT/CONTEXT/tab/tokenizer/lrs?lang=ita&url=https://raw.githubusercontent.com/clarin-eric/LRS-Hackathon/master/samples/resources/txt/hermes-it.txt" [-O out_file]


Please note that services designed for Language Resource Switchboard clearly work by themselves invoking the commands above.


As for plain text you can use: 
Mi chiamo Riccardo. Abito a Roma

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
In case of problems write an email to ILC-Clarin-tech-staff@ilc.cnr.it with all the information needed to solve the issues, included the version number.
Use the following subject "Problems in ILC4CLARIN Offered services"


