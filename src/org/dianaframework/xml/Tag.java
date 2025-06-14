package org.dianaframework.xml;

import java.util.Iterator;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * 
 * A XML tag.
 * 
 * @version $Id: Tag.java 1594 2007-02-07 11:12:15Z bruno $
 * @author <a href="mailto:aryjunior@gmail.com">Ary Junior</a>
 *
 */
public class Tag {
private HashMap attributes = new HashMap();
private String name;
private String content;
private Tag father;
private ArrayList children = new ArrayList();

    public void addAttribute(TagAttribute attribute) {
        attributes.put(attribute.getName(), attribute);
    }

    public TagAttribute getAttribute(String name) {
        return (TagAttribute)attributes.get(name);
    }

    public TagAttributesCollection getAttributes() {
        TagAttributesCollection ret = new TagAttributesCollection();
        Iterator chaves = attributes.keySet().iterator();
        while (chaves.hasNext()) {
            ret.addAtribute((TagAttribute)attributes.get((String)chaves.next()));
        }
        return ret;
    }

    public void setFather(Tag father) {
        this.father = father;
    }

    public Tag getFather() {
        return father;
    }

    public void addChild(Tag child) {
        child.setFather(this);
        children.add(child);
    }

    public Tag getChild(int ind) {
        return (Tag)children.get(ind);
    }

    /**
     * 
     * Returns the first named child.
     * 
     */
    public Tag getChild(String name) {
        for (int inc = 0; inc < children.size(); inc++)
            if (((Tag)children.get(inc)).getName().equals(name))
                return (Tag)children.get(inc);
        return null;
    }

    /**
     * 
     * Returns the first named child.
     * 
     */
    public Tag getChild(String name, String attribute, String value) {
        Tag child = null;
        for (int inc = 0; inc < children.size(); inc++) {
            child = (Tag)children.get(inc);
            if (child.getName().equals(name) && child.getAttribute(attribute).getValue().equals(value))
                return (Tag)children.get(inc);
        }
        return null;
    }

    public TagsCollection getChildren() {
        return new TagsCollection(children);
    }

    /**
     * 
     * Returns the first named child.
     *
     */
    public TagsCollection getChildren(String name) {
        TagsCollection ret = new TagsCollection();
        for (int inc = 0; inc < children.size(); inc++)
            if (((Tag)children.get(inc)).getName().equals(name))
                ret.addTag((Tag)children.get(inc));
        return ret;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
