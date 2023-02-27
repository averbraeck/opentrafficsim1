package org.opentrafficsim.editor;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.TableCellRenderer;

import de.javagl.treetable.JTreeTable;

/**
 * Renderer for columns in the tree table.
 * @author wjschakel
 */
public class StringCellRenderer extends JLabel implements TableCellRenderer
{

    /** */
    private static final long serialVersionUID = 20230218L;

    /** Tree table. */
    private final JTreeTable treeTable;

    /**
     * Constructor.
     * @param treeTable JTreeTable; tree table.
     */
    public StringCellRenderer(final JTreeTable treeTable)
    {
        this.treeTable = treeTable;
        setOpaque(true);
    }

    /** {@inheritDoc} */
    @Override
    public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected,
            final boolean hasFocus, final int row, final int column)
    {
        setText(value == null ? "" : value.toString());
        setFont(table.getFont());

        if (isSelected)
        {
            setBackground(UIManager.getColor("Table.selectionBackground"));
        }
        else
        {
            int treeColumn = this.treeTable.convertColumnIndexToView(0); // columns may have been moved in view
            int idColumn = this.treeTable.convertColumnIndexToView(1);
            int valueColumn = this.treeTable.convertColumnIndexToView(2);
            XsdTreeNode node = (XsdTreeNode) this.treeTable.getValueAt(row, treeColumn);
            String message =
                    column == idColumn ? node.reportInvalidId() : (column == valueColumn ? node.reportInvalidValue() : null);
            if (this.treeTable.isCellEditable(row, column) && message != null)
            {
                setToolTipText(AttributeCellRenderer.limitMessage(message));
                setBackground(OtsEditor.INVALID_COLOR);
            }
            else
            {
                setToolTipText(null);
                setBackground(UIManager.getColor("Table.background"));
            }
        }

        Border border;
        if (this.treeTable.isCellEditable(row, column))
        {
            border = new LineBorder(UIManager.getColor("Table.gridColor"));
        }
        else
        {
            border = new EmptyBorder(0, 0, 0, 0);
        }
        setBorder(border);

        return this;
    }

}
