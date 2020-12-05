//����
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public class Gui extends JFrame implements ActionListener {
	String filePath;
	JTextField term = new JTextField(13); // �˻���
	Container contentPane;
	JButton b_search = new JButton("�˻�");
	DefaultTableModel model;

	String colNames[] = { "�˻���ȣ", "������ȣ", "��������", "����" };

	static JLabel status = new JLabel("", Label.LEFT);

	static JTable table;

	Gui() {

		this.setTitle("�����϶� 1005 - Hantec2.0 Search");

		DefaultTableModel model = new DefaultTableModel(colNames, 0) {

			// �� �����Ұ�
			public boolean isCellEditable(int row, int col) {
				return false;
			}
		};

		table = new JTable(model);

		table.getColumn("�˻���ȣ").setPreferredWidth(2);
		table.getColumn("������ȣ").setPreferredWidth(5);
		table.getColumn("����").setPreferredWidth(2);
		table.getColumn("��������").setPreferredWidth(100);

		table.getColumn("����").setWidth(0);
		table.getColumn("����").setMinWidth(0);
		table.getColumn("����").setMaxWidth(0);

		table.setPreferredScrollableViewportSize(new Dimension(450, 270)); // ������

		table.getTableHeader().setReorderingAllowed(false); // �� �̵�����
		table.getTableHeader().setResizingAllowed(false); // �� ���������
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // �Ѱ� �ο츸
																		// ���� ����
		// table.setAutoCreateRowSorter(true);

		setBackground(Color.white);
		table.setBackground(Color.white);

		setLayout(new FlowLayout(FlowLayout.CENTER));
		contentPane = getContentPane(); // ��ü �г� ���

		JPanel panel = new JPanel();
		panel.add(new JLabel("KoreaStem Analyzer"));
		term.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (term.getText().length() != 0) {
					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						b_search.doClick();
					}
				}
			}
		});
		panel.add(term);
		
		panel.add(b_search);

		JPanel panel2 = new JPanel();
		panel2.setLayout(new GridLayout(0, 1));
		status.setForeground(Color.gray);
		panel2.add(status);
		panel2.add(new JLabel("Copyright �� �����϶� 1005"));

		contentPane.add(panel, BorderLayout.NORTH);
		contentPane.add(new JScrollPane(table), BorderLayout.CENTER);
		contentPane.add(panel2, BorderLayout.SOUTH);

		contentPane.setBackground(Color.white);
		b_search.setBackground(Color.white);
		panel.setBackground(Color.white);
		panel2.setBackground(Color.white);

		table.addMouseListener(new Mouse(table));
		b_search.addActionListener(this);
	}

	public Gui(String filepate) {
		this();
		this.filePath = filepate;
	}

	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == b_search) {

			if (term.getText().length() != 0) {
				String text = term.getText().trim();

				try {

					Searcher sc = new Searcher(filePath, term.getText());
					model.setRowCount(0);
					Iterator<String[]> it = sc.vc.iterator();
					int i = 0;

					while (it.hasNext()) {
						model.addRow(it.next());
						i++;
					}
					status.setText(i + "���� �˻��Ǿ����ϴ�.");
				} catch (Exception e1) {

					e1.printStackTrace();
				}

			}

		}
	}

	public void initTable() {

		model = (DefaultTableModel) table.getModel();

	}

	public static void main(String[] args) {

		Gui a = new Gui(args[0]);
		a.setSize(500, 415);
		a.initTable();
		a.setResizable(false);
		a.setVisible(true);
		a.setLocation(420, 150);

	}

}
