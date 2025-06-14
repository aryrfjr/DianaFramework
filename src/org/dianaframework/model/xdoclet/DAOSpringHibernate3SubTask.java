package org.dianaframework.model.xdoclet;

import xdoclet.TemplateSubTask;
import xdoclet.XDocletException;

/**
 *
 * Generate the files for the Spring/Hibernate DAO support.
 *
 * @version $Id: DAOSpringHibernate3SubTask.java 1594 2007-02-07 11:12:15Z bruno $
 * @author <a href="mailto:aryjunior@gmail.com">Ary Junior</a>
 *
 */
public class DAOSpringHibernate3SubTask extends TemplateSubTask {
private final static String RESOURCE = "resources/dao-spring-hibernate3_java.xdt";
public final static String DEFAULT_NAME = "DAO{0}";

    public DAOSpringHibernate3SubTask() {
        setTemplateURL(getClass().getResource(RESOURCE));
        setDestinationFile(DEFAULT_NAME+".java");
        setHavingClassTag("dianaframework:domain-class");
    }
    
    public void startProcess() throws XDocletException {
        super.getXJavaDoc().setDocEncoding("ISO-8859-1");
        super.startProcess();
    }
    
}
