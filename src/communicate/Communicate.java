package communicate;

import java.io.IOException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import dataservice.StoreDataService;

import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Servlet implementation class Communicate
 */
@WebServlet("/Communicate")
public class Communicate extends HttpServlet {
	/*Logger by log4j1.2.17*/
	public static Logger logger=Logger.getLogger(Communicate.class);
	/*ListenThread*/
	public static Thread clientListenTread;
	public static Thread deviceListenTread;
	/*ServerSocket*/
	public static ServerSocket deviceSS; 
    public static ServerSocket clientSS;
    /*running state*/
    public static boolean started=false; 
	/*for debug*/
	private static final long serialVersionUID = 1L;
    private int devicePort=16000;
    private int clientPort=18000;
    static List<StoreDataService> dataList=new ArrayList<StoreDataService>();
    static List<Device> devices=new ArrayList<Device>();
    static List<Client> clients=new ArrayList<Client>();
    static ExecutorService executor=Executors.newCachedThreadPool();//Executors.newFixedThreadPool(3);
    static DataSource devicesDS;//数据库对象
    static Context context;
    
    /** 
     * @see HttpServlet#HttpServlet()
     */
    public Communicate() {
        super();
        // TODO Auto-generated constructor stub        
       //boolean started=false;
 	 //  ServerSocket deviceSS=null; 
     //  ServerSocket clientSS=null;
       //配置数据库
       try{
           Communicate.context=new InitialContext();
       }catch(Exception e){
    	   e.printStackTrace();
       }
       /*数据库单独测试段*/
       //if(this.devicesDS!=null)
       //new Thread(new DBConnTest(this.devicesDS)).start();
       /*测试段结束*/
       //开启端口监听 
       /*
       try{
     	   deviceSS=new ServerSocket(devicePort); 
           clientSS=new ServerSocket(clientPort);
           started=true;
           //Log提示启动
           //Communicate.logger.info("Service Started!");
       }catch(Exception e){
        	//e.printStackTrace();
        }
       //开启连接监听
        if(started){
            Communicate.clientListenTread=new Thread(new ClientListener(clientSS));
            Communicate.clientListenTread.start();
            Communicate.deviceListenTread=new Thread(new DeviceListener(deviceSS));
            Communicate.deviceListenTread.start();
           //Log提示
           //Communicate.logger.info("Start Listening!");
        }
        */
       startupInitialize();
    }


	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		if(!started){
			startupInitialize();
		}
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
    private void startupInitialize(){
    	try{
      	    deviceSS=new ServerSocket(devicePort); 
            clientSS=new ServerSocket(clientPort);
            started=true;
            //Log提示启动
            //Communicate.logger.info("Service Started!");
        }catch(Exception e){
         	//e.printStackTrace();
         }
        //开启连接监听
         if(started){
             Communicate.clientListenTread=new Thread(new ClientListener(clientSS));
             Communicate.clientListenTread.start();
             Communicate.deviceListenTread=new Thread(new DeviceListener(deviceSS));
             Communicate.deviceListenTread.start();
            //Log提示
            //Communicate.logger.info("Start Listening!");
         }
    }
   
}
