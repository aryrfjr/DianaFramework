package org.dianaframework.swing;

/**
 *
 * Aplicacao cliente.
 *
 * @version $Id: DianaClient.java 1594 2007-02-07 11:12:15Z bruno $
 * @author <a href="mailto:aryjunior@gmail.com">Ary Junior</a>
 *
 */
public class DianaClient {
private MainWindow janelaPrincipal;

    public DianaClient() {
        ClientConfig conf = ClientConfig.instance();
        try {
            janelaPrincipal = conf.buildMainWindow();
            conf.loadSpringIoC();
            conf.loadConfig();
        } catch (ClientConfigException cce) {
            System.out.println(cce.getMensagem());
            cce.getExcecaoSubjacente().printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        janelaPrincipal.setVisible(true);
    }

}
