package org.dianaframework.swing.view;

import com.opensymphony.xwork.ActionInvocation;
import com.opensymphony.xwork.ValidationAware;
import java.awt.Color;
import java.awt.Container;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.swing.AbstractButton;
import javax.swing.AbstractListModel;
import javax.swing.ButtonGroup;
import javax.swing.ComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.text.JTextComponent;
import net.sf.nachocalendar.components.DateField;
import org.dianaframework.action.Action;
import org.dianaframework.servicelocator.BusinessServiceFactory;
import org.dianaframework.swing.ClientConfig;
import org.dianaframework.view.BasicView;
import org.dianaframework.action.ActionReference;
import org.dianaframework.view.FormField;
import org.dianaframework.view.CollectionModel;
import org.dianaframework.view.JTableColumnModel;
import org.springframework.transaction.PlatformTransactionManager;

/**
 *
 * Classe que representa uma view ( um Container ), alem de ser um listener de todas as acoes desta view.
 *
 * @version $Id: SwingView.java 2239 2007-03-16 21:34:52Z aryjunior $
 * @author <a href="mailto:aryjunior@gmail.com">Ary Junior</a>
 *
 */
public class SwingView extends BasicView implements ActionListener, MouseListener, MouseMotionListener, MouseWheelListener, KeyListener, ListSelectionListener, FocusListener, ChangeListener {
    private Container windowRef;
    private String windowClass;
    private String mainWindowType;
    private HashMap attributes = new HashMap();
    private boolean lockFields = false;
    public static int FROM_SHOWVIEW_ACTION = 0;
    public static int FROM_ACTION_RESULT = 1;

    public Container getWindowReference() {
        return windowRef;
    }
    
    private JInternalFrame getJInternalFrame() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        JInternalFrame jif = (JInternalFrame)Class.forName(windowClass).newInstance();
        jif.setTitle(getTitle());
        return jif;
    }
    
    private JDialog getJDialog(Container owner) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Class cls = Class.forName(windowClass);
        Class partypes[] = new Class[2];
        partypes[0] = Frame.class;
        partypes[1] = Boolean.TYPE;
        Constructor ct = cls.getConstructor(partypes);
        Object arglist[] = new Object[2];
        arglist[0] = (Frame)owner;
        arglist[1] = true;
        JDialog dialogo = (JDialog)ct.newInstance(arglist);
        dialogo.setTitle(getTitle());
        loadDefinicoes(dialogo);
        return dialogo;
    }
    
    // @todo Alberto: Defini este metodo com a finalidade de executar os metodos da classe Pai do Formulario. Estes metodos sao utilizados para setar a parte visual do formulario  -->
    public void loadDefinicoes(JDialog dialogo) {
        Method[] methods  = dialogo.getClass().getMethods();
        int      controle = 0;
        for (int i = 0; i < methods.length; i++) {
            if (methods[i].getName().equalsIgnoreCase("setCentralizado")) {
                try {
                    methods[i].invoke(dialogo, new Object[] {});
                    controle++;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (methods[i].getName().equalsIgnoreCase("setVisual")) {
                try {
                    methods[i].invoke(dialogo, new Object[] {});
                    controle++;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            
            if (controle >= 2) {
                break;
            }
        }
    }
    
    public void setWindowClass(String windowClass) {
        this.windowClass = windowClass;
    }
    
    private void loadListeners() {
        ActionReference action;
        FormField ff;
        Table table;
        try {
            Iterator<FormField> itff = getFormFields().values().iterator();
            Method[] methods = windowRef.getClass().getMethods();
            while (itff.hasNext()) {
                ff = itff.next();
                for (int i = 0; i < methods.length; i++) {
                    if (methods[i].getName().equalsIgnoreCase("get"+ff.getName())) {
                        setListener(ff.getKeyPressAction(), methods[i].invoke(windowRef, new Object[] {}));
                        setListener(ff.getSelectionAction(), methods[i].invoke(windowRef, new Object[] {}));
                        setListener(ff.getFocusGainedAction(), methods[i].invoke(windowRef, new Object[] {}));
                        setListener(ff.getChangeAction(), methods[i].invoke(windowRef, new Object[] {}));
                    }
                }
            }
            Iterator<ActionReference> itactions = getActions().values().iterator();
            methods = windowRef.getClass().getMethods();
            while (itactions.hasNext()) {
                action = itactions.next();
                for (int i = 0; i < methods.length; i++) {
                    if (methods[i].getName().equalsIgnoreCase(action.getSourceAccessor())) {
                        setListener(action, methods[i].invoke(windowRef, new Object[] {}));
                    }
                }
            }
            Iterator<Table> ittables = getTables().values().iterator();
            methods = windowRef.getClass().getMethods();
            JTable jtable;
            while (ittables.hasNext()) {
                table = ittables.next();
                for (int i = 0; i < methods.length; i++) {
                    if (methods[i].getName().equalsIgnoreCase("get"+table.getName())) {
                        jtable = (JTable)methods[i].invoke(windowRef, new Object[] {});
                        setListener(table.getClickAction(), jtable);
                        // All tables have a listenner for sort rows by click on header
                        jtable.getTableHeader().addMouseListener(this);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void setListener(ActionReference action, Object actionRef) {
        // All tables and jlists have mouse listeners
        if (actionRef instanceof JTable || actionRef instanceof JList) {
            ((JComponent)actionRef).addMouseListener(this);
        }
        if (action == null)
            return;
        if (actionRef instanceof JButton) {
            ((JButton)actionRef).addActionListener(this);
            ((JButton)actionRef).setActionCommand(action.getSourceAccessor());
        } else if (actionRef instanceof ButtonGroup) {
            Enumeration<AbstractButton> btns = ((ButtonGroup)actionRef).getElements();
            AbstractButton btn;
            // @todo 30012007: Eventos nos buttons ou nos buttonggroup... o valor agora fica na propriedade name e nao na actionCommand mais...
            while (btns.hasMoreElements()) {
                btn = btns.nextElement();
                btn.setActionCommand(action.getSourceAccessor());
                btn.addActionListener(this);
            }
        } else if (actionRef instanceof GenericFormField) {
            ((GenericFormField)actionRef).addActionListener(this);
            ((GenericFormField)actionRef).setActionCommand(action.getSourceAccessor());
        } else if (actionRef instanceof JComboBox) {
            ((JComboBox)actionRef).addActionListener(this);
            ((JComboBox)actionRef).setActionCommand(action.getSourceAccessor());
        } else if (actionRef instanceof JCheckBox) {
            ((JCheckBox)actionRef).addActionListener(this);
            ((JCheckBox)actionRef).setActionCommand(action.getSourceAccessor());
        } else if (actionRef instanceof JList) {
            ((JList)actionRef).addListSelectionListener(this);
        } else if (actionRef instanceof JTextField) {
            // @todo Refactorar esta classe urgente!!!
            //((JTextField)actionRef).addKeyListener(this);
            ((JComponent)actionRef).addFocusListener(this);
        } else if (actionRef instanceof JTable) {
            ((JTable)actionRef).addKeyListener(this);
        } else if (actionRef instanceof JTabbedPane) {
            ((JTabbedPane)actionRef).addChangeListener(this);
        }
    }
    
    public void loadWindow(Container owner) {
        if (windowRef == null) {
            try {
                if (mainWindowType.equals("MDI")) {
                    windowRef = getJInternalFrame();
                } else if (mainWindowType.equals("MODAL")) {
                    windowRef = getJDialog(owner);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // @todo 13022007: Tentando preencher alguns combos de uma visao atraves de um action e definir o valor default de todos os combos
    public void show(Container owner, int type, boolean clean) {
        loadWindow(owner);
        show(type, clean);
    }

    public void show(int type, boolean clean) {
        loadListeners();
        // @todo 14022007: enableControl nao funciona com result SHOW_VIEW
        //if (type == SwingView.FROM_ACTION_RESULT && !clean)
        checkStateSwingFormFields();
        if (!clean)
            updateSwingFormFields(); // setSelectedObject ==> ff.getValue()
        updateSwingActions();
        if (clean || type == SwingView.FROM_SHOWVIEW_ACTION)
            clearFormFields();
        reloadModel(type == SwingView.FROM_SHOWVIEW_ACTION && clean);
        windowRef.setVisible(true);
    }

    private void checkStateSwingFormFields() {
        FormField ff;
        try {
            Method[] methods = windowRef.getClass().getMethods();
            Iterator<FormField> cff = getFormFields().values().iterator();
            while (cff.hasNext()) {
                ff = cff.next();
                for (int i = 0; i < methods.length; i++) {
                    if (methods[i].getName().equalsIgnoreCase("get"+ff.getName())) {
                        checkStateSwingFormField(ff, methods[i].invoke(windowRef, new Object[] {}));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkStateSwingFormField(FormField ff, Object ffRef) {
        if (!ff.isIgnoreState() && ffRef instanceof JComponent) {
            ((JComponent)ffRef).setEnabled(ff.getStrEnabled().equalsIgnoreCase("true"));
            ((JComponent)ffRef).setVisible(ff.getStrVisible().equalsIgnoreCase("true"));
        }
    }

    public void close() {
        windowRef.setVisible(false);
        clearFormFields();
        windowRef = null;
    }

    // @todo 03022007: Problemas com combos
    /*public void reload(boolean clearView) {
        updateSwingFormFields();
        // @todo 03022007: Problemas com combos
        if (show) {
            updateSwingActions();
        }
        reloadModel();
    }
    
    public void reload() {
        reloadModel();
    }*/
    
    /**
     * 
     * Nao busca no model estado atual da view
     *
     */
    public void update() {
        updateSwingFormFields();
        updateTables();
    }
    
    /**
     * 
     * Busca no model estado atual da view
     *
     */
    public void reloadModel(boolean reloadAll) {
        reloadTables(reloadAll);
        updateFormFieldsModels(reloadAll);
        try {
            updateFormFields();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateTables() {
        Table table;
        JTable jt;
        try {
            Method[] methods = windowRef.getClass().getMethods();
            Iterator<Table> ct = getTables().values().iterator();
            while (ct.hasNext()) {
                table = ct.next();
                for (int i = 0; i < methods.length; i++) {
                    if (methods[i].getName().equalsIgnoreCase("get"+table.getName())) {
                        jt = (JTable)methods[i].invoke(windowRef, new Object[] {});
                        ((JTable)jt).setModel((AbstractTableModel)table.getModel());
                        jt.updateUI();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void reloadTables(boolean reload) {
        Table table;
        try {
            Method[] methods = windowRef.getClass().getMethods();
            Iterator<Table> ct = getTables().values().iterator();
            while (ct.hasNext()) {
                table = ct.next();
                for (int i = 0; i < methods.length; i++) {
                    if (methods[i].getName().equalsIgnoreCase("get"+table.getName())) {
                        reloadTable(table, methods[i].invoke(windowRef, new Object[] {}), reload);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void reloadTable(Table table, Object tableRef, boolean reload) {
        if (reload) {
            ((CollectionModel)table.getModel()).reload();
            ((CollectionModel)table.getModel()).setUIReference(tableRef);
        }
        ((JTable)tableRef).setModel((AbstractTableModel)table.getModel());
        ((JTable)tableRef).setName(table.getName());
        ((JTable)tableRef).updateUI();
        loadSwingTableConfig(table, (JTable)tableRef);
    }

    public static void loadSwingTableConfig(Table table, JTable jtable) {
        // The column widths
        int[]  widths       = ((JTableColumnModel)table.getModel()).getColumnWidths();
        String rowSelection = ((JTableColumnModel)table.getModel()).getRowSelection();
        if ( widths != null ) {
            if (widths.length > 0)
                jtable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            for (int inc = 0; inc < widths.length; inc++) {
                jtable.getColumnModel().getColumn(inc).setPreferredWidth(widths[inc]);
            }
        }
        if ( rowSelection != null ) {
            if ( rowSelection.equals("multiple") ) {
                jtable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            }
        }
        // Locking the columns resize by the user
        // Solicitacao #67 (defeito) - Desabilita redimensionamento de JTable
        jtable.getTableHeader().setResizingAllowed(false);
        // Locking drag columns in jtable
        jtable.getTableHeader().setReorderingAllowed(false);
    }
    
    private void updateFormFields() throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException {
        // Updating the form fields values from swing objects
        Method[] methods = windowRef.getClass().getMethods();
        FormField ff;
        Object sff;
        Iterator<FormField> cff = getFormFields().values().iterator();
        while (cff.hasNext()) {
            ff = cff.next();
            for (int i = 0; i < methods.length; i++) {
                if (methods[i].getName().equalsIgnoreCase("get"+ff.getName())) {
                    sff = methods[i].invoke(windowRef, new Object[] {});
                    if (sff instanceof JTextComponent) {
                        // @todo Campos com Datas!!!
                        if (ff.getType().equals(FormField.DATE)) {
                            ff.setValue(convertDate(((JTextComponent)sff).getText()));
                        } else {
                            ff.setValue(((JTextComponent)sff).getText());
                        }
                    } else if (sff instanceof GenericFormField) {
                        ff.setValue(((GenericFormField)sff).getValue());
                    } else if (sff instanceof DateField) {
                        ff.setValue(((DateField)sff).getFormattedTextField().getValue());
                    } else if (sff instanceof ButtonGroup && ((ButtonGroup)sff).getSelection() != null) {
                        // @todo 30012007: Eventos nos buttons ou nos buttonggroup... o valor agora fica na propriedade name e nao na actionCommand mais...
                        //ff.setValue(((ButtonGroup)sff).getSelection().getActionCommand());
                        Enumeration<AbstractButton> enu = ((ButtonGroup)sff).getElements();
                        AbstractButton btn;
                        while (enu.hasMoreElements()) {
                            btn = enu.nextElement();
                            if (btn.isSelected())
                                ff.setValue(btn.getName());
                        }
                    } else if (sff instanceof JCheckBox) {
                        ff.setValue(((AbstractButton)sff).isSelected() ? "selected" : "deselected");
                    } else if (sff instanceof JTabbedPane) {
                        ff.setValue(String.valueOf(((JTabbedPane)sff).getSelectedIndex()));
                    } else if (sff instanceof JComboBox) {
                        ff.setValue(((CollectionModel)((JComboBox)sff).getModel()).getSelectedValue());
                    } else if (sff instanceof JLabel) {
                        if (ff.getType().equals(FormField.IMAGE)) {
                            //Image img = ((ImageIcon)((JLabel)sff).getIcon()).getImage();
                            //ff.setValue(img);
                        } else {
                            ff.setValue(((JLabel)sff).getText());
                        }
                    }
                }
            }
        }
    }
    
    private void updateSwingFormFields() {
        // Updating the swing form objects from diana form fields
        FormField ff;
        try {
            Method[] methods = windowRef.getClass().getMethods();
            Iterator<FormField> cff = getFormFields().values().iterator();
            while (cff.hasNext()) {
                ff = cff.next();
                for (int i = 0; i < methods.length; i++) {
                    if (methods[i].getName().equalsIgnoreCase("get"+ff.getName()) && !ff.isIgnoreValue()) {
                        updateSwingFormField(ff, methods[i].invoke(windowRef, new Object[] {}));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateSwingActions() {
        if (windowRef == null) return;
        ActionReference action;
        try {
            Iterator<ActionReference> itactions = getActions().values().iterator();
            Method[] methods = windowRef.getClass().getMethods();
            while (itactions.hasNext()) {
                action = itactions.next();
                for (int i = 0; i < methods.length; i++) {
                    if (methods[i].getName().equalsIgnoreCase("get"+action.getName()) || methods[i].getName().equalsIgnoreCase(action.getSourceAccessor())) {
                        updateAction(action, methods[i].invoke(windowRef, new Object[] {}));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateFormFieldsModels(boolean reloadAll) {
        FormField ff;
        try {
            Method[] methods = windowRef.getClass().getMethods();
            Iterator<FormField> cff = getFormFields().values().iterator();
            while (cff.hasNext()) {
                ff = cff.next();
                for (int i = 0; i < methods.length; i++) {
                    if (methods[i].getName().equalsIgnoreCase("get"+ff.getName())) {
                        updateFormFieldModel(ff, methods[i].invoke(windowRef, new Object[] {}), reloadAll || ff.isReload());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void clearFormFields() {
        FormField ff;
        try {
            Method[] methods = windowRef.getClass().getMethods();
            Iterator<FormField> cff = getFormFields().values().iterator();
            while (cff.hasNext()) {
                ff = cff.next();
                for (int i = 0; i < methods.length; i++) {
                    if (methods[i].getName().equalsIgnoreCase("get"+ff.getName())) {
                        clearFormField(ff, methods[i].invoke(windowRef, new Object[] {}));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        updateSwingFormFields();
    }
    
    private void clearFormField(FormField ff, Object ffRef) {
        // @todo Limpar outros tipos de campos tbm!!!
        if (ffRef instanceof ButtonGroup) {
            // @todo 01032007: Solicitacao #130 (defeito) - (RMT) Checkbox selecionado
            AbstractButton btn = ((ButtonGroup)ffRef).getElements().nextElement();
            ff.setValue(btn.getName());
        } else if (ffRef instanceof JTextComponent) {
            if (ff.getType().equals(FormField.DATE)) {
                // @todo Campos com data!!!
                // @todo 19012007: Campos com data!!!
                ff.setValue("__/__/____");
            } else {
                ff.setValue("");
            }
        } else if (ffRef instanceof DateField) {
            ff.setValue(new Date());
        } else if (ffRef instanceof JCheckBox) {
            ff.setValue("deselected");
        } else if (ffRef instanceof JList || ffRef instanceof JComboBox) {
            ff.setValue("");
        } else if (ffRef instanceof GenericFormField) {
            ff.setValue(null);
        } else if (ffRef instanceof JLabel) {
            if (ff.getType().equals(FormField.IMAGE)) {
                //ff.setValue(null);
            } else {
                ff.setValue("");
            }
        } 
    }
    
    private void updateSwingFormField(FormField ff, Object ffRef) {
        // If the form-field is enabled
        // @todo 26022007: Solicitacao #114 (defeito)
//        if (ffRef instanceof JComponent && !ff.isIgnoreState()) {
//            ((JComponent)ffRef).setEnabled(ff.isEnabled());
//            ((JComponent)ffRef).setVisible(ff.isVisible());
//        }
        // Check if the lock fields is true
        if (lockFields && (ffRef instanceof JTextComponent || ffRef instanceof JComboBox)) {
            ((JComponent)ffRef).setEnabled(false);
            ((JComponent)ffRef).setBackground(new Color(238,238,237));
        }
        if (lockFields && ffRef instanceof ButtonGroup) {
            Enumeration<AbstractButton> btns = ((ButtonGroup)ffRef).getElements();
            AbstractButton btn;
            while (btns.hasMoreElements()) {
                btn = btns.nextElement();
                btn.setEnabled(false);
            }
        }
        if (lockFields && ffRef instanceof JCheckBox) {
            ((JComponent)ffRef).setEnabled(false);
        }
        // Updating values
        if (ffRef instanceof JTextComponent) {
            if (ff.getType().equals(FormField.DATE)) {
                // @todo Campos com data!!!
                // @todo 19012007: Campos com data!!!
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                //TODO Ary: Exception gerada a partir desse código, comentado - Bruno
                /*
                 * java.lang.ClassCastException: javax.swing.JTextField cannot be cast to javax.swing.JFormattedTextField
                 * 
                 */
                if (ff.getValue() != null && !ff.getValue().equals("") && !ff.getValue().equals("__/__/____"))
                    ((JFormattedTextField)ffRef).setValue(df.format((Date)ff.getValue()));
                else
                    ((JFormattedTextField)ffRef).setValue("__/__/____");
            } else {
                ((JTextComponent)ffRef).setText(String.valueOf(ff.getValue() == null ? "" : ff.getValue()));
            }
        } else if (ffRef instanceof JLabel) {
            if (ff.getType().equals(FormField.IMAGE)) {
                //((JLabel)ffRef).setIcon(new ImageIcon((Image)ff.getValue()));
            } else {
                ((JLabel)ffRef).setText(ff.getValue() == null ? "" : ff.getValue().toString());
            }
        } else if (ffRef instanceof GenericFormField) {
            ((GenericFormField)ffRef).setValue(ff.getValue());
        } else if (ffRef instanceof DateField) {
            ((DateField)ffRef).getFormattedTextField().setValue((Date)ff.getValue());
        } else if (ffRef instanceof ButtonGroup) {
            Enumeration<AbstractButton> btns = ((ButtonGroup)ffRef).getElements();
            AbstractButton btn;
            while (btns.hasMoreElements()) {
                btn = btns.nextElement();
                // @todo 30012007: Eventos nos buttons ou nos buttonggroup... o valor agora fica na propriedade name e nao na actionCommand mais...
                //if (btn.getActionCommand().equals(ff.getValue())) {                
                if (btn.getName() == null) {
                    System.out.println("A propriedade name do componente Swing "+btn+" nao pode ser nula.");
                    return;
                }
                if (btn.getName().equals(ff.getValue())) {
                    ((ButtonGroup)ffRef).setSelected(btn.getModel(), true);
                }
                btn.updateUI();
            }
        } else if (ffRef instanceof JCheckBox) {
            ((JCheckBox)ffRef).setSelected(ff.getValue() == null ? false : ((String)ff.getValue()).equals("selected"));
            ((JCheckBox)ffRef).updateUI();
        } else if (ffRef instanceof JList) {
            ((JList)ffRef).updateUI();
        } else if (ffRef instanceof JComboBox) {
            // @todo 10012007: ClassCastException aqui com o CollectionModel
            if (ff.getValue() != null && ((JComboBox)ffRef).getModel() instanceof CollectionModel) {
                ((CollectionModel)((JComboBox)ffRef).getModel()).setSelectedObject(ff.getValue());
            }
            ((JComboBox)ffRef).updateUI();
        }
    }

    // @todo Alberto: Adicionado este metodo para desabilitar os botoes na opcao de Consulta
    // @todo 03022007: Resolvendo problemas com combos
    private void updateAction(ActionReference action, Object actionRef) {
        if (lockFields) {
            if (actionRef instanceof JButton) {
                if ( ((JButton)actionRef).getText().equals("Cancelar") ) {
                    ((JButton)actionRef).setText("Fechar");
                }
                if ( ((JButton)actionRef).getText().equals("Gravar") ) {
                    ((JButton)actionRef).setVisible(false);
                }
                if ( ! ((JButton)actionRef).getText().equals("Fechar") ) {
                    // @todo 19012007: rever este metodo aqui
                    //((JButton)actionRef).setEnabled(false);
                }
            }
        }
    }

    private void updateFormFieldModel(FormField ff, Object ffRef, boolean reload) {
        if (ffRef instanceof JComboBox) {
            if (ff.getModel() == null)
                System.out.println("The '"+ff.getName()+"' form field model is null.");
            ((CollectionModel)ff.getModel()).setUIReference(ffRef);
            if (reload)
                ((CollectionModel)ff.getModel()).reload();
            ((JComboBox)ffRef).setModel((ComboBoxModel)ff.getModel());
            // @todo 13022007: O fonte abaixo limpa o combo qdo exibe-se a janela
            if (ff.getValue() != null && !ff.getValue().equals("")) {
                ((CollectionModel)((JComboBox)ffRef).getModel()).setSelectedObject(ff.getValue());
            } else {
                ((CollectionModel)((JComboBox)ffRef).getModel()).setSelectedItem(0);
            }
            ((JComboBox)ffRef).updateUI();
        } else if (ffRef instanceof JList) {
            if (ff.getModel() == null)
                System.out.println("The '"+ff.getName()+"' form field model is null.");
            if (reload)
                ((CollectionModel)ff.getModel()).reload();
            ((JList)ffRef).setModel((AbstractListModel)ff.getModel());
            ((CollectionModel)ff.getModel()).setUIReference(ffRef);
            ((CollectionModel)((JList)ffRef).getModel()).setSelectedItem(0);
            ((JList)ffRef).setName(ff.getName());
            ((JList)ffRef).updateUI();
            // @todo 13022007: Comentei aqui para eliminar o problema com o mapeamento de campos JRadioButton
        /*} else if (ffRef instanceof JComponent) {
            ((JComponent)ffRef).setName(ff.getName());*/
        }
    }
    
    private Date convertDate(String sdate) {
        // @todo 22012007: Campos com datas!!!
        // Sugestao: http://fisheye5.cenqua.com/browse/javaserverfaces-sources/jsf-api/src/javax/faces/convert/DateTimeConverter.java?r=1.32
        try {
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            return df.parse(sdate);
        } catch (ParseException e) {
            return null;
        }
    }
    public void lockActions() {
        enableActions(false);
    }
    
    public void unlockActions() {
        enableActions(true);
    }
    
    private void enableActions(boolean enabled) {
        if (windowRef == null) return;
        ActionReference action;
        Table table;
        try {
            Iterator<ActionReference> itactions = getActions().values().iterator();
            Method[] methods = windowRef.getClass().getMethods();
            while (itactions.hasNext()) {
                action = itactions.next();
                for (int i = 0; i < methods.length; i++) {
                    if (methods[i].getName().equalsIgnoreCase("get"+action.getName()) || methods[i].getName().equalsIgnoreCase(action.getSourceAccessor())) {
                        enableAction(methods[i].invoke(windowRef, new Object[] {}), enabled);
                    }
                }
            }
            Iterator<Table> ittables = getTables().values().iterator();
            methods = windowRef.getClass().getMethods();
            while (ittables.hasNext()) {
                table = ittables.next();
                for (int i = 0; i < methods.length; i++) {
                    if (methods[i].getName().equalsIgnoreCase("get"+table.getName())) {
                        enableAction(methods[i].invoke(windowRef, new Object[] {}), enabled);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void enableAction(String name, boolean enabled) {
        if (windowRef == null) return;
        ActionReference action;
        try {
            Iterator<ActionReference> itactions = getActions().values().iterator();
            Method[] methods = windowRef.getClass().getMethods();
            while (itactions.hasNext()) {
                action = itactions.next();
                if (action.getName().equals(name)) {
                    for (int i = 0; i < methods.length; i++) {
                        if (methods[i].getName().equalsIgnoreCase("get"+action.getName()) || methods[i].getName().equalsIgnoreCase(action.getSourceAccessor())) {
                            enableAction(methods[i].invoke(windowRef, new Object[] {}), enabled);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showControl(String name, boolean visible) {        
        if (windowRef == null) return;
        try {
            Method[] methods = windowRef.getClass().getMethods();
            for (int i = 0; i < methods.length; i++) {                
                if (methods[i].getName().equalsIgnoreCase("get"+name)) {                    
                    if (getFormFields().get(name.toUpperCase()) != null) {                        
                        getFormFields().get(name.toUpperCase()).setVisible(visible);
                        showSwingControl(methods[i].invoke(windowRef, new Object[] {}), visible);                        
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showSwingControl(Object ref, boolean visible) {                
        // @todo (Traker 153) EVELAINE Se é passado um objeto JLabel ou JText que estendem de JComponent
        // eles nao sao marcados como invisivel
        if (ref instanceof JComponent){
            ((JComponent)ref).setVisible(visible);
        }        
    }
    
    public void enableControl(String name, boolean enabled) {
        if (windowRef == null) return;
        try {
            Method[] methods = windowRef.getClass().getMethods();
            
            for (int i = 0; i < methods.length; i++) {
                if (methods[i].getName().equalsIgnoreCase("get"+name)) {
                    // @todo 13022007: SwingView.enableControl funcionando agora com combos
                    if (getFormFields().get(name.toUpperCase()) != null) {
                        getFormFields().get(name.toUpperCase()).setEnabled(enabled);
                    /*} else if (getActions().get(name) != null) {
                        getActions().get(name).setEnabled(enabled);*/
                    }
                    enableAction(methods[i].invoke(windowRef, new Object[] {}), enabled);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void disableSelectionTable(String name) {
        if (windowRef == null) return;
        
        try {
            Method[] methods = windowRef.getClass().getMethods();
            
            for (int i = 0; i < methods.length; i++) {
                if (methods[i].getName().equalsIgnoreCase("get"+name)) {
                    disableSelectionTable(methods[i].invoke(windowRef, new Object[] {}));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void focusControl(String name) {
        if (windowRef == null) return;
        
        try {
            Method[] methods = windowRef.getClass().getMethods();
            for (int i = 0; i < methods.length; i++) {
                if (methods[i].getName().equalsIgnoreCase("get"+name)) {
                    focusControl(methods[i].invoke(windowRef, new Object[] {}));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Object getFormFieldUIReference(String name) {
        try {
            Method[] methods = windowRef.getClass().getMethods();
            for (int i = 0; i < methods.length; i++) {
                if (methods[i].getName().equalsIgnoreCase("get"+name)) {
                    return methods[i].invoke(windowRef, new Object[] {});
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public void visibleAction(String name, boolean visible) {
        if (windowRef == null) return;
        ActionReference action;
        try {
            Iterator<ActionReference> itactions = getActions().values().iterator();
            Method[] methods = windowRef.getClass().getMethods();
            while (itactions.hasNext()) {
                action = itactions.next();
                if (action.getName().equals(name)) {
                    for (int i = 0; i < methods.length; i++) {
                        if (methods[i].getName().equalsIgnoreCase("get"+action.getName()) || methods[i].getName().equalsIgnoreCase(action.getSourceAccessor())) {
                            visibleAction(action, methods[i].invoke(windowRef, new Object[] {}), visible);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void enableAction(Object actionRef, boolean enabled) {
        if (actionRef instanceof JButton) {
            ((JButton)actionRef).setEnabled(enabled);
        } else if (actionRef instanceof JTextField) {
            ((JTextField)actionRef).setEnabled(enabled);
        } else if (actionRef instanceof JCheckBox) {
            ((JCheckBox)actionRef).setEnabled(enabled);
        } else if (actionRef instanceof JComboBox) {
            ((JComboBox)actionRef).setEnabled(enabled);
        } else if (actionRef instanceof JRadioButton) {
            ((JRadioButton)actionRef).setEnabled(enabled);
        } else if (actionRef instanceof JTable) {
            ((JTable)actionRef).setEnabled(enabled);
        }
    }

    private void focusControl(Object actionRef) {
        if (actionRef instanceof JButton) {
            ((JButton)actionRef).requestFocus();
        } else if (actionRef instanceof JTextField) {
            ((JTextField)actionRef).requestFocus();
        } else if (actionRef instanceof JCheckBox) {
            ((JCheckBox)actionRef).requestFocus();
        } else if (actionRef instanceof JComboBox) {
            ((JComboBox)actionRef).requestFocus();
        } else if (actionRef instanceof JRadioButton) {
            ((JRadioButton)actionRef).requestFocus();
        } else if (actionRef instanceof JTable) {
            ((JTable)actionRef).requestFocus();
        }
    }

    private void disableSelectionTable(Object actionRef) {
        if (actionRef instanceof JTable) {
            ((JTable)actionRef).clearSelection();
        }
    }
    
    private void visibleAction(ActionReference action, Object actionRef, boolean visible) {
        if (actionRef instanceof JButton) {
            ((JButton)actionRef).setVisible(visible);
        }
    }
    
    public void setMainWindowType(String mainWindowType) {
        this.mainWindowType = mainWindowType;
    }
    
    public void addAttribute(String name, Object value) {
        attributes.put(name, value);
    }
    
    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    public boolean isLockFields() {
        return lockFields;
    }

    public void setLockFields(boolean lockFields) {
        this.lockFields = lockFields;
    }

    private void invokeXWorkAction(String name) throws Exception {
        ActionReference ar = ClientConfig.instance().getActionReference(getName()+"."+name);
        if (ar.getName().equals("closeView")) {
            close();
        } else if (ar.getName().indexOf("showView") >= 0) {
            // @todo Verificar se aqui windowRef e null
            // Sempre limpando as janelas aqui antes de exibir!!! Isto somente qdo o showView vem direto de um botao.
            ClientConfig.instance().getView(ar.getName().substring(9)).show(windowRef.getParent(), SwingView.FROM_SHOWVIEW_ACTION, true);
        } else {
            // @todo Problemas com o SF nas actions contornado aqui, injeto a dependencia na mao
            ActionInvocation ai = ClientConfig.instance().invokeXWorkAction(ar.getName(), new HashMap());
            ((Action)ai.getAction()).setView(this);
            BusinessServiceFactory sf = (BusinessServiceFactory)ClientConfig.instance().getSpringBean("BusinessServiceFactory");
            ((Action)ai.getAction()).setServiceFactory(sf);
            PlatformTransactionManager ptm = (PlatformTransactionManager)ClientConfig.instance().getSpringBean("hibernateTransactionManager");
            ((Action)ai.getAction()).setTransactionManager(ptm);
            // @todo 12022007: Parametros deveriam estar no ActionContext
            ((Action)ai.getAction()).setParameters(ar.getParams());
            String result = ai.getProxy().execute();
            System.out.println(ar.getName()+" action result: "+result);
            // Working with the validation errors
            // @todo 19012007: Foco nos campos com erro
            if (result.equals("input")) {
                Iterator errors = ((ValidationAware)ai.getAction()).getFieldErrors().keySet().iterator();
                if (errors.hasNext())
                    focusControl((String)errors.next());
            }
        }
    }
    
    // The listeners methods
    public void keyTyped(KeyEvent keyEvent) {
        try {
            //updateFormFields();
            // Executing the xwork action
            if ( ! (keyEvent.getSource() instanceof JTable) ) {
                JTextField tf = (JTextField)keyEvent.getSource();
                FormField ff = getFormFields().get(tf.getName());
                invokeXWorkAction(ff.getKeyPressAction().getSourceAccessor());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void keyPressed(KeyEvent e) {
        if (! lockFields && e.getSource() instanceof JTable) {
            JTable jt = (JTable)e.getSource();
            Table t = getTables().get(jt.getName());
            // The table selected object is defined here.
            CollectionModel model = (CollectionModel)jt.getModel();
            model.setSelectedObject(model.getObjectAt(jt.getSelectedRow()));
            // Now the action related with the table
            try {
                if ( e.getKeyCode() == e.VK_ENTER )
                    if (t.getDoubleClickAction() != null)
                        invokeXWorkAction(t.getDoubleClickAction().getSourceAccessor());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public void keyReleased(KeyEvent e) {
        if (! lockFields && e.getSource() instanceof JTable) {
            JTable jt = (JTable)e.getSource();
            Table t = getTables().get(jt.getName());
            CollectionModel model = (CollectionModel)jt.getModel();
            // The table selected object is defined here.
            if ( model.getObjectAt(jt.getSelectedRow()) != null ) {
                model.setSelectedObject(model.getObjectAt(jt.getSelectedRow()));
            }
        }
    }
    
    public void valueChanged(ListSelectionEvent e) {
        if (e.getSource() instanceof JList && !e.getValueIsAdjusting()) {
            JList jl = (JList)e.getSource();
            FormField ff = getFormFields().get(jl.getName().toUpperCase());
            // The jlist selected object is defined here.
            CollectionModel model = (CollectionModel)jl.getModel();
            model.setSelectedItem(jl.getSelectedIndex());
            ff.setValue(model.getSelectedValue());
            try {
                invokeXWorkAction(ff.getSelectionAction().getSourceAccessor());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    
    // @todo 25012007: Quais metodos de acesso a objetos da visao deverao estar aqui e no Action???
    public void setFormFieldCollection(String name, List collection) {
        FormField ff = getFormFields().get(name.toUpperCase());
        if (ff.getType().equalsIgnoreCase(FormField.GENERIC)) {
            ((GenericFormField)getFormFieldUIReference(name)).setCollection(collection);
        } else {
            ((CollectionModel)ff.getModel()).setObjectsCollection(collection);
        }
    }

    public void setFormFieldSelectedObject(String name, Object obj) {
        ((CollectionModel)getFormFields().get(name.toUpperCase()).getModel()).setSelectedObject(obj);
    }

    public Object getFormFieldSelectedObject(String name) {
        Object objeto = null;
                
        try {
            objeto = ((CollectionModel)getFormFields().get(name.toUpperCase()).getModel()).getSelectedObject();
        } catch (Exception e) {
        }
        
        return objeto;
    }
    
    public Object getTableSelectedObject(String name) {
        return ((CollectionModel)getTables().get(name).getModel()).getSelectedObject();
    }
    
    public Object getTableObject(String name) {
        return ((CollectionModel)getTables().get(name).getModel()).getObjects();
    }
    
    public Object removeTableSelectedObject(String tableName) {
        Table table = getTables().get(tableName);
        CollectionModel cm = (CollectionModel)table.getModel();
        Object retorno = cm.removeSelectedObject();
        cm.setSelectedObject(null);
        return retorno;
    }

    public void setTableObjects(String tableName, List objects) {
        CollectionModel cm = ((CollectionModel)getTables().get(tableName).getModel());
        if (cm.getUIReference() == null)
            cm.setUIReference(loadTableUIReference(tableName));
        cm.setObjectsCollection(objects);
        disableSelectionTable(tableName);
    }

    private JTable loadTableUIReference(String tableName) {
        try {
            Method[] methods = windowRef.getClass().getMethods();
            for (int i = 0; i < methods.length; i++)
                if (methods[i].getName().equalsIgnoreCase("get"+tableName))
                    return (JTable)methods[i].invoke(windowRef, new Object[] {});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public Object removeTableObject(String tableName, Object object) {
        Table table = getTables().get(tableName);
        CollectionModel cm = (CollectionModel)table.getModel();
        Object retorno = cm.removeObject(object);
        cm.setSelectedObject(null);
        return retorno;
    }
    
    public void actionPerformed(ActionEvent actionEvent) {
        if ( windowRef == null )
            return;
        try {
            updateFormFields();
            // Executing the xwork action
            invokeXWorkAction(actionEvent.getActionCommand());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void mouseClicked(MouseEvent e) {
        if (! lockFields && e.getSource() instanceof JTableHeader) {
            // @todo Solicitacao #68 (defeito) - Ordenação por JTable
            JTableHeader header = (JTableHeader)e.getSource();
            JTable jt = header.getTable();
            int col = header.columnAtPoint(e.getPoint());
            ((CollectionModel)jt.getModel()).sortByColumn(jt.convertColumnIndexToModel(col), true);
        } else if (! lockFields && e.getSource() instanceof JTable) {
            JTable jt = (JTable)e.getSource();
            Table t = getTables().get(jt.getName());
            // The table selected object is defined here.
            CollectionModel model = (CollectionModel)jt.getModel();
            model.setSelectedObject(model.getObjectAt(jt.rowAtPoint(e.getPoint())));
            // Now the action related with the table
            try {
                if ( e.getClickCount() == 1 )
                    if (t.getClickAction() != null)
                        invokeXWorkAction(t.getClickAction().getSourceAccessor());
                if ( e.getClickCount() == 2 )
                    if (t.getDoubleClickAction() != null)
                        invokeXWorkAction(t.getDoubleClickAction().getSourceAccessor());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (! lockFields && e.getSource() instanceof JList) {
            JList jl = (JList)e.getSource();
            CollectionModel model = (CollectionModel)jl.getModel();
            model.setSelectedObject(model.getObjectAt(jl.getSelectedIndex()));
        }
    }
    
    public void mouseEntered(MouseEvent e) {
        
    }
    
    public void mouseExited(MouseEvent e) {
        
    }
    
    public void mousePressed(MouseEvent e) {
        
    }
    
    public void mouseReleased(MouseEvent e) {
        
    }

    public void mouseDragged(MouseEvent e) {
        
    }
    
    public void mouseMoved(MouseEvent e) {
        
    }

    public void mouseWheelMoved(MouseWheelEvent e) {
        
    }
    
    public void focusLost(FocusEvent e) {
        
    }

    public void focusGained(FocusEvent e) {
        try {
            //updateFormFields();
            // Executing the xwork action
            JComponent jc = (JComponent)e.getSource();
            FormField ff = getFormFields().get(jc.getName().toUpperCase());
            invokeXWorkAction(ff.getFocusGainedAction().getSourceAccessor());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void stateChanged(ChangeEvent e) {
        try {
            //updateFormFields();
            // Executing the xwork action
            JTabbedPane jtp = (JTabbedPane)e.getSource();
            FormField ff = getFormFields().get(jtp.getName().toUpperCase());
            invokeXWorkAction(ff.getChangeAction().getSourceAccessor());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
