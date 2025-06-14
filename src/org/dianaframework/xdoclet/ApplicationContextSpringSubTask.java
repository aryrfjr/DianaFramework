package org.dianaframework.xdoclet;

import xdoclet.XmlSubTask;

/**
 *
 * Generate the ApplicationContext.xml file for Spring Framework.
 *
 * @version $Id: ApplicationContextSpringSubTask.java 1594 2007-02-07 11:12:15Z bruno $
 * @author <a href="mailto:aryjunior@gmail.com">Ary Junior</a>
 *
 */
public class ApplicationContextSpringSubTask extends XmlSubTask {
private final static String DD_PUBLICID = "-//SPRING//DTD BEAN//EN";
private final static String DD_SYSTEMID = "http://www.springframework.org/dtd/spring-beans.dtd";
private final static String RESOURCE = "resources/applicationcontextspring_xml.xdt";
private final static String GENERATED_FILE = "ApplicationContext.xml";

    public ApplicationContextSpringSubTask() {
        setTemplateURL(getClass().getResource(RESOURCE));
        setDestinationFile(GENERATED_FILE);
        setPublicId(DD_PUBLICID);
        setSystemId(DD_SYSTEMID);
    }

}
