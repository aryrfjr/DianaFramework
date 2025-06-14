package org.dianaframework.swing;

import org.dianaframework.DianaException;

/**
 *
 * Excecao para o acesso ao arquivo de configuracao do cliente.
 *
 * @version $Id: ClientConfigException.java 1594 2007-02-07 11:12:15Z bruno $
 * @author <a href="mailto:aryjunior@gmail.com">Ary Junior</a>
 *
 */
public class ClientConfigException extends DianaException {

    /**
    *
    * Construtor com uma mensagem e uma excecao.
    *
    * @param mensagem String com a mensagem desta excecao.
    * @param subjacente Excecao q sera aninhada nesta.
    *
    */
    public ClientConfigException(String mensagem, Exception subjacente) {
        super(mensagem, subjacente);
    }
    
    /**
    *
    * Construtor com uma mensagem.
    *
    * @param mensagem String com a mensagem desta excecao.
    *
    */
    public ClientConfigException(String mensagem) {
        super(mensagem);
    }

}
