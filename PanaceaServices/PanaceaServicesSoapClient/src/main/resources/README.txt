Dependency Graph

panaceaServicesSoapClient (This project)
    |
    |----PanaceaServiceImpl  (Implementation of interfaces) 
    |       |
    |       |----PanaceaServiceInterface
    |       |       |
    |       |       |----IlcSimpleTypes (Simple types such as token, lemmas, sentences)
    |       |               |
    |       |               |----IlcUtils  (Some variables)    
    |       |                        
    |       |----IlcIOUtils (Classes and methods to manage string and files) 
    |
    |----IlcConsumerAndProvider  (Consumes the types and provides the outputs) 
            |
            |----IlcSimpleTypes (Simple types such as token, lemmas, sentences)
                    |
                    |----IlcUtils  (Some variables)   