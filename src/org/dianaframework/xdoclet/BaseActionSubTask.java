package org.dianaframework.xdoclet;

import xdoclet.TemplateSubTask;
import xdoclet.XDocletException;
import xjavadoc.XClass;

/**
*
* XDoclet task for base actions.
*
* @version $Id: BaseActionSubTask.java 1594 2007-02-07 11:12:15Z bruno $
* @author <a href="mailto:aryjunior@gmail.com">Ary Junior</a>
*
*/
public class BaseActionSubTask extends TemplateSubTask {
private final static String RESOURCE = "resources/base_action.xdt";
public final static String DEFAULT_NAME = "{0}BaseAction";

    public BaseActionSubTask() {
        setTemplateURL(getClass().getResource(RESOURCE));
        setDestinationFile(DEFAULT_NAME+".java");
        setHavingClassTag("dianaframework:action");
    }

    protected boolean matchesGenerationRules(XClass clazz) throws XDocletException {
        if (getCurrentClass().getDoc().getTag("dianaframework:action") != null && getCurrentClass().getDoc().getTag("dianaframework:action").getAttributeValue("validated-fields") != null) {
            return super.matchesGenerationRules(clazz);
        } else {
            return false;
        }
    }
    
    public void startProcess() throws XDocletException {
        super.getXJavaDoc().setDocEncoding("ISO-8859-1");
        super.startProcess();
    }
    
}
