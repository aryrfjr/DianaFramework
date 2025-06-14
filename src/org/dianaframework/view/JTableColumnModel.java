package org.dianaframework.view;

/**
 *
 * Interface para definicao do column model das tabelas.
 *
 * $Id: JTableColumnModel.java 1594 2007-02-07 11:12:15Z bruno $
 * @author <a href="mailto:aryjunior@gmail.com">Ary Junior</a>
 *
 */
public interface JTableColumnModel {

    public void setColumnWidths(int[] widths);
    public int[] getColumnWidths();

    public void setRowSelection(String arg);
    public String getRowSelection();
    
}
