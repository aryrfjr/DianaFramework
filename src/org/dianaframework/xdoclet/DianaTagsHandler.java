package org.dianaframework.xdoclet;

import java.util.Properties;
import java.util.StringTokenizer;
import org.dianaframework.view.FormField;
import xdoclet.XDocletException;
import xdoclet.XDocletTagSupport;
import xjavadoc.XClass;
import xjavadoc.XMethod;

/**
 *
 * @version $Id: DianaTagsHandler.java 2091 2007-03-02 15:02:16Z aryjunior $
 * @author <a href="mailto:aryjunior@gmail.com">Ary Junior</a>
 *
 * @xdoclet.taghandler
 *   namespace="Diana"
 *
 */
public class DianaTagsHandler extends XDocletTagSupport {
private XView currentView;

    /**
     *
     * Return the package for collections to be generated.
     *
     */
    public String viewCollectionPackage() throws XDocletException {
        return getCurrentClassTag().getAttributeValue("collections-package");
    }
    
    /**
     *
     * Return the name for collections to be generated.
     *
     */
    public String collectionName() throws XDocletException {
        return getCurrentMethodTag().getAttributeValue("model");
    }
    
    /**
     *
     * Return the package for actions to be generated.
     *
     */
    public String viewActionPackage() throws XDocletException {
        return getCurrentClassTag().getAttributeValue("actions-package");
    }
    
    /**
     *
     * Return the name for actions to be generated.
     *
     */
    public String actionName() throws XDocletException {
        if (getCurrentMethodTag().getAttributeValue("name") != null)
            return getCurrentMethodTag().getAttributeValue("name");
        else if (getCurrentMethodTag().getAttributeValue("keypress-action") != null)
            return getCurrentMethodTag().getAttributeValue("keypress-action");
        else if (getCurrentMethodTag().getAttributeValue("focus-gained-action") != null)
            return getCurrentMethodTag().getAttributeValue("focus-gained-action");
        else if (getCurrentMethodTag().getAttributeValue("selection-action") != null)
            return getCurrentMethodTag().getAttributeValue("selection-action");
        else if (getCurrentMethodTag().getAttributeValue("change-action") != null)
            return getCurrentMethodTag().getAttributeValue("change-action");
        else if (getCurrentMethodTag().getAttributeValue("click-action") != null)
            return getCurrentMethodTag().getAttributeValue("click-action");
        else if (getCurrentMethodTag().getAttributeValue("doubleclick-action") != null)
            return getCurrentMethodTag().getAttributeValue("doubleclick-action");
        else
            return "";
    }
    
    /**
     *
     * Generate code for action interceptors params.
     *
     */
    public void interceptorParams(String content, Properties attributes) throws XDocletException {
        StringTokenizer stk = new StringTokenizer(getCurrentClassTag().getAttributeValue("params"), ";");
        String param = null;
        int inc = 0;
        while (stk.hasMoreTokens()) {
            param = stk.nextToken();
            generate("<param name=\""+param.substring(0,param.indexOf("="))+"\">"+param.substring(param.indexOf("=")+1)+"</param>\n");
        }
    }
    
    /**
     *
     * Generate code for action results params.
     *
     */
    public void resultParams(String content, Properties attributes) throws XDocletException {
        StringTokenizer stk = new StringTokenizer(getCurrentClassTag().getAttributeValue("params"), ";");
        String param = null;
        int inc = 0;
        while (stk.hasMoreTokens()) {
            param = stk.nextToken();
            generate("<param name=\""+param.substring(0,param.indexOf("="))+"\">"+param.substring(param.indexOf("=")+1)+"</param>\n");
        }
    }
    
    /**
     *
     * Generate code for table model column names.
     *
     */
    public void tableModelColumnLabels(String content, Properties attributes) throws XDocletException {
        XClass currentClass = getCurrentClass();
        StringTokenizer stk = new StringTokenizer(currentClass.getDoc().getTag("dianaframework:table-model").getAttributeValue("column-labels"), ",");
        String columnLabel = null;
        int inc = 0;
        while (stk.hasMoreTokens()) {
            columnLabel = stk.nextToken();
            generate("case "+(inc++)+": return \""+columnLabel+"\";\n");
        }
    }
    
    /**
     *
     * Generate code for table model column order.
     *
     */
    public void tableModelColumnsOrder(String content, Properties attributes) throws XDocletException {
        XClass currentClass = getCurrentClass();
        StringTokenizer stk = new StringTokenizer(currentClass.getDoc().getTag("dianaframework:table-model").getAttributeValue("column-labels"), ",");
        generate("columnsOrder = new int["+stk.countTokens()+"];\n");
        for (int inc = 0; inc < stk.countTokens(); inc++) {
            generate("columnsOrder["+inc+"] = "+inc+";\n");
        }
    }
    
    /**
     *
     * Generate code for table model column values.
     *
     */
    public void tableModelColumnValues(String content, Properties attributes) throws XDocletException {
        XClass currentClass = getCurrentClass();
        StringTokenizer stk = new StringTokenizer(currentClass.getDoc().getTag("dianaframework:table-model").getAttributeValue("column-values"), ",");
        String columnValue = null;
        int inc = 0;
        while (stk.hasMoreTokens()) {
            columnValue = stk.nextToken();
            generate("case "+(inc++)+": return object."+columnValue+"();\n");
        }
    }
    
    /**
     *
     * Generate code for load all methods of a bean thats used in table models.
     *
     */
    public void tableModelBeanLoad(String content, Properties attributes) throws XDocletException {
        XClass currentClass = getCurrentClass();
        String bean = currentClass.getDoc().getTag("dianaframework:table-model").getAttributeValue("bean");
        StringTokenizer stk = new StringTokenizer(currentClass.getDoc().getTag("dianaframework:table-model").getAttributeValue("column-values"), ",");
        String columnValue = null;
        int inc = 0;
        while (stk.hasMoreTokens()) {
            columnValue = stk.nextToken();
            generate("(("+bean+")obj)."+columnValue+"();\n");
        }
    }
    
    /**
     *
     * Generate code for table model column count.
     *
     */
    public void tableModelColumnCount(String content, Properties attributes) throws XDocletException {
        XClass currentClass = getCurrentClass();
        StringTokenizer stk = new StringTokenizer(currentClass.getDoc().getTag("dianaframework:table-model").getAttributeValue("column-values"), ",");
        generate("return "+stk.countTokens()+";\n");
    }

    /**
     *
     * Generate code for fields validateds on base actions.
     *
     */
    public void validatedFieldsBaseAction(String content, Properties attributes) throws XDocletException {
        // @todo 22012007: Validacao de campos do tipo data. Diferenciando a geracao para campos do tipo DATE
        XClass currentClass = getCurrentClass();
        if (currentClass.getDoc().getTag("dianaframework:action").getAttributeValue("validated-fields") == null) return;
        String types = currentClass.getDoc().getTag("dianaframework:action").getAttributeValue("validated-fields-types");
        StringTokenizer stkt = types == null ? null : new StringTokenizer(types, ",");
        StringTokenizer stk = new StringTokenizer(currentClass.getDoc().getTag("dianaframework:action").getAttributeValue("validated-fields"), ",");
        String field, type;
        while (stk.hasMoreTokens()) {
            field = stk.nextToken();
            if (stkt == null) {
                generate("public String get"+field+"() {\n");
                generate("    return getFormFieldValue(\""+field+"\").toString();\n");
            } else {
                type = stkt.nextToken();
                if (type.equals(FormField.DATE)) {
                    generate("public Date get"+field+"() {\n");
                    generate("    return getDateFormFieldValue(\""+field+"\");\n");
                } else {
                    generate("public String get"+field+"() {\n");
                    generate("    return getFormFieldValue(\""+field+"\").toString();\n");
                }
            }
            generate("}\n\n");
        }
    }
    
    /**
     *
     * Generate code for DAOs declaration on base business services.
     *
     */
    public void daosDeclarationBaseBS(String content, Properties attributes) throws XDocletException {
        XClass currentClass = getCurrentClass();
        if (currentClass.getDoc().getTag("dianaframework:business-service").getAttributeValue("domain-classes") == null) return;
        StringTokenizer stk = new StringTokenizer(currentClass.getDoc().getTag("dianaframework:business-service").getAttributeValue("domain-classes"), ",");
        String dc;
        while (stk.hasMoreTokens()) {
            dc = stk.nextToken();
            generate("protected GenericDAO dao"+dc.substring(dc.lastIndexOf(".")+1)+";\n");
        }
    }
    
    /**
     *
     * Generate code for DAOs accessors on base business services.
     *
     */
    public void daosAccessorsBaseBS(String content, Properties attributes) throws XDocletException {
        XClass currentClass = getCurrentClass();
        if (currentClass.getDoc().getTag("dianaframework:business-service").getAttributeValue("domain-classes") == null) return;
        StringTokenizer stk = new StringTokenizer(currentClass.getDoc().getTag("dianaframework:business-service").getAttributeValue("domain-classes"), ",");
        String dc;
        while (stk.hasMoreTokens()) {
            dc = stk.nextToken();
            dc = dc.substring(dc.lastIndexOf(".")+1);
            generate("public GenericDAO getDao"+dc+"() {\n");
            generate("    return dao"+dc+";\n");
            generate("}\n\n");
            generate("public void setDao"+dc+"(GenericDAO dao"+dc+") {\n");
            generate("    this.dao"+dc+" = dao"+dc+";\n");
            generate("}\n\n");
        }
    }
    
    /**
     *
     * Generate XML in ApplicationContext.xml for daos attributes in business services.
     *
     */
    public void daosAttributeBS(String content, Properties attributes) throws XDocletException {
        XClass currentClass = getCurrentClass();
        if (currentClass.getDoc().getTag("dianaframework:business-service").getAttributeValue("domain-classes") == null) return;
        StringTokenizer stk = new StringTokenizer(currentClass.getDoc().getTag("dianaframework:business-service").getAttributeValue("domain-classes"), ",");
        String dc, dao;
        while (stk.hasMoreTokens()) {
            dc = stk.nextToken();
            dc = dc.substring(dc.lastIndexOf(".")+1);
            generate("        <property name=\"dao"+dc+"\">\n");
            generate("            <ref bean=\"DAO"+dc+"\"/>\n");
            generate("        </property>\n\n");
        }
    }
    
    /**
     *
     * Iterate all views, for the generic views.
     *
     */
    public void forAllViews(String content, Properties attributes) throws XDocletException {
        XClass currentClass = getCurrentClass();
        String[] views = tokensToArray(currentClass.getDoc().getTag("dianaframework:view").getAttributeValue("name"), ",");
        String[] titles = tokensToArray(currentClass.getDoc().getTag("dianaframework:view").getAttributeValue("title"), ",");
        XView view;
        for (int inc = 0; inc < views.length; inc++) {
            view = new XView();
            view.setName(views[inc]);
            view.setIndex(inc);
            view.setTitle(titles[inc >= titles.length  - 1 ? titles.length  - 1 : inc]);
            setCurrentView(view);
            generate(content);
        }
    }

    /**
     *
     * Generate the current view name.
     *
     */
    public String viewName() throws XDocletException {
        XView view = getCurrentView();
        return view.getName();
    }
    
    /**
     *
     * Generate the current view window title.
     *
     */
    public String viewTitle() throws XDocletException {
        XView view = getCurrentView();
        return view.getTitle();
    }
    
    /**
     *
     * Generate form field model name for the current view.
     *
     */
    public String viewFormFieldModel() throws XDocletException {
        return getCurrentViewMethodValue("dianaframework:form-field", "model");
    }
    
    /**
     *
     * Generate form field ignore state attribute for the current view.
     *
     */
    public String viewFormFieldIgnoreState() throws XDocletException {
        return getCurrentViewMethodValue("dianaframework:form-field", "ignore-state");
    }
    
    /**
     *
     * Generate form field ignore value attribute for the current view.
     *
     */
    public String viewFormFieldIgnoreValue() throws XDocletException {
        return getCurrentViewMethodValue("dianaframework:form-field", "ignore-value");
    }
    
    /**
     *
     * Generate form field visible attribute for the current view.
     *
     */
    public String viewFormFieldVisible() throws XDocletException {
        return getCurrentViewMethodValue("dianaframework:form-field", "visible");
    }
    
    /**
     *
     * Generate form field enabled attribute for the current view.
     *
     */
    public String viewFormFieldEnabled() throws XDocletException {
        return getCurrentViewMethodValue("dianaframework:form-field", "enabled");
    }
    
    /**
     *
     * Generate form field model name for the current view.
     *
     */
    public String viewFormFieldType() throws XDocletException {
        return getCurrentViewMethodValue("dianaframework:form-field", "type");
    }
    
    /**
     *
     * Generate action name for the current view.
     *
     */
    public String viewActionName() throws XDocletException {
        return getCurrentViewMethodValue("dianaframework:action", "name");
    }
    
    /**
     *
     * Generate action name for the current view.
     *
     */
    public String viewTableDoubleClickAction() throws XDocletException {
        return getCurrentViewMethodValue("dianaframework:table", "double-click-action");
    }
    
    /**
     *
     * Generate action name for the current view.
     *
     */
    public String viewTableClickAction() throws XDocletException {
        return getCurrentViewMethodValue("dianaframework:table", "click-action");
    }
    
    /**
     *
     * Generate action name for the current view.
     *
     */
    public String viewTableModel() throws XDocletException {
        return getCurrentViewMethodValue("dianaframework:table", "model");
    }

    /**
     *
     * Generate focus-gained-action name on form fields for the current view.
     *
     */
    public String viewFormFieldFocusGainedAction() throws XDocletException {
        return getCurrentViewMethodValue("dianaframework:form-field", "focus-gained-action");
    }

    /**
     *
     * Generate change-action name on form fields for the current view.
     *
     */
    public String viewFormFieldChangeAction() throws XDocletException {
        return getCurrentViewMethodValue("dianaframework:form-field", "change-action");
    }

    /**
     *
     * Generate selection-action name on form fields for the current view.
     *
     */
    public String viewFormFieldSelectionAction() throws XDocletException {
        return getCurrentViewMethodValue("dianaframework:form-field", "selection-action");
    }

    /**
     *
     * Generate model-params name on form fields for the current view.
     *
     */
    public String viewFormFieldModelParams() throws XDocletException {
        return getCurrentViewMethodValue("dianaframework:form-field", "model-params");
    }

    /**
     *
     * Generate action-params name on form fields for the current view.
     *
     */
    public String viewFormFieldActionParams() throws XDocletException {
        return getCurrentViewMethodValue("dianaframework:form-field", "action-params");
    }

    /**
     *
     * Generate model-params name on tables for the current view.
     *
     */
    public String viewTableModelParams() throws XDocletException {
        return getCurrentViewMethodValue("dianaframework:table", "model-params");
    }

    /**
     *
     * Generate action-params name on tables for the current view.
     *
     */
    public String viewTableActionParams() throws XDocletException {
        return getCurrentViewMethodValue("dianaframework:table", "action-params");
    }

    /**
     *
     * Generate params name on actions for the current view.
     *
     */
    public String viewActionParams() throws XDocletException {
        return getCurrentViewMethodValue("dianaframework:action", "params");
    }

    /**
     *
     * Generate reload attribute on form fields for the current view.
     *
     */
    public String viewFormFieldReload() throws XDocletException {
        return getCurrentViewMethodValue("dianaframework:form-field", "reload");
    }

    /**
     *
     * Generate form field name for the current view.
     *
     */
    public String viewFormFieldName() {
        XMethod method = getCurrentMethod();
        String name = method.getName();
        // Ever get before the field name. Ex: public Type getFormFieldName();
        return name.substring(3);
    }
    
    private String getCurrentViewMethodValue(String tagName, String attributeName) {
        XMethod method = getCurrentMethod();
        if (method.getDoc().getTag(tagName).getAttributeValue(attributeName) == null) return "";
        String[] values = tokensToArray(method.getDoc().getTag(tagName).getAttributeValue(attributeName), ",");
        XView view = getCurrentView();
        return values[view.getIndex() > values.length - 1 ? values.length - 1 : view.getIndex()];
    }
    
    public XView getCurrentView() {
        return currentView;
    }

    public void setCurrentView(XView currentView) {
        this.currentView = currentView;
    }

    private String[] tokensToArray(String tokens, String tokenizer) {
        if (tokens == null)
            return new String[] {""};
        StringTokenizer stk = new StringTokenizer(tokens, tokenizer);
        String[] array = new String[stk.countTokens()];
        int inc = 0;
        while (stk.hasMoreTokens()) {
            array[inc++] = stk.nextToken();
        }
        return array;
    }
    
}
