package org.dianaframework.xml;

/**
 * 
 * A XML tag attribute.
 * 
 * @author <a href="mailto:aryjunior@gmail.com">Ary Junior</a>
 * @version $Id: TagAttribute.java 1594 2007-02-07 11:12:15Z bruno $
 *
 */
public class TagAttribute {
private String name;
private String value;

    public TagAttribute(String name, String value) {
        this.setName(name);
        this.setValue(value);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
