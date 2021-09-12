package View;


import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class BoardRenderer extends JLabel implements TableCellRenderer
{

    public BoardRenderer()
    {
        super.setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                   boolean hasFocus, int row, int column)
    {
        if(value == null) {
            super.setBackground(Color.YELLOW);
        }


        return this;
    }

}