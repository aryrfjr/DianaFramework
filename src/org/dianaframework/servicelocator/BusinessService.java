package org.dianaframework.servicelocator;

/**
 *
 * Classe que guarga informacoes sobre os servicos de negocios do sistema.
 *
 * @version $Id: BusinessService.java 1594 2007-02-07 11:12:15Z bruno $
 * @author <a href="mailto:aryjunior@gmail.com">Ary Junior</a>
 *
 */
public class BusinessService {
public static final int EJB_SESSION = 0;
public static final int LOCAL = 1;
private String name;
private int type;
private String classs;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setType(String type) {
        if (type.equalsIgnoreCase("EJB_SESSION")) {
            setType(EJB_SESSION);
        } else if (type.equalsIgnoreCase("LOCAL")) {
            setType(LOCAL);
        }
    }

    public int getType() {
        return type;
    }

    public void setClass(String classs) {
        this.classs = classs;
    }

    public String getClasss() {
        return classs;
    }

}
