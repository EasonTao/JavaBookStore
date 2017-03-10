package client;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login {
	private int port  = 9999;
	private ServerSocket server;
	private Socket ss;
	private Socket client;
	private PrintWriter out;
	
	private JFrame frame = new JFrame("登录");
	private Container c = frame.getContentPane();
	private JTextField username = new JTextField();
	private JPasswordField password = new JPasswordField();
	private JButton ok = new JButton("登录");
	private JButton signup = new JButton("注册");
	//data
	private String id;
	private String pwd;

	public Login() {
		frame.setSize(500, 330);
		c.setLayout(new BorderLayout());
		initFrame();
		frame.setVisible(true);
	}

	private void initFrame() {
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
		JLabel a = new JLabel("书店系统登录");
		a.setFont(new Font("SanSerif",Font.BOLD,30));
		titlePanel.add(a);
		c.add(titlePanel, "North");

		// 中部表单
		JPanel fieldPanel = new JPanel();
		fieldPanel.setLayout(null);
		JLabel a1 = new JLabel("用户名:");
		a1.setBounds(80, 50, 150, 35);
		a1.setFont(new Font (Font.DIALOG, Font.BOLD, 20));
		JLabel a2 = new JLabel("密  码:");
		a2.setBounds(80, 110, 150, 35);
		a2.setFont(new Font (Font.DIALOG, Font.BOLD, 20));
		fieldPanel.add(a1);
		fieldPanel.add(a2);
		username.setFont(new Font (Font.DIALOG, Font.BOLD, 20));
		password.setFont(new Font (Font.DIALOG, Font.BOLD, 20));
		username.setBounds(200, 50, 180, 30);
		password.setBounds(200, 110, 180, 30);
		fieldPanel.add(username);
		fieldPanel.add(password);
		c.add(fieldPanel, "Center");

		// 底部按钮
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		ok.setPreferredSize(new Dimension(100,40));
		ok.setFont(new Font (Font.DIALOG, Font.BOLD, 20));
		signup.setPreferredSize(new Dimension(100,40));
		signup.setFont(new Font (Font.DIALOG, Font.BOLD, 20));
		buttonPanel.add(ok);
		buttonPanel.add(signup);
		c.add(buttonPanel, "South");
		ok.addActionListener(new myActionListener(){
			@Override
            public void actionPerformed(ActionEvent e) {
				id = username.getText();
				pwd = password.getText();
				 
				if(id.length()<=0||pwd.length()<=0){
					//提示用户名或密码不能为空
					JOptionPane.showMessageDialog(null, "用户名或密码不能为空", "提示 ", JOptionPane.ERROR_MESSAGE);
				}else{
					try{
					    //向服务器发送请求
						client = new Socket("127.0.0.1",port);
						out = new PrintWriter(client.getOutputStream());
						 
						OutputStream os = client.getOutputStream();
						OutputStreamWriter osw=new OutputStreamWriter(os);
						out = new PrintWriter(osw,true);
						 
						//send messages to server
						out.println("1");
						out.println(id);
						out.println(pwd);
						out.flush();
						 
						//get feedback from server
						InputStreamReader sr=new InputStreamReader(client.getInputStream());
						BufferedReader in=new BufferedReader(sr);
						String str=in.readLine();
						//System.out.println(str);
						if(str.equals("success")){
							new Index(id);
							frame.dispose();
						}else if(str.equals("errorN")||str.equals("errorP")){
							//提示用户不存在或密码错误
							JOptionPane.showMessageDialog(null, "用户不存在或密码错误", "错误 ", JOptionPane.ERROR_MESSAGE);
						}
					}catch(Exception error){
						System.out.println( error);
					} 
				}
            }
		});
		signup.addActionListener(new myActionListener(){
			@Override
            public void actionPerformed(ActionEvent e) {
				 new Signup();
				 frame.dispose();
            }
		});
		
	}

	private class myActionListener implements ActionListener{
		public void actionPerformed(ActionEvent arg0){
			
		}
    }
	
	public static void main(String[] args) {
		new Login();
	}
}