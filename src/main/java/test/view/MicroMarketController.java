package test.view;

import test.entity.MicroMarket;
import test.view.util.JsfUtil;
import test.view.util.JsfUtil.PersistAction;
import test.facade.MicroMarketFacade;

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

@Named("microMarketController")
@ViewScoped
public class MicroMarketController implements Serializable {

    @EJB
    private test.facade.MicroMarketFacade ejbFacade;
    private List<MicroMarket> items = null;
    private MicroMarket created;
    private List<MicroMarket> selectedItems;
    private Map<String, Object> searchCons;

    public MicroMarketController() {
    }

    @PostConstruct
    public void init() {
        this.searchCons = new HashMap();
    }

    public MicroMarket getCreated() {
        return created;
    }

    public void setCreated(MicroMarket created) {
        this.created = created;
    }

    public List<MicroMarket> getSelectedItems() {
        return selectedItems;
    }

    public void setSelectedItems(List<MicroMarket> selectedItems) {
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

    private MicroMarketFacade getFacade() {
        return ejbFacade;
    }

    public MicroMarket prepareCreate() {
        created = new MicroMarket();
        initializeEmbeddableKey();
        return created;
    }

    public List<MicroMarket> prepareSearch() {
        this.items = null;
        return this.items;

    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("MicroMarketCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("MicroMarketUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("MicroMarketDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selectedItems = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<MicroMarket> searchItems() {
        items = getFacade().findByConditions(searchCons);
        return items;
    }

    public List<MicroMarket> allItems() {
        items = getFacade().findAll();

        return items;
    }

    public List<MicroMarket> getItems() {
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {

        try {

            switch (persistAction) {
                case CREATE:
                    getFacade().edit(created);
                    break;

                default: {
                    for (MicroMarket selected : selectedItems) {
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

    public MicroMarket getMicroMarket(java.lang.String id) {
        return getFacade().find(id);
    }

    public List<MicroMarket> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<MicroMarket> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = MicroMarket.class)
    public static class MicroMarketControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            MicroMarketController controller = (MicroMarketController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "microMarketController");
            return controller.getMicroMarket(getKey(value));
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
            if (object instanceof MicroMarket) {
                MicroMarket o = (MicroMarket) object;
                return getStringKey(o.getZipCode());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), MicroMarket.class.getName()});
                return null;
            }
        }

    }

}
