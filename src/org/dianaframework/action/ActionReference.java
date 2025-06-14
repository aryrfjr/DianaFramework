package org.dianaframework.action;

import java.io.Serializable;
import java.util.HashMap;

/**
 *
 * Uma referencia para uma acao.
 *
 * @version $Id: ActionReference.java 1767 2007-02-12 13:47:30Z aryjunior $
 * @author <a href="mailto:aryjunior@gmail.com">Ary Junior</a>
 *
 */
public class ActionReference implements Serializable {
private String name;
private String sourceAccessor;
private HashMap<String, String> params = new HashMap();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSourceAccessor() {
        return sourceAccessor;
    }

    public void setSourceAccessor(String sourceAccessor) {
        this.sourceAccessor = sourceAccessor;
    }
    
    public void addParam(String name, String value) {
        getParams().put(name, value);
    }

    public String getParam(String name) {
        return getParams().get(name);
    }

    public HashMap<String, String> getParams() {
        return params;
    }
    
}
