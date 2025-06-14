package org.dianaframework.swing.result;

import com.opensymphony.xwork.ActionInvocation;
import com.opensymphony.xwork.Result;

/**
 *
 * Result de uma acao que nao possui result.
 *
 * @version $Id: NoneResult.java 1594 2007-02-07 11:12:15Z bruno $
 * @author <a href="mailto:aryjunior@gmail.com">Ary Junior</a>
 *
 */
public class NoneResult implements Result {
    
    public NoneResult() {
        super();
    }
    
    public void execute(ActionInvocation invocation) throws Exception {}

}
