/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package test.util;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author 谢金光
 */
@ManagedBean
@RequestScoped
public class FileUploadHanlder {

    /**
     * Creates a new instance of fileUploadHanlder
     */
    public FileUploadHanlder() {
    }
        public void handleFileUpload(FileUploadEvent fue) {
        UploadedFile uf = fue.getFile();
        String filename = uf.getFileName();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Successful", "The file:" + filename + " is uploaded!"));
    }
    
}
