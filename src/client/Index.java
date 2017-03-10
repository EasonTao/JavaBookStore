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

public class Index {
	private int port  = 9999;
	private ServerSocket server;
	private Socket ss;
	private Socket client;
	private PrintWriter out;
	private BufferedReader in ;
	
	private JFrame frame = new JFrame("书城");
	private JPanel titlePanel = new JPanel();
	private JPanel titlePanel2 = new JPanel();
	private JPanel searchPanel = new JPanel();
	//card layout
	private CardLayout card = new CardLayout(); 
	private JPanel panelMain = new JPanel();
	private JPanel panelOne = new JPanel();
	private JScrollPane panelTwo = new JScrollPane();
	private JPanel panelInfo = new JPanel();
	private JScrollPane panelShopcar = new JScrollPane();
	private JScrollPane panelOrder = new JScrollPane();
	private JScrollBar scrollBar = new JScrollBar();
	
	private Container c = frame.getContentPane();
	private JTextField searchField = new JTextField(30);
	private JButton searchButton = new JButton("搜索");
	private JButton mainButton = new JButton("主页");
	private JButton infoButton = new JButton("我的信息");
	private JButton shopcarButton = new JButton("购物车");
	private JButton orderButton = new JButton("我的订单");
	
	//data
	private String[] bookurl =new String[6];
	private String[] bookisbn =new String[6];
	private String[] bookname =new String[6];
	private String userid;
	
	public Index(String id) {
		frame.setSize(900, 800);
		c.setLayout(new BorderLayout());
		userid = id;
		initConnection();
		initFrame();
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
					out.println("g");
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
			out.println("3");
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
		JLabel a = new JLabel("欢迎光临");
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
		panelOne.setLayout(new GridLayout(0,3)); 
        //热销书
		ImagePane book1= new ImagePane(bookurl[0],bookname[0],bookisbn[0]);
		ImagePane book2= new ImagePane(bookurl[1],bookname[1],bookisbn[1]);
		ImagePane book3= new ImagePane(bookurl[2],bookname[2],bookisbn[2]);
		ImagePane book4= new ImagePane(bookurl[3],bookname[3],bookisbn[3]);
		ImagePane book5= new ImagePane(bookurl[4],bookname[4],bookisbn[4]);
		ImagePane book6= new ImagePane(bookurl[5],bookname[5],bookisbn[5]);
	
		panelOne.add(book1);
		panelOne.add(book2);
		panelOne.add(book3);
		panelOne.add(book4);
		panelOne.add(book5);
		panelOne.add(book6);
		
		
		//
		panelMain.add("first panel",panelOne); 
		panelMain.add("info panel",panelInfo); 
		
		c.add(panelMain, "Center");
		

		// 底部按钮
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		mainButton.setPreferredSize(new Dimension(150,40));
		mainButton.setFont(new Font (Font.DIALOG, Font.BOLD, 20));
		infoButton.setPreferredSize(new Dimension(150,40));
		infoButton.setFont(new Font (Font.DIALOG, Font.BOLD, 20));
		shopcarButton.setPreferredSize(new Dimension(150,40));
		shopcarButton.setFont(new Font (Font.DIALOG, Font.BOLD, 20));
		orderButton.setPreferredSize(new Dimension(150,40));
		orderButton.setFont(new Font (Font.DIALOG, Font.BOLD, 20));
		buttonPanel.add(mainButton);
		buttonPanel.add(infoButton);
		buttonPanel.add(shopcarButton);
		buttonPanel.add(orderButton);
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

		infoButton.addActionListener(new myActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ChangeInfoPage();
			}
		});

		shopcarButton.addActionListener(new myActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ChangeShopCarPage();
			}
		});

		orderButton.addActionListener(new myActionListener() {
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
				JOptionPane.showMessageDialog(null, "对不起，未找到此书。", "提示",JOptionPane.WARNING_MESSAGE);
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
//			        if(!searchBookIsbn[i].equals(searchBookIsbn[0]) || i==0)
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
	
	private void ChangeShopCarPage(){
		String []shopCarItemID;
		String []shopCarBookUrl;
		String []shopCarBookIsbn;
		String []shopCarBookName;
		String []shopCarBookNum;
		String []shopCarBookPrice;
		boolean existitem = false;
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
			out.println("12");
			out.println(userid);
			out.flush();
			 
			//get feedback from server
			InputStreamReader sr=new InputStreamReader(client.getInputStream());
			in=new BufferedReader(sr);
			num=Integer.parseInt(in.readLine());
			//System.out.println(num);
			if(num==0){//没有记录
				Object[] options = {"现在就去","等等再说"};
				int response=JOptionPane.showOptionDialog(null, "啊~您的购物车还空空如也，快去逛逛吧~","Tips:",JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
				if(response==0) {         
					 card.show(panelMain,"first panel");
				} else if(response==1) {           
					 card.show(panelMain,"first panel");
				} 
			}else{		
				shopCarItemID = new String[num];
				shopCarBookUrl = new String[num];
				shopCarBookName = new String[num];
				shopCarBookPrice = new String[num];
				shopCarBookIsbn = new String[num];
				shopCarBookNum = new String[num];
				
				for(int i =0;i<num;i++){
					shopCarItemID[i] = in.readLine();
					shopCarBookIsbn[i]=in.readLine();
					shopCarBookName[i]=in.readLine();
					shopCarBookPrice[i]=in.readLine();
					shopCarBookNum[i]=in.readLine();
					shopCarBookUrl[i]=in.readLine();
				}
			
			JPanel bookpanelSum = new JPanel();
	        bookpanelSum.setLayout(new BoxLayout(bookpanelSum, BoxLayout.Y_AXIS));
	        ShopCarPanel [] bookarray = new ShopCarPanel[num];
			
			for(int i = 0;i<num;i++){
				bookarray[i] = new ShopCarPanel(shopCarItemID[i],shopCarBookUrl[i],shopCarBookIsbn[i],"《"+shopCarBookName[i]+"》",shopCarBookPrice[i],shopCarBookNum[i]);
		        bookpanelSum.add(bookarray[i]);
			}
			panelShopcar = new JScrollPane(bookpanelSum);
			panelShopcar.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	        
			panelMain.add("shopcar panel",panelShopcar); 		
			card.show(panelMain,"shopcar panel");
			}
		}catch(Exception error){
			System.out.println( error);
		}	
	}
	
	private void ChangeInfoPage(){
		String myAccount=userid;
		String myPwd="";
		String myNickname="";
		String myPhone="";
		String myAddress="";
		//用id号查询个人信息
		try{
			//send messages to server
			//向服务器发送请求
			client = new Socket("127.0.0.1",port);
			out = new PrintWriter(client.getOutputStream());
			 
			OutputStream os = client.getOutputStream();
			OutputStreamWriter osw=new OutputStreamWriter(os);
			out = new PrintWriter(osw,true);
			out.println("5");
			out.println(userid);
			
			//get feedback from server
			InputStreamReader sr=new InputStreamReader(client.getInputStream());
			in=new BufferedReader(sr);
			myPwd= in.readLine();
			myNickname= in.readLine();
			myPhone= in.readLine();
			myAddress= in.readLine();
			//System.out.println(myPwd+" "+myNickname+" "+myPhone+" "+myAddress);
		}catch (IOException error){
			System.out.println(error);
		}
		String pwd = myPwd;
		
		JButton changePwd = new JButton("修改密码");
		JButton changeNickname = new JButton("修改昵称");
		JButton changePhone = new JButton("修改电话");
		JButton changeAddress = new JButton("修改地址");
		
		JLabel a1 = new JLabel("账  号: "+myAccount);
		a1.setFont(new Font (Font.DIALOG, Font.BOLD, 30));
		a1.setBounds(200, 100, 400, 35);
		JLabel a2 = new JLabel("昵  称: "+myNickname);
		a2.setFont(new Font (Font.DIALOG, Font.BOLD, 30));
		a2.setBounds(200, 150, 400, 35);
		JLabel a3 = new JLabel("电  话: "+myPhone);
		a3.setFont(new Font (Font.DIALOG, Font.BOLD, 30));
		a3.setBounds(200, 200, 400, 35);
		JLabel a4 = new JLabel("地  址: "+myAddress);
		a4.setFont(new Font (Font.DIALOG, Font.BOLD, 30));
		a4.setBounds(200, 250, 400, 35);

		
		changePwd.setFont(new Font (Font.DIALOG, Font.BOLD, 20));
		changePwd.setBounds(600, 100, 200, 35);
		changeNickname.setFont(new Font (Font.DIALOG, Font.BOLD, 20));
		changeNickname.setBounds(600, 150, 200, 35);
		changePhone.setFont(new Font (Font.DIALOG, Font.BOLD, 20));
		changePhone.setBounds(600, 200, 200, 35);		
		changeAddress.setFont(new Font (Font.DIALOG, Font.BOLD, 20));
		changeAddress.setBounds(600, 250, 200, 35);

		
		panelInfo.add(a1);
		panelInfo.add(a2);
		panelInfo.add(a3);
		panelInfo.add(a4);
		panelInfo.add(changePwd);
		panelInfo.add(changeNickname);
		panelInfo.add(changePhone);
		panelInfo.add(changeAddress);
		
        panelInfo.setLayout(null); 
        panelMain.add("info panel", panelInfo);
		card.show(panelMain, "info panel");

		changePwd.addActionListener(new myActionListener(){
			@Override
            public void actionPerformed(ActionEvent e) {
				String inputValue = JOptionPane.showInputDialog(null, "请输入原始密码");
				if(inputValue == null || inputValue.length() <= 0)
					System.out.println("点击取消或没有输入");
				else{
					boolean check = true;
					if(inputValue.equals(pwd)){
						while(check){
							inputValue = JOptionPane.showInputDialog(null, "请输入新密码");
							if(inputValue == null || inputValue.length() <= 0)
							{
								//System.out.println("点击取消或没有输入");
								check = false;
							}
							else{
								Pattern pt = Pattern.compile("^[0-9a-zA-Z_]+$");
								Matcher mt = pt.matcher(inputValue);
								if (!(mt.matches() && (inputValue.length() >= 6 && inputValue.length() <= 16))) {
									JOptionPane.showMessageDialog(null, "密码不规范（由字母数字组成，长度为6~16位）", "错误 ", JOptionPane.ERROR_MESSAGE);
								}
								else{
									//System.out.println("密码正确");
									try{
										//向服务器发送请求
										client = new Socket("127.0.0.1",port);
										out = new PrintWriter(client.getOutputStream());
										 
										OutputStream os = client.getOutputStream();
										OutputStreamWriter osw=new OutputStreamWriter(os);
										out = new PrintWriter(osw,true);
										out.println("6");
										out.println(userid);
										out.println(inputValue);
										
										//get feedback from server
										InputStreamReader sr=new InputStreamReader(client.getInputStream());
										in=new BufferedReader(sr);
										String tip=in.readLine();
										JOptionPane.showMessageDialog(null, tip);
										
									}catch (IOException error){
										System.out.println(error);
									}
									check = false;
								}
							}
						}
					}else{
						JOptionPane.showMessageDialog(null, "密码错误", "错误 ", JOptionPane.ERROR_MESSAGE);
					}
				}
					//System.out.println(inputValue);//点击确定
            }
		});

		changeNickname.addActionListener(new myActionListener(){
			@Override
            public void actionPerformed(ActionEvent e) {
				String inputValue = JOptionPane.showInputDialog(null, "请输入新昵称");
				if(inputValue == null || inputValue.length() <= 0)
					System.out.println("点击取消或没有输入");
				else{
					if (!(inputValue.length() >= 0 && inputValue.length() <= 16)) {
						JOptionPane.showMessageDialog(null, "昵称过长", "错误 ", JOptionPane.ERROR_MESSAGE);
					}else{
						//System.out.println(inputValue);//点击确定
						try{
							//向服务器发送请求
							client = new Socket("127.0.0.1",port);
							out = new PrintWriter(client.getOutputStream());
							 
							OutputStream os = client.getOutputStream();
							OutputStreamWriter osw=new OutputStreamWriter(os);
							out = new PrintWriter(osw,true);
							out.println("7");
							out.println(userid);
							out.println(inputValue);
							
							//get feedback from server
							InputStreamReader sr=new InputStreamReader(client.getInputStream());
							in=new BufferedReader(sr);
							String tip=in.readLine();
							JOptionPane.showMessageDialog(null, tip);
							RefreshInfoPage(a2,"昵  称: "+inputValue);
							}catch (IOException error){
							System.out.println(error);
						}
						
					}
				}
				
            }
		});

		changePhone.addActionListener(new myActionListener(){
			@Override
            public void actionPerformed(ActionEvent e) {
				String inputValue = JOptionPane.showInputDialog(null, "请输入新号码");
				if(inputValue == null || inputValue.length() <= 0)
					System.out.println("点击取消或没有输入");
				else{
					if (!(inputValue.length() >= 0 && inputValue.length()<= 11)) {
						JOptionPane.showMessageDialog(null, "电话号码过长", "错误 ", JOptionPane.ERROR_MESSAGE);
					}else{
						//System.out.println(inputValue);//点击确定
						try{
							//向服务器发送请求
							client = new Socket("127.0.0.1",port);
							out = new PrintWriter(client.getOutputStream());
							 
							OutputStream os = client.getOutputStream();
							OutputStreamWriter osw=new OutputStreamWriter(os);
							out = new PrintWriter(osw,true);
							out.println("8");
							out.println(userid);
							out.println(inputValue);
							
							//get feedback from server
							InputStreamReader sr=new InputStreamReader(client.getInputStream());
							in=new BufferedReader(sr);
							String tip=in.readLine();
							JOptionPane.showMessageDialog(null, tip, "提示 ", JOptionPane.ERROR_MESSAGE);
							RefreshInfoPage(a3,"电  话: "+inputValue);
							}catch (IOException error){
							System.out.println(error);
						}
						
					}
				}
            }
		});
		changeAddress.addActionListener(new myActionListener(){
			@Override
            public void actionPerformed(ActionEvent e) {
				String inputValue = JOptionPane.showInputDialog(null, "请输入新地址");
				if(inputValue == null || inputValue.length() <= 0)
					System.out.println("点击取消或没有输入");
				else{
					if (inputValue.length() <=0) {
						JOptionPane.showMessageDialog(null, "地址不能为空", "错误 ", JOptionPane.ERROR_MESSAGE);
					}else{
						//System.out.println(inputValue);//点击确定
						try{
							//向服务器发送请求
							client = new Socket("127.0.0.1",port);
							out = new PrintWriter(client.getOutputStream());
							 
							OutputStream os = client.getOutputStream();
							OutputStreamWriter osw=new OutputStreamWriter(os);
							out = new PrintWriter(osw,true);
							out.println("9");
							out.println(userid);
							out.println(inputValue);
							
							//get feedback from server
							InputStreamReader sr=new InputStreamReader(client.getInputStream());
							in=new BufferedReader(sr);
							String tip=in.readLine();
							JOptionPane.showMessageDialog(null, tip);
							RefreshInfoPage(a4,"地  址: "+inputValue);
						}catch (IOException error){
							System.out.println(error);
						}
						
					}
				}
            }
		});
		
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
			out.println("13");
			out.println(userid);
			out.flush();
			 
			//get feedback from server
			InputStreamReader sr=new InputStreamReader(client.getInputStream());
			in=new BufferedReader(sr);
			num=Integer.parseInt(in.readLine());
			//System.out.println(num);
			
			if(num==0){//没有记录
				Object[] options = {"现在就去","等等再说"};
				int response=JOptionPane.showOptionDialog(null, "啊~您的订单还空空如也，快去下单吧~","Tips:",JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
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
	    	  img.setImage(img.getImage().getScaledInstance(280, 280,Image.SCALE_DEFAULT));
	          JLabel l1 = new JLabel(img); 
	          l1.setBorder(BorderFactory.createEtchedBorder());
	          //l1.setBounds(new Rectangle(10,10,300,200));//设置jpanel的左边距、上边距、长度、高度，在jp没设置setLayout(null);是无效的
	          l1.setBackground(Color.GRAY);
	          l1.setSize(300, 300);
	          l1.addMouseListener(new MouseAdapter(){
	        	  public void mouseClicked(MouseEvent e){
	        		  //System.out.println(isbn);
	        		  new BookPage(userid,isbn);
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
//			this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
//			this.setLayout(new FlowLayout());
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
	        		  new BookPage(userid,bookisbn);
	  			  }
	          });
			b1.addActionListener(new myActionListener(){
				@Override
	            public void actionPerformed(ActionEvent e) {
					 new BookPage(userid,bookisbn);
	            }
			});
			
			this.add(leftPanel);
			this.add(midPanel);
			this.add(rightPanel);
		}
	}
	public class ShopCarPanel extends JPanel{
		public ShopCarPanel(String itemid,String url,String bookisbn,String bookname,String bookprice,String booknum){
//			this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
//			this.setLayout(new FlowLayout());
			this.setLayout(new GridLayout(0,3));
			
			JPanel leftPanel = new JPanel();
			leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
			JPanel midPanel = new JPanel();
			midPanel.setLayout(new BoxLayout(midPanel, BoxLayout.Y_AXIS));
			JPanel rightPanel = new JPanel();
			rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		
			ImagePane2 bookImg = new ImagePane2(url,100,100);
			leftPanel.add(bookImg);
			double singleprice=Double.parseDouble(bookprice);
			int num = Integer.parseInt(booknum);
			double sum = singleprice*num;
			JLabel l1 = new JLabel("书  名:"+bookname);
			l1.setFont(new Font (Font.DIALOG, Font.BOLD, 20));
			JLabel l2 = new JLabel("单  价:"+bookprice+" ¥");
			l2.setFont(new Font (Font.DIALOG, Font.BOLD, 20));
			JLabel l3 = new JLabel("总  价:"+sum+" ¥");
			l3.setFont(new Font (Font.DIALOG, Font.BOLD, 20));
			JLabel l4 = new JLabel("数  量:"+booknum);
			l4.setFont(new Font (Font.DIALOG, Font.BOLD, 20));
			midPanel.add(l1);
			midPanel.add(l2);
			midPanel.add(l4);
			midPanel.add(l3);


			JButton b1 = new JButton("立 即 下 单");
			b1.setFont(new Font (Font.DIALOG, Font.BOLD, 20));			
			JButton b2 = new JButton("删除此商品");
			b2.setFont(new Font (Font.DIALOG, Font.BOLD, 20));
			rightPanel.add(b1);
			rightPanel.add(b2);

			b1.addActionListener(new myActionListener(){
				@Override
	            public void actionPerformed(ActionEvent e) {
					Object[] options = {"确定","取消"};
					int response=JOptionPane.showOptionDialog(null, "您确定要下单吗？","Tips:",JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
					if(response==0) {
						try{
							//向服务器发送请求
							client = new Socket("127.0.0.1",port);
							out = new PrintWriter(client.getOutputStream());
							 
							OutputStream os = client.getOutputStream();
							OutputStreamWriter osw=new OutputStreamWriter(os);
							out = new PrintWriter(osw,true);
							 
							Date date=new Date();
							DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							String time=format.format(date);
							
							//send messages to server
							out.println("15");
							out.println(itemid);
							out.println(userid);
							out.println(bookisbn);
							out.println(num);
							out.println(time);
							out.println(sum);
							out.println("未处理");
							out.println(url);
							out.flush();
							 
							//get feedback from server
							InputStreamReader sr=new InputStreamReader(client.getInputStream());
							in=new BufferedReader(sr);
							if(in.readLine().equals("succeed")){
								JOptionPane.showMessageDialog(null, "下单成功");
							}else{
								JOptionPane.showMessageDialog(null, "下单失败", "提示 ", JOptionPane.ERROR_MESSAGE);
							}
							ChangeShopCarPage();
						}catch(Exception error){
							System.out.println( error);
						}
					}
	            }
			});
			b2.addActionListener(new myActionListener(){
				@Override
	            public void actionPerformed(ActionEvent e) {
					try{
						//向服务器发送请求
						client = new Socket("127.0.0.1",port);
						out = new PrintWriter(client.getOutputStream());
						 
						OutputStream os = client.getOutputStream();
						OutputStreamWriter osw=new OutputStreamWriter(os);
						out = new PrintWriter(osw,true);
						
						//send messages to server
						out.println("16");
						out.println(itemid);
						out.println(userid);
						out.flush();
						 
						//get feedback from server
						InputStreamReader sr=new InputStreamReader(client.getInputStream());
						in=new BufferedReader(sr);
						if(in.readLine().equals("delete succeed")){
							JOptionPane.showMessageDialog(null, "删除成功");
						}else{
							JOptionPane.showMessageDialog(null, "删除失败", "提示 ", JOptionPane.ERROR_MESSAGE);
						}
						ChangeShopCarPage();
	               }catch(IOException error){
	            	error.getStackTrace();
	               }
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

			JButton b1 = new JButton("取 消 订 单");
			b1.setFont(new Font (Font.DIALOG, Font.BOLD, 20));			
			rightPanel.add(l5);
			rightPanel.add(b1);

			b1.addActionListener(new myActionListener(){
				@Override
	            public void actionPerformed(ActionEvent e) {
					if(orderstate.equals("未处理")){
						Object[] options = {"确定","取消"};
						int response=JOptionPane.showOptionDialog(null, "您确定要取消订单吗？","Tips:",JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
						if(response==0) {         
							try{
								//向服务器发送请求
								client = new Socket("127.0.0.1",port);
								out = new PrintWriter(client.getOutputStream());
								 
								OutputStream os = client.getOutputStream();
								OutputStreamWriter osw=new OutputStreamWriter(os);
								out = new PrintWriter(osw,true);
								 
								//send messages to server
								out.println("14");
								out.println(orderid);
								out.println(ordernum);
								out.println(url);
								out.flush();
								 
								//get feedback from server
								InputStreamReader sr=new InputStreamReader(client.getInputStream());
								in=new BufferedReader(sr);
								if(in.readLine().equals("delete succeed")){
									JOptionPane.showMessageDialog(null, "取消成功");
								}
								ChangeOrderPage();
							}catch(Exception error){
								System.out.println( error);
							}
						}
						
					}else{
						JOptionPane.showMessageDialog(null, "商品以发货，无法取消", "提示 ", JOptionPane.ERROR_MESSAGE);

					}
					
	            }
			});
			
			this.add(leftPanel);
			this.add(midPanel);
			this.add(rightPanel);

		}
	}
}
