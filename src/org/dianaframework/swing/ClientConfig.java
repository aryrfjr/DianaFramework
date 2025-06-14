package org.dianaframework.swing;

import com.opensymphony.xwork.ActionInvocation;
import com.opensymphony.xwork.ActionProxy;
import com.opensymphony.xwork.ActionProxyFactory;
import com.opensymphony.xwork.ObjectFactory;
import com.opensymphony.xwork.config.Configuration;
import com.opensymphony.xwork.config.ConfigurationManager;
import com.opensymphony.xwork.spring.SpringObjectFactory;
import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.StringTokenizer;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.table.AbstractTableModel;
import org.dianaframework.view.CollectionModel;
import org.dianaframework.view.FormField;
import org.dianaframework.swing.view.Table;
import org.dianaframework.swing.view.SwingView;
import org.dianaframework.action.ActionReference;
import org.dianaframework.swing.view.ListModelReference;
import org.dianaframework.view.BusinessObjectsCollection;
import org.dianaframework.view.JTableColumnModel;
import org.dianaframework.xml.XMLDOMParser;
import org.dianaframework.xml.Tag;
import org.dianaframework.xml.TagsCollection;
import org.dianaframework.xml.XMLDOMParserException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 *
 * Parser XML DOM para o arquivo client-config.xml.
 *
 * @version $Id: ClientConfig.java 2091 2007-03-02 15:02:16Z aryjunior $
 * @author <a href="mailto:aryjunior@gmail.com">Ary Junior</a>
 *
 */
public class ClientConfig {
    private MainWindow mainWindow;
    private static ClientConfig singleton;
    private HashMap views = new HashMap();
    private HashMap listModels = new HashMap();
    private HashMap tableModels = new HashMap();
    private BeanFactory bf;
    private JDesktopPane MDIPanel;
    private String mainWindowType;
    private HashMap<String, ActionReference> allActions = new HashMap();
    
    static {
        // Initializing the SpringObjectFactory as the default Object Factory for xwork actions
        ApplicationContext appContext = new ClassPathXmlApplicationContext("ApplicationContext.xml");
        SpringObjectFactory springObjectFactory = new SpringObjectFactory();
        springObjectFactory.setApplicationContext(appContext);
        ObjectFactory.setObjectFactory(springObjectFactory);
    }
    
    public static ClientConfig instance() {
        if (singleton == null) {
            singleton = new ClientConfig();
        }
        return singleton;
    }
    
    public void loadSpringIoC() {
        try {
            // The Spring bean factory
            Resource resource = new ClassPathResource("ApplicationContext.xml");
            bf = new XmlBeanFactory(resource);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     *
     * Return the main window.
     *
     */
    public MainWindow buildMainWindow() throws ClientConfigException {
        XMLDOMParser xmlParser = null;
        try {
            xmlParser = new XMLDOMParser();
            InputStream configXml = Thread.currentThread().getContextClassLoader().getResourceAsStream("config/main-window.xml");
            xmlParser.parse(configXml);
        } catch(XMLDOMParserException pe) {
            throw new ClientConfigException("XMLDOMParserException on parse the main-window.xml file.", pe);
        } catch(IOException ioe) {
            throw new ClientConfigException("IOException on parse the main-window.xml file.", ioe);
        }
        mainWindow = new MainWindow();
        Tag windowTag = xmlParser.getRoot();
        mainWindow.setTitle(windowTag.getAttribute("title").getValue());
        // @todo Nao fica maximizada!!!
        mainWindow.setExtendedState(JFrame.MAXIMIZED_BOTH);
        mainWindow.setSize(1024,768);
        mainWindow.setJMenuBar(buildMenuBar(windowTag.getChild("menu-bar").getChildren("menu")));
        mainWindowType = windowTag.getAttribute("type").getValue();
        if (mainWindowType.equals("MDI")) {
            MDIPanel = new JDesktopPane();
            MDIPanel.setBackground(new Color(190,190,190));
            mainWindow.getContentPane().add(MDIPanel);
        } else if (mainWindowType.equals("MODAL")) {
            // @todo Nada a fazer aqui?
        }
        return mainWindow;
    }
    
    private JMenuBar buildMenuBar(TagsCollection menus) {
        JMenuBar barraMenu = new JMenuBar();
        for (int inc = 0; inc < menus.countTags(); inc++)
            barraMenu.add(buildMenu(menus.getTag(inc)));
        return barraMenu;
    }
    
    private JMenu buildMenu(Tag tmenu) {
        // @todo Seguranca e niveis de acesso ao sistema aqui!!!
        JMenu jmenu = new JMenu(tmenu.getAttribute("label").getValue());
        JMenuItem jitem;
        if (tmenu.getAttribute("atalho") != null && !tmenu.getAttribute("atalho").getValue().equals(""))
            jmenu.setMnemonic(tmenu.getAttribute("atalho").getValue().toCharArray()[0]);
        TagsCollection submenus = tmenu.getChildren();
        Tag submenu;
        for (int inc = 0; inc < submenus.countTags(); inc++) {
            submenu = submenus.getTag(inc);
            if (submenu.getName().equals("item-menu")) {
                if (submenu.getAttribute("label").getValue().equals("-") )
                    jmenu.add(new JSeparator());
                else {
                    jitem = new JMenuItem(submenu.getAttribute("label").getValue());
                    jitem.setActionCommand(submenu.getAttribute("action").getValue());
                    jitem.addActionListener(mainWindow);
                    jmenu.add(jitem);
                }
            } else {
                jmenu.add(buildMenu(submenu));
            }
        }
        return jmenu;
    }
    
    public void loadConfig() throws ClientConfigException {
        XMLDOMParser xmlParser = null;
        try {
            xmlParser = new XMLDOMParser();
            InputStream configXml = Thread.currentThread().getContextClassLoader().getResourceAsStream("config/client-config.xml");
            xmlParser.parse(configXml);
        } catch(XMLDOMParserException pe) {
            throw new ClientConfigException("XMLDOMParserException on parse the client-config.xml file.", pe);
        } catch(IOException ioe) {
            throw new ClientConfigException("IOException on parse the client-config.xml file.", ioe);
        }
        try {
            if (xmlParser.getRoot().getChild("table-models") != null)
                loadTableModels(xmlParser.getRoot().getChild("table-models").getChildren("table-model"));
            if (xmlParser.getRoot().getChild("list-models") != null)
                loadListModels(xmlParser.getRoot().getChild("list-models").getChildren("list-model"));
            if (xmlParser.getRoot().getChild("views") != null)
                loadViews(xmlParser.getRoot().getChild("views").getChildren("view"));
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    private void loadTableModels(TagsCollection tmCollection) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Class tmClass;
        Tag tmTag;
        AbstractTableModel atm;
        for (int inc = 0; inc < tmCollection.countTags(); inc++) {
            tmTag = tmCollection.getTag(inc);
            tmClass = Class.forName(tmTag.getAttribute("class").getValue());
            atm = (AbstractTableModel)Class.forName(tmClass.getPackage().getName()+"."+tmTag.getAttribute("name").getValue()+"TableModel").newInstance();
            try {
                ((CollectionModel)atm).setBusinessObjectsCollection((BusinessObjectsCollection)bf.getBean(tmTag.getAttribute("name").getValue()));
            } catch(Exception e) {
                ((CollectionModel)atm).setBusinessObjectsCollection((BusinessObjectsCollection)Class.forName(tmTag.getAttribute("class").getValue()).newInstance());
            }
            // Loading the column widths
            if (tmTag.getAttribute("column-widths") != null) {
                StringTokenizer stk = new StringTokenizer(tmTag.getAttribute("column-widths").getValue(), ",");
                int[] widths = new int[stk.countTokens()];
                for (int i = 0; stk.hasMoreTokens(); i++) {
                    widths[i] = Integer.parseInt(stk.nextToken());
                }
                ((JTableColumnModel)atm).setColumnWidths(widths);
            }
            
            // Selecao Multipla
            if (tmTag.getAttribute("row-selection") != null) {
                if ( tmTag.getAttribute("row-selection").getValue().equals("multiple") ) {
                    ((JTableColumnModel)atm).setRowSelection( tmTag.getAttribute("row-selection").getValue() );
                }
            }
                    
            getTableModels().put(tmTag.getAttribute("name").getValue(), atm);
        }
    }
    
    private void loadListModels(TagsCollection lmCollection) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        ListModelReference lmr;
        Tag lmTag;
        for (int inc = 0; inc < lmCollection.countTags(); inc++) {
            lmTag = lmCollection.getTag(inc);
            lmr = new ListModelReference();
            lmr.setName(lmTag.getAttribute("name").getValue());
            lmr.setClazz(Class.forName(lmTag.getAttribute("class").getValue()));
            listModels.put(lmr.getName(), lmr);
        }
    }
    
    private void loadViews(TagsCollection viewsCollection) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        // @todo Seguranca e niveis de acesso ao sistema aqui???
        Tag viewTag, ffTag, actionTag, tableTag;
        SwingView view;
        TagsCollection formFields, actions, tables = null;
        FormField ff;
        ActionReference action = null;
        Table table;
        for (int inc = 0; inc < viewsCollection.countTags(); inc++) {
            viewTag = viewsCollection.getTag(inc);
            view = new SwingView();
            view.setName(viewTag.getAttribute("name").getValue());
            view.setTitle(viewTag.getAttribute("title").getValue());
            view.setMainWindowType(mainWindowType);
            // @todo Devo construir todas as janelas antes? Melhor instancia-las somente qdo forem ser usadas!!!
            view.setWindowClass(viewTag.getAttribute("window").getValue());
            if (viewTag.getChild("form-fields") != null) {
                formFields = viewTag.getChild("form-fields").getChildren("form-field");
                for (int iff = 0; iff < formFields.countTags(); iff++) {
                    ffTag = formFields.getTag(iff);
                    ff = new FormField();
                    ff.setName(ffTag.getAttribute("name").getValue());
                    if (ffTag.getAttribute("model") != null) {
                        if (ffTag.getAttribute("model").getValue().equals("disabled")) {
                            ff.setModel(((ListModelReference)listModels.get("Empty")).createModel(bf));
                            ff.setEnabled(false);
                        } else {
                            // @todo Log4J!!!
                            if (listModels.get(ffTag.getAttribute("model").getValue()) == null)
                                System.out.println("Parse Error!!! The list model '"+ffTag.getAttribute("model").getValue()+"' does not exists in 'client-config.xml' file.");
                            ff.setModel(((ListModelReference)listModels.get(ffTag.getAttribute("model").getValue())).createModel(bf));
                        }
                    }
                    if (ffTag.getAttribute("ignore-state") != null) {
                        ff.setIgnoreState(ffTag.getAttribute("ignore-state") != null && ffTag.getAttribute("ignore-state").getValue().equalsIgnoreCase("true"));
                    }
                    if (ffTag.getAttribute("ignore-value") != null) {
                        ff.setIgnoreValue(ffTag.getAttribute("ignore-value") != null && ffTag.getAttribute("ignore-value").getValue().equalsIgnoreCase("true"));
                    }
                    if (ffTag.getAttribute("enabled") != null) {
                        ff.setStrEnabled(ffTag.getAttribute("enabled").getValue());
                    }
                    if (ffTag.getAttribute("visible") != null) {
                        ff.setStrVisible(ffTag.getAttribute("visible").getValue());
                    }
                    if (ffTag.getAttribute("reload") != null) {
                        ff.setReload(ffTag.getAttribute("reload").getValue());
                    }
                    if (ffTag.getAttribute("type") != null) {
                        ff.setType(ffTag.getAttribute("type").getValue());
                    }
                    if (ffTag.getAttribute("keypress-action") != null) {
                        action = new ActionReference();
                        action.setName(ffTag.getAttribute("keypress-action").getValue());
                        action.setSourceAccessor(ff.getName());
                        ff.setKeyPressAction(action);
                    }
                    if (ffTag.getAttribute("focus-gained-action") != null) {
                        action = new ActionReference();
                        action.setName(ffTag.getAttribute("focus-gained-action").getValue());
                        action.setSourceAccessor(ff.getName());
                        ff.setFocusGainedAction(action);
                    }
                    if (ffTag.getAttribute("selection-action") != null) {
                        action = new ActionReference();
                        action.setName(ffTag.getAttribute("selection-action").getValue());
                        action.setSourceAccessor(ff.getName());
                        ff.setSelectionAction(action);
                    }
                    if (ffTag.getAttribute("change-action") != null) {
                        action = new ActionReference();
                        action.setName(ffTag.getAttribute("change-action").getValue());
                        action.setSourceAccessor(ff.getName());
                        ff.setChangeAction(action);
                    }
                    if (ffTag.getAttribute("action-params") != null && action != null) {
                        StringTokenizer params = new StringTokenizer(ffTag.getAttribute("action-params").getValue(), "+");
                        String param;
                        while (params.hasMoreTokens()) {
                            param = params.nextToken();
                            action.addParam(param.substring(0, param.indexOf("=")), param.substring(param.indexOf("=") + 1));
                        }
                    }
                    if (ffTag.getAttribute("model-params") != null && ff.getModel() != null) {
                        StringTokenizer params = new StringTokenizer(ffTag.getAttribute("model-params").getValue(), "+");
                        String param;
                        while (params.hasMoreTokens()) {
                            param = params.nextToken();
                            ff.getModel().addParameter(param.substring(0, param.indexOf("=")), param.substring(param.indexOf("=") + 1));
                        }
                    }
                    if (action != null)
                        allActions.put(view.getName()+"."+action.getSourceAccessor(), action);
                    view.addFormField(ff);
                    action = null;
                }
            }
            if (viewTag.getChild("actions") != null) {
                actions = viewTag.getChild("actions").getChildren("action");
                for (int ia = 0; ia < actions.countTags(); ia++) {
                    actionTag = actions.getTag(ia);
                    action = new ActionReference();
                    action.setName(actionTag.getAttribute("name").getValue());
                    action.setSourceAccessor(actionTag.getAttribute("source-accessor").getValue());
                    view.addAction(action);
                    if (actionTag.getAttribute("params") != null && action != null) {
                        StringTokenizer params = new StringTokenizer(actionTag.getAttribute("params").getValue(), "+");
                        String param;
                        while (params.hasMoreTokens()) {
                            param = params.nextToken();
                            action.addParam(param.substring(0, param.indexOf("=")), param.substring(param.indexOf("=") + 1));
                        }
                    }
                    if (action != null)
                        allActions.put(view.getName()+"."+action.getSourceAccessor(), action);
                }
            }
            if (viewTag.getChild("tables") != null) {
                tables = viewTag.getChild("tables").getChildren("table");
                for (int it = 0; it < tables.countTags(); it++) {
                    tableTag = tables.getTag(it);
                    table = new Table();
                    table.setName(tableTag.getAttribute("name").getValue());
                    table.setModel((AbstractTableModel)getTableModels().get(tableTag.getAttribute("model").getValue()));
                    if (tableTag.getAttribute("click-action") != null) {
                        action = new ActionReference();
                        action.setName(tableTag.getAttribute("click-action").getValue());
                        action.setSourceAccessor(table.getName());
                        table.setClickAction(action);
                    }
                    if (tableTag.getAttribute("double-click-action") != null) {
                        action = new ActionReference();
                        action.setName(tableTag.getAttribute("double-click-action").getValue());
                        action.setSourceAccessor(table.getName());
                        table.setDoubleClickAction(action);
                    }
                    if (tableTag.getAttribute("action-params") != null && action != null) {
                        StringTokenizer params = new StringTokenizer(tableTag.getAttribute("action-params").getValue(), "+");
                        String param;
                        while (params.hasMoreTokens()) {
                            param = params.nextToken();
                            action.addParam(param.substring(0, param.indexOf("=")), param.substring(param.indexOf("=") + 1));
                        }
                    }
                    if (tableTag.getAttribute("model-params") != null && table.getModel() != null) {
                        StringTokenizer params = new StringTokenizer(tableTag.getAttribute("model-params").getValue(), "+");
                        String param;
                        while (params.hasMoreTokens()) {
                            param = params.nextToken();
                            ((CollectionModel)table.getModel()).addParameter(param.substring(0, param.indexOf("=")), param.substring(param.indexOf("=") + 1));
                        }
                    }
                    if (action != null)
                        allActions.put(view.getName()+"."+action.getSourceAccessor(), action);
                    view.addTable(table);
                }
            }
            views.put(view.getName(), view);
        }
    }
    
    public SwingView getView(String name) {
        return (SwingView)views.get(name);
    }
    
    public boolean checkView(String name) {
        return views.get(name) != null;
    }
    
    public ActionProxy createXWorkActionProxy(String name, HashMap actionConfig) throws Exception {
        Configuration conf = ConfigurationManager.getConfiguration();
        ActionProxyFactory apf = ActionProxyFactory.getFactory();
        ActionProxy ap = apf.createActionProxy("", name, actionConfig);
        return ap;
    }
    
    public ActionInvocation invokeXWorkAction(String name, HashMap actionConfig) throws Exception {
        ActionProxy ap = createXWorkActionProxy(name, actionConfig);
        ActionInvocation ai = ap.getInvocation();
        return ai;
    }
    
    public Object getSpringBean(String name) {
        if (bf.containsBean(name))
            return bf.getBean(name);
        else
            return null;
    }

    public HashMap getTableModels() {
        return tableModels;
    }
    
    public ActionReference getActionReference(String name) {
        return allActions.get(name);
    }
    
}
