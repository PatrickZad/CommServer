package dataservice;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import common.Utility;
import communicate.Communicate;
import net.sf.json.JSONObject; 

/**
 * Servlet implementation class DataService
 */
@WebServlet("/DataService")
public class DownloadService extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static DataSource deviceDS;
    private boolean dbReady=false;
    //String xlsPath="C:\\\\ProgramData\\\\MySQL\\\\MySQL Server 5.7\\\\Uploads\\\\";//for windows
    String xlsPath="/var/lib/mysql-files/";//for linux 
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DownloadService() {
        super();
        // TODO Auto-generated constructor stub
        Context context=null;
        try{
        	context=new InitialContext();
        	DownloadService.deviceDS=(DataSource)context.lookup("java:/comp/env/devicesDS");
        	this.dbReady=true;
        	//
        	Communicate.logger.info("DataSource Created!");
            //
        }catch(Exception e){
        	//e.printStackTrace();
        }
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
	    if(this.dbReady){
		    JSONObject info=Utility.sheetInfo(request); 
	        XLSHandler xlshdlr=new XLSHandler(DownloadService.deviceDS,xlsPath);
	        
	        String fileName=xlshdlr.xlsGenerator(info.getString("deviceID"),info.getString("staTime"),info.getString("endTime"));
	        if((fileName!=null)&dbReady){
	            @SuppressWarnings("resource")
				BufferedInputStream ins=new BufferedInputStream(new FileInputStream(xlsPath+fileName));
	            BufferedOutputStream outs=new BufferedOutputStream(response.getOutputStream());
	            response.setHeader("Content-Disposition", "attachment;filename="+fileName);
	            byte[] bytes=new byte[ins.available()];
	            String temp=null;
	            int read=0;
	            StringBuilder builder=new StringBuilder();
	            while((read=ins.read(bytes))!=-1){
	        	    temp=new String(bytes,"utf-8");
	        	    builder.append(temp);
	            }
	            outs.write(temp.getBytes("gbk"));
	            outs.flush();
	            outs.close();
	        }else{
	        	response.getWriter().append("Mission Failed!");
	        }
	    }else{
	    	response.getWriter().append("DBerror!");
	    }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	    if(this.dbReady){
	        XLSHandler xlshdlr=new XLSHandler(DownloadService.deviceDS,xlsPath);
	        Communicate.logger.debug("requestInfoGot");
	        String fileName=xlshdlr.xlsGenerator(request.getParameter("deviceID"),request.getParameter("staTime"),request.getParameter("endTime"));
	        if(fileName!=null){
	            @SuppressWarnings("resource")
				BufferedInputStream ins=new BufferedInputStream(new FileInputStream(xlsPath+fileName));
	            BufferedOutputStream outs=new BufferedOutputStream(response.getOutputStream());
	            response.setHeader("Content-Disposition", "attachment;filename="+fileName);
	            Communicate.logger.debug("StartTransferingFile");
	            byte[] bytes=new byte[ins.available()];
	            byte[] buffer;
	            String temp=null;
	            //int len=0;
	            //StringBuilder builder=new StringBuilder();
	            while((ins.read(bytes))!=-1){
	        	    temp=new String(bytes,"utf-8");
	        	    buffer=temp.getBytes("gbk");
	        	    outs.write(buffer);
	        	    //builder.append(temp);
	            }
	            //outs.write(temp.getBytes("gbk"));
	            outs.flush();
	            outs.close();
	        }else{
	        	response.getWriter().append("Mission Failed!");
	        }
	    }else{
	    	response.getWriter().append("DBerror!");
	    }
	}

}
