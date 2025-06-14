package org.dianaframework.view;

import java.io.Serializable;

/**
 *
 * Uma referencia para uma acao.
 *
 * @version $Id: ActionReference.java 1594 2007-02-07 11:12:15Z bruno $
 * @author <a href="mailto:aryjunior@gmail.com">Ary Junior</a>
 *
 */
public class ActionReference implements Serializable {
private String name;
private String sourceAccessor;

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

}
