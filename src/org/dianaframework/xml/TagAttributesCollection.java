package org.dianaframework.xml;

import java.util.ArrayList;

/**
 * 
 * A TagAttribute collection.
 * 
 * @author <a href="mailto:aryjunior@gmail.com">Ary Junior</a>
 * @version $Id: TagAttributesCollection.java 1594 2007-02-07 11:12:15Z bruno $
 *
 */
public class TagAttributesCollection {
private ArrayList attributes = null;

    public TagAttributesCollection() {
        attributes = new ArrayList();
    }

    public TagAttributesCollection(ArrayList tags) {
        this.attributes = attributes;
    }

    public void addAtribute(TagAttribute atributo) {
        attributes.add(atributo);
    }

    public TagAttribute getAtribute(int ind) {
        return (TagAttribute)attributes.get(ind);
    }

    public int atributesCount() {
        return attributes.size();
    }

}
