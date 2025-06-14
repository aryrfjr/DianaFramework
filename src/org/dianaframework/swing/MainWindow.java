package org.dianaframework.swing;

import com.opensymphony.xwork.ActionInvocation;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import javax.swing.JFrame;
import org.dianaframework.action.Action;
import org.dianaframework.servicelocator.BusinessServiceFactory;
import org.dianaframework.swing.result.ViewResult;
import org.dianaframework.swing.view.SwingView;
import org.springframework.transaction.PlatformTransactionManager;

/**
 *
 * Janela principal MDI.
 *
 * @version $Id: MainWindow.java 1831 2007-02-14 14:41:54Z aryjunior $
 * @author <a href="mailto:aryjunior@gmail.com">Ary Junior</a>
 *
 */
public class MainWindow extends JFrame implements ActionListener {
    
    public MainWindow() {
        addWindowListener(
                new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        }
        );
    }
    
    // Main menu listener
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().indexOf("showView") >= 0) {
            // Sempre limpando as janelas aqui antes de exibir!!! Isto somente qdo o showView vem direto de um menu.
            ClientConfig.instance().getView(e.getActionCommand().substring(9)).show(this, SwingView.FROM_SHOWVIEW_ACTION, true);
        } else {
            try {
                invokeXWorkAction(e.getActionCommand());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    
    private void invokeXWorkAction(String name) throws Exception {
        // @todo Problemas com o SF nas actions contornado aqui, injeto a dependencia na mao
        ActionInvocation ai = ClientConfig.instance().invokeXWorkAction(name, new HashMap());
        //((Action)ai.getAction()).setView(this);
        ViewResult.mainWindow = this;
        BusinessServiceFactory sf = (BusinessServiceFactory)ClientConfig.instance().getSpringBean("BusinessServiceFactory");
        ((Action)ai.getAction()).setServiceFactory(sf);
        PlatformTransactionManager ptm = (PlatformTransactionManager)ClientConfig.instance().getSpringBean("hibernateTransactionManager");
        ((Action)ai.getAction()).setTransactionManager(ptm);
        String result = ai.getProxy().execute();
        System.out.println(name+" action result: "+result);
    }
    
}
