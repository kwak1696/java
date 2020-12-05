//메인
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
	JTextField term = new JTextField(13); // 검색어
	Container contentPane;
	JButton b_search = new JButton("검색");
	DefaultTableModel model;

	String colNames[] = { "검색번호", "문서번호", "문서제목", "내용" };

	static JLabel status = new JLabel("", Label.LEFT);

	static JTable table;

	Gui() {

		this.setTitle("응답하라 1005 - Hantec2.0 Search");

		DefaultTableModel model = new DefaultTableModel(colNames, 0) {

			// 셀 수정불가
			public boolean isCellEditable(int row, int col) {
				return false;
			}
		};

		table = new JTable(model);

		table.getColumn("검색번호").setPreferredWidth(2);
		table.getColumn("문서번호").setPreferredWidth(5);
		table.getColumn("내용").setPreferredWidth(2);
		table.getColumn("문서제목").setPreferredWidth(100);

		table.getColumn("내용").setWidth(0);
		table.getColumn("내용").setMinWidth(0);
		table.getColumn("내용").setMaxWidth(0);

		table.setPreferredScrollableViewportSize(new Dimension(450, 270)); // 사이즈

		table.getTableHeader().setReorderingAllowed(false); // 열 이동고정
		table.getTableHeader().setResizingAllowed(false); // 열 사이즈고정
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // 한개 로우만
																		// 선택 가능
		// table.setAutoCreateRowSorter(true);

		setBackground(Color.white);
		table.setBackground(Color.white);

		setLayout(new FlowLayout(FlowLayout.CENTER));
		contentPane = getContentPane(); // 전체 패널 얻기

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
		panel2.add(new JLabel("Copyright ⓒ 응답하라 1005"));

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
					status.setText(i + "건이 검색되었습니다.");
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
