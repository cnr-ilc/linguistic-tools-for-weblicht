Dependency Graph

panaceaServicesSoapClient (This project)
|
|
|----IlcFillSimpleTypes (Interface and class to read from different service outputs) 
|       |
|       |
        |----IlcUtils  (Some variables)
        |
|       |----IlcIOUtils (Classes and methods to manage string and files)
|       |
|       |----IlcSimpleTypes (Simple types such as token, lemmas, sentences)
|
|----IlcConsumerAndProvider  (Consumes the types and provides the outputs) 
        |
        |
        |----IlcUtils  (Some variables)
        |
        |----IlcSimpleTypes (Simple types such as token, lemmas, sentences)
                |
                |
                |----IlcUtils  (Some variables)