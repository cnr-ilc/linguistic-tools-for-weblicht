/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.openerserviceswrapper.app.json.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 *
 * @author Riccardo Del Gratta &lt;riccardo.delgratta@ilc.cnr.it&gt;
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "file",
    "language"

})
public class InputData {

    @JsonProperty("file")
    private String file;
    @JsonProperty("language")
    private String language;

    @JsonProperty("iformat")
    private String iformat;

    /**
     * @return the file
     */
    @JsonProperty("file")
    public String getFile() {
        return file;
    }

    /**
     * @param file the file to set
     */
    @JsonProperty("file")
    public void setFile(String file) {
        this.file = file;
    }

    /**
     * @return the language
     */
    @JsonProperty("language")
    public String getLanguage() {
        return language;
    }

    /**
     * @param language the language to set
     */
    @JsonProperty("language")
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * @return the iformat
     */
    @JsonProperty("iformat")
    public String getIformat() {
        return iformat;
    }

    /**
     * @param iformat the iformat to set
     */
    @JsonProperty("iformat")
    public void setIformat(String iformat) {
        this.iformat = iformat;
    }

}
