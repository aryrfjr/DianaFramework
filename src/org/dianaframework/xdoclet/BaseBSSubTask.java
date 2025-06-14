package org.dianaframework.xdoclet;

import xdoclet.TemplateSubTask;
import xdoclet.XDocletException;

/**
*
* XDoclet task for base business services.
*
* @version $Id: BaseBSSubTask.java 1594 2007-02-07 11:12:15Z bruno $
* @author <a href="mailto:aryjunior@gmail.com">Ary Junior</a>
*
*/
public class BaseBSSubTask extends TemplateSubTask {
private final static String RESOURCE = "resources/base_bs.xdt";
public final static String DEFAULT_NAME = "{0}BaseBS";

    public BaseBSSubTask() {
        setTemplateURL(getClass().getResource(RESOURCE));
        setDestinationFile(DEFAULT_NAME+".java");
        setHavingClassTag("dianaframework:business-service");
    }
    
    public void startProcess() throws XDocletException {
        super.getXJavaDoc().setDocEncoding("ISO-8859-1");
        super.startProcess();
    }
    
}