//���콺
import java.awt.event.MouseAdapter;
import javax.swing.JButton;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.event.MouseEvent;

public class Mouse extends MouseAdapter {

	JTable table = new JTable();
	
	public Mouse(JTable table) {
		
		this.table = table;
	}

	public void mouseClicked(MouseEvent e) {

		int row = table.getSelectedRow();
		
			if (e.getSource() == table) {
			
				if(e.getClickCount()==2){
					
					String title = table.getValueAt(row, 2).toString();
					String content = table.getValueAt(row, 3).toString();
					
					//row 0�� �˻���ȣ, row 1�� ������ȣ row 2�� ���� ����
					
				System.out.println(content);
				
				//Detail���� �Ѱ��� �� ����
				Detail a = new Detail(title,content);
				a.setSize(500, 500);
				a.initTable();
				a.setResizable(false);
				a.setVisible(true);
				a.setLocation(420, 100);
				}
			}

	}

	

}