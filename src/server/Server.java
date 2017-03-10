package server;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;


import java.net.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.net.*;
import java.io.*;

import db.DBHelper;


public class Server {
	
	private ServerSocket server;
	private Socket ss;
	private int port = 9999;
	//private PrintWriter out;
	//private BufferedReader in ;
	private int userNum=0;
	private int adminNum=0;
	private int totalNum=0;//总共登录的用户数
	private int i=0;//进入线程的个数标号
	ArrayList<String> userName=new ArrayList<String >();//维护已
	ArrayList<String> adminName=new ArrayList<String >();
	
	static String sql = null;  
    static DBHelper db = new DBHelper();  
    static ResultSet ret = null; 
    
    static JFrame frame=new JFrame("服务端");
    JLabel l1=new JLabel();
    JLabel l2=new JLabel();
	
	public void initServerSocket(){
		GUI();
		try{
			ServerSocket server = new ServerSocket(port);
			System.out.println("----------服务器已启动-----------");
			while(true){
				Socket ss = server.accept();
				new HandlerThread(ss);
			}
		}catch(IOException error){
			System.out.println(error); 	
		}
	}
	
	private class HandlerThread implements Runnable{
		private Socket socket;
		
		public HandlerThread(Socket client){
			socket=client;
			new Thread(this).start();
		}
		
		public void run(){
//            i++;
//            System.out.println("第"+i+"个线程开始");
			try{
				//get the messages from client
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				String str1=in.readLine();
				
				//send the messages to client   	
	            OutputStream os=socket.getOutputStream();
	  			OutputStreamWriter osw=new OutputStreamWriter(os);
	  			PrintWriter out=new PrintWriter(osw,true);
				
				int selection=Integer.parseInt(str1);
				
				switch(selection){
				
				case 0://监听用户退出
					if(in.readLine().equals("g")){
						userNum--;
						out.println("ok");
						System.out.println("有1个普通用户退出登录");
					}else{
						adminNum--;
						out.println("ok");
						System.out.println("有1个管理员退出登录");
					}
					break;
				
				case 1://登录
					String id=in.readLine();
					String pwd=in.readLine();

					if(id==null||id.length()<=0){
						out.println("errorEmp");
						break;
					}
					sql = "select * from member where MemberAccount='"+id+"'";
					db.prepareSQL(sql);
					try{
						ret=db.pst.executeQuery();
						String mAccount;
						String mPwd;
						if(ret.next()==false){//没有该用户
			      			out.println("errorN");
						}else{
							mAccount = ret.getString(2);
							mPwd = ret.getString(3);
							
							if(pwd.equals(mPwd)){
								out.println("success");
								userNum++;
								System.out.println("第"+userNum+"个普通用户已登录");
							}else{
								out.println("errorP");
							}
						}
					}catch(SQLException error){
						error.printStackTrace();
					}
					break;
					
				case 2://注册
					String username=in.readLine();
					String password=in.readLine();
					String nickname=in.readLine();
					String phone=in.readLine();
					String address=in.readLine();
					
					sql="select * from member where MemberAccount='"+username+"'";
					db.prepareSQL(sql);										
					try{
						ret=db.pst.executeQuery();
						if(ret.next()){
							out.println("exist");
						}else{
							sql="insert into member(MemberAccount,MemberPwd,MemberNickname,MemberPhone,MemberAddress) "
									+ "values('"+username+"','"+password+"','"+nickname+"',"+phone+",'"+address+"');";
							db.prepareSQL(sql);
							db.pst.executeUpdate(sql);
							
							//每个用户维护一张表
							sql="create table shopcar"+username+"(ItemID int(11) not null primary key auto_increment,"
										+ "BookISBN char(13) not null,BookName varchar(50) not null,BookPrice decimal(10,2) not null,"
										+ "BookNum int(10) not null,BookImageUrl varchar(225) not null);";
							db.prepareSQL(sql);
							db.pst.executeUpdate(sql);
							out.println("ok");		
						}		
					}catch(SQLException error){
						out.println("failed");
						error.printStackTrace();
					}	
					break;
					
				case 3://登录后显示销量前6本书
					sql="select * from book  order by BookSold DESC limit 6;";
					db.prepareSQL(sql);
					try{
						ret=db.pst.executeQuery();
						String[] bookurl =new String[6];
						String[] bookisbn =new String[6];
						String[] bookname =new String[6];
						int i=0;
						while(ret.next()){
							bookisbn[i]=ret.getString(1);
							bookname[i]=ret.getString(2);
							bookurl[i]=ret.getString(9);
							i++;
						}
						for(int j=0;j<6;j++){
							out.println(bookisbn[j]);
							out.println(bookname[j]);
							out.println(bookurl[j]);
						}
						
					}catch(SQLException error){
						error.printStackTrace();
					}
					
					out.println("ok");
					break;
					
				case 4://点击书本
					String bookisbn = in.readLine();
					//System.out.println(bookisbn);
					String bookname = null;
					String bookauthor = null;
					String bookpublish = null;
					String bookprice = null;
					String bookremain = null;
					String bookdescription = null;
					String booksoldnum = null;
					String bookimgurl = null;
					
					sql="select * from book where BookISBN="+bookisbn;
					db.prepareSQL(sql);
					try{
						ret=db.pst.executeQuery();
						while(ret.next()){
							bookname=ret.getString(2);
							bookauthor=ret.getString(3);
							bookpublish=ret.getString(4);
							bookprice=ret.getString(5);
							bookremain=ret.getString(6);
							booksoldnum=ret.getString(7);
							bookdescription=ret.getString(8);
							bookimgurl=ret.getString(9);
						}
						out.println(bookname);
						out.println(bookauthor);
						out.println(bookpublish);
						out.println(bookprice);
						out.println(bookremain);
						out.println(booksoldnum);
						out.println(bookdescription);
						out.println(bookimgurl);
					}catch(SQLException error){
						error.printStackTrace();
					}
					break;
					
				case 5://进入个人信息
					String myAccount=in.readLine();
					//System.out.println(myAccount);
					String myPwd = null;
					String myNickname = null;
					String myPhone = null;
					String myAddress = null;
					sql="select * from member where MemberAccount='"+myAccount+"'";
					db.prepareSQL(sql);
					try{
						ret=db.pst.executeQuery();
						while(ret.next()){
							myPwd=ret.getString(3);
							myNickname=ret.getString(4);
							myPhone=ret.getString(5);
							myAddress=ret.getString(6);
						}
						out.println(myPwd);
						out.println(myNickname);
						out.println(myPhone);
						out.println(myAddress);
					}catch(SQLException error){
						error.printStackTrace();
					}
					break;
					
				case 6://修改密码
					String mAccount=in.readLine();
					String mPwd=in.readLine();
					sql="update member set MemberPwd='"+mPwd+"' where MemberAccount='"+mAccount+"'";
					db.prepareSQL(sql);
					try{
						db.pst.executeUpdate(sql);
						out.println("密码修改成功！");
					}catch(SQLException error){
						out.println("密码修改失败！");
						error.printStackTrace();
					}
					break;
					
				case 7://修改昵称
					String mAccount1=in.readLine();
					String mNickname=in.readLine();
					sql="update member set MemberNickname='"+mNickname+"' where MemberAccount='"+mAccount1+"'";
					db.prepareSQL(sql);
					try{
						db.pst.executeUpdate(sql);
						out.println("昵称修改成功！");
					}catch(SQLException error){
						out.println("昵称修改失败！");
						error.printStackTrace();
					}
					break;
					
				case 8://修改号码
					String mAccount2=in.readLine();
					String mPhone=in.readLine();
					sql="update member set MemberPhone="+mPhone+" where MemberAccount='"+mAccount2+"'";
					db.prepareSQL(sql);
					try{
						db.pst.executeUpdate(sql);
						out.println("号码修改成功！");
					}catch(SQLException error){
						out.println("号码修改失败！");
						error.printStackTrace();
					}
					break;
					
				case 9://修改地址
					String mAccount3=in.readLine();
					String mAddress=in.readLine();
					sql="update member set MemberAddress='"+mAddress+"' where MemberAccount='"+mAccount3+"'";
					db.prepareSQL(sql);
					try{
						db.pst.executeUpdate(sql);
						out.println("地址修改成功！");
					}catch(SQLException error){
						out.println("地址修改失败！");
						error.printStackTrace();
					}
					break;
					
				case 10://搜索书本
					String searchContent=in.readLine();
//					System.out.println(searchContent);
					ArrayList<String> searchBookUrl=new ArrayList<String>();
					ArrayList<String> searchBookIsbn=new ArrayList<String>();
					ArrayList<String> searchBookName=new ArrayList<String>();
					ArrayList<String> searchBookAuthor=new ArrayList<String>();
					ArrayList<String> searchBookPrice=new ArrayList<String>();
					
					char[] ch = searchContent.toCharArray();//将字符串转换成char数组
                    int length=ch.length;
                    String fuzzyQuery="";
                    for (char element : ch) {//提取元素
                    	fuzzyQuery+="%%"+element;    
                    }
                    fuzzyQuery+="%%"; 
                    //System.out.println(fuzzyQuery);
					
					sql="select * from book where BookName='"+searchContent+"'" ;
					db.prepareSQL(sql);
					try{
						ret=db.pst.executeQuery(sql);
						int num=0;
						while(ret.next()){
							searchBookIsbn.add(ret.getString(1));
							searchBookName.add(ret.getString(2));
							searchBookAuthor.add(ret.getString(3));
							searchBookPrice.add(ret.getString(5));
							searchBookUrl.add(ret.getString(9));
							num++;
						}
						//模糊查询
						if(num != 0)
							sql="select * from book where BookName LIKE '"+fuzzyQuery+"' and bookisbn <>(select bookisbn from book where bookname='"+searchContent+"')";
						else
							sql="select * from book where BookName LIKE'"+fuzzyQuery+"'";
						//System.out.println(sql);
						
						db.prepareSQL(sql);
						ret=db.pst.executeQuery(sql);
						while(ret.next()){
							searchBookIsbn.add(ret.getString(1));
							searchBookName.add(ret.getString(2));
							searchBookAuthor.add(ret.getString(3));
							searchBookPrice.add(ret.getString(5));
							searchBookUrl.add(ret.getString(9));
							num++;
						}
						out.println(num+"");	
						for(int i=0;i<num;i++){
							out.println(searchBookIsbn.get(i));
							out.println(searchBookName.get(i));
							out.println(searchBookAuthor.get(i));
							out.println(searchBookPrice.get(i));
							out.println(searchBookUrl.get(i));
						}	
					}catch(SQLException error){
						error.printStackTrace();
					}
					break;
					
				case 11://加入购物车
					String memberaccount=in.readLine();
					String bookIsbn=in.readLine();
					String bookName = in.readLine();
					String bookPrice = in.readLine();
					String num=in.readLine();
					int bookNum=Integer.parseInt(num);
					String bookImgurl = in.readLine();	
					String bookRemain=in.readLine();
					
					sql="insert into shopcar"+memberaccount+"(BookISBN,BookName,BookPrice,BookNum,BookImageUrl) values('"+bookIsbn+"','"+bookName+"','"
							+bookPrice+"','"+bookNum+"','"+bookImgurl+"')";				
					db.prepareSQL(sql);
					try{
						db.pst.executeUpdate(sql);
						out.println("insert succeed");
					}catch(SQLException error){
						out.println("insert failed");
						error.printStackTrace();
					}
					break;
					
				case 12://查询购物车
					ArrayList<String> shopCarBookUrl=new ArrayList<String>();
					ArrayList<String> shopCarBookName=new ArrayList<String>();
					ArrayList<String> shopCarBookPrice=new ArrayList<String>();
					ArrayList<String> shopCarBookIsbn=new ArrayList<String>();
					ArrayList<String> shopCarBookNum=new ArrayList<String>();
					ArrayList<String> shopCarItemID=new ArrayList<String>();

					sql="select * from shopcar"+in.readLine();
					db.prepareSQL(sql);
					try{
						ret=db.pst.executeQuery(sql);
						int num2=0;
						if(ret.next()==false){//没有这本书
							out.println(num2+"");
						}else{
							do{
								shopCarItemID.add(ret.getString(1));
								shopCarBookIsbn.add(ret.getString(2));
								shopCarBookName.add(ret.getString(3));
								shopCarBookPrice.add(ret.getString(4));
								shopCarBookNum.add(ret.getString(5));
								shopCarBookUrl.add(ret.getString(6));
								num2++;
							}while(ret.next());
							
							out.println(num2+"");
							for(int i=0;i<num2;i++){
								out.println(shopCarItemID.get(i));
								out.println(shopCarBookIsbn.get(i));
								out.println(shopCarBookName.get(i));
								out.println(shopCarBookPrice.get(i));
								out.println(shopCarBookNum.get(i));
								out.println(shopCarBookUrl.get(i));
							}
						}		
					}catch(SQLException error){
						error.printStackTrace();
					}
					break;
					
				case 13://查询订单
					ArrayList<String> orderBookUrl=new ArrayList<String>();
					ArrayList<String> orderBookNum=new ArrayList<String>();
					ArrayList<String> orderBookSum=new ArrayList<String>();
					ArrayList<String> orderState=new ArrayList<String>();
					ArrayList<String> orderID=new ArrayList<String>();
					ArrayList<String> orderDate=new ArrayList<String>();
					
					sql="select * from transaction where OrderMemberAccount='"+in.readLine()+"'";
					db.prepareSQL(sql);
					try{
						ret=db.pst.executeQuery(sql);
						int num3=0;
						if(ret.next()==false){//没有这本书
							out.println(num3+"");
						}else{
							do{
								orderID.add(ret.getString(1));
								orderState.add(ret.getString(7));
								orderBookNum.add(ret.getString(4));
								orderBookSum.add(ret.getString(6));
								orderBookUrl.add(ret.getString(8));
								orderDate.add(ret.getString(5));
								num3++;
							}while(ret.next());
							
							out.println(num3+"");
							for(int i=0;i<num3;i++){
								out.println(orderID.get(i));
								out.println(orderBookNum.get(i));
								out.println(orderDate.get(i));
								out.println(orderBookSum.get(i));
								out.println(orderState.get(i));
								out.println(orderBookUrl.get(i));
							}
						}		
					}catch(SQLException error){
						error.printStackTrace();
					}
					break;
					
				case 14://取消订单
					String orderid=in.readLine();
					String ordernum = in.readLine();
					String orderurl = in.readLine();
					sql="delete from transaction where orderid ='"+orderid+"'";				
					db.prepareSQL(sql);
					try{
						db.pst.executeUpdate(sql);
						//获取库存
						sql="select bookremain from book where bookimageurl ='"+orderurl+"'";				
						db.prepareSQL(sql);
						ret=db.pst.executeQuery(sql);

						//库存+
						int remain14 = 0;
						while(ret.next())
							remain14 = ret.getInt(1);
						remain14 += Integer.parseInt(ordernum);
						sql="update book set BookRemain="+remain14+" where bookimageurl='"+orderurl+"'";				
						db.prepareSQL(sql);
						db.pst.executeUpdate(sql);
						
						out.println("delete succeed");
					}catch(SQLException error){
						out.println("delete failed");
						error.printStackTrace();
					}
					break;
					
				case 15://下订单
					String itemid =in.readLine();
					String userid15 =in.readLine();
					String bookisbn15 = in.readLine();
					String num15 = in.readLine();
					String time15 = in.readLine();
					String sum15 = in.readLine();
					String state15 = in.readLine();
					String url15 = in.readLine();
					sql="select bookremain from book where bookisbn ='"+bookisbn15+"'";				
					db.prepareSQL(sql);
					try{
						//判断有没有库存
						ret=db.pst.executeQuery(sql);
						int remain15 = 0;
						while(ret.next())
							remain15 = ret.getInt(1);
						if(remain15>Integer.parseInt(num15)){
							//添加到订单
								sql="insert into transaction (ordermemberaccount,orderbookisbn,ordernum,orderdate,ordersum,orderstate,orderurl)"
										+ " values('"+userid15+"','"+bookisbn15+"',"
									+Integer.parseInt(num15)+",'"+time15+"',"+Double.parseDouble(num15)+",'"+state15+"','"+url15+"')";
							db.prepareSQL(sql);
							db.pst.executeUpdate(sql);
							//从购物车中删除
							sql="delete from shopcar"+userid15+" where itemid="+itemid;
							db.prepareSQL(sql);
							db.pst.executeUpdate(sql);
							//库存-
							remain15 -= Integer.parseInt(num15);
							sql="update book set BookRemain="+remain15+" where BookISBN="+bookisbn15;				
							db.prepareSQL(sql);
							db.pst.executeUpdate(sql);
							
							out.println("succeed");
						}else{
							out.println("failed");
						}
					}catch(SQLException error){
						out.println("failed");
						error.printStackTrace();
					}
					break;
					
				case 16://购物车中删除此商品
					String itemid16=in.readLine();
					String userid16 = in.readLine();
					sql="delete from shopcar"+userid16+" where itemid ='"+itemid16+"'";				
					db.prepareSQL(sql);
					try{
						db.pst.executeUpdate(sql);
						out.println("delete succeed");
					}catch(SQLException error){
						out.println("delete failed");
						error.printStackTrace();
					}
					break;
					
				case 17://管理员登录
					String id17=in.readLine();
					String pwd17=in.readLine();

					if(id17==null||id17.length()<=0){
						out.println("errorEmp");
						break;
					}
					sql = "select * from admin where AdminAccount='"+id17+"'";
					db.prepareSQL(sql);
					try{
						ret=db.pst.executeQuery();
						String mAccount17;
						String mPwd17;
						if(ret.next()==false){//没有该用户
			      			out.println("errorN");
						}else{
							mAccount17 = ret.getString(2);
							mPwd17 = ret.getString(3);		
//							System.out.println(id17+" "+ pwd17+"  "+mAccount17+" "+mPwd17);
							if(pwd17.equals(mPwd17)){
								out.println("success");
								adminNum++;
								System.out.println("第"+adminNum+"个管理员已登录");
							}else{
								out.println("errorP");
							}
						}
					}catch(SQLException error){
						error.printStackTrace();
					}
					break;
						
				case 18://管理员注册
					String adminname18=in.readLine();
					String adminpassword18=in.readLine();
					
					sql="select * from admin where AdminAccount='"+adminname18+"'";
					db.prepareSQL(sql);										
					try{
						ret=db.pst.executeQuery();
						if(ret.next()){
							out.println("exist");
						}else{
							sql="insert into admin(AdminAccount,AdminPwd) "
									+ "values('"+adminname18+"','"+adminpassword18+"');";
							db.prepareSQL(sql);
							db.pst.executeUpdate(sql);

							out.println("ok");		
						}		
					}catch(SQLException error){
						out.println("failed");
						error.printStackTrace();
					}	
					break;
						
						
				case 19://管理员登录初始化Index
					sql="select * from book  order by BookSold DESC limit 6;";
					db.prepareSQL(sql);
					try{
						ret=db.pst.executeQuery();
						String[] bookurl19 =new String[6];
						String[] bookisbn19 =new String[6];
						String[] bookname19 =new String[6];
						int i=0;
						while(ret.next()){
							bookisbn19[i]=ret.getString(1);
							bookname19[i]=ret.getString(2);
							bookurl19[i]=ret.getString(9);
							i++;
						}
						for(int j=0;j<6;j++){
							out.println(bookisbn19[j]);
							out.println(bookname19[j]);
							out.println(bookurl19[j]);
						}
						
					}catch(SQLException error){
						error.printStackTrace();
					}
					out.println("ok");
					break;
						
				case 20://更新库存
					String bookisbn20=in.readLine();
					String bookremian20=in.readLine();
					
					sql="update book set BookRemain="+bookremian20+" where bookisbn='"+bookisbn20+"'";
					db.prepareSQL(sql);										
					try{
						db.pst.executeUpdate(sql);
						out.println("ok");		
								
					}catch(SQLException error){
						out.println("failed");
						error.printStackTrace();
					}	
					break;
					
				case 21://下架书籍
					String bookisbn21=in.readLine();
					
					sql="delete from book where bookisbn='"+bookisbn21+"'";
					db.prepareSQL(sql);										
					try{
						db.pst.executeUpdate(sql);
						out.println("ok");		
								
					}catch(SQLException error){
						out.println("failed");
						error.printStackTrace();
					}	
					break;
					
				case 22://查询用户
					String memberid22=in.readLine();
					
					sql="select * from member where memberid='"+memberid22+"'";
					db.prepareSQL(sql);										
					try{
						ret=db.pst.executeQuery();
						if(ret.next()){
							out.println("success");		
							out.println(ret.getString(2));
							out.println(ret.getString(4));
							out.println(ret.getString(5));
							out.println(ret.getString(6));
						}else{
							out.println("failed");		
						}
								
					}catch(SQLException error){
						out.println("failed");
						error.printStackTrace();
					}	
					break;
					
				case 23://处理订单，发货
					String orderid23=in.readLine();
					sql="update transaction set OrderState= '已处理' where orderid='"+orderid23+"'";				
					db.prepareSQL(sql);
					try{
						db.pst.executeUpdate(sql);
						out.println("succeed");
					}catch(SQLException error){
						out.println("failed");
						error.printStackTrace();
					}
					break;
					
				case 24://取消订单
					String orderid24=in.readLine();
					sql="delete from transaction where orderid ='"+orderid24+"'";				
					db.prepareSQL(sql);
					try{
						db.pst.executeUpdate(sql);
						out.println("delete succeed");
					}catch(SQLException error){
						out.println("delete failed");
						error.printStackTrace();
					}
					break;
					
				case 25://查询订单
					ArrayList<String> orderBookUrl25=new ArrayList<String>();
					ArrayList<String> orderBookNum25=new ArrayList<String>();
					ArrayList<String> orderBookSum25=new ArrayList<String>();
					ArrayList<String> orderState25=new ArrayList<String>();
					ArrayList<String> orderID25=new ArrayList<String>();
					ArrayList<String> orderDate25=new ArrayList<String>();
					
					sql="select * from transaction";
					db.prepareSQL(sql);
					try{
						ret=db.pst.executeQuery(sql);
						int num25=0;
						if(ret.next()==false){
							out.println(num25+"");
						}else{
							do{
								orderID25.add(ret.getString(1));
								orderState25.add(ret.getString(7));
								orderBookNum25.add(ret.getString(4));
								orderBookSum25.add(ret.getString(6));
								orderBookUrl25.add(ret.getString(8));
								orderDate25.add(ret.getString(5));
								num25++;
							}while(ret.next());
							
							out.println(num25+"");
							for(int i=0;i<num25;i++){
								out.println(orderID25.get(i));
								out.println(orderBookNum25.get(i));
								out.println(orderDate25.get(i));
								out.println(orderBookSum25.get(i));
								out.println(orderState25.get(i));
								out.println(orderBookUrl25.get(i));
							}
						}		
					}catch(SQLException error){
						error.printStackTrace();
					}
					break;
					
				case 26://添加图书时获取新的isbn号
					sql="select * from book";
					db.prepareSQL(sql);										
					try{
						ret=db.pst.executeQuery(sql);
						ret.last();
						out.println(ret.getString(1));				
					}catch(SQLException error){
						out.println("failed");
						error.printStackTrace();
					}	
					break;
					
				case 27://确认添加图书
                    String[] newBookArray = new String[8];
					
					for(int i = 0;i<8;i++)
						newBookArray[i] = in.readLine();
					
					sql="insert into book(bookisbn,bookname,bookauthor,bookpublish,bookprice,BookRemain,booksold,bookdescription,BookImageUrl) "
									+ "values('"+newBookArray[0]+"','"+newBookArray[1]+"','"+newBookArray[2]+"','"+newBookArray[3]
									+"',"+Double.parseDouble(newBookArray[4])+","+Integer.parseInt(newBookArray[5])
									+","+0+",'"+newBookArray[6]+"','"+newBookArray[7]+"');";
					db.prepareSQL(sql);										
					try{
						db.pst.executeUpdate(sql);
						out.println("success");				
					}catch(SQLException error){
						out.println("failed");
						error.printStackTrace();
					}	
					break;
					
				default :
					out.println("error");
					break;
		        }
			}catch (Exception error){
		        error.printStackTrace();
            }finally{
    	        if(socket!=null){
    	        	try{
    	        		socket.close();
    	        	}catch(Exception e){
    	        		socket=null;
    	        		System.out.println("服务器finally异常："+e.getMessage());
    	        	}
    	        }
            }
			//totalNum=userNum+adminNum;
			l1.setText("登录的普通用户个数："+userNum);
			l2.setText("登录的管理员个数："+adminNum);
			//System.out.println("第"+i+"个线程消失");
	    }

	}
	
    public void GUI(){
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//以便自己设置位置
		frame.setLayout(null);
		
		l1.setText("登录的普通用户个数："+userNum);
		l1.setFont(new Font("SanSerif",Font.BOLD,20));
		l1.setBounds(100, 180, 230, 30);
		l2.setText("登录的管理员个数："+adminNum);
		l2.setFont(new Font("SanSerif",Font.BOLD,20));
		l2.setBounds(100, 230, 230, 30);
		frame.add(l1);
		frame.add(l2);
		
		frame.setSize(500,500);
		frame.setVisible(true);
		frame.addWindowListener(new myWindowAdapter(){//监听关闭事件
			public void windowClosing(WindowEvent e) {  
				super.windowClosing(e);  
				System.out.println("----------服务器已关闭-----------");
			}
		});
    }
		private class myWindowAdapter extends WindowAdapter{
			public void windowClosing(WindowEvent e) {  
	 
				 }  
	    }

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Server s=new Server();
		s.initServerSocket();
	}

}
