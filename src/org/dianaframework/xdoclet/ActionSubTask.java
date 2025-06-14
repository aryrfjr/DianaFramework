package org.dianaframework.xdoclet;

/**
*
* XDoclet task for actions.
*
* @version $Id: ActionSubTask.java 1594 2007-02-07 11:12:15Z bruno $
* @author <a href="mailto:aryjunior@gmail.com">Ary Junior</a>
*
*/
public class ActionSubTask extends MethodTemplateSubTask {
private final static String RESOURCE = "resources/action.xdt";
public final static String DEFAULT_NAME = "{0}";

    public ActionSubTask() {
        setTemplateURL(getClass().getResource(RESOURCE));
        setDestinationFile(DEFAULT_NAME+".java");
        setHavingClassTag("dianaframework:view");
        setDestinationPackageAttribute("actions-package");
        addHavingMethodTag("dianaframework:action");
        addMethodTagAttributeValue("name");
        addHavingMethodTag("dianaframework:form-field");
        addMethodTagAttributeValue("keypress-action");
        addHavingMethodTag("dianaframework:form-field");
        addMethodTagAttributeValue("focus-gained-action");
        addHavingMethodTag("dianaframework:form-field");
        addMethodTagAttributeValue("selection-action");
        addHavingMethodTag("dianaframework:form-field");
        addMethodTagAttributeValue("change-action");
        addHavingMethodTag("dianaframework:table");
        addMethodTagAttributeValue("click-action");
        addHavingMethodTag("dianaframework:table");
        addMethodTagAttributeValue("doubleclick-action");
    }
    
}
