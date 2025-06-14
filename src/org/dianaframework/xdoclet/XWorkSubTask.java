package org.dianaframework.xdoclet;

import xdoclet.XmlSubTask;

/**
 *
 * Generate the xwork.xml file for XWork Framework.
 *
 * @version $Id: XWorkSubTask.java 1594 2007-02-07 11:12:15Z bruno $
 * @author <a href="mailto:aryjunior@gmail.com">Ary Junior</a>
 *
 */
public class XWorkSubTask extends XmlSubTask {
private final static String DD_PUBLICID = "-//OpenSymphony Group//XWork 1.1.1//EN";
private final static String DD_SYSTEMID = "http://www.opensymphony.com/xwork/xwork-1.1.1.dtd";
private final static String RESOURCE = "resources/xwork_xml.xdt";
private final static String GENERATED_FILE = "xwork.xml";

    public XWorkSubTask() {
        setTemplateURL(getClass().getResource(RESOURCE));
        setDestinationFile(GENERATED_FILE);
        setPublicId(DD_PUBLICID);
        setSystemId(DD_SYSTEMID);
    }

}
