package org.dianaframework.servicelocator;

/**
 *
 * Interface que deve ser implementada por todas as classes que forem usar o Service Locator do Spring Framework.
 *
 * @version $Id: BusinessServiceClient.java 1594 2007-02-07 11:12:15Z bruno $
 * @author <a href="mailto:aryjunior@gmail.com">Ary Junior</a>
 *
 */
public interface BusinessServiceClient {
    
    public void setServiceFactory(BusinessServiceFactory sf);
    
}
