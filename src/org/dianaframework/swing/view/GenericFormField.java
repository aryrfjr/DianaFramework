package org.dianaframework.swing.view;

import java.awt.event.ActionListener;
import java.util.List;

public interface GenericFormField {

    public void setValue(Object value);
    public void setCollection(List collection);
    public Object getValue();
    public void addActionListener(ActionListener al);
    public void setActionCommand(String ac);
    
}
