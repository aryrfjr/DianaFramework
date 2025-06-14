package org.dianaframework.view;

import java.util.HashMap;
import java.util.List;

/**
 *
 * Interface para acesso do framework a classes que recuperam colecoes de objetos para popular itens GUI do Swing.
 *
 * $Id: CollectionModel.java 2100 2007-03-02 20:49:38Z aryjunior $
 * @author <a href="mailto:aryjunior@gmail.com">Ary Junior</a>
 *
 */
public interface CollectionModel {

    public void setBusinessObjectsCollection(BusinessObjectsCollection boc);
    public void reload();
    public void setObjectsCollection(List objects);
    public List getObjects();
    public Object getObjectAt(int index);
    //public Object getSelectedIndex();
    public Object getSelectedObject();
    public List getSelectedObjects();
    public void setSelectedItem(int item);
    public void setSelectedObject(Object obj);
    public String getSelectedValue();
    public void addObject(Object obj);
    public void setColumnsOrder(int[] order);
    public void removeAll();
    public Object removeSelectedObject();
    public Object removeObject(int index);
    public Object removeObject(Object obj);
    public void setUIReference(Object ui);
    public Object getUIReference();
    public void setParameters(HashMap<String, String> parameters);
    public HashMap<String, String> getParameters();
    public void addParameter(String name, String value);
    public void sortByColumn(int column, boolean isAscent);
    
}
