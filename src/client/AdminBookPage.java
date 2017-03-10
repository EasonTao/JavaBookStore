package client;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class AdminBookPage {
	private int port  = 9999;
	private ServerSocket server;
	private Socket ss;
	private Socket client;
	private PrintWriter out;
	
	private JFrame frame = new JFrame("书籍信息");
	private Container c = frame.getContentPane();
	private ImagePane bookPane;
	private JButton remainButton = new JButton("管理库存");
	private JButton deleteButton = new JButton("下架此书");

	//Data
	private String bookisbn;
	private String bookimgurl;
	private String bookname;
	private String bookauthor;
	private String bookpublish;
	private String bookprice;
	private String bookremain;
	private String bookdescription;
	private String booksoldnum;
	private String bookbuynum;
	private String memberaccount;
	
	public AdminBookPage(String account,String isbn) {
		frame.setSize(500, 700);
		bookisbn = isbn;
		memberaccount = account;
		c.setLayout(new BorderLayout());
		//此处用isbn号查询获取Data
		initConnection(isbn);
		initFrame();
		frame.setVisible(true);
	}
	
	private void initConnection(String isbn){
		try{
		    //向服务器发送请求
			client = new Socket("127.0.0.1",port);
			out = new PrintWriter(client.getOutputStream());
			 
			OutputStream os = client.getOutputStream();
			OutputStreamWriter osw=new OutputStreamWriter(os);
			out = new PrintWriter(osw,true);
			 
			//send messages to server
			out.println("4");
			out.println(isbn);
			out.flush();
			 
			//get feedback from server
			InputStreamReader sr=new InputStreamReader(client.getInputStream());
			BufferedReader in=new BufferedReader(sr);
			bookname=in.readLine();
			bookauthor=in.readLine();
			bookpublish=in.readLine();
			bookprice=in.readLine();
			bookremain=in.readLine();
			booksoldnum=in.readLine();
			bookdescription=in.readLine();
			bookimgurl=in.readLine();
			
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

		// 顶部
		JPanel titlePanel = new JPanel();
		titlePanel.setLayout(new FlowLayout());
		JLabel a = new JLabel("书籍详细信息");
		a.setFont(new Font("SanSerif",Font.BOLD,30));
		titlePanel.add(a);
		c.add(titlePanel, "North");

		// 中部
		JPanel fieldPanel = new JPanel();
		fieldPanel.setLayout(null);
		bookPane= new ImagePane(bookimgurl,bookname);
		bookPane.setBounds(100, 30, 300 , 300);
		
		JLabel a1 = new JLabel("书  名: "+bookname);
		a1.setFont(new Font (Font.DIALOG, Font.BOLD, 20));
		a1.setBounds(100, 350, 400, 35);
		JLabel a2 = new JLabel("作  者: "+bookauthor);
		a2.setFont(new Font (Font.DIALOG, Font.BOLD, 20));
		a2.setBounds(100, 380, 400, 35);
		JLabel a3 = new JLabel("出版社: "+bookpublish);
		a3.setFont(new Font (Font.DIALOG, Font.BOLD, 20));
		a3.setBounds(100, 410, 400, 35);
		JLabel a4 = new JLabel("介  绍: "+bookdescription);
		a4.setFont(new Font (Font.DIALOG, Font.BOLD, 20));
		a4.setBounds(100, 440, 400, 35);
		JLabel a5 = new JLabel("销售量: "+booksoldnum);
		a5.setFont(new Font (Font.DIALOG, Font.BOLD, 20));
		a5.setBounds(100, 470, 400, 35);
		JLabel a6 = new JLabel("库   存: "+bookremain);
		a6.setFont(new Font (Font.DIALOG, Font.BOLD, 20));
		a6.setBounds(100, 500, 400, 35);


		fieldPanel.add(bookPane);
		fieldPanel.add(a1);
		fieldPanel.add(a2);
		fieldPanel.add(a3);
		fieldPanel.add(a4);
		fieldPanel.add(a5);
		fieldPanel.add(a6);

		c.add(fieldPanel, "Center");

		// 底部按钮
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		remainButton.setPreferredSize(new Dimension(200,40));
		remainButton.setFont(new Font (Font.DIALOG, Font.BOLD, 20));
		deleteButton.setPreferredSize(new Dimension(200,40));
		deleteButton.setFont(new Font (Font.DIALOG, Font.BOLD, 20));

		
		//事件
		//更新库存
		remainButton.addActionListener(new myActionListener(){
			@Override
            public void actionPerformed(ActionEvent e) {
				try{
					String inputValue = JOptionPane.showInputDialog(null, "请输入新库存");
					Pattern pt = Pattern.compile("^[0-9]+$");
					Matcher mt = pt.matcher(inputValue);
					if(mt.matches())
					{
						//向服务器发送请求
						client = new Socket("127.0.0.1",port);
						out = new PrintWriter(client.getOutputStream());
						 
						OutputStream os = client.getOutputStream();
						OutputStreamWriter osw=new OutputStreamWriter(os);
						out = new PrintWriter(osw,true);
						 
						//send messages to server
						out.println("20");
						out.println(bookisbn);
						out.println(inputValue);

						out.flush();
						 
						//get feedback from server
						InputStreamReader sr=new InputStreamReader(client.getInputStream());
						BufferedReader in=new BufferedReader(sr);
						if(in.readLine().equals("failed")){
							JOptionPane.showMessageDialog(null, "更新失败~","提示",JOptionPane.ERROR_MESSAGE);
						}else{
							JOptionPane.showMessageDialog(null, "成功更新库存~");
							a6.setText("库   存: "+inputValue);
						}	
					}else{
						JOptionPane.showMessageDialog(null, "数量不合法", "错误 ", JOptionPane.ERROR_MESSAGE);
					}
					
				}catch(Exception error){
					System.out.println( error);
				}
            }
		});
		//下架
		deleteButton.addActionListener(new myActionListener(){
			@Override
            public void actionPerformed(ActionEvent e) {
				Object[] options = {"确定","取消"};
				int response=JOptionPane.showOptionDialog(null, "您确定要下架此书吗？","Tips:",JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
				if(response==0){
					try{
						//向服务器发送请求
						client = new Socket("127.0.0.1",port);
						out = new PrintWriter(client.getOutputStream());
						 
						OutputStream os = client.getOutputStream();
						OutputStreamWriter osw=new OutputStreamWriter(os);
						out = new PrintWriter(osw,true);
						 
						//send messages to server
						out.println("21");
						out.println(bookisbn);
						out.flush();
						
						//get feedback from server
						InputStreamReader sr=new InputStreamReader(client.getInputStream());
						BufferedReader in=new BufferedReader(sr);
						
						if(in.readLine().equals("failed")){
							JOptionPane.showMessageDialog(null, "下架失败~","提示",JOptionPane.ERROR_MESSAGE);
						}else{
							JOptionPane.showMessageDialog(null, "下架成功~");
						}
					}catch(Exception error){
						System.out.println( error);
					}
				}
				frame.dispose();//关闭窗口
            }
		});
	

		buttonPanel.add(remainButton);
		buttonPanel.add(deleteButton);
		c.add(buttonPanel, "South");
	}
	
	private class myActionListener implements ActionListener{
		public void actionPerformed(ActionEvent arg0){
			
		}
    }
	
	public class ImagePane extends JPanel{
	    public ImagePane(String url,String bookname) 
	    { 
	          this.setLayout(null);
	    	  ImageIcon img = new ImageIcon(url); 
	          JLabel l1 = new JLabel(img); 
	          l1.setBorder(BorderFactory.createEtchedBorder());
	          l1.setBackground(Color.GRAY);
	          l1.setSize(300, 300);
	          this.add(l1);
	      }
	}
}
