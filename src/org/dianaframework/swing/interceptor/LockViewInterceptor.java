package org.dianaframework.swing.interceptor;

import com.opensymphony.xwork.ActionInvocation;
import com.opensymphony.xwork.interceptor.Interceptor;
import org.dianaframework.action.Action;
import org.dianaframework.view.BasicView;

/**
 *
 * Interceptor que desabilita os disparadores de acao para uma visao.
 *
 * @version $Id: LockViewInterceptor.java 1594 2007-02-07 11:12:15Z bruno $
 * @author <a href="mailto:aryjunior@gmail.com">Ary Junior</a>
 *
 */
public class LockViewInterceptor implements Interceptor {
    
    public void destroy() {
    }
    
    public void init() {
    }
    
    public String intercept(ActionInvocation invocation) throws Exception {

        BasicView view = ((Action)invocation.getAction()).getView();
        view.lockActions();
        String result = invocation.invoke();
        view.unlockActions();
        return result;
    }
    
}
