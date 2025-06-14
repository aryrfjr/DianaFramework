package org.dianaframework.swing.view;

import javax.swing.table.AbstractTableModel;
import org.dianaframework.action.ActionReference;

/**
 *
 * Classe que representa uma tabela contida em uma view.
 *
 * @version $Id: Table.java 1594 2007-02-07 11:12:15Z bruno $
 * @author <a href="mailto:aryjunior@gmail.com">Ary Junior</a>
 *
 */
public class Table {
private String name;
private AbstractTableModel model;
private ActionReference clickAction;
private ActionReference doubleClickAction;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AbstractTableModel getModel() {
        return model;
    }

    public void setModel(AbstractTableModel model) {
        this.model = model;
    }

    public ActionReference getClickAction() {
        return clickAction;
    }

    public void setClickAction(ActionReference clickAction) {
        this.clickAction = clickAction;
    }

    public ActionReference getDoubleClickAction() {
        return doubleClickAction;
    }

    public void setDoubleClickAction(ActionReference doubleClickAction) {
        this.doubleClickAction = doubleClickAction;
    }

}
