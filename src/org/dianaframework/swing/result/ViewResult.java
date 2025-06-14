package org.dianaframework.swing.result;

import com.opensymphony.xwork.ActionInvocation;
import com.opensymphony.xwork.Result;
import java.util.HashMap;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.dianaframework.action.Action;
import org.dianaframework.servicelocator.BusinessServiceFactory;
import org.dianaframework.swing.ClientConfig;
import org.dianaframework.swing.view.SwingView;
import org.springframework.transaction.PlatformTransactionManager;

/**
 *
 * Result de uma acao que exibe uma visao.
 *
 * @version $Id: ViewResult.java 2241 2007-03-16 21:56:48Z aryjunior $
 * @author <a href="mailto:aryjunior@gmail.com">Ary Junior</a>
 *
 */
public class ViewResult implements Result {
    public static String QUESTION = "QUESTION";
    public static String SHOW_VIEW = "SHOW_VIEW";
    public static String SHOW_VIEW_LOCK_FIELDS = "SHOW_VIEW_LOCK_FIELDS";
    public static String RELOAD_VIEW = "RELOAD_VIEW";
    public static String UPDATE_VIEW = "UPDATE_VIEW";
    public static String CLOSE_VIEW = "CLOSE_VIEW";
    public static String CLOSE_VIEW_CHAIN_ACTION = "CLOSE_VIEW_CHAIN_ACTION";
    public static String CLOSE_VIEW_UPDATE = "CLOSE_VIEW_UPDATE";
    public static String CLOSE_VIEW_RELOAD_MODEL = "CLOSE_VIEW_RELOAD_MODEL";
    public static String RELOAD_MODEL = "RELOAD_MODEL";
    public static JFrame mainWindow;// If the action is executed on main menu
    private String action;
    private String actionName;
    private String actionView;
    private String question;
    private String yesAction;
    private String noAction;
    private String viewName;
    private boolean clearView = false;
    
    public ViewResult() {
        super();
    }
    
    public void execute(ActionInvocation invocation) throws Exception {
        // @todo Alberto: Verificar se o viewname é nulo
        SwingView view = (SwingView)((Action)invocation.getAction()).getView();
        if (view == null)
            view = ClientConfig.instance().getView(viewName);
        if (action.equals(QUESTION)) {
            int n;
            if (view.getWindowReference() == null) {
                n = JOptionPane.showConfirmDialog(mainWindow,
                        question,
                        "",
                        JOptionPane.YES_NO_OPTION);
            } else {
                n = JOptionPane.showConfirmDialog(view.getWindowReference(),
                        question,
                        "",
                        JOptionPane.YES_NO_OPTION);
            }
            if (n == JOptionPane.YES_OPTION) {
                executeActionOnView(yesAction, view);
            } else {
                executeActionOnView(noAction, view);
            }
        } else {
            executeActionOnView(action, view);
        }
    }
    
    private void executeActionOnView(String action, SwingView view) {
        if (action.equals(RELOAD_VIEW)) {
            view.reloadModel(true); // true ==> reloadAll
        } else if (action.equals(SHOW_VIEW)) {
            // @todo (Tracker 154) EVELAINE: No Menu Aluno, Verificar Pendencias Matriculas, ao escolher a opção Relatorio
            // dá erro nessa linha, no viewName.
            ClientConfig.instance().getView(viewName).setLockFields(false);
            if (view.getWindowReference() == null) {
                ClientConfig.instance().getView(viewName).show(mainWindow, SwingView.FROM_ACTION_RESULT, clearView);
            } else {
                SwingView sv = ClientConfig.instance().getView(viewName);
                if (sv.getWindowReference() == null) {
                    sv.show(view.getWindowReference().getParent(), SwingView.FROM_ACTION_RESULT, clearView);
                } else {
                    sv.show(SwingView.FROM_ACTION_RESULT, clearView);
                }
            }
        } else if (action.equals(SHOW_VIEW_LOCK_FIELDS)) {
            ClientConfig.instance().getView(viewName).setLockFields(true);
            if (view.getWindowReference() == null) {
                ClientConfig.instance().getView(viewName).show(mainWindow, SwingView.FROM_ACTION_RESULT, clearView);
            } else {
                SwingView sv = ClientConfig.instance().getView(viewName);
                if (sv.getWindowReference() == null) {
                    sv.show(view.getWindowReference().getParent(), SwingView.FROM_ACTION_RESULT, clearView);
                } else {
                    sv.show(SwingView.FROM_ACTION_RESULT, clearView);
                }
            }
        } else if (action.equals(CLOSE_VIEW_CHAIN_ACTION)) {
            view.close();
            try {
                invokeXWorkAction(actionName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (action.equals(CLOSE_VIEW_UPDATE)) {
            view.close();
            ClientConfig.instance().getView(viewName).update();
        } else if (action.equals(UPDATE_VIEW)) {
            if (clearView)
                view.clearFormFields();
            view.update();
        } else if (action.equals(RELOAD_MODEL)) {
            view.reloadModel(true); // true ==> reloadAll
        } else if (action.equals(CLOSE_VIEW_RELOAD_MODEL)) {
            view.close();
            ClientConfig.instance().getView(viewName).reloadModel(true); // true ==> reloadAll
        } else if (action.equals(CLOSE_VIEW)) {
            view.clearFormFields();
            view.close();
        }
    }
    
    public void setAction(String action) {
        this.action = action;
    }
    
    public void setQuestion(String question) {
        this.question = question;
    }
    
    public void setYesAction(String yesAction) {
        this.yesAction = yesAction;
    }
    
    public void setNoAction(String noAction) {
        this.noAction = noAction;
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public void setClearView(String clearView) {
        this.clearView = clearView.equalsIgnoreCase("YES");
    }

    private void invokeXWorkAction(String name) throws Exception {
        // @todo Problemas com o SF nas actions contornado aqui, injeto a dependencia na mao
        ActionInvocation ai = ClientConfig.instance().invokeXWorkAction(name, new HashMap());
        ((Action)ai.getAction()).setView(ClientConfig.instance().getView(getActionView()));
        BusinessServiceFactory sf = (BusinessServiceFactory)ClientConfig.instance().getSpringBean("BusinessServiceFactory");
        ((Action)ai.getAction()).setServiceFactory(sf);
        PlatformTransactionManager ptm = (PlatformTransactionManager)ClientConfig.instance().getSpringBean("hibernateTransactionManager");
        ((Action)ai.getAction()).setTransactionManager(ptm);
        String result = ai.getProxy().execute();
        System.out.println(name+" action result: "+result);
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public String getActionView() {
        return actionView;
    }

    public void setActionView(String actionView) {
        this.actionView = actionView;
    }
    
}
