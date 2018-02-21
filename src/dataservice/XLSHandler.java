package dataservice;

import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.sql.DataSource;

import communicate.Communicate;

public class XLSHandler {
	String xlsPath;
	private DataSource dataSrc;
	public XLSHandler(DataSource dataSrc,String xlsPath){
		this.dataSrc=dataSrc;
		//"D:/Tomcat/myAppLogs/CommServer/"
		this.xlsPath=xlsPath;
	}
	public String xlsGenerator(String deviceID,String staDate,String endDate){
		Connection conn=null;
		String fileName=null;
		if(this.dataSrc!=null){
		try{
			fileName=System.currentTimeMillis()+"_id"+deviceID+".xls";
			conn=this.dataSrc.getConnection();
			//
			Communicate.logger.info("Connection Generated!");
			//
			PreparedStatement statmt=conn.prepareStatement("SELECT* FROM device_temple UNION SELECT * FROM device_"+deviceID+
					                 " WHERE time BETWEEN '"+staDate+" 00:00:00'"+" AND '"+
					                 endDate+" 00:00:00'"+" INTO OUTFILE '"+this.xlsPath+fileName+"';");
			statmt.execute();
			//
			Communicate.logger.info("File Generated!");
			//
		}catch(Exception e){
			e.printStackTrace();
			fileName=null;
		}
		}
		return fileName;
	}
	public String xlsGenerator(String deviceID){
		Connection conn=null;
		String fileName=null;
		if(this.dataSrc!=null){
		try{
			fileName=System.currentTimeMillis()+deviceID+".xls";
			conn=this.dataSrc.getConnection();
			PreparedStatement statmt=conn.prepareStatement("SELECT* FROM device_temple UNION SELECT * FROM device_"+deviceID+
					                 " INTO OUTFILE '"+this.xlsPath+fileName+";");
			statmt.execute();
		}catch(Exception e){
			e.printStackTrace();
			fileName=null;
		}
		}
		return fileName;
	} 
	public String xlsGenerator(){
		String fileName=null;
		return fileName;
	}
}
