package org.dianaframework.xdoclet;

/**
 *
 * @version $Id: XView.java 1594 2007-02-07 11:12:15Z bruno $
 * @author <a href="mailto:aryjunior@gmail.com">Ary Junior</a>
 *
 * @xdoclet.taghandler
 *   namespace="Diana"
 *
 */
public class XView {
private String name;
private String title;
private int index;

    /** Creates a new instance of XView */
    public XView() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
