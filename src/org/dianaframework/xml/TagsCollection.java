package org.dianaframework.xml;

import java.util.ArrayList;

/**
 * 
 * 
 * @author <a hrefTagsCollectionr@valoriza.com.br">Ary Junior</a>
 * @version $Id: TagsCollection.java 1594 2007-02-07 11:12:15Z bruno $
 */
public class TagsCollection {
private ArrayList tags = null;

    public TagsCollection() {
        tags = new ArrayList();
    }

    public TagsCollection(ArrayList tags) {
        this.tags = tags;
    }

    public void addTag(Tag tag) {
        tags.add(tag);
    }

    public Tag getTag(int ind) {
        return (Tag)tags.get(ind);
    }

    public int countTags() {
        return tags.size();
    }

}