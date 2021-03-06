package kr.co.sist.log.view;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import kr.co.sist.log.evt.SelectLogEvt;

@SuppressWarnings("serial")
public class SelectLog extends JDialog {

	private JButton jbView;
	private JButton jbReport;
	private JButton jbLineView;
	private JTextField jtStartLine;
	private JTextField jtEndLine;
	
	public SelectLog(JFrame login) {
		super(login, "Log Analysis App");
		
		JLabel jlStart = new JLabel("시작");
		JLabel jlEnd = new JLabel("끝");
		jbView = new JButton("View");
		jbReport = new JButton("Report");
		jbLineView = new JButton("LineLog 정보");
		jtStartLine = new JTextField();
		jtEndLine = new JTextField();
		
		setLayout(null);
		
		jbView.setBounds(40, 30, 150, 50);
		add(jbView);
		jbReport.setBounds(220, 30, 150, 50);
		add(jbReport);
		jbLineView.setBounds(260, 100, 110, 70);
		add(jbLineView);
		jtStartLine.setBounds(95, 100, 150, 30);
		add(jtStartLine);
		jtEndLine.setBounds(95, 140, 150, 30);
		add(jtEndLine);
		jlStart.setBounds(55,100,50,30);
		add(jlStart);
		jlEnd.setBounds(55,140,50,30);
		add(jlEnd);
		
		SelectLogEvt sde = new SelectLogEvt(this);
		jbView.addActionListener(sde);
		jbReport.addActionListener(sde);
		jbLineView.addActionListener(sde);
		
		setBounds(400, 300, 430, 240);
		setResizable(false);
		setVisible(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	}

	public JButton getJbView() {
		return jbView;
	}
	public JButton getJbReport() {
		return jbReport;
	}
	public JButton getJbLineView() {
		return jbLineView;
	}
	public JTextField getJtStartLine() {
		return jtStartLine;
	}
	public JTextField getJtEndLine() {
		return jtEndLine;
	}
}
