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


public class BookPage {
	private int port  = 9999;
	private ServerSocket server;
	private Socket ss;
	private Socket client;
	private PrintWriter out;
	
	private JFrame frame = new JFrame("书籍信息");
	private Container c = frame.getContentPane();
	private ImagePane bookPane;
	private JButton okButton = new JButton("加入购物车");
	private JButton cancelButton = new JButton("返回");

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
	
	public BookPage(String account,String isbn) {
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
		JLabel a6 = new JLabel("数  量: ");
		a6.setFont(new Font (Font.DIALOG, Font.BOLD, 20));
		a6.setBounds(100, 500, 400, 35);

		JSpinner spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(1, 1, 999, 1));
		spinner.setBounds(170, 500, 50, 35);
		
		fieldPanel.add(bookPane);
		fieldPanel.add(a1);
		fieldPanel.add(a2);
		fieldPanel.add(a3);
		fieldPanel.add(a4);
		fieldPanel.add(a5);
		fieldPanel.add(a6);
		fieldPanel.add(spinner);

		c.add(fieldPanel, "Center");

		// 底部按钮
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		okButton.setPreferredSize(new Dimension(200,40));
		okButton.setFont(new Font (Font.DIALOG, Font.BOLD, 20));
		cancelButton.setPreferredSize(new Dimension(200,40));
		cancelButton.setFont(new Font (Font.DIALOG, Font.BOLD, 20));
		
		//事件
		okButton.addActionListener(new myActionListener(){
			@Override
            public void actionPerformed(ActionEvent e) {
				if((int) spinner.getValue()>Integer.parseInt(bookremain)){//库存不足
					JOptionPane.showMessageDialog(null, "目前该书库存仅有"+Integer.parseInt(bookremain)+",请减少购买数量~","提示",JOptionPane.WARNING_MESSAGE);
				}else{
					try{
					    //向服务器发送请求
						client = new Socket("127.0.0.1",port);
						out = new PrintWriter(client.getOutputStream());
						 
						OutputStream os = client.getOutputStream();
						OutputStreamWriter osw=new OutputStreamWriter(os);
						out = new PrintWriter(osw,true);
						 
						//send messages to server
						out.println("11");
						out.println(memberaccount);
						out.println(bookisbn);
						out.println(bookname);
						out.println(bookprice);
						out.println(spinner.getValue());
						out.println(bookimgurl);
						out.println(bookremain);
						out.flush();
						 
						//get feedback from server
						InputStreamReader sr=new InputStreamReader(client.getInputStream());
						BufferedReader in=new BufferedReader(sr);
						String tip1=in.readLine();
						if(tip1.equals("insert failed")){
							JOptionPane.showMessageDialog(null, "添加失败~","提示",JOptionPane.ERROR_MESSAGE);
						}else{
							JOptionPane.showMessageDialog(null, "成功加入购物车~");
						}	
					}catch(Exception error){
						System.out.println( error);
					}
					frame.dispose();
				}		
            }
		});
		cancelButton.addActionListener(new myActionListener(){
			@Override
            public void actionPerformed(ActionEvent e) {
				frame.dispose();
            }
		});
		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);
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
