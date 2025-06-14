package org.dianaframework.xdoclet;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import xdoclet.TemplateSubTask;
import xdoclet.XDocletException;
import xdoclet.XDocletMessages;
import xdoclet.template.TemplateException;
import xdoclet.util.Translator;
import xjavadoc.XClass;
import xjavadoc.XDoc;
import xjavadoc.XJavaDoc;
import xjavadoc.XMethod;

/**
*
* XDoclet task for generate .java files by method tags.
*
* @version $Id: MethodTemplateSubTask.java 1594 2007-02-07 11:12:15Z bruno $
* @author <a href="mailto:aryjunior@gmail.com">Ary Junior</a>
*
*/
public class MethodTemplateSubTask extends TemplateSubTask {
private ArrayList<String> havingMethodTag = new ArrayList<String>();
protected String destinationPackage = "";
protected String destinationPackageAttribute = "";
private ArrayList<String> methodTagAttributeValue = new ArrayList<String>();

    protected void startProcess() throws XDocletException {
        super.getXJavaDoc().setDocEncoding("ISO-8859-1");
        startProcessPerMethod();
    }
 
    protected void startProcessPerMethod() throws XDocletException {
        //Log log = LogUtil.getLog(TemplateSubTask.class, "startProcessPerMethod");
         //if (log.isDebugEnabled()) {
             System.out.println("Per method.");
         //}
         Collection classes;
         XJavaDoc xjavadoc = getXJavaDoc();
         xjavadoc.setUseNodeParser(false);
         classes = xjavadoc.getSourceClasses(new XJavaDoc.NoInnerClassesPredicate());
         for (Iterator i = classes.iterator(); i.hasNext(); ) {
             XClass clazz = (XClass) i.next();
             if (clazz.getDoc().hasTag(getHavingClassTag())) {
                 if (clazz.getDoc().getTag(getHavingClassTag()).getAttributeValue(getDestinationPackageAttribute()) == null) {
                     System.out.println("There is no actions-package or collections-package attributes on dianaframework.view tag. Nothing to do.");
                 } else {
                     setDestinationPackage(clazz.getDoc().getTag(getHavingClassTag()).getAttributeValue(getDestinationPackageAttribute()));
                     Iterator meth = clazz.getMethods().iterator();
                     while (meth.hasNext()) {
                        XMethod xm = (XMethod)meth.next();
                        // A table and form-field tags can have model attribute containing the collection name
                        // A action can have a name attribute containing the action name
                        // A form-field can have a keypress-action, a focus-gained-action, a selection-action or a change-action attribute containing the action name
                        // A table can have a click-action or a doubleclick-action attribute containing the action name
                        checkHavingMethodTag(xm);
                     }
                 }
             }
         }
    }

    private void checkHavingMethodTag(XMethod xm) throws XDocletException {
        for (int inc = 0; inc < havingMethodTag.size(); inc++)
            if (xm.getDoc().hasTag(havingMethodTag.get(inc)))
                generateForMethod(xm, inc);
    }
    
    /**
    * Processed template for method and generates output file for method.
    *
    */
    protected void generateForMethod(XMethod meth, int index) throws XDocletException {
        //Log log = LogUtil.getLog(TemplateSubTask.class, "generateForMethod");
        XDoc doc = meth.getDoc();
        String value = doc.getTag(getHavingMethodTag(index)).getAttributeValue(getMethodTagAttributeValue(index));
        if (value == null)
         return;
        File file = new File(getDestDir().toString()+"/"+getDestinationPackage().replace('.','/'), value+".java");
        //if (log.isDebugEnabled()) {
            /*System.out.println("destDir.toString()=" + getDestDir().toString());
            System.out.println("getGeneratedFileName()=" + value+".java");
            System.out.println("file=" + file);*/
        //}
        if (file.exists()) {
            System.out.println("The file "+file.getPath()+" cannot be overwrited !!!");
        } else if (!value.equals("closeView") && value.indexOf("showView") == -1 && !value.equals("Empty")) {
            file.getParentFile().mkdirs();
            try {
                setCurrentPackage(meth.getContainingPackage());
                setCurrentClass(meth.getContainingClass());
                setCurrentClassTag(meth.getContainingClass().getDoc().getTag(getHavingClassTag()));
                setCurrentMethodTag(meth.getDoc().getTag(getHavingMethodTag(index)));
                startEngine(getTemplateURL(), file);
            } catch (TemplateException e) {
                if (e instanceof XDocletException) {
                    throw (XDocletException) e;
                } else {
                    System.out.println("generateForMethod()");
                    e.printStackTrace();
                    throw new XDocletException(e, Translator.getString(XDocletMessages.class, XDocletMessages.RUNNING_FAILED));
                }
            }
        }
    }

    public String getHavingMethodTag(int index) {
        return getHavingMethodTag().get(index);
    }

    public void addHavingMethodTag(String havingMethodTag) {
        this.getHavingMethodTag().add(havingMethodTag);
    }

    public String getDestinationPackage() {
        return destinationPackage;
    }

    public void setDestinationPackage(String destinationPackage) {
        if (destinationPackage != null)
            this.destinationPackage = destinationPackage;
    }

    public String getDestinationPackageAttribute() {
        return destinationPackageAttribute;
    }

    public void setDestinationPackageAttribute(String destinationPackageAttribute) {
        this.destinationPackageAttribute = destinationPackageAttribute;
    }

    public String getMethodTagAttributeValue(int index) {
        return getMethodTagAttributeValue().get(index);
    }

    public void addMethodTagAttributeValue(String methodTagAttributeValue) {
        this.getMethodTagAttributeValue().add(methodTagAttributeValue);
    }

    public ArrayList<String> getHavingMethodTag() {
        return havingMethodTag;
    }

    public ArrayList<String> getMethodTagAttributeValue() {
        return methodTagAttributeValue;
    }
    
}
