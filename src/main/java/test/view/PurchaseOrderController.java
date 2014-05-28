package test.view;

import test.entity.PurchaseOrder;
import test.view.util.JsfUtil;
import test.view.util.JsfUtil.PersistAction;
import test.facade.PurchaseOrderFacade;

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

@Named("purchaseOrderController")
@ViewScoped
public class PurchaseOrderController implements Serializable {

    @EJB
    private test.facade.PurchaseOrderFacade ejbFacade;
    private List<PurchaseOrder> items = null;
    private PurchaseOrder created;
    private List<PurchaseOrder> selectedItems;
    private Map<String, Object> searchCons;

    public PurchaseOrderController() {
    }

    @PostConstruct
    public void init() {
        this.searchCons = new HashMap();
    }

    public PurchaseOrder getCreated() {
        return created;
    }

    public void setCreated(PurchaseOrder created) {
        this.created = created;
    }

    public List<PurchaseOrder> getSelectedItems() {
        return selectedItems;
    }

    public void setSelectedItems(List<PurchaseOrder> selectedItems) {
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

    private PurchaseOrderFacade getFacade() {
        return ejbFacade;
    }

    public PurchaseOrder prepareCreate() {
        created = new PurchaseOrder();
        initializeEmbeddableKey();
        return created;
    }

    public List<PurchaseOrder> prepareSearch() {
        this.items = null;
        return this.items;

    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("PurchaseOrderCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("PurchaseOrderUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("PurchaseOrderDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selectedItems = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<PurchaseOrder> searchItems() {
        items = getFacade().findByConditions(searchCons);
        return items;
    }

    public List<PurchaseOrder> allItems() {
        items = getFacade().findAll();

        return items;
    }

    public List<PurchaseOrder> getItems() {
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {

        try {

            switch (persistAction) {
                case CREATE:
                    getFacade().edit(created);
                    break;

                default: {
                    for (PurchaseOrder selected : selectedItems) {
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

    public PurchaseOrder getPurchaseOrder(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<PurchaseOrder> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<PurchaseOrder> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = PurchaseOrder.class)
    public static class PurchaseOrderControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            PurchaseOrderController controller = (PurchaseOrderController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "purchaseOrderController");
            return controller.getPurchaseOrder(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof PurchaseOrder) {
                PurchaseOrder o = (PurchaseOrder) object;
                return getStringKey(o.getOrderNum());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), PurchaseOrder.class.getName()});
                return null;
            }
        }

    }

}
