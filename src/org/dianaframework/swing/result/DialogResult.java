package org.dianaframework.swing.result;

import com.opensymphony.xwork.ActionInvocation;
import com.opensymphony.xwork.Result;
import javax.swing.JOptionPane;
import org.dianaframework.action.Action;
import org.dianaframework.swing.view.SwingView;

/**
 *
 * Result de uma acao que exibe uma caixa de dialogo para o usuario alertando erros.
 *
 * @version $Id: DialogResult.java 1594 2007-02-07 11:12:15Z bruno $
 * @author <a href="mailto:aryjunior@gmail.com">Ary Junior</a>
 *
 */
public class DialogResult implements Result {
private String message;
    
    public DialogResult() {
        super();
    }
    
    public void execute(ActionInvocation invocation) throws Exception {
        SwingView view = (SwingView)((Action)invocation.getAction()).getView();
        JOptionPane.showMessageDialog(view.getWindowReference(),
            message,
            "",
            JOptionPane.ERROR_MESSAGE);
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
