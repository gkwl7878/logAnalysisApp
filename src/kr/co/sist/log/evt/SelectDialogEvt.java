package kr.co.sist.log.evt;

import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import kr.co.sist.log.view.SelectDialog;

public class SelectDialogEvt implements ActionListener {

	private SelectDialog sd;
	private String logFilePath;
	// 1~6�� ó���� ������ instance ������ �����ؾ� ��
	// ���� �� ����� �����Ͽ� JOptionPane.showMessageDialog�� �ٿ� ���
	
	public SelectDialogEvt(SelectDialog sd) {
		this.sd = sd;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == sd.getJbView()) {
			
			selectFile();
			
		}
		
		if (e.getSource() == sd.getJbReport()) {
			// jbView�� �ѹ��̻� ������ �� ����ǵ��� ����
		}
	}
	
	public void selectFile() {
		
		FileDialog fd = new FileDialog(sd, "log ���� ����", FileDialog.LOAD);
		
		fd.setVisible(true);
		
		String directoryPath = fd.getDirectory();
		String fileName = fd.getName();
		logFilePath = directoryPath+fileName;
		
	}
}
