//��â
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.color.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.JScrollPane;

import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.BadLocationException;

public class Detail extends JFrame implements ActionListener{

	Container contentPane;
	JButton b_Close = new JButton("�ݱ�");
	String title;
	String content;
	SimpleAttributeSet attribute;
	JScrollPane scroll;
	
	Detail(String title, String content) {
		
		this.setTitle(title+"�� �˻� ����");
		this.title=title;
		this.content=content;
		
		contentPane = getContentPane(); // ��ü �г� ���
		
		StyleContext context = new StyleContext();
		StyledDocument document = new DefaultStyledDocument(context);
		Style style = context.getStyle(StyleContext.DEFAULT_STYLE); //������ ���� �⺻ ���� ����
		
		JTextPane text = new JTextPane(document);
		text.setEditable(false); //â�� ���� �� �� ����.
		
		scroll = new JScrollPane(text);
		
		
		StyleConstants.setAlignment(style, StyleConstants.ALIGN_JUSTIFIED); //���� ��ġ ����
		StyleConstants.setFontSize(style, 15);
		StyleConstants.setForeground(style, Color.black); //���� Į�� ����
		//StyleConstants.setBackground(style, Color.BLUE);
		
		StyleConstants.setFirstLineIndent(style, 10);
		StyleConstants.setLeftIndent(style,5);
		StyleConstants.setRightIndent(style, 5);
		StyleConstants.setSpaceAbove(style, 5);
		StyleConstants.setSpaceBelow(style, 5);
		
		try{
			
			document.insertString(0, content, style);
			//����
			
		}catch(Exception e){}
		
		
		contentPane.add(scroll,BorderLayout.CENTER);	
		contentPane.add(b_Close,BorderLayout.SOUTH);
		scroll.getVerticalScrollBar().setValue(0);
		text.setCaretPosition(0);
		text.requestFocus();
		b_Close.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == b_Close) {
			this.dispose();
		}

	}

	public void initTable() {
	
	}

	
	
	public static void main(String[] args) {

	
	}

}
