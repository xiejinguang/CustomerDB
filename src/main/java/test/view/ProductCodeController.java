package test.view;

import test.entity.ProductCode;
import test.view.util.JsfUtil;
import test.view.util.JsfUtil.PersistAction;
import test.facade.ProductCodeFacade;

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

@Named("productCodeController")
@ViewScoped
public class ProductCodeController implements Serializable {

    @EJB
    private test.facade.ProductCodeFacade ejbFacade;
    private List<ProductCode> items = null;
    private ProductCode created;
    private List<ProductCode> selectedItems;
    private Map<String, Object> searchCons;

    public ProductCodeController() {
    }

    @PostConstruct
    public void init() {
        this.searchCons = new HashMap();
    }

    public ProductCode getCreated() {
        return created;
    }

    public void setCreated(ProductCode created) {
        this.created = created;
    }

    public List<ProductCode> getSelectedItems() {
        return selectedItems;
    }

    public void setSelectedItems(List<ProductCode> selectedItems) {
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

    private ProductCodeFacade getFacade() {
        return ejbFacade;
    }

    public ProductCode prepareCreate() {
        created = new ProductCode();
        initializeEmbeddableKey();
        return created;
    }

    public List<ProductCode> prepareSearch() {
        this.items = null;
        return this.items;

    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("ProductCodeCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("ProductCodeUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("ProductCodeDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selectedItems = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<ProductCode> searchItems() {
        items = getFacade().findByConditions(searchCons);
        return items;
    }

    public List<ProductCode> allItems() {
        items = getFacade().findAll();

        return items;
    }

    public List<ProductCode> getItems() {
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {

        try {

            switch (persistAction) {
                case CREATE:
                    getFacade().edit(created);
                    break;

                default: {
                    for (ProductCode selected : selectedItems) {
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

    public ProductCode getProductCode(java.lang.String id) {
        return getFacade().find(id);
    }

    public List<ProductCode> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<ProductCode> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = ProductCode.class)
    public static class ProductCodeControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ProductCodeController controller = (ProductCodeController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "productCodeController");
            return controller.getProductCode(getKey(value));
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
            if (object instanceof ProductCode) {
                ProductCode o = (ProductCode) object;
                return getStringKey(o.getProdCode());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), ProductCode.class.getName()});
                return null;
            }
        }

    }

}
