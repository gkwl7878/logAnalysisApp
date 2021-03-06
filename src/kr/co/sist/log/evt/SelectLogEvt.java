package kr.co.sist.log.evt;

import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.swing.JOptionPane;

import kr.co.sist.log.view.Result;
import kr.co.sist.log.view.SelectLog;

public class SelectLogEvt implements ActionListener {

	// 1~6을 처리한 내용을 instance 변수에 저장해야 함
	private SelectLog sl;
	private String filePath;
	private Map<String, Integer> mapKey;
	private Map<String, Integer> mapBrowser;
	private Map<String, Integer> mapHour;
	private int code200, code404, code403;
	private int requestNum;
	private int start, end;
	private String code403Share;
	private Map<String, String> mapBrowserShare;
	private String mostFrequentHour;
	private String mostFrequentKey;
	private boolean reportFlag;

	public SelectLogEvt(SelectLog sl) {
		this.sl = sl;
		mapKey = new HashMap<String, Integer>();
		mapBrowser = new HashMap<String, Integer>();
		mapHour = new HashMap<String, Integer>();
		mapBrowserShare = new HashMap<String, String>();
		reportFlag = false;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == sl.getJbView()) {

			selectLog();

			try {
				readLog();

				// readLog로 읽어들인 log의 내용을 가공, instance변수에 저장
				calMostFrequentKey();
				calBrowserShare();
				calCode403Share();
			
				// 결과창
				new Result(this, sl);
				System.out.println("결과창 생성자 호출");

			} catch (FileNotFoundException fnfe) {
				fnfe.printStackTrace();
			} catch (IOException ie) {
				ie.printStackTrace();
			}
		}

		// jbView가 한번 이상 눌렸다면 JOptionPane.showMessageDialog에 붙여 결과 출력
		if (e.getSource() == sl.getJbReport()) {
			// jbView가 한번이상 눌렸을 때 수행되도록 구현(boolean flag로 구현)
			if (reportFlag == true) {
				 // "report 출력"을 Component로 대체해야 함, Component를 반환하는 method 만들 것
                // 파일 출력 FileDialog 구현
				try {
					mkLogReport();
				} catch (IOException e1) {
					e1.printStackTrace();
				}

			} else {
				JOptionPane.showMessageDialog(sl, "Log View를 먼저 수행하여 주세요.");
			}
		}

		if (e.getSource() == sl.getJbLineView()) {
			 // 시작,끝 라인이 입력됐을 때 해당 라인 수를 가져온다
			start = Integer.parseInt(sl.getJtStartLine().getText());
			end = Integer.parseInt(sl.getJtEndLine().getText());

			selectLog();

			System.out.println(start + " " + end);
			try {
				readLog();
				calMostFrequentKey();
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void mkLogReport() throws IOException {

		// report, FileDialog SAVE, 출력하는 method
	}

	public void calMostFrequentKey() {
		// 가장 빈도수 높은 key(mostFrequentKey) 구하는 method
	}

	public void calMostFrequentHour() {
		// 가장 빈도수 높은 시간(mostFrequentHour) 구하는 method 
	}
	
/////////////////////12.22 선의 코드 추가 (2.브라우저의 비율구해서 반환) 시작//////////////////////////////
	public void calBrowserShare() {
		Set<String> set = mapBrowser.keySet();
		Iterator<String> ita = set.iterator();
		Iterator<String> ita2 = set.iterator();

		for (int i = 0; i < browser.length; i++) {
			mapBrowserShare.put(ita2.next(), String.format("%4.2f", ((mapBrowser.get(ita.next()) / (double) requestNum) * 100)));
		}
	}
/////////////////////12.22 선의 코드 추가 (2.브라우저의 비율구해서 반환) 끝//////////////////////////////

	public void calCode403Share() {
		code403Share = String.format("%3.2f", (code403 / (double) requestNum) * 100);
//		System.out.println(" code200의 횟수: "+code200+" code404의 횟수 : "+code404+ 
//				" code403의 횟수: " + code403+ " requestnum: "+requestNum+" code403의 비율은: "+code403Share);
	}

	public void selectLog() {
		 // 읽어들인 log파일의 경로를 저장하는 method
		FileDialog fd = new FileDialog(sl, "log 파일 선택", FileDialog.LOAD);
		fd.setVisible(true);

		String dirPath = fd.getDirectory();
		String fName = fd.getFile();
		filePath = dirPath + fName;
	}

	public void readLog() throws IOException, FileNotFoundException {

		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(filePath));

			String temp = "";
			while ((temp = br.readLine()) != null) {

				requestNum++;
				// 선택된 파일의 내용을 한 줄씩 읽어들임
				// 읽어들이는 내용을 처리하는건 따로 method 만들어서 처리할 것
				if (start == 0 && end == 0) {
					countKey(temp);
					countBrowser(temp);
					countHttpStatusCode(temp);
					countRequestHour(temp);

				} else if (requestNum >= start && requestNum <= end) {
					countKey(temp);

				}

			}

			reportFlag = true;
		} finally {
			if (br != null)
				br.close();
		}
	}
	
//////////////////////12.23 선의 추가 코드(key count 구하기) 시작 ////////////////////////////
	private String[] key = {"mongodb","res","ora","javascript", "java","hadoop","jsp","d8","jk9k","front" };
	private int[] keyCnt = new int[key.length];
	public void countKey(String temp) {
		for(int i=0; i<key.length;i++) {
			if(temp.contains(key[i])) {
				mapKey.put(key[i], keyCnt[i]++);				
			}
		}
//		System.out.println(mapKey);
	}
//////////////////////12.23 선의 추가 코드(key count 구하기) 끝 ////////////////////////////
	

//////////////////////12.22 선의 추가 코드(2. 브라우저,카운터 mapBrowser에 넣기) 시작 ////////////////////////////
	private String[] browser = { "opera", "ie", "firefox", "Chrome", "Safari" };
	private int[] browserCnt = new int[browser.length];

	public void countBrowser(String temp) {
		// 2. 브라우저별 접속 횟수 구하는 method, 비율 구하기(아직)
//		System.out.println(temp);
//		System.out.println("temp :"+temp  );
		for (int i = 0; i < browser.length; i++) {
			if (temp.contains(browser[i])) {
				browserCnt[i]++;
			} // end for
//			count=0;
			mapBrowser.put(browser[i], browserCnt[i]);
		} // end for
//		System.out.println("requestNum = " +requestNum+", "+mapBrowser);
	}// countBrowser
/////////////////////12.22 선의 추가 코드(2. 브라우저,카운터 mapBrowser에 넣기) 끝////////////////////////////

/////////////////////12.23 선의 추가 코드(3.성공횟수, 실패횟수 저장//5.비정상요청의 횟수 저장) 시작////////////////////////////
	public void countHttpStatusCode(String temp) {
		// 3. 서비스를 성공적으로 수행한 횟수, 실패(404) 횟수
		int serviceNum = Integer.parseInt(temp.substring(temp.indexOf("[")+1, temp.indexOf("]")));
		if((serviceNum==200)){
			code200++;
//			System.out.println("성공횟수: "+code200);
		}else if(serviceNum==404) {
			code404++;
//			System.out.println("실패횟수: "+code404);			
		// 6. 비정상적인 요청(403)이 발생한 횟수 구하는 method, 비율 구하기 method는 calBrowserShare()로 구현
		}else if(serviceNum==403) {
			code403++;		
		}
	}
/////////////////////12.23 선의 추가 코드(3.성공횟수, 실패횟수 저장//5.비정상요청의 횟수 저장) 끝////////////////////////////
	
/////////////////////12.23 선의 추가 코드(4.시간횟수) 시작////////////////////////////
	private String[] arrayHour = {"09","10","11","12"};
	private int[] hourCnt = new int[arrayHour.length];
	public void countRequestHour(String temp) {
		// 4. 요청 시간별 횟수를 구하는 method.
		String time = temp.substring(temp.lastIndexOf("[")+1, temp.lastIndexOf("]"));
		String hour = time.substring(time.indexOf(":")-2,time.indexOf(":"));
		
		for(int i =0; i<arrayHour.length;i++) {
			if(hour.equals(arrayHour[i])) {
				hourCnt[i]++;
				mapHour.put(hour, hourCnt[i]);
			}
		}
//		System.out.println("mph: "+mapHour+"/ 전체수: "+requestNum);
	}
/////////////////////12.23 선의 추가 코드(4.시간횟수) 끝////////////////////////////

	public SelectLog getSl() {
		return sl;
	}

	public String getFilePath() {
		return filePath;
	}

	public Map<String, Integer> getMapKey() {
		return mapKey;
	}

	public Map<String, Integer> getMapBrowser() {
		return mapBrowser;
	}

	public Map<String, Integer> getMapHour() {
		return mapHour;
	}

	public int getCode200() {
		return code200;
	}

	public int getCode404() {
		return code404;
	}

	public int getCode403() {
		return code403;
	}

	public int getRequestNum() {
		return requestNum;
	}

	public int getStart() {
		return start;
	}

	public int getEnd() {
		return end;
	}

	public String getCode403Share() {
		return code403Share;
	}

	public Map<String, String> getMapBrowserShare() {
		return mapBrowserShare;
	}

	public String getMostFrequentKey() {
		return mostFrequentKey;
	}

	public boolean isReportFlag() {
		return reportFlag;
	}
}
