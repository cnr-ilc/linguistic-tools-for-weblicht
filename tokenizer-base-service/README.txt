weblicht-nentities-ws-archetype

Created using WebLicht NamedEntities Webservice Dropwizard Archetype.

What is it?
===========

This is a starter project for TCF processing WebLicht Webservice. It can be
deployed using Maven 2.0.10 or greater with Java 6.0 or greater.
This demo application exposes serveral endpoints for three different webservices:

Tokenization and sentence splitting:
* /toksentence/bytes
* /tokenstence/stream

References identifying:
* /refidf

Name entity recognition:
* /ner/bytes
* /ner/stream


Using it for your own projects
==============================
This project comes with some basic functionality that can easily be used as the
basis for your own project implementing WebLicht web-service.

Depends on the complexity of the tool you want to wrap into a web-service, you can follow the code examples of the three services.

If your tool is simple and doesn't require loading external resources or models, you can follow the code of tokenization and sentence splitting.
If your tool requires considerable amount time and resource to be created, you need to either follow the code of references identifying or named entity recognition, depending on whether the tool is thread-safe or not.
The three services are described in details below:

Tokenization and Sentence Splitting
==========
This web-service imitates the case when its processing tool object is not expensive to create and it does not consume a lot of memory.
In such a case it is convenient to create the tool object with each client POST request, as shown in the service implementation. You don't need to worry about whether the tool is thread-safe or not.

This web-service also demonstrates two ways of returning the TCF output in HTTP response: as a streaming output and as an array of bytes. Both ways have their advantages and disadvantages.
In case of returning bytes array the implementation is simpler, but the whole output TCF is held in memory at once, so in case the TCF output is big, the server might run out of memory. In case of returning streaming output the implementation is slightly more complicated, but TCF output is streamed and only a part of the output is held in memory at a time. This makes it possible to handle TCF output of bigger size.
Notice the following line in ```de.tuebingen.uni.sfs.calrind.DemoApplication.java```

```
environment.jersey().register(TokSentencesResource.class);
```
This makes sure one TokenSentenceResource object is created per request.

All the classes below should be renamed to reflect the name of your tool.

* ```de.tuebingen.uni.sfs.calrind.DemoApplication.java``` - is the application definition.
* ```de.tuebingen.uni.sfs.calrind.resources.TokSentencesResource.java``` - is the definition of a resource, in case more resources are required you can use it as a template for any further resources. (Don't forget to add them to your class that extends Application, e.g. DemoApplication)
* ```de.tuebingen.uni.sfs.calrind.core.TokSentencesTool.java``` - is the place where an actual
implementation of a tool resides. In this template a mock implementation of
a named entities recognizer is provided.

References Identifying
==========
This web-service imitates the case when its processing tool object requires the model for identifying the references.
Since a model can consume much memory and/or require much time when loading, the tool instance is created only once (the corresponding model is loaded only once), when the application is created. The example shows the case when the tool is thread-safe, it can be shared among the clients without any synchronization.

Of course you would probably like to customize the provided code. Let's take a look at the files we have in the project:
* ```ReferencesResource.java```  This is the definition of a resource, in case more resources are required you can use it as a template for any further resources (don't forget to add them to the ReferencesService.java).
Since the resource is registered as a singleton resource, only one its instance is created per application.
The resource initializes a TextCorpusProcessor tool used for processing (in this case ReferencesTool object) in its constructor, so that only one instance of the tool is created per application as well.
This is useful when the tool used for processing consumes much memory and/or requires much time when loading. Annotated with @POST resource method processes client requests containing TCF input and sends response to the clients with the TCF output.
For that, it initializes TextCorpusStreamed object requesting the layers of interest and uses the ReferencesTool object to identify the references and create reference annotations in TCF.
It also takes care about catching exceptions and sending the HTTP error code with short cause message in case an exception occurs during the processing.
* ```ReferencesTool.java``` Here, an actual implementation of a tool resides. In this template an imitation of reference detector is provided.
In case you are writing a web service wrapper for already existing tool, here is where you would call your tool, translating input/output data from/into TCF format.
Here, the wlfxb library can be of a help, as used in this resource implementation. In this example, the tool loads an imitation of a model in its constructor method.
The tool provides process() method that takes TCF document with the layers of interest, uses the loaded model to identify the references in the document, and adds the identified references as a new annotation layer to the TCF document.
This example imitates the thread-safe implementation of the tool. It means that client requests can share the same tool objects and no synchronization is required to call the tool process() method.

Named Entity Recognition
==========
This web-service imitates named entities recognizer service. The service
processes POST requests containing TCF data with tokens. It uses token
annotations to produce named entity annotations.

It imitates a tool that requires loading a named entities list for identifying
named entities. In this web-service example the tool instance is created only
once (the corresponding list resource is loaded only once), when the application
is created. The example shows the case when the tool is not thread-safe.
Therefore, the tool's process() method requires synchronization.

All the classes below should be renamed for your own tool.

* ```de.tuebingen.uni.sfs.calrind.DemoApplication.java``` - is the application definition.
* ```de.tuebingen.uni.sfs.calrind.resources.NamedEntitiesResource.java``` - is the definition of a
resource, in case more resources are required you can use it as a template
for any further resources. (Don't forget to add them to your class that
extends Application, e.g. DemoApplication)
* ```de.tuebingen.uni.sfs.calrind.core.NamedEntitiesTool.java``` - is the place where an actual
implementation of a tool resides. In this template a mock implementation of
a named entities recognizer is provided.

How To Run And Test
=============
Make a runnable jar
```
mvn clean package
```

Run the application
```
java -jar <generated jar> server
```

Once the application is started, it can be accessed using the following URL:

```
http://localhost:8080/
```
Fowllowing the instructions on the homepage, you can test the three services.

It is recommended that you delete all the files for testing when deploying this
tool in a production environment, especially:

* ```src/main/resources/input_ner.xml```
*  ```src/main/resources/input_ref.xml```
*  ```src/main/resources/input_tok.xml```

