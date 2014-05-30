/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package test.util;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.inject.Named;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import test.entity.DiscountCode;

/**
 *
 * @author 谢金光
 */
@Named
@Dependent
public class ChooseDialogView {
      Map<Class, String> viewMap;

    @PostConstruct
    public void init() {
        viewMap = new HashMap<Class, String>();
        viewMap.put(DiscountCode.class, "/discountCode/List.xhtml");
    }



    public <T> void showDlg(String outcome) {
        Map<String,Object> options = new HashMap<String, Object>();
      //  RequestContext.getCurrentInstance().openDialog(outcome);
        options.put("modal", true);
        options.put("draggable", true);
        options.put("resizable", true);
        options.put("height", 450);
        options.put("width", 650);
        options.put("contentMinWidth ", 600);
        options.put("contentMinHeight", 400);
                
        RequestContext.getCurrentInstance().openDialog(outcome,options,null);
    }

    public void onChosen(SelectEvent selectEvent) {

    }

    private <T> String getOutcomeFor(T entityClass) {
        return this.viewMap.get(this);
    }
}
