package org.dianaframework.swing.model;

import java.util.ArrayList;
import java.util.List;
import org.dianaframework.view.BusinessObjectsCollection;

/**
 * 
 * Modelo vazio padrao para 
 *
 */
public class Empty extends BusinessObjectsCollection {
    
    public List collection() {
        return new ArrayList();
    }
    
}
