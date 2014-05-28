package test.view;

import test.entity.DiscountCode;
import test.view.util.JsfUtil;
import test.view.util.JsfUtil.PersistAction;
import test.facade.DiscountCodeFacade;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@Named("discountCodeController")
@ViewScoped
public class DiscountCodeController implements Serializable {

    @EJB
    private test.facade.DiscountCodeFacade ejbFacade;
    private List<DiscountCode> items = null;
    private DiscountCode created;
    private List<DiscountCode> selectedItems;
    private Map<String, Object> searchCons;

    public DiscountCodeController() {
    }

    @PostConstruct
    public void init() {
        this.searchCons = new HashMap();
    }

    public DiscountCode getCreated() {
        return created;
    }

    public void setCreated(DiscountCode created) {
        this.created = created;
    }

    public List<DiscountCode> getSelectedItems() {
        return selectedItems;
    }

    public void setSelectedItems(List<DiscountCode> selectedItems) {
        this.selectedItems = selectedItems;
    }

    public Map<String, Object> getSearchCons() {
        return searchCons;
    }

    public void setSearchCons(Map<String, Object> searchCons) {
        this.searchCons = searchCons;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private DiscountCodeFacade getFacade() {
        return ejbFacade;
    }

    public DiscountCode prepareCreate() {
        created = new DiscountCode();
        initializeEmbeddableKey();
        return created;
    }

    public List<DiscountCode> prepareSearch() {
        this.items = null;
        return this.items;

    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("DiscountCodeCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("DiscountCodeUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("DiscountCodeDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selectedItems = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<DiscountCode> searchItems() {
        items = getFacade().findByConditions(searchCons);
        return items;
    }

    public List<DiscountCode> allItems() {
        items = getFacade().findAll();

        return items;
    }

    public List<DiscountCode> getItems() {
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {

        try {

            switch (persistAction) {
                case CREATE:
                    getFacade().edit(created);
                    break;

                default: {
                    for (DiscountCode selected : selectedItems) {
                        if (selected != null) {
                            setEmbeddableKeys();
                            switch (persistAction) {
                                case DELETE:
                                    getFacade().remove(selected);
                                    break;
                                case UPDATE:
                                    getFacade().edit(selected);
                                    break;
                            }
                        }
                    }
                }

            }

            JsfUtil.addSuccessMessage(successMessage);
        } catch (EJBException ex) {
            String msg = "";
            Throwable cause = ex.getCause();
            if (cause != null) {
                msg = cause.getLocalizedMessage();
            }
            if (msg.length() > 0) {
                JsfUtil.addErrorMessage(msg);
            } else {
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }

    }

    public DiscountCode getDiscountCode(java.lang.String id) {
        return getFacade().find(id);
    }

    public List<DiscountCode> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<DiscountCode> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = DiscountCode.class)
    public static class DiscountCodeControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            DiscountCodeController controller = (DiscountCodeController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "discountCodeController");
            return controller.getDiscountCode(getKey(value));
        }

        java.lang.String getKey(String value) {
            java.lang.String key;
            key = value;
            return key;
        }

        String getStringKey(java.lang.String value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof DiscountCode) {
                DiscountCode o = (DiscountCode) object;
                return getStringKey(o.getDiscountCode());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), DiscountCode.class.getName()});
                return null;
            }
        }

    }

}
