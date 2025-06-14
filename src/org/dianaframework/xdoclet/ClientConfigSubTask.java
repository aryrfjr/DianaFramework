package org.dianaframework.xdoclet;

import xdoclet.XmlSubTask;

/**
 *
 * Generate the xwork.xml file for XWork Framework.
 *
 * @version $Id: ClientConfigSubTask.java 1594 2007-02-07 11:12:15Z bruno $
 * @author <a href="mailto:aryjunior@gmail.com">Ary Junior</a>
 *
 */
public class ClientConfigSubTask extends XmlSubTask {
private final static String DD_PUBLICID = "";
private final static String DD_SYSTEMID = "";
private final static String RESOURCE = "resources/clientconfig_xml.xdt";
private final static String GENERATED_FILE = "client-config.xml";

    public ClientConfigSubTask() {
        setTemplateURL(getClass().getResource(RESOURCE));
        setDestinationFile(GENERATED_FILE);
        setPublicId(DD_PUBLICID);
        setSystemId(DD_SYSTEMID);
    }

}
