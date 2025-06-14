package org.dianaframework.view;

import java.util.HashMap;
import java.util.List;

/**
 *
 * Classe basica para classes que recuperam colecoes de objetos para popular itens GUI do Swing.
 *
 * $Id: BusinessObjectsCollection.java 2091 2007-03-02 15:02:16Z aryjunior $
 * @author <a href="mailto:aryjunior@gmail.com">Ary Junior</a>
 *
 */
public class BusinessObjectsCollection {
private HashMap<String, String> parameters = new HashMap();

    /** Creates a new instance of BusinessObjectsCollection */
    public BusinessObjectsCollection() {
    }

    public List collection() {
        return null;
    }

    public HashMap<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(HashMap<String, String> parameters) {
        this.parameters = parameters;
    }
    
    public String getParameter(String name) {
        return parameters.get(name);
    }
    
}
