package org.dianaframework.servicelocator;

/**
 *
 * Interface para o Service Locator com o Spring Framework.
 *
 * @version $Id: BusinessServiceFactory.java 1594 2007-02-07 11:12:15Z bruno $
 * @author <a href="mailto:aryjunior@gmail.com">Ary Junior</a>
 *
 */
public interface BusinessServiceFactory {
    
    public BusinessService getService(String name);
    
}
