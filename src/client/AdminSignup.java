package client;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.*;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.regex.*;

public class AdminSignup {
	private int port  = 9999;
	private ServerSocket server;
	private Socket ss;
	private Socket client;
	private PrintWriter out;
	
	private JFrame frame = new JFrame("管理员注册");
	private Container c = frame.getContentPane();
	private JTextField usernameField = new JTextField();
	private JPasswordField passwordField = new JPasswordField();
	private JPasswordField passwordField2 = new JPasswordField();


	private JButton okButton = new JButton("确定");
	private JButton cancelButton = new JButton("返回");

	//Data
	private String username;
	private String password;
	private String password2;

	
	public AdminSignup() {
		frame.setSize(500, 400);
		c.setLayout(new BorderLayout());
		initFrame();
		frame.setVisible(true);
		frame.addWindowListener(new myWindowAdapter(){//监听关闭事件
			public void windowClosing(WindowEvent e) {  
				super.windowClosing(e);  
                new AdminLogin();
			}  
		});
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
		JLabel a = new JLabel("管理员注册");
		a.setFont(new Font("SanSerif",Font.BOLD,30));
		titlePanel.add(a);
		c.add(titlePanel, "North");

		// 中部表单
		JPanel fieldPanel = new JPanel();
		fieldPanel.setLayout(null);
		JLabel a1 = new JLabel("用户名:");
		a1.setFont(new Font (Font.DIALOG, Font.BOLD, 20));
		a1.setBounds(80, 50, 150, 35);
		JLabel a2 = new JLabel("密  码:");
		a2.setFont(new Font (Font.DIALOG, Font.BOLD, 20));
		a2.setBounds(80, 110, 150, 35);
		JLabel a3 = new JLabel("重复密码:");
		a3.setFont(new Font (Font.DIALOG, Font.BOLD, 20));
		a3.setBounds(80, 170, 150, 35);
		fieldPanel.add(a1);
		fieldPanel.add(a2);
		fieldPanel.add(a3);
		
		usernameField.setFont(new Font (Font.DIALOG, Font.BOLD, 20));
		passwordField.setFont(new Font (Font.DIALOG, Font.BOLD, 20));
		passwordField2.setFont(new Font (Font.DIALOG, Font.BOLD, 20));
		
		usernameField.setBounds(200, 50, 180, 30);
		passwordField.setBounds(200, 110, 180, 30);
		passwordField2.setBounds(200, 170, 180, 30);

		fieldPanel.add(usernameField);
		fieldPanel.add(passwordField);
		fieldPanel.add(passwordField2);

		c.add(fieldPanel, "Center");

		// 底部按钮
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		okButton.setPreferredSize(new Dimension(100,40));
		okButton.setFont(new Font (Font.DIALOG, Font.BOLD, 20));
		cancelButton.setPreferredSize(new Dimension(100,40));
		cancelButton.setFont(new Font (Font.DIALOG, Font.BOLD, 20));
		okButton.addActionListener(new myActionListener(){
			@Override
            public void actionPerformed(ActionEvent e) {
				username = usernameField.getText();
				password = passwordField.getText();
				password2 = passwordField2.getText();

				Boolean bl = true;
				  
				//首先,使用Pattern解释要使用的正则表达式，其中^表是字符串的开始，$表示字符串的结尾。
				Pattern pt = Pattern.compile("^[0-9a-zA-Z_]+$");
				 
				//然后使用Matcher来对比目标字符串与上面解释得结果
				Matcher mt = pt.matcher(username);
				if (!(mt.matches() && (username.length() >= 3 && username.length() <= 16))) {
					System.out.println("error1");
					JOptionPane.showMessageDialog(null, "用户名不规范（由字母数字组成，长度为3~16位）", "错误 ", JOptionPane.ERROR_MESSAGE);
					bl = false;
				}
				mt = pt.matcher(password);
				if (!(mt.matches() && (password.length() >= 3 && password.length() <= 16))) {
					System.out.println("error2");
					JOptionPane.showMessageDialog(null, "密码不规范（由字母数字组成，长度为3~16位）", "错误 ", JOptionPane.ERROR_MESSAGE);
					bl = false;
				}
				if (!password.equals(password2)) {
					System.out.println("error3");
					JOptionPane.showMessageDialog(null, "密码两遍不同", "错误 ", JOptionPane.ERROR_MESSAGE);
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
						out.println("18");
						out.println(username);
						out.println(password);
						out.flush();
							 
						//get feedback from server
						InputStreamReader sr=new InputStreamReader(client.getInputStream());
						BufferedReader in=new BufferedReader(sr);
						String str=in.readLine();
						if(str.equals("ok")){
							//提示注册成功
							System.out.println(str);
							JOptionPane.showMessageDialog(null, "管理员注册成功");
							frame.dispose();
							new AdminLogin();
						}else if(str.equals("exist")){
							//提示注册失败
							System.out.println(str);
							JOptionPane.showMessageDialog(null, "用户名已存在，请重新输入", "提示 ", JOptionPane.ERROR_MESSAGE);
						}else{
							//提示注册失败
							System.out.println(str);
							JOptionPane.showMessageDialog(null, "注册失败", "提示 ", JOptionPane.ERROR_MESSAGE);
						}
							 
					}catch (Exception error){
						System.out.println( error);
					}
				}
				  
            }
		});
		cancelButton.addActionListener(new myActionListener(){
			@Override
            public void actionPerformed(ActionEvent e) {
				frame.dispose();
				new Login();
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
	private class myWindowAdapter extends WindowAdapter{
		public void windowClosing(WindowEvent e) {  
 
			 }  
    }
}
