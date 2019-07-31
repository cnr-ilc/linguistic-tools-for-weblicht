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
 * Changes made for being used with dockers: The file is read from url and
 * locally created Added a property fromUrl in the json pojo This POJO encodes
 * the following json data when sent as input in a POST request {
 * "file":"the_file_to_read_in_either_kaf_or_raw_format",
 * "language":"one_in_it[a]_de[u]_fr[a]_en[g]_es[p]_nl[d]",
 * "iformat":"either_kaf_or_raw", "oformat":"one_intab_tcf_kaf" }
 *
 * @author Riccardo Del Gratta &lt;riccardo.delgratta@ilc.cnr.it&gt;
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "file",
    "language",
    "iformat",
    "oformat"

})
public class InputData {

    @JsonProperty("file")
    private String file;

    @JsonProperty("language")
    private String language;

    @JsonProperty("iformat")
    private String iformat;

    @JsonProperty("oformat")
    private String oformat;

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

    /**
     * @return the oformat
     */
    @JsonProperty("oformat")
    public String getOformat() {
        return oformat;
    }

    /**
     * @param oformat the oformat to set
     */
    @JsonProperty("oformat")
    public void setOformat(String oformat) {
        this.oformat = oformat;
    }

}
