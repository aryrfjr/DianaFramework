package org.dianaframework.action;

import com.opensymphony.xwork.ActionSupport;
import com.opensymphony.xwork.ValidationAware;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import org.dianaframework.servicelocator.BusinessServiceClient;
import org.dianaframework.servicelocator.BusinessServiceFactory;
import org.dianaframework.swing.ClientConfig;
import org.dianaframework.swing.result.ViewResult;
import org.dianaframework.swing.view.SwingView;
import org.dianaframework.swing.view.Table;
import org.dianaframework.view.BasicView;
import org.dianaframework.view.CollectionModel;
import org.dianaframework.view.FormField;
import org.dianaframework.view.JTableColumnModel;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

/**
 *
 * Classe basica para todas as acoes da aplicacao.
 *
 * $Id: Action.java 2071 2007-03-01 20:12:29Z aryjunior $
 * @author <a href="mailto:aryjunior@gmail.com">Ary Junior</a>
 *
 */
public class Action extends ActionSupport implements BusinessServiceClient, ValidationAware {
protected BusinessServiceFactory sf;
private BasicView view;
protected TransactionTemplate transactionTemplate;
private HashMap<String, String> parameters = new HashMap();

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        transactionTemplate = new TransactionTemplate(transactionManager);
    }

    public void setServiceFactory(BusinessServiceFactory sf) {
        this.sf = sf;
    }

    public boolean getCheckBoxValue(String name) {
        return getView().getFormFields().get(name.toUpperCase()).getValue().equals("selected");
    }
    
    public String getFormFieldValue(String name) {
        // @todo 10012007: Validacao de datas
        String value = (String)getView().getFormFields().get(name.toUpperCase()).getValue();
        return value == null ? "" : value.toString();
    }
    
    public int getIntegerFormFieldValue(String name) {
        Object objeto = getView().getFormFields().get(name.toUpperCase()).getValue();
        if ( objeto == null ) {
            return 0;
        } else if (((String)objeto).equals("")) {
            return 0;
        } else {
            return Integer.parseInt(((String)objeto));
        }
    }
    
    public double getDoubleFormFieldValue(String name) {
        Object objeto = getView().getFormFields().get(name.toUpperCase()).getValue();
        if ( objeto == null ) {
            return 0;
        } else if (((String)objeto).equals("")) {
            return 0;
        } else {
            return Double.parseDouble(((String)objeto));
        }
    }
    
    public Date getDateFormFieldValue(String name) {
        return (Date)getView().getFormFields().get(name.toUpperCase()).getValue();
    }
    
    public String getTableSelectedValue(String name) {
        return ((CollectionModel)getView().getTables().get(name).getModel()).getSelectedValue();
    }
    
    public void selectAllTableObjects(String name) {
        JTable jtable = (JTable)((CollectionModel)getView().getTables().get(name).getModel()).getUIReference();
        jtable.selectAll();
    }
    
    public void clearTableSelection(String name) {
        JTable jtable = (JTable)((CollectionModel)getView().getTables().get(name).getModel()).getUIReference();
        jtable.clearSelection();
    }
    
    public void addObjectOnList(String name, Object object) {
        CollectionModel cm = (CollectionModel)getView().getFormFields().get(name.toUpperCase()).getModel();
        cm.addObject(object);
    }

    public void addObjectOnTable(String tableName, Object object) {
        CollectionModel cm = (CollectionModel)getView().getTables().get(tableName).getModel();
        cm.addObject(object);
    }

    public void setTableColumnsOrder(String tableName, int[] order) {
        CollectionModel cm = (CollectionModel)getView().getTables().get(tableName).getModel();
        cm.setColumnsOrder(order);
    }
    
    public void changeTableModel(String tableName, String modelName) {
        Table t = getView().getTables().get(tableName);
        JTable jtable = (JTable)((CollectionModel)t.getModel()).getUIReference();
        AbstractTableModel atm = (AbstractTableModel)ClientConfig.instance().getTableModels().get(modelName);
        ((CollectionModel)atm).setUIReference(jtable);
        t.setModel(atm);
        jtable.setModel(atm);
        // Solicitacao #129 (defeito) - Tamanhos das colunas ao alterar um table model
        SwingView.loadSwingTableConfig(t, jtable);
    }
    
    public Object removeListSelectedObject(String name) {
        FormField ff = getView().getFormFields().get(name.toUpperCase());
        CollectionModel cm = (CollectionModel)ff.getModel();
        Object retorno = cm.removeSelectedObject();
        cm.setSelectedObject(null);
        return retorno;
    }

    public Object removeListObject(String name, String value) {                     
        FormField ff = getView().getFormFields().get(name.toUpperCase());
        CollectionModel cm = (CollectionModel)ff.getModel();
        cm.setSelectedObject(value);
        Object retorno = cm.removeSelectedObject();
        cm.setSelectedObject(null);
        return retorno;
    }

    public void reloadListObject(String name) {
        FormField ff = getView().getFormFields().get(name.toUpperCase());
        CollectionModel cm = (CollectionModel)ff.getModel();
        cm.reload();
    }
    
    public Object removeListObject(String name, Object object) {                     
        FormField ff = getView().getFormFields().get(name.toUpperCase());
        CollectionModel cm = (CollectionModel)ff.getModel();
        Object retorno = cm.removeObject(object);
        cm.setSelectedObject(null);
        return retorno;
    }

    public Object removeTableSelectedObject(String tableName) {
        Table table = getView().getTables().get(tableName);
        CollectionModel cm = (CollectionModel)table.getModel();
        Object retorno = cm.removeSelectedObject();
        cm.setSelectedObject(null);
        return retorno;
    }

    public Object removeTableObject(String tableName, Object object) {
        Table table = getView().getTables().get(tableName);
        CollectionModel cm = (CollectionModel)table.getModel();
        Object retorno = cm.removeObject(object);
        cm.setSelectedObject(null);
        return retorno;
    }

    public void removeAllTableObjects(String tableName) {
        Table table = getView().getTables().get(tableName);
        CollectionModel cm = (CollectionModel)table.getModel();
        cm.removeAll();
    }

    public void removeAllListObjects(String name) {
        FormField ff = getView().getFormFields().get(name.toUpperCase());
        CollectionModel cm = (CollectionModel)ff.getModel();
        cm.removeAll();
    }

    public void setTableObjects(String tableName, List objects) {
        CollectionModel cm = ((CollectionModel)getView().getTables().get(tableName).getModel());
        cm.setObjectsCollection(objects);
        
        ((SwingView)getView()).disableSelectionTable(tableName);
    }

    public List getTableObjects(String tableName) {
        return ((CollectionModel)getView().getTables().get(tableName).getModel()).getObjects();
    }

    public List getListObjects(String name) {
        return ((CollectionModel)getView().getFormFields().get(name.toUpperCase()).getModel()).getObjects();
    }

    public Object getTableSelectedObject(String name) {
        return ((CollectionModel)getView().getTables().get(name).getModel()).getSelectedObject();
    }

    public List getTableSelectedObjects(String name) {
        return ((CollectionModel)getView().getTables().get(name).getModel()).getSelectedObjects();
    }

    public void clearTableSelectedObject(String name) {
        ((CollectionModel)getView().getTables().get(name).getModel()).setSelectedObject(null);
    }

    public Object getFormFieldSelectedObject(String name) {
        Object objeto = null;
                
        try {
            objeto = ((CollectionModel)getView().getFormFields().get(name.toUpperCase()).getModel()).getSelectedObject();
        } catch (Exception e) {
        }
        
        return objeto;
    }

    public void setFormFieldSelectedItem(String name, int index) {
        ((CollectionModel)getView().getFormFields().get(name.toUpperCase()).getModel()).setSelectedItem(index);
    }
    
    public void setFormFieldSelectedObject(String name, Object obj) {
        ((CollectionModel)getView().getFormFields().get(name.toUpperCase()).getModel()).setSelectedObject(obj);
    }
    
    public String getFormFieldSelectedValue(String name) {
        return ((CollectionModel)getView().getFormFields().get(name.toUpperCase()).getModel()).getSelectedValue();
    }

    public void setFormFieldCollection(String name, List collection) {
        ((CollectionModel)getView().getFormFields().get(name.toUpperCase()).getModel()).setObjectsCollection(collection);
/* Alberto: retirei este codigo pois estava atrapalhando a tela de enturmacao.
 * Este procedimento deve ser outro metodo.*/
        //if (collection == null) {
        //    ((CollectionModel)getView().getFormFields().get(name.toUpperCase()).getModel()).setSelectedItem(-1);            
        //} else {
            //((CollectionModel)getView().getFormFields().get(name.toUpperCase()).getModel()).setSelectedItem(0);
        //}
// Ary: Retornei com o codigo acima
        /*if (collection == null)
            ((CollectionModel)getView().getFormFields().get(name.toUpperCase()).getModel()).setSelectedItem(0);            */
    }

    public void setView(BasicView view) {
        this.view = view;
    }
    
    public void updateTableModel(String name, List objects) {
        CollectionModel cm = (CollectionModel)getView().getTables().get(name).getModel();
        cm.setObjectsCollection(objects);
    }

    public BasicView getView() {
        return view;
    }

    // @todo 14022007: enableControl nao funciona com result SHOW_VIEW
    public BasicView getView(String name) {
        SwingView sv = ClientConfig.instance().getView(name);
        // @todo 25022007: Action invocada pelo menu... estava dando NullPointerException aqui
        if (view != null && ((SwingView)view).getWindowReference() != null) {
            sv.loadWindow(((SwingView)view).getWindowReference().getParent());
        } else {
            sv.loadWindow(ViewResult.mainWindow);
        }
        return sv;
    }

    public HashMap<String, String> getParameters() {
        return parameters;
    }
    
    public String getParam(String name) {
        return parameters.get(name);
    }

    public void setParameters(HashMap<String, String> parameters) {
        this.parameters = parameters;
    }

}
