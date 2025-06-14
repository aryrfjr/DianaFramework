package org.dianaframework;

/**
*
* Excecao basica para qualquer outra excecao e pode guardar a excecao subjacente.
*
* @version $Id: DianaException.java 1594 2007-02-07 11:12:15Z bruno $
* @author <a href="mailto:aryjunior@gmail.com">Ary Junior</a>
*
*/
public class DianaException extends Exception {
private Exception subjacente = null;
private String mensagem = "";
    
    /**
    *
    * Construtor com uma mensagem e uma excecao.
    *
    * @param mensagem String com a mensagem desta excecao.
    * @param subjacente Excecao q sera aninhada nesta.
    *
    */
    public DianaException(String mensagem, Exception subjacente) {
        this.subjacente = subjacente;
        this.mensagem = mensagem;
    }
    
    /**
    *
    * Construtor com uma mensagem.
    *
    * @param mensagem String com a mensagem desta excecao.
    *
    */
    public DianaException(String mensagem) {
        this.mensagem = mensagem;
    }

    /**
    *
    * Retorna a excecao aninhada.
    *
    */
    public Exception getExcecaoSubjacente() {
        return subjacente;
    }

    /**
    *
    * Retorna a mensagem desta excecao.
    *
    */
    public String getMensagem() {
        return mensagem;
    }

}