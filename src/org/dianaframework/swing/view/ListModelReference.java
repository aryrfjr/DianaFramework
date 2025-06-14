package org.dianaframework.swing.view;

import java.io.Serializable;
import javax.swing.AbstractListModel;
import org.dianaframework.view.BusinessObjectsCollection;
import org.dianaframework.view.CollectionModel;
import org.springframework.beans.factory.BeanFactory;

/**
 *
 * Uma referencia para um listmodel.
 *
 * @version $Id: ListModelReference.java 1594 2007-02-07 11:12:15Z bruno $
 * @author <a href="mailto:aryjunior@gmail.com">Ary Junior</a>
 *
 */
public class ListModelReference implements Serializable {
private String name;
private Class clazz;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }
    
    public CollectionModel createModel(BeanFactory bf) throws InstantiationException, ClassNotFoundException, IllegalAccessException {
        AbstractListModel alm = (AbstractListModel)Class.forName(clazz.getName()+"ListModel").newInstance();
        try {
            ((CollectionModel)alm).setBusinessObjectsCollection((BusinessObjectsCollection)bf.getBean(name));
        } catch(Exception e) {
            ((CollectionModel)alm).setBusinessObjectsCollection((BusinessObjectsCollection)clazz.newInstance());
        }
        return (CollectionModel)alm;
    }

}
