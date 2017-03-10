package client;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.swing.*;
import javax.swing.border.TitledBorder;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.regex.*;

public class AdminIndex {
	private int port  = 9999;
	private ServerSocket server;
	private Socket ss;
	private Socket client;
	private PrintWriter out;
	private BufferedReader in ;
	
	private JFrame frame = new JFrame("管理系统");
	private JPanel titlePanel = new JPanel();
	private JPanel titlePanel2 = new JPanel();
	private JPanel searchPanel = new JPanel();
	//main
	private JLabel mainHotSoldLabel= new JLabel();
	//card layout
	private CardLayout card = new CardLayout(); 
	private JPanel panelMain = new JPanel();
	private JPanel panelOne = new JPanel();
	private JScrollPane panelTwo = new JScrollPane();
	private JPanel panelInfo = new JPanel();
	private JPanel panelAddBook = new JPanel();
	private JScrollPane panelOrder = new JScrollPane();
	private JScrollBar scrollBar = new JScrollBar();
	
	private Container c = frame.getContentPane();
	private JTextField searchField = new JTextField(30);
	private JButton searchButton = new JButton("搜索");
	private JButton mainButton = new JButton("主  页");
	private JButton searchMemberButton = new JButton("查询用户");
	private JButton manageBookButton = new JButton("添加图书");
	private JButton manageOrderButton = new JButton("处理订单");
	
	//data
	private String[] bookurl =new String[6];
	private String[] bookisbn =new String[6];
	private String[] bookname =new String[6];
	private String userid;
	
	public AdminIndex(String id) {
		frame.setSize(700, 600);
		c.setLayout(new BorderLayout());
		userid = id;
		initConnection();
		initFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.addWindowListener(new myWindowAdapter(){//监听关闭事件
			public void windowClosing(WindowEvent e) {  
				super.windowClosing(e);  
				//向服务器报告退出登录
				try{
					//向服务器发送请求
					client = new Socket("127.0.0.1",port);
					out = new PrintWriter(client.getOutputStream());
					 
					OutputStream os = client.getOutputStream();
					OutputStreamWriter osw=new OutputStreamWriter(os);
					out = new PrintWriter(osw,true);
					 
					//send messages to server
					out.println("0");
					out.println("a");
					out.flush();	 
					
					//get feedback from server
					InputStreamReader sr=new InputStreamReader(client.getInputStream());
					in=new BufferedReader(sr);
					if(in.readLine().equals("ok")){
						System.exit(0);
					}
				}catch(IOException error){
					error.getStackTrace();
				}
			}  
		});
	}
	
	private void initConnection(){
		try{
			//向服务器发送请求
			client = new Socket("127.0.0.1",port);
			out = new PrintWriter(client.getOutputStream());
			 
			OutputStream os = client.getOutputStream();
			OutputStreamWriter osw=new OutputStreamWriter(os);
			out = new PrintWriter(osw,true);
			 
			//send messages to server
			out.println("19");
			out.flush();
			 
			//get feedback from server
			InputStreamReader sr=new InputStreamReader(client.getInputStream());
			in=new BufferedReader(sr);
			for(int i=0;i<6;i++){
				bookisbn[i]=in.readLine();
				bookname[i]=in.readLine();
				bookurl[i]=in.readLine();
			}
		}catch(Exception error){
			System.out.println( error);
		} 
	}

	private void initFrame() {

		//居中显示
		int windowWidth = frame.getWidth(); // 获得窗口宽
		int windowHeight = frame.getHeight(); // 获得窗口高
		Toolkit kit = Toolkit.getDefaultToolkit(); // 定义工具包
		Dimension screenSize = kit.getScreenSize(); // 获取屏幕的尺寸
		int screenWidth = screenSize.width; // 获取屏幕的宽
		int screenHeight = screenSize.height; // 获取屏幕的高
		frame.setLocation(screenWidth / 2 - windowWidth / 2, screenHeight / 2 - windowHeight / 2);// 设置窗口居中显示
		frame.setResizable(false);

		// 顶部
		titlePanel.setLayout(new GridLayout(0,1));
		titlePanel2.setLayout(new FlowLayout());
		searchPanel.setLayout(new FlowLayout());
		
		searchField.setFont(new Font (Font.DIALOG, Font.BOLD, 20));
		searchField.setBounds(200, 50, 180, 30);
		JLabel a = new JLabel("管理员系统");
		a.setFont(new Font("SanSerif",Font.BOLD,30));
		JLabel a1 = new JLabel("搜索书籍:");
		a1.setFont(new Font (Font.DIALOG, Font.BOLD, 20));
		searchPanel.add(a1);
		searchPanel.add(searchField);
		searchPanel.add(searchButton);
		titlePanel2.add(a);
		titlePanel.add(titlePanel2);
		titlePanel.add(searchPanel);
		c.add(titlePanel, "North");
		
		// 中部表单
		panelMain.setLayout(card);
		panelOne.setLayout(null); 
        //热销书
		JLabel mainlabel1 = new JLabel("今日销量最高 ");
		mainlabel1.setFont(new Font("SanSerif",Font.BOLD,30));
		mainlabel1.setBounds(250,20,400,30);
		
		mainHotSoldLabel.setText(bookname[0]);
		mainHotSoldLabel.setFont(new Font("SanSerif",Font.BOLD,30));
		mainHotSoldLabel.setBounds(280,50,400,30);
		ImagePane book1= new ImagePane(bookurl[0],bookname[0],bookisbn[0]);
		book1.setBounds(250, 90, 200, 200);
		panelOne.add(mainlabel1);
		panelOne.add(mainHotSoldLabel);
		panelOne.add(book1);
		
		//
		panelMain.add("first panel",panelOne); 
		
		c.add(panelMain, "Center");
		

		// 底部按钮
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		mainButton.setPreferredSize(new Dimension(150,40));
		mainButton.setFont(new Font (Font.DIALOG, Font.BOLD, 20));
		searchMemberButton.setPreferredSize(new Dimension(150,40));
		searchMemberButton.setFont(new Font (Font.DIALOG, Font.BOLD, 20));
		manageBookButton.setPreferredSize(new Dimension(150,40));
		manageBookButton.setFont(new Font (Font.DIALOG, Font.BOLD, 20));
		manageOrderButton.setPreferredSize(new Dimension(150,40));
		manageOrderButton.setFont(new Font (Font.DIALOG, Font.BOLD, 20));
		buttonPanel.add(mainButton);
		buttonPanel.add(searchMemberButton);
		buttonPanel.add(manageBookButton);
		buttonPanel.add(manageOrderButton);
		c.add(buttonPanel, "South");
		//事件
		searchButton.addActionListener(new myActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Search(searchField.getText());
			}
		});
		mainButton.addActionListener(new myActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				card.show(panelMain, "first panel");
			}
		});

		searchMemberButton.addActionListener(new myActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ChangeSearchInfoPage();
			}
		});

		manageBookButton.addActionListener(new myActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ChangeAddBookPage();
			}
		});

		manageOrderButton.addActionListener(new myActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ChangeOrderPage();
			}
		});
		
	}

	private class myActionListener implements ActionListener{
		public void actionPerformed(ActionEvent arg0){
			
		}
    }
	
	private class myWindowAdapter extends WindowAdapter{
		public void windowClosing(WindowEvent e) {  
 
			 }  
    }
	
	private void Search(String s){
		String searchContent = s;
		
		String []searchBookUrl;
		String []searchBookIsbn;
		String []searchBookName;
		String []searchBookAuthor;
		String []searchBookPrice;
		int num = 0;
		//此处查询,num为语句条数
		try{
			//向服务器发送请求
			client = new Socket("127.0.0.1",port);
			out = new PrintWriter(client.getOutputStream());
			 
			OutputStream os = client.getOutputStream();
			OutputStreamWriter osw=new OutputStreamWriter(os);
			out = new PrintWriter(osw,true);
			 
			//send messages to server
			out.println("10");
			out.println(searchContent);
			out.flush();
			 
			//get feedback from server
			InputStreamReader sr=new InputStreamReader(client.getInputStream());
			in=new BufferedReader(sr);
			num=Integer.parseInt(in.readLine());
			//System.out.println(num);
			if(num==0){//没有该书
				JOptionPane.showMessageDialog(null, "对不起，未找到此书。","提示",JOptionPane.WARNING_MESSAGE);
			}else{		
				searchBookIsbn = new String[num];
				searchBookName = new String[num];
				searchBookAuthor = new String[num];
				searchBookPrice = new String[num];
				searchBookUrl = new String[num];
				
				for(int i =0;i<num;i++){
					searchBookIsbn[i]=in.readLine();
					searchBookName[i]=in.readLine();
					searchBookAuthor[i]=in.readLine();
					searchBookPrice[i]=in.readLine();
					searchBookUrl[i]=in.readLine();
				}

				JPanel bookpanelSum = new JPanel();
		        bookpanelSum.setLayout(new BoxLayout(bookpanelSum, BoxLayout.Y_AXIS));
                SearchBookPanel [] bookarray = new SearchBookPanel[num];
				
				for(int i = 0;i<num;i++){
					bookarray[i] = new SearchBookPanel(searchBookUrl[i],searchBookIsbn[i],"《"+searchBookName[i]+"》",searchBookAuthor[i],searchBookPrice[i]);
			        bookpanelSum.add(bookarray[i]);
				}
				panelTwo = new JScrollPane(bookpanelSum);
		        panelTwo.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		        
				panelMain.add("second panel",panelTwo); 		
				card.show(panelMain,"second panel");
			}
		}catch(Exception error){
			System.out.println( error);
		}
		
	}
	
	private void ChangeAddBookPage(){
//		JTextField newBookISBNField = new JTextField();
//		JTextField newBookNameField = new JTextField();
//		JTextField newBookAuthorField = new JTextField();
//		JTextField newBookPublishField = new JTextField();
//		JTextField newBookPriceField = new JTextField();
//		JTextField newBookRemainField = new JTextField();
//		JTextField newBookDescriptionField = new JTextField();
//		JTextField newBookImageUrlField = new JTextField();
		String isbn26 = "";
		int isbn = 0;
		try{
			//向服务器发送请求
			client = new Socket("127.0.0.1",port);
			out = new PrintWriter(client.getOutputStream());
			 
			OutputStream os = client.getOutputStream();
			OutputStreamWriter osw=new OutputStreamWriter(os);
			out = new PrintWriter(osw,true);
			 
			//send messages to server
			out.println("26");
			out.flush();
			 
			//get feedback from server
			InputStreamReader sr=new InputStreamReader(client.getInputStream());
			in=new BufferedReader(sr);
			isbn26=in.readLine();
			if(isbn26.equals("failed")){
				JOptionPane.showMessageDialog(null, "获取书本isbn号失败 ", "提示", JOptionPane.ERROR_MESSAGE);
			}else{
				isbn = Integer.parseInt(isbn26);
				isbn++;
				isbn26 = ""+isbn;
				int l=13-isbn26.length();
				for(int i =0;i<l;i++)
					isbn26 = "0"+isbn26;
			}
		}catch(IOException error){
			error.getStackTrace();
		}
		
		JButton okButton = new JButton("确认");
		okButton.setFont(new Font (Font.DIALOG, Font.BOLD, 20));
		okButton.setBounds(250, 350, 200, 40);
		panelAddBook.setLayout(null);
		//初始化
		String[] newBookArray = new String[8];
		JTextField[] textFieldArray = new JTextField[8];
		JLabel[] labelArray = new JLabel[8];
		
		for(int i = 0;i<8;i++){
			labelArray[i] = new JLabel();
			labelArray[i].setFont(new Font (Font.DIALOG, Font.BOLD, 30));
			labelArray[i].setBounds(120, 10+i*40, 150, 30);
			textFieldArray[i] = new JTextField();
			textFieldArray[i] .setFont(new Font (Font.DIALOG, Font.BOLD, 24));
			textFieldArray[i].setBounds(270, 10+i*40, 250, 30);
			newBookArray[i] = "";
			panelAddBook.add(labelArray[i]);
			panelAddBook.add(textFieldArray[i]);
		}

		labelArray[0].setText("ISBN号:");	
		labelArray[1].setText("书  名:");
		labelArray[2].setText("作  者:");
		labelArray[3].setText("出版社:");
		labelArray[4].setText("价  格:");
		labelArray[5].setText("库  存:");
		labelArray[6].setText("介  绍:");
		labelArray[7].setText("图片路径:");
		
		textFieldArray[0].setText(isbn26);
		textFieldArray[0].setEditable(false);
		panelAddBook.add(okButton);
		panelMain.add("addbook panel",panelAddBook); 		
		card.show(panelMain,"addbook panel");
		
		
		okButton.addActionListener(new myActionListener(){
			@Override
            public void actionPerformed(ActionEvent e) {
				Object[] options = {"确定","取消"};
				int response=JOptionPane.showOptionDialog(null, "您确定要添加此书吗？","Tips:",JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
				if(response==0){
					Boolean bl = true;
					
					for(int i = 0;i<8;i++){
						newBookArray[i] = textFieldArray[i].getText();
					}
					//正则ISBN
					Pattern pt = Pattern.compile("^[0-9]+$");
					Matcher mt = pt.matcher(newBookArray[0]);
					if (!(mt.matches() && (newBookArray[0].length() == 13))) {
						JOptionPane.showMessageDialog(null, "ISBN必须为13位数字", "错误 ", JOptionPane.ERROR_MESSAGE);
						bl = false;
					}
					//Name
					if (!(newBookArray[1].length() <= 50)) {
						JOptionPane.showMessageDialog(null, "书名不规范或过长", "错误 ", JOptionPane.ERROR_MESSAGE);
						bl = false;
					}
					//Author
					if (!(newBookArray[2].length() <= 50)) {
						JOptionPane.showMessageDialog(null, "图书作者不规范或过长", "错误 ", JOptionPane.ERROR_MESSAGE);
						bl = false;
					}
					//Publish
					if (!(newBookArray[3].length() <= 50)) {
						JOptionPane.showMessageDialog(null, "图书出版社不规范或过长", "错误 ", JOptionPane.ERROR_MESSAGE);
						bl = false;
					}
					//Price
//					mt = pt.matcher(newBookArray[4]);
					if (!((newBookArray[4].length() <= 10)&&(Double.parseDouble(newBookArray[4])>=0))) {
						JOptionPane.showMessageDialog(null, "图书价格有误", "错误 ", JOptionPane.ERROR_MESSAGE);
						bl = false;
					}
					//remain
					mt = pt.matcher(newBookArray[5]);
					if (!(mt.matches() && (newBookArray[5].length() <= 10))) {
						JOptionPane.showMessageDialog(null, "库存有误", "错误 ", JOptionPane.ERROR_MESSAGE);
						bl = false;
					}
					//Description
					if (!(newBookArray[6].length() <= 255)) {
						JOptionPane.showMessageDialog(null, "图书介绍不规范或过长", "错误 ", JOptionPane.ERROR_MESSAGE);
						bl = false;
					}
					//url
					if (!(newBookArray[7].length() <= 255)) {
						JOptionPane.showMessageDialog(null, "图书图片路径不规范或过长", "错误 ", JOptionPane.ERROR_MESSAGE);
						bl = false;
					}
					if(bl){
						try{
							//向服务器发送请求
							client = new Socket("127.0.0.1",port);
							out = new PrintWriter(client.getOutputStream());
							 
							OutputStream os = client.getOutputStream();
							OutputStreamWriter osw=new OutputStreamWriter(os);
							out = new PrintWriter(osw,true);
							 
							//send messages to server
							out.println("27");
							for(int i = 0;i<8;i++)
								out.println(newBookArray[i]);
							out.flush();
							 
							//get feedback from server
							InputStreamReader sr=new InputStreamReader(client.getInputStream());
							in=new BufferedReader(sr);
//							String temp = in.readLine();
//							System.out.println(temp);
							if(in.readLine().equals("success")){
								JOptionPane.showMessageDialog(null, "添加成功");
								card.show(panelMain,"first panel");
							}else{
								JOptionPane.showMessageDialog(null, "添加失败 ", "Tips: ", JOptionPane.ERROR_MESSAGE);
							}

						}catch(Exception error){
							System.out.println( error);
						}	
					}
					
				}
            }
		});

	}
	
	private void ChangeSearchInfoPage(){
		String memberAccount="";
		String memberNickname="";
		String memberPhone="";
		String memberAddress="";
		//用id号查询个人信息
		String inputValue = JOptionPane.showInputDialog(null, "请输入要查询的用户ID号");
		Pattern pt = Pattern.compile("^[0-9]+$");
		Matcher mt = pt.matcher(inputValue);
		if(mt.matches() && inputValue.length()<=11)
		{
			try{
				//send messages to server
				//向服务器发送请求
				client = new Socket("127.0.0.1",port);
				out = new PrintWriter(client.getOutputStream());
				 
				OutputStream os = client.getOutputStream();
				OutputStreamWriter osw=new OutputStreamWriter(os);
				out = new PrintWriter(osw,true);
				out.println("22");
				out.println(inputValue);
				
				//get feedback from server
				InputStreamReader sr=new InputStreamReader(client.getInputStream());
				in=new BufferedReader(sr);
				if(in.readLine().equals("success")){
					memberAccount= in.readLine();
					memberNickname= in.readLine();
					memberPhone= in.readLine();
					memberAddress= in.readLine();
					JLabel a1 = new JLabel("账  号: "+memberAccount);
					a1.setFont(new Font (Font.DIALOG, Font.BOLD, 30));
					a1.setBounds(200,50,400,30);
					JLabel a2 = new JLabel("昵  称: "+memberNickname);
					a2.setFont(new Font (Font.DIALOG, Font.BOLD, 30));
					a2.setBounds(200, 100, 400, 35);
					JLabel a3 = new JLabel("电  话: "+memberPhone);
					a3.setFont(new Font (Font.DIALOG, Font.BOLD, 30));
					a3.setBounds(200, 150, 400, 35);
					JLabel a4 = new JLabel("地  址: "+memberAddress);
					a4.setFont(new Font (Font.DIALOG, Font.BOLD, 30));
					a4.setBounds(200, 200, 400, 35);

					
					panelInfo.add(a1);
					panelInfo.add(a2);
					panelInfo.add(a3);
					panelInfo.add(a4);

			        panelInfo.setLayout(null); 
			        panelMain.add("info panel", panelInfo);
					card.show(panelMain, "info panel");
					
				}else{
					JOptionPane.showMessageDialog(null, "没有此用户", "错误 ", JOptionPane.ERROR_MESSAGE);
				}
			}catch (IOException error){
				System.out.println(error);
			}
		}
		
	}
	
	private void ChangeOrderPage(){
		String []orderBookUrl;
		String []orderBookNum;
		String []orderBookSum;
		String []orderID;
		String []orderState;
		String []orderDate;
		boolean existorder = false;
		int num = 4;
		//此处查询,num为语句条数
		existorder = true;
		try{
			//向服务器发送请求
			client = new Socket("127.0.0.1",port);
			out = new PrintWriter(client.getOutputStream());
			 
			OutputStream os = client.getOutputStream();
			OutputStreamWriter osw=new OutputStreamWriter(os);
			out = new PrintWriter(osw,true);
			 
			//send messages to server
			out.println("25");
			out.flush();
			 
			//get feedback from server
			InputStreamReader sr=new InputStreamReader(client.getInputStream());
			in=new BufferedReader(sr);
			num=Integer.parseInt(in.readLine());
			//System.out.println(num);
			
			if(num==0){//没有记录
				Object[] options = {"确定","取消"};
				int response=JOptionPane.showOptionDialog(null, "还没有客户下单","Tips:",JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
				if(response==0) {         
					 card.show(panelMain,"first panel");
				} else if(response==1) {           
					 card.show(panelMain,"first panel");
				} 
			}else{		
				orderBookUrl = new String[num];
				orderBookNum = new String[num];
				orderBookSum = new String[num];
				orderState = new String[num];
				orderID = new String[num];
				orderDate = new String[num];
				for(int i =0;i<num;i++){
					orderID[i]=in.readLine();
					orderBookNum[i]=in.readLine();
					orderDate[i]=in.readLine();
					orderBookSum[i]=in.readLine();
					orderState[i]=in.readLine();
					orderBookUrl[i]=in.readLine();
				}
				JPanel bookpanelSum = new JPanel();
		        bookpanelSum.setLayout(new BoxLayout(bookpanelSum, BoxLayout.Y_AXIS));
		        
				OrderPanel [] bookarray = new OrderPanel[num];
				
				for(int i = 0;i<num;i++){
					bookarray[i] = new OrderPanel(orderBookUrl[i],orderID[i],orderBookNum[i],orderBookSum[i],orderState[i],orderDate[i]);
			        bookpanelSum.add(bookarray[i]);
				}
				panelOrder = new JScrollPane(bookpanelSum);
				panelOrder.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		        
				panelMain.add("order panel",panelOrder); 
				card.show(panelMain,"order panel");
			}
		}catch(Exception error){
			System.out.println( error);
		}
		
	}

	public void RefreshInfoPage(JLabel l,String s){
		l.setText(s);
	}
	
	public class ImagePane extends JPanel{
	    public ImagePane(String url,String bookname,String isbn) 
	    { 
	          this.setLayout(null);
	    	  ImageIcon img = new ImageIcon(url); 
	    	  img.setImage(img.getImage().getScaledInstance(180, 180,Image.SCALE_DEFAULT));
	          JLabel l1 = new JLabel(img); 
	          l1.setBorder(BorderFactory.createEtchedBorder());
	          //l1.setBounds(new Rectangle(10,10,300,200));//设置jpanel的左边距、上边距、长度、高度，在jp没设置setLayout(null);是无效的
	          l1.setBackground(Color.GRAY);
	          l1.setSize(200, 200);
	          l1.addMouseListener(new MouseAdapter(){
	        	  public void mouseClicked(MouseEvent e){
	        		  //System.out.println(isbn);
	        		  new AdminBookPage(userid,isbn);
	  			  }
	          });
	          JLabel l2 = new JLabel(bookname); 
	          this.add(l1);
	          //this.add(l2);
	      }
	}
	
	public class ImagePane2 extends JPanel{
	    public ImagePane2(String url,int width,int height) 
	    { 
	          this.setLayout(null);
	    	  ImageIcon img = new ImageIcon(url); 
	    	  img.setImage(img.getImage().getScaledInstance(width, height,Image.SCALE_DEFAULT));
	          JLabel l1 = new JLabel(img); 
	          l1.setBorder(BorderFactory.createEtchedBorder());
	          l1.setBackground(Color.GRAY);
	          l1.setSize(width+10, height+10);
	          this.add(l1);
	      }
	}
	
	public class SearchBookPanel extends JPanel{
		public SearchBookPanel(String url,String bookisbn,String bookname,String bookauthor,String bookprice){
			this.setLayout(new GridLayout(0,3));
			
			JPanel leftPanel = new JPanel();
			leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
			JPanel midPanel = new JPanel();
			midPanel.setLayout(new BoxLayout(midPanel, BoxLayout.Y_AXIS));
			JPanel rightPanel = new JPanel();
			rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		
			ImagePane2 bookImg = new ImagePane2(url,100,100);
			leftPanel.add(bookImg);
			JLabel l1 = new JLabel("书  名:"+bookname);
			l1.setFont(new Font (Font.DIALOG, Font.BOLD, 20));
			JLabel l2 = new JLabel("作  者:"+bookauthor);
			l2.setFont(new Font (Font.DIALOG, Font.BOLD, 20));
			midPanel.add(l1);
			midPanel.add(l2);
			JLabel l3 = new JLabel("价  格:"+bookprice+"¥");
			l3.setFont(new Font (Font.DIALOG, Font.BOLD, 20));
			JButton b1 = new JButton("查看详情");
			b1.setFont(new Font (Font.DIALOG, Font.BOLD, 20));
			rightPanel.add(l3);
			rightPanel.add(b1);

			bookImg.addMouseListener(new MouseAdapter(){
	        	  public void mouseClicked(MouseEvent e){
	        		  new AdminBookPage(userid,bookisbn);
	  			  }
	          });
			b1.addActionListener(new myActionListener(){
				@Override
	            public void actionPerformed(ActionEvent e) {
					 new AdminBookPage(userid,bookisbn);
	            }
			});
			
			this.add(leftPanel);
			this.add(midPanel);
			this.add(rightPanel);
		}
	}
	
	public class OrderPanel extends JPanel{
		public OrderPanel(String url,String orderid,String ordernum,String ordersum,String orderstate,String orderdate){
			this.setLayout(new GridLayout(0,3));
			
			JPanel leftPanel = new JPanel();
			leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
			JPanel midPanel = new JPanel();
			midPanel.setLayout(new BoxLayout(midPanel, BoxLayout.Y_AXIS));
			JPanel rightPanel = new JPanel();
			rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		
			ImagePane2 bookImg = new ImagePane2(url,100,100);
			leftPanel.add(bookImg);
			JLabel l1 = new JLabel("订单号:"+orderid);
			l1.setFont(new Font (Font.DIALOG, Font.BOLD, 20));
			JLabel l2 = new JLabel("数   量:"+ordernum);
			l2.setFont(new Font (Font.DIALOG, Font.BOLD, 20));
			JLabel l3 = new JLabel("总   价:"+ordersum+" ¥");
			l3.setFont(new Font (Font.DIALOG, Font.BOLD, 20));
			JLabel l4 = new JLabel("订 单 状 态:"+orderstate);
			l4.setFont(new Font (Font.DIALOG, Font.BOLD, 20));
			JLabel l5 = new JLabel("日   期:"+orderdate);
			l5.setFont(new Font (Font.DIALOG, Font.BOLD, 20));
			midPanel.add(l1);
			midPanel.add(l2);
			midPanel.add(l3);
//			midPanel.add(l5);
			midPanel.add(l4);

			JButton b1 = new JButton("确 认 订 单");
			b1.setFont(new Font (Font.DIALOG, Font.BOLD, 20));
			JButton b2 = new JButton("完 成 交 易");
			b2.setFont(new Font (Font.DIALOG, Font.BOLD, 20));			
			rightPanel.add(l5);
			if(orderstate.equals("未处理"))
				rightPanel.add(b1);
			rightPanel.add(b2);

			b1.addActionListener(new myActionListener(){
				@Override
	            public void actionPerformed(ActionEvent e) {
					if(orderstate.equals("未处理")){
						Object[] options = {"确定","取消"};
						int response=JOptionPane.showOptionDialog(null, "您确定要发货么？","Tips:",JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
						if(response==0) {         
							try{
								//向服务器发送请求
								client = new Socket("127.0.0.1",port);
								out = new PrintWriter(client.getOutputStream());
								 
								OutputStream os = client.getOutputStream();
								OutputStreamWriter osw=new OutputStreamWriter(os);
								out = new PrintWriter(osw,true);
								 
								//send messages to server
								out.println("23");
								out.println(orderid);
								out.flush();
								 
								//get feedback from server
								InputStreamReader sr=new InputStreamReader(client.getInputStream());
								in=new BufferedReader(sr);
								if(in.readLine().equals("succeed")){
									JOptionPane.showMessageDialog(null, "发货成功");
									l4.setText("订 单 状 态:已处理");
									rightPanel.remove(b1);
								}
							}catch(Exception error){
								System.out.println( error);
							}
						}
					}else{
						JOptionPane.showMessageDialog(null, "商品以发货，无法取消", "提示 ", JOptionPane.ERROR_MESSAGE);

					}
					
	            }
			});
			
			b2.addActionListener(new myActionListener(){
				@Override
	            public void actionPerformed(ActionEvent e) {
					if(l4.getText().equals("订 单 状 态:已处理")){
						Object[] options = {"确定","取消"};
						int response=JOptionPane.showOptionDialog(null, "您确定要完成交易吗？","Tips:",JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
						if(response==0) {         
							try{
								//向服务器发送请求
								client = new Socket("127.0.0.1",port);
								out = new PrintWriter(client.getOutputStream());
								 
								OutputStream os = client.getOutputStream();
								OutputStreamWriter osw=new OutputStreamWriter(os);
								out = new PrintWriter(osw,true);
								 
								//send messages to server
								out.println("24");
								out.println(orderid);
								out.flush();
								 
								//get feedback from server
								InputStreamReader sr=new InputStreamReader(client.getInputStream());
								in=new BufferedReader(sr);
								if(in.readLine().equals("delete succeed")){
									JOptionPane.showMessageDialog(null, "确认交易成功");
								}
								ChangeOrderPage();
							}catch(Exception error){
								System.out.println( error);
							}
						}
					}else{
						JOptionPane.showMessageDialog(null, "商品还未发货，无法完成交易", "提示 ", JOptionPane.ERROR_MESSAGE);
					}
					
	            }
			});
			
			this.add(leftPanel);
			this.add(midPanel);
			this.add(rightPanel);

		}
	}
}
