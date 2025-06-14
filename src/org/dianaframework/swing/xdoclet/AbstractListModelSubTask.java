package org.dianaframework.swing.xdoclet;

import xdoclet.TemplateSubTask;
import xdoclet.XDocletException;

/**
*
* XDoclet task for the swing list model.
*
* @version $Id: AbstractListModelSubTask.java 1594 2007-02-07 11:12:15Z bruno $
* @author <a href="mailto:aryjunior@gmail.com">Ary Junior</a>
*
*/
public class AbstractListModelSubTask extends TemplateSubTask {
private final static String RESOURCE = "resources/abstract_list_model.xdt";
public final static String DEFAULT_NAME = "{0}ListModel";

    public AbstractListModelSubTask() {
        setTemplateURL(getClass().getResource(RESOURCE));
        setDestinationFile(DEFAULT_NAME+".java");
        setHavingClassTag("dianaframework:list-model");
    }
    
    public void startProcess() throws XDocletException {
        super.getXJavaDoc().setDocEncoding("ISO-8859-1");
        super.startProcess();
    }
    
}