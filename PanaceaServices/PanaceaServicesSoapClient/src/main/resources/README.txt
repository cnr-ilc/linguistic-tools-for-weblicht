Dependency Graph

panaceaServicesSoapClient (This project)
|
|
|----IlcFillSimpleTypes (Interface and class to read from different service outputs) 
|       |
|       |
|       |----IlcIOUtils (Classes and methods to manage string and files)
|       |
|       |----IlcSimpleTypes (Simple types such as token, lemmas, sentences)
|
|----IlcConsumerAndProvider  (Consumes the types and provides the outputs) 
        |
        |
        |----IlcSimpleTypes (Simple types such as token, lemmas, sentences)