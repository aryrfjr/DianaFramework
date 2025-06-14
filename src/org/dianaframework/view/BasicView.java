package org.dianaframework.view;

import java.util.HashMap;
import org.dianaframework.action.ActionReference;
import org.dianaframework.swing.view.Table;

/**
 *
 * Classe que representa uma view basica.
 *
 * $Id: BasicView.java 1956 2007-02-23 17:58:50Z aryjunior $
 * @author <a href="mailto:aryjunior@gmail.com">Ary Junior</a>
 *
 */
public class BasicView {
private String name;
private String title;
private HashMap<String, FormField> formFields = new HashMap<String, FormField>();
private HashMap<String, Table> tables = new HashMap<String, Table>();
private HashMap<String, ActionReference> actions = new HashMap<String, ActionReference>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addFormField(FormField ff) {
        getFormFields().put(ff.getName().toUpperCase(), ff);
    }

    public void setFormFieldValue(String name, Object value) {
        FormField ff = getFormFields().get(name.toUpperCase());
        if (ff == null)
            System.out.println(name);
        // @todo Campos com datas!!!
        if (value == null && ff.getType().equals(FormField.DATE)) {
            ff.setValue("__/__/____");
        } else {
            ff.setValue(value);
        }
    }
    
    public void setFormFieldValue(String name, Boolean value) {
        FormField ff = getFormFields().get(name.toUpperCase());
        ff.setValue(value == null ? "deselected" : value.booleanValue() ? "selected" : "deselected");
    }
    
    public void setFormFieldValue(String name, int value) {
        getFormFields().get(name.toUpperCase()).setValue(String.valueOf(value));
    }
    
    public void addAction(ActionReference ar) {
        getActions().put(ar.getSourceAccessor(), ar);
    }

    public void addTable(Table t) {
        getTables().put(t.getName(), t);
    }

    public HashMap<String, FormField> getFormFields() {
        return formFields;
    }

    public HashMap<String, ActionReference> getActions() {
        return actions;
    }

    public HashMap<String, Table> getTables() {
        return tables;
    }

    public void show() {
        
    }

    public void update() {
        
    }

    public void reload() {
        
    }
    
    public void close() {
        
    }
    
    public void lockActions() {
    
    }

    public void unlockActions() {
    
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
