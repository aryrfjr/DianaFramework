package org.dianaframework.xdoclet;

/**
*
* XDoclet task for actions.
*
* @version $Id: CollectionSubTask.java 1594 2007-02-07 11:12:15Z bruno $
* @author <a href="mailto:aryjunior@gmail.com">Ary Junior</a>
*
*/
public class CollectionSubTask extends MethodTemplateSubTask {
private final static String RESOURCE = "resources/collection.xdt";
public final static String DEFAULT_NAME = "{0}";

    public CollectionSubTask() {
        setTemplateURL(getClass().getResource(RESOURCE));
        setDestinationFile(DEFAULT_NAME+".java");
        setHavingClassTag("dianaframework:view");
        setDestinationPackageAttribute("collections-package");
        addHavingMethodTag("dianaframework:form-field");
        addMethodTagAttributeValue("model");
        addHavingMethodTag("dianaframework:table");
        addMethodTagAttributeValue("model");
    }
    
}
