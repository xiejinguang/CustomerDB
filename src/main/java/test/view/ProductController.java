package test.view;

import test.entity.Product;
import test.view.util.JsfUtil;
import test.view.util.JsfUtil.PersistAction;
import test.facade.ProductFacade;

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


@Named("productController")
@ViewScoped
public class ProductController implements Serializable {


    @EJB private test.facade.ProductFacade ejbFacade;
    private List<Product> items = null;
    private Product created;
    private List<Product> selectedItems;
    private Map<String,Object> searchCons;

    public ProductController() {
    }
    
    @PostConstruct
    public void init(){
        this.searchCons = new HashMap();
    }


    public Product getCreated() {
        return created;
    }

    public void setCreated(Product created) {
        this.created = created;
    }

    public  List<Product> getSelectedItems() {
        return selectedItems;
    }
    
    public void setSelectedItems(List<Product> selectedItems){
        this.selectedItems =selectedItems;
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

    private ProductFacade getFacade() {
        return ejbFacade;
    }

    public Product prepareCreate() {
        created = new Product();
        initializeEmbeddableKey();
        return created;
    }
    public List<Product> prepareSearch(){
        this.items=null;
        return this.items;
        
}

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("ProductCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("ProductUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("ProductDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selectedItems = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Product> searchItems() {
        items = getFacade().findByConditions(searchCons);
        return items;
    }

    public List<Product> allItems() {
            items = getFacade().findAll();

        return items;
    }






    public List<Product> getItems() {
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {

        try {

        switch (persistAction) {
            case CREATE:getFacade().edit(created);break;
            
            default:{
                for( Product selected : selectedItems){
                    if (selected != null) {
                        setEmbeddableKeys();
                        switch (persistAction) {
                            case DELETE: getFacade().remove(selected);break;               
                            case UPDATE: getFacade().edit(selected);break;
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

    public Product getProduct(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<Product> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Product> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass=Product.class)
    public static class ProductControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ProductController controller = (ProductController)facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "productController");
            return controller.getProduct(getKey(value));
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
            if (object instanceof Product) {
                Product o = (Product) object;
                return getStringKey(o.getProductId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Product.class.getName()});
                return null;
            }
        }

    }

}
