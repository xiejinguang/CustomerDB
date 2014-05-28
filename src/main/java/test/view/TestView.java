/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package test.view;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author 谢金光
 */
@Named
@ViewScoped
public class TestView {
    
    private String url = "index.jsf";

    /**
     * Get the value of url
     *
     * @return the value of url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Set the value of url
     *
     * @param url new value of url
     */
    public void setUrl(String url) {
        this.url = url;
    }
    public void queueEvent(){ 
    }

    
}
