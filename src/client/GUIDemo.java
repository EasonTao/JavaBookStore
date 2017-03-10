package client;

import java.awt.*;
import java.awt.event.KeyEvent;

import javax.swing.*;

/*
 * 作者:钟志钢
 * 功能:图形界面GUI,Swing
 * 日期:2013-2-2
 * 1. awt, swt,swing,jFace之间关联
 *  awt,sun公司提供的GUI开发工具包AWT(Abstract Window Toolkit),包括一些抽象窗口工具
 *  swing,sun公司的又一个GUI框架,解决了awt存在的lcd(本地化)问题
 *  awt,IBM创建的一个新的GUI库,认为Swing很消耗内存
 *  JFace,IBM提供的更强大的GUI工具
 * 2, 布局管理器:
 *  边界布局(BorderLayout),
 *  流式布局(FlowLayout,从左到右,自动换行)
 *  网格布局(GridLayout)
 * 
 * 3, 界面开发的一般步骤:
 *  继承JFrame
 *  定义组件(大类)
 *  创建组件(构造函数)
 *  添加组件(加入到JFrame)
 *  对窗体进行设置
 *  显示窗体
 * 4, 面板组件,JPael,多布局管理器,非顶层容器,默认为流式布局
 */
public class GUIDemo extends JFrame {// 在类里继承JFrame
	// JButton jb ;//在此定义组件
	JButton jb1, jb2, jb3, jb4, jb5, jb6;// 按钮
	JPanel jp1, jp2, jp3, jp4, jp5;// 面板
	JLabel jl1, jl2, jl3, jl4, jl5;// 标签
	JTextField jtf1, jtf2;// 可编辑文本框
	JPasswordField jpf1;// 密码框
	JCheckBox jcb1, jcb2, jcb3;// 复选框
	JRadioButton jrb1, jrb2;// 单选框,需要先放入到ButtonGroup中,否则不能实现单选
	ButtonGroup bg;
	JComboBox jcb;// 下拉框
	JList jl;// 列表框组件
	JScrollPane jsp;// 滚动条,通常与JList结合使用
	JSplitPane jsp1;// 拆分窗口,容器类组件
	JTextArea jta; // 多行文本框
	JTabbedPane jtp;// 选项卡窗格
	JMenuBar jmb;// 菜单条组件:树干
	JMenu jm1, jm2, jm3, jm4, jm5, jm6;// 菜单条组件:树枝,下面可再有树枝...直到树叶 为止
	JMenuItem jmi1, jmi2, jmi3, jmi4, jmi5, jmi6, jmi7, jmi8, jmi9;// 菜单项组件:树叶
	JToolBar jtb;// 容器类组件,又叫功能组件
	int size = 9;
	JButton[] jbs = new JButton[size];

	public static void main(String args[]) {
		GUIDemo gui = new GUIDemo();
	}

	public GUIDemo() {
		 JFrame jf = new JFrame();//窗体,继承之后就不用实例化了
		 this.setTitle("hi, JFrame");//标题,继承之后就用this代替jf;
		 this.setSize(400, 400);//大小(像素)
		 this.setLocation(100,200);//设置窗体的位置,x = 100, y = 200;
		 this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//设置当窗体关闭时,退出jvm(虚拟机);
		 this.setVisible(true);//显示,默认为false

		// 边界布局,BorderLayout
//		 jb1 = new JButton("中");//实例化按钮并设置文字
//		 jb2 = new JButton("北");//实例化按钮并设置文字
//		 jb3 = new JButton("东");//实例化按钮并设置文字
//		 jb4 = new JButton("南");//实例化按钮并设置文字
//		 jb5 = new JButton("西");//实例化按钮并设置文字
//		 this.add(jb1,BorderLayout.CENTER);//添加组件
//		 this.add(jb2,BorderLayout.NORTH);
//		 this.add(jb3,BorderLayout.EAST);
//		 this.add(jb4,BorderLayout.SOUTH);
//		 this.add(jb5,BorderLayout.WEST);//当缺少一个方向时,中间就会马上占有其"地盘"
//		 this.setTitle("我是边界布局");
//		 this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//设置当窗体关闭时,退出jvm(虚拟机);
//		 this.setSize(200, 200);//大小(像素)
//		 this.setVisible(true);//显示,默认为false

		// 流式布局,FlowLayout
		// jb1 = new JButton("关雨");//实例化按钮并设置文字
		// jb2 = new JButton("张飞");
		// jb3 = new JButton("赵云");
		// jb4 = new JButton("马超");
		// jb5 = new JButton("刘备");
		// jb6 = new JButton("我地");
		// this.add(jb1);
		// this.add(jb2);
		// this.add(jb3);
		// this.add(jb4);
		// this.add(jb5);
		// this.add(jb6);
		// //设置布局方式,并设置对齐方式,默认为居中
		// this.setLayout(new FlowLayout(FlowLayout.LEFT));
		// this.setTitle("我是流式布局");
		// this.setResizable(false);//设置用户不能随意修改大小
		// this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//设置当窗体关闭时,退出jvm(虚拟机);
		// this.setSize(300, 400);//大小(像素)
		// this.setLocation(200,200);//设置窗体的位置,x = 200, y = 200;
		// this.setVisible(true);//显示,默认为false

		// 网格布局GridLayout
		// for(int i = 0; i < size; i ++){
		// System.out.println("aa");
		// jbs[i] = new JButton(String.valueOf(i+1));
		// this.add(jbs[i]);
		// }
		// //设置网格布局,参数分别表示:3行,3列,水平间距,垂直间距
		// this.setLayout(new GridLayout(3, 3, 5, 5));
		// this.setTitle("我是网格布局");
		// this.setResizable(false);//设置用户不能随意修改大小
		// this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//设置当窗体关闭时,退出jvm(虚拟机);
		// this.setSize(300, 400);//大小(像素)
		// this.setLocation(200,200);//设置窗体的位置,x = 200, y = 200;
		// this.setVisible(true);//显示,默认为false

		// 面板管理器,JPanel
		// jp1 = new JPanel();//默认为流布局
		// jp2 = new JPanel();
		// jb1 = new JButton("西瓜");
		// jb2 = new JButton("苹果");
		// jb3 = new JButton("香蕉");
		// jb4 = new JButton("桔子");
		// jb5 = new JButton("木瓜");
		// jb6 = new JButton("荔枝");
		// //添加到JPanel
		// jp1.add(jb1);
		// jp1.add(jb2);
		// jp2.add(jb3);
		// jp2.add(jb4);
		// jp2.add(jb5);
		// //加入到JFame
		// this.add(jp1, BorderLayout.NORTH);
		// this.add(jp2, BorderLayout.SOUTH);
		// this.add(jb6, BorderLayout.CENTER);
		// this.setTitle("我是面板布局");
		// this.setResizable(false);//设置用户不能随意修改大小
		// this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//设置当窗体关闭时,退出jvm(虚拟机);
		// this.setSize(300, 200);//大小(像素)
		// this.setLocation(200,200);//设置窗体的位置,x = 200, y = 200;
		// this.setVisible(true);//显示,默认为false

		// 几个常用组件的综合应用:登录系统-可编辑文本框,标签,密码框
		// jb1 = new JButton("确定");
		// jb2 = new JButton("取消");
		// jl1 = new JLabel("用户名");
		// jl2 = new JLabel("密 码");
		// jpf1 = new JPasswordField(10);//实例化并设置大小
		// jtf1 = new JTextField(10);
		// //定义面板
		// jp1 = new JPanel();
		// jp2 = new JPanel();
		// jp3 = new JPanel();
		// //组件加入面板
		// jp1.add(jl1);
		// jp1.add(jtf1);
		// jp2.add(jl2);
		// jp2.add(jpf1);
		// jp3.add(jb1);
		// jp3.add(jb2);
		// this.add(jp1);
		// this.add(jp2);
		// this.add(jp3);
		// //设置布局方式
		// this.setLayout(new GridLayout(3,1,1,1));
		// this.setTitle("请登录");
		// this.setResizable(false);//设置用户不能随意修改大小
		// this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//设置当窗体关闭时,退出jvm(虚拟机);
		// this.setSize(300, 170);//大小(像素)
		// this.setLocation(200,200);//设置窗体的位置,x = 200, y = 200;
		// this.setVisible(true);//显示,默认为false

		// 几个常用组件的综合应用:复选框与单选框
		// jl1 = new JLabel("你喜欢的水果:");
		// jl2 = new JLabel("您的性别:");
		// jb1 = new JButton("注册");
		// jb2 = new JButton("取消");
		// jcb1 = new JCheckBox("苹果");
		// jcb2 = new JCheckBox("桔子");
		// jcb3 = new JCheckBox("香蕉");
		// jrb1 = new JRadioButton("男");
		// jrb2 = new JRadioButton("女");
		// bg = new ButtonGroup();
		// bg.add(jrb1);//把单选按钮放入到bg中
		// bg.add(jrb2);
		// //面板
		// jp1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		// jp2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		// jp3 = new JPanel();
		// jp1.add(jl1);
		// jp1.add(jcb1);
		// jp1.add(jcb2);
		// jp1.add(jcb3);
		// jp2.add(jl2);
		// jp2.add(jrb1);
		// jp2.add(jrb2);
		// jp3.add(jb1);
		// jp3.add(jb2);
		// this.add(jp1);
		// this.add(jp2);
		// this.add(jp3);
		// this.setLayout(new GridLayout(3,1,1,1));
		// this.setTitle("请登录");
		// this.setResizable(false);//设置用户不能随意修改大小
		// this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//设置当窗体关闭时,退出jvm(虚拟机);
		// this.setSize(300, 150);//大小(像素)
		// this.setLocation(200,200);//设置窗体的位置,x = 200, y = 200;
		// this.setVisible(true);//显示,默认为false

		// 几个常用组件的综合应用:列表框,下拉框,
		// jl1 = new JLabel("请选择城市:");
		// jl2 = new JLabel("请选择景点:");
		// String jg [] = new String[]{"北京", "南京", "天津"};
		// jcb = new JComboBox(jg);
		// String jd [] = new String[]{"长城", "天安门", "黄河"};
		// jl = new JList(jd);
		// jl.setVisibleRowCount(2);////设置希望显示多少个选项
		// jsp = new JScrollPane(jl);//把列表加入到滚动条中
		// //面板
		// jp1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		// jp2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		// jp1.add(jl1);
		// jp1.add(jcb);
		// jp2.add(jl2);
		// jp2.add(jsp);//加入带列表的滚动条
		// this.add(jp1);
		// this.add(jp2);
		// this.setLayout(new GridLayout(2,1,1,1));
		// this.setTitle("请选择");
		// this.setResizable(false);//设置用户不能随意修改大小
		// this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//设置当窗体关闭时,退出jvm(虚拟机);
		// this.setSize(300, 200);//大小(像素)
		// this.setLocation(200,200);//设置窗体的位置,x = 200, y = 200;
		// this.setVisible(true);//显示,默认为false

		// 拆分窗口
		// String words [] = new String []{"girl","boy","book","good" };
		// jl = new JList(words);
		// jl1 = new JLabel(new ImageIcon("images/bd.jpg"));
		// jsp1 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, jl, jl1);//水平拆分
		// jsp1.setOneTouchExpandable(true);//拆分的窗口可伸缩
		// this.add(jsp1);
		// this.setTitle("拆分窗口");
		// this.setResizable(false);//设置用户不能随意修改大小
		// this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//设置当窗体关闭时,退出jvm(虚拟机);
		// this.setSize(500, 500);//大小(像素)
		// this.setLocation(200,200);//设置窗体的位置,x = 200, y = 200;
		// this.setVisible(true);//显示,默认为false

		// QQ聊天界面
		// jta = new JTextArea();
		// jsp = new JScrollPane(jta);//加入滚动条
		// jp1 = new JPanel();
		// String myj [] = new String [] {"钟志钢","王磊","刘绮霞"};
		// jcb = new JComboBox(myj);
		// jtf1 = new JTextField(20);
		// jb1 = new JButton("发送");
		// jp1.add(jcb);
		// jp1.add(jtf1);
		// jp1.add(jb1);
		// this.setLayout(new BorderLayout());
		// this.add(jsp);
		// this.add(jp1, BorderLayout.SOUTH);
		// this.setTitle("qq聊天");
		// this.setIconImage(new ImageIcon("images/f8.png").getImage());//设置Icon
		// this.setResizable(false);//设置用户不能随意修改大小
		// this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//设置当窗体关闭时,退出jvm(虚拟机);
		// this.setSize(500, 500);//大小(像素)
		// this.setLocation(200,200);//设置窗体的位置,x = 200, y = 200;
		// this.setVisible(true);//显示,默认为false

		// qq登录界面
		 jl1 = new JLabel(new ImageIcon("images/bg.gif"));
		 jl2 = new JLabel("QQ号码", JLabel.CENTER);
		 jl3 = new JLabel("QQ密码", JLabel.CENTER);
		 jl4 = new JLabel("忘记密码", JLabel.CENTER);
		 jl4.setFont(new Font("宋体", Font.PLAIN, 16));//设置字体
		 jl4.setForeground(Color.BLUE);
		 jl5 = new JLabel("<html><a href='www.qq.com'>申请密码保护</a>");//设置超链接
		 jl5.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));//鼠标变成"手形"
		 jcb1 = new JCheckBox("隐身登录");
		 jcb2 = new JCheckBox("记住密码");
		 jtf1 = new JTextField();
		 jpf1 = new JPasswordField();
		 jtp = new JTabbedPane();
		 jp1 = new JPanel();
		 jp2 = new JPanel(new GridLayout(3,3,5,5));
		 jp3 = new JPanel();
		 jp3.setBackground(Color.BLUE);
		 jp4 = new JPanel();
		 jp4.setBackground(Color.CYAN);
		 jb4 = new JButton("清除号码");
		 jb1 = new JButton("登 录");
		 jb2 = new JButton("取 消");
		 jb3 = new JButton("注册向导");
		 jp1.add(jb1);
		 jp1.add(jb2);
		 jp1.add(jb3);
		 jp2.add(jl2);
		 jp2.add(jtf1);
		 jp2.add(jb4);
		 jp2.add(jl3);
		 jp2.add(jpf1);
		 jp2.add(jl4);
		 jp2.add(jcb1);
		 jp2.add(jcb2);
		 jp2.add(jl5);
		 jtp.add("QQ号码",jp2);
		 jtp.add("手机号码",jp3);
		 jtp.add("QQ邮箱",jp4);
		 this.setLayout(new BorderLayout());
		 this.add(jtp);
		 this.add(jl1, BorderLayout.NORTH);
		 this.add(jp1, BorderLayout.SOUTH);
		 //this.setTitle("qq聊天");
		 this.setIconImage(new ImageIcon("images/f8.png").getImage());//设置Icon
		 this.setResizable(false);//设置用户不能随意修改大小
		 this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//设置当窗体关闭时,退出jvm(虚拟机);
		 this.setSize(300, 250);//大小(像素)
		 this.setLocation(500,400);//设置窗体的位置,x = 200, y = 200;
		 this.setVisible(true);//显示,默认为false

		// 记事本:菜单应用
//		jtb = new JToolBar();// 新建工具条
//		jb1 = new JButton("新建");
//		jb1.setToolTipText("新建一个文件");// 设置当鼠标移动到这个按钮时显示的提示信息
//		jb2 = new JButton("保存");
//		jb2.setToolTipText("保存一个文件");
//		jb3 = new JButton("打开");
//		jb4 = new JButton("复制");
//		jb5 = new JButton("粘贴");
//		jb6 = new JButton("剪切");
//		jmb = new JMenuBar();// 新建菜单条:树干
//		jm1 = new JMenu("文件(F)");
//		jm1.setMnemonic('F');// 设置助记符
//		jm2 = new JMenu("编辑(E)");
//		jm2.setMnemonic('E');
//		jm3 = new JMenu("格式(O)");
//		jm3.setMnemonic('O');
//		jm4 = new JMenu("查看(V)");
//		jm4.setMnemonic('V');
//		jm5 = new JMenu("帮助(H)");
//		jm5.setMnemonic('H');
//		jm6 = new JMenu("新建");
//		jmi1 = new JMenuItem("文件");
//		jmi2 = new JMenuItem("工程");
//		jmi3 = new JMenuItem("打开", new ImageIcon("iamges/f8.png"));
//		jmi4 = new JMenuItem("保存(s)");
//		jmi4.setMnemonic('S');
//		// jmi4.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.vk_S,));
//		jmi5 = new JMenuItem("另存为");
//		jmi6 = new JMenuItem("页面设置");
//		jmi7 = new JMenuItem("打印");
//		jmi8 = new JMenuItem("退出");
//		jm6.add(jmi1);// 新建中加入树叶
//		jm6.add(jmi2);
//		jm1.add(jm6);// 为文件菜单加入树枝树叶
//		// jm1.add(jmi2);
//		jm1.add(jmi3);
//		jm1.add(jmi4);
//		jm1.add(jmi5);
//		jm1.addSeparator();// 加入分割线
//		jm1.add(jmi6);
//		jm1.add(jmi7);
//		jm1.addSeparator();
//		jm1.add(jmi8);
//		jmb.add(jm1);// 将菜单添加到菜单条上
//		jmb.add(jm2);
//		jmb.add(jm3);
//		jmb.add(jm4);
//		jmb.add(jm5);
//		jta = new JTextArea();// 多行文本框
//		jtb.add(jb1);
//		jtb.add(jb2);
//		jtb.add(jb3);
//		jtb.add(jb4);
//		jtb.add(jb5);
//		jtb.add(jb6);
//		// 将菜单条加入到窗体上
//		this.setJMenuBar(jmb);
//		this.add(jtb, BorderLayout.NORTH);// 将工具条加入到窗体(布局)中
//		jsp = new JScrollPane(jta);
//		// jsp.setVerticalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);//设置下拉条风格
//		this.add(jsp);
//		this.setTitle("记事本");
//		this.setIconImage(new ImageIcon("images/f8.png").getImage());// 设置Icon
//		this.setResizable(false);// 设置用户不能随意修改大小
//		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// 设置当窗体关闭时,退出jvm(虚拟机);
//		this.setSize(400, 550);// 大小(像素)
//		this.setLocation(500, 400);// 设置窗体的位置,x = 200, y = 200;
//		this.setVisible(true);// 显示,默认为false

	}
}
