package org.dianaframework.model.xdoclet;

import xdoclet.XmlSubTask;

/**
 *
 * Generate the the hibernate.cfg.xml file for persistence.
 *
 * @version $Id: HibernateConfigSubTask.java 1594 2007-02-07 11:12:15Z bruno $
 * @author <a href="mailto:aryjunior@gmail.com">Ary Junior</a>
 *
 */
public class HibernateConfigSubTask extends XmlSubTask {
private final static String DD_PUBLICID = "-//Hibernate/Hibernate Configuration DTD 3.0//EN";
private final static String DD_SYSTEMID = "http://hibernate.sourceforge.net/hibernate-configuration-3.0.dtd";
private final static String RESOURCE = "resources/hibernatecfg_xml.xdt";
private final static String GENERATED_FILE = "hibernate.cfg.xml";

    public HibernateConfigSubTask() {
        setTemplateURL(getClass().getResource(RESOURCE));
        setDestinationFile(GENERATED_FILE);
        setPublicId(DD_PUBLICID);
        setSystemId(DD_SYSTEMID);
    }

}
