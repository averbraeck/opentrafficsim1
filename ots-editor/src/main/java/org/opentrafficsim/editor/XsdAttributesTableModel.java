package org.opentrafficsim.editor;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import org.djutils.exceptions.Throw;
import org.w3c.dom.Node;

import de.javagl.treetable.JTreeTable;

/**
 * Model for a {@code JTable} to display the attributes of a {@code XsdTreeNode}.
 * @author wjschakel
 */
public class XsdAttributesTableModel extends AbstractTableModel
{

    /** */
    private static final long serialVersionUID = 20230217L;

    /** Column names. */
    private static final String[] COLUMN_NAMES = new String[] {"Property", "Value", "Use"};

    /** Minimum column widths. */
    private static final int[] MIN_COLUMN_WIDTHS = new int[] {50, 50, 30};

    /** Preferred column widths. */
    private static final int[] PREFERRED_COLUMN_WIDTHS = new int[] {200, 200, 30};

    /** The node of which the attributes are displayed. */
    private final XsdTreeNode node;
    
    /** Tree table, so it can be updated visually when a value has changed. */
    private final JTreeTable treeTable;

    /**
     * Constructor.
     * @param node XsdTreeNode; node of which the attributes are displayed.
     * @param treeTable JTreeTable; tree table.
     */
    public XsdAttributesTableModel(final XsdTreeNode node, final JTreeTable treeTable)
    {
        this.node = node;
        this.treeTable = treeTable;
    }

    /** {@inheritDoc} */
    @Override
    public int getRowCount()
    {
        return this.node == null ? 0 : this.node.attributeCount();
    }

    /** {@inheritDoc} */
    @Override
    public int getColumnCount()
    {
        return 3;
    }

    /** {@inheritDoc} */
    @Override
    public String getColumnName(final int columnIndex)
    {
        return COLUMN_NAMES[columnIndex];
    }

    /** {@inheritDoc} */
    @Override
    public Class<?> getColumnClass(final int columnIndex)
    {
        return String.class;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isCellEditable(final int rowIndex, final int columnIndex)
    {
        return columnIndex == 1;
    }

    /** {@inheritDoc} */
    @Override
    public Object getValueAt(final int rowIndex, final int columnIndex)
    {
        Node attribute = this.node.getAttributeNode(rowIndex);
        switch (columnIndex)
        {
            case 0:
                return XsdSchema.getAttribute(attribute, "name").toLowerCase();
            case 1:
                return this.node.getAttributeValue(rowIndex);
            case 2:
                String use = XsdSchema.getAttribute(attribute, "use");
                return use != null && use.equals("required") ? "*" : "";
            default:
                throw new IndexOutOfBoundsException();
        }
    }

    /** {@inheritDoc} */
    @Override
    public void setValueAt(final Object aValue, final int rowIndex, final int columnIndex)
    {
        this.treeTable.updateUI();
        Throw.when(columnIndex != 1, IllegalStateException.class,
                "Attribute table model requested to set a value from a column that does not represent the attribute value.");
        this.node.setAttributeValue(rowIndex, aValue.toString());
        this.fireTableCellUpdated(rowIndex, columnIndex);
    }
    
    /**
     * Returns the underlying node for which attributes are shown.
     * @return XsdTreeNode; underlying node for which attributes are shown.
     */
    public XsdTreeNode getNode()
    {
        return this.node;
    }

    /**
     * Apply the column widths to a newly created table.
     * @param attributeTable JTable; table.
     */
    public static void applyColumnWidth(final JTable attributeTable)
    {
        for (int i = 0; i < attributeTable.getColumnCount(); i++)
        {
            attributeTable.getColumn(COLUMN_NAMES[i]).setMinWidth(MIN_COLUMN_WIDTHS[i]);
            attributeTable.getColumn(COLUMN_NAMES[i]).setPreferredWidth(PREFERRED_COLUMN_WIDTHS[i]);
        }
    }

}
