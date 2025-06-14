package org.dianaframework.swing.interceptor;

import com.opensymphony.xwork.ActionInvocation;
import com.opensymphony.xwork.interceptor.Interceptor;
import javax.swing.JOptionPane;
import org.dianaframework.action.Action;
import org.dianaframework.swing.view.SwingView;

/**
 *
 * Interceptor que exibe uma mensagem de confirmacao.
 *
 * @version $Id: ConfirmInterceptor.java 1594 2007-02-07 11:12:15Z bruno $
 * @author <a href="mailto:aryjunior@gmail.com">Ary Junior</a>
 *
 */
public class ConfirmInterceptor implements Interceptor {
private String message;
    
    public void destroy() {
    }
    
    public void init() {
    }
    
    public String intercept(ActionInvocation invocation) throws Exception {
        SwingView view = (SwingView)((Action)invocation.getAction()).getView();
        int n = JOptionPane.showConfirmDialog(view.getWindowReference(),
            message,
            "",
            JOptionPane.YES_NO_OPTION);        
        if (n == JOptionPane.YES_OPTION) {
            return invocation.invoke();
        } else {
            return Action.NONE;
        }
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
}
