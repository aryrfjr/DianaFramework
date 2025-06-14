package org.dianaframework.swing.result;

import com.opensymphony.xwork.ActionInvocation;
import com.opensymphony.xwork.Result;
import com.opensymphony.xwork.ValidationAware;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JOptionPane;
import org.dianaframework.action.Action;
import org.dianaframework.swing.view.SwingView;

/**
 *
 * Result de uma acao que exibe uma caixa de dialogo para o usuario alertando requerimentos na validacao.
 *
 * @version $Id: ValidationDialogResult.java 1594 2007-02-07 11:12:15Z bruno $
 * @author <a href="mailto:aryjunior@gmail.com">Ary Junior</a>
 *
 */
public class ValidationDialogResult implements Result {
private String message;
    
    public ValidationDialogResult() {
        super();
    }
    
    public void execute(ActionInvocation invocation) throws Exception {
        SwingView view = (SwingView)((Action)invocation.getAction()).getView();
        ValidationAware va = (ValidationAware)invocation.getAction();
        Iterator<ArrayList<String>> errors = va.getFieldErrors().values().iterator();
        // @todo So para funcionar na apresentacao
        JOptionPane.showMessageDialog(view.getWindowReference(),
            errors.next().get(0),
            "",
            JOptionPane.ERROR_MESSAGE);
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
