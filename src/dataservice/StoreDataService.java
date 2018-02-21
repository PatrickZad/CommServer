package dataservice;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.sql.DataSource;

import common.Utility;
import communicate.Communicate;

/*
 * 
 * */
public class StoreDataService implements Runnable{
	static DataSource devicedata;
	private Connection conn;
	private ExecutorService executor;
	private String ID;//
	private Timestamp timestp;//COLUMN time,DATATYPE datetime
	private String time;
	private float longitude;//COLUMN longitude,DATATYPE float
	private float latitude;//COLUMN latitude,DATATYPE float
	private byte state;//COLUMN state,DATATYPE char
	private String staStr;
	private short speed;//COLUMN speed,DATATYPE varchar(5)
	private String speStr;
	private byte soc;//COLUMN soc,DATATYPE varchar(4)
	private String socStr;
	private short tMaxH2;//COLUMN tMaxH2,DATATYPE varchar(6)
	private String tMHStr;
	private int mMaxH2;//COLUMN mMaxH2,DATATYPE varchar(5)
	private String mMHStr;
	private float pMaxH2;//COLUMN pMaxH2,DATATYPE float(3,1)
	private int voltage;//COLUMN voltage,DATATYPE  varchar(6)
	private String volStr;
	private short current;//COLUMN current,DATATYPE varchar(6)
	private String curStr;
	private byte mState;//COLUMN mState,DATATYPE char(4)
	private String mStStr;
	private int mSpeed;//COLUMN mSpeed,DATATYPE varchr(6)
	private String mSpStr;
	private int mTorque;//COLUMN mTorque,DATATYPE varchar(6)
	private String mToStr;
	private byte alarm;//COLUMN alarm,DATATYPE set('温度差异','电池高温',
	                   //'动力蓄电池包过压','动力蓄电池包欠压','SOC低',
	                   //'单体蓄电池过压','单体蓄电池欠压','SOC过高','正常')
	private List<String> alaStr=new ArrayList<String>();
	private String alaSetStr;
	/*
	 * constructor
	 * */
	public StoreDataService(Context servletContext) throws NamingException, SQLException{
		if(StoreDataService.devicedata==null){
			StoreDataService.devicedata=(DataSource)servletContext.lookup("java:/comp/env/devicesDS");
		}
		this.conn=StoreDataService.devicedata.getConnection();
		this.executor=Executors.newSingleThreadExecutor();
	}
	/*
	 * store data service
	 * */
    public void storeData(byte[] bytes){
    	//
    	if(bytes.length==30){
            Byte bytNum;
            int intNum;
            short shtNum=0;
            //LRC暂时取消
            /*
            for(int i=1;i<29;i++){
        	   shtNum=(short) (shtNum+Utility.bytes2short(bytes, i, i));
        	}
            shtNum=(short)(~shtNum+1);
            if((byte)shtNum==bytes[29]){
            */
        	   //id
        	   bytNum=new Byte(bytes[0]);
        	   this.ID=bytNum.toString();
        	   //time
        	   this.timestp=new Timestamp(System.currentTimeMillis());
        	   //longitude
        	   intNum=Utility.bytes2int(bytes, 1, 4);
        	   this.longitude=(float)intNum/1000000;
        	   //latitude
        	   intNum=Utility.bytes2int(bytes, 5, 8);
        	   this.latitude=(float)intNum/1000000;
        	   //state
        	   this.state=bytes[9];
        	   //speed
        	   this.speed=Utility.bytes2short(bytes, 10, 11);
        	   //soc
        	   this.soc=bytes[12];
        	   //tMaxH2
        	   this.tMaxH2=Utility.bytes2short(bytes, 13, 14);
        	   //mMaxH2
        	   this.mMaxH2=Utility.bytes2int(bytes, 15, 16);
        	   //pMaxH2
        	   shtNum=Utility.bytes2short(bytes, 17, 18);
        	   this.pMaxH2=(float)shtNum/10;
        	   //voltage
        	   this.voltage=Utility.bytes2int(bytes, 19, 20);
        	   //current
        	   this.current=Utility.bytes2short(bytes, 21, 22);
        	   //mState
        	   this.mState=bytes[23];
        	   //mSpeed
        	   this.mSpeed=Utility.bytes2int(bytes, 24, 25);
        	   //mTorque
        	   this.mTorque=Utility.bytes2int(bytes, 26, 27);
        	   //alarm
        	   this.alarm=bytes[28];
        	   //
        	   this.executor.execute(this);
    	   //}
      }
}
	public void storeData(byte[] bytes,String timeString){
		this.time=timeString;
		this.storeData(bytes);
	}
    /*
	 * 
	 * */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		dataMember2Column();
	    updateTable();
	}
	/*
	 * transform number data to column data
	 * 原始数据转换成数据库保存数据
	 * */
	
	void dataMember2Column(){
		Float fltNum;
		Byte bytNum;
		Integer intNum;
		//this.time="'"+this.timestp.toString().substring(0, 19)+"'";
		//
		switch(state){
		case 0x01 : staStr="就绪";break;
		case 0x02 : staStr="熄火";break;
		case 0x03 : staStr="其他";break;
		case (byte) 0xfe : staStr="异常";break;
		case (byte) 0xff : staStr="无效";break;
		}
		staStr="'"+staStr+"'";
		//
		switch(speed){
		case (short) 0xfffe:speStr="异常";break;
		case (short) 0xffff:speStr="无效";break;
		default: fltNum=new Float((float)speed/10);
		         speStr=fltNum.toString();
		}
		speStr="'"+speStr+"'";
		//SOC
		switch(soc){
		case (byte)0x0fe:socStr="异常";break;
		case (byte)0x0ff:socStr="无效";break;
		default : bytNum=new Byte(soc);
		          socStr=bytNum.toString();
		}
		socStr="'"+socStr+"'";
		//
		switch(tMaxH2){
		case (short)0xfffe:tMHStr="异常";break;
		case (short)0xffff:tMHStr="无效";break;
		default:fltNum=new Float(tMaxH2/10-40);
		        tMHStr=fltNum.toString();
		}
		tMHStr="'"+tMHStr+"'";
		//
		switch(mMaxH2){
		case 0x0fffe:mMHStr="异常";break;
		case 0x0ffff:mMHStr="无效";break;
		default:intNum=new Integer(mMaxH2);
		        mMHStr=intNum.toString();
		}
		mMHStr="'"+mMHStr+"'";
		//
		switch(voltage){
		case 0x0fffe:volStr="异常";break;
		case 0x0ffff:volStr="无效";break;
		default: fltNum=new Float(voltage/10);
		         volStr=fltNum.toString();
		}
		volStr="'"+volStr+"'";
		//
		switch(current){
		case (short)0xfffe:curStr="异常";break;
		case (short)0xffff:curStr="无效";break;
		default :fltNum=new Float((float)current/10-1000);
		         curStr=fltNum.toString();
		}
		curStr="'"+curStr+"'";
		//
		switch(mState){
		case 0x01:mStStr="耗电";break;
		case 0x02:mStStr="发电";break;
		case 0x03:mStStr="关闭";break;
		case 0x04:mStStr="准备";break;
		case (byte)0xfe:mStStr="异常";break;
		}
		mStStr="'"+mStStr+"'";
		//
		switch(mSpeed){
		case 0x0fffe:mSpStr="异常";break;
		case 0x0ffff:mSpStr="无效";break;
		default : intNum=new Integer(mSpeed-20000);
		          mSpStr=intNum.toString();
		}
		mSpStr="'"+mSpStr+"'";
		//
		switch(mTorque){
		case 0x0fffe:mToStr="异常";break;
		case 0x0ffff:mToStr="无效";break;
		default :fltNum=new Float(((float)mTorque-20000)/10);
		         mToStr=fltNum.toString();
		}
		mToStr="'"+mToStr+"'";
		//
		alaStr.clear();
		if(alarm==0){
			alaStr.add("正常");
		}else{
		    if((alarm&0x01)==0x01){
			    alaStr.add("温度差异");
		    }
		    if((alarm&0x02)==0x02){
			    alaStr.add("电池高温");
		    }
		    if((alarm&0x04)==0x04){
			    alaStr.add("动力蓄电池包过压");
		    }
		    if((alarm&0x08)==0x08){
			    alaStr.add("动力蓄电池包欠压");
		    }
		    if((alarm&0x10)==0x10){
			    alaStr.add("SOC低");
		    }
		    if((alarm&0x20)==0x20){
			    alaStr.add("单体蓄电池过压");
		    }
		    if((alarm&0x40)==0x40){
		    	alaStr.add("单体蓄电池欠压");
		    }
		    if((alarm&0x80)==0x80){
		    	alaStr.add("SOC过高");
		    }
		}
		StringBuilder builder=new StringBuilder();
		builder.append("'");
		for(String str : alaStr){
			builder.append(str);
			if(!str.equals(alaStr.get(alaStr.size()-1))){
			    builder.append(",");
			}
		}
		builder.append("'");
		this.alaSetStr=builder.toString();
	}
	/*
	 * insert data into table
	 * */
	void updateTable(){
		PreparedStatement stment=null;
		//see if TABLE device_id exists 
		try {
			//generate PreparedStatement 
			stment=conn.prepareStatement("SELECT * FROM device_"+ID);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			stment=null;
		}
		if(stment!=null){
			ResultSet result=null;
			try{
				//query TABLE device_id 
				result=stment.executeQuery();
			}catch(SQLException e){
				//the TABLE doesn`t exist
				result=null;
			}
			if(result==null){
				try {
					//create new TABLE device_id
					stment=conn.prepareStatement("create table device_"+ID+
						   "("+
					       "time DATETIME PRIMARY KEY,"+
						   "longitude FLOAT(5,2),"+
					       "latitude FLOAT(5,2),"+
						   "state CHAR(4),"+
					       "speed VARCHAR(5),"+
						   "soc VARCHAR(4),"+
					       "tMaxH2 VARCHAR(6),"+
						   "mMaxH2 VARCHAR(5),"+
					       "pMaxH2 FLOAT(4,1),"+
						   "voltage VARCHAR(6),"+
					       "current VARCHAR(6),"+
						   "mState CHAR(4),"+
					       "mSpeed VARCHAR(6),"+
						   "mTorque VARCHAR(6),"+
					       "alarm SET('温度差异','电池高温',"+
	                       "'动力蓄电池包过压','动力蓄电池包欠压','SOC低',"+
	                       "'单体蓄电池过压','单体蓄电池欠压','SOC过高','正常')"+
					       ")DEFAULT CHARSET=utf8;");
					stment.executeUpdate();
				} catch (SQLException e1) {
			        // TODO Auto-generated catch block
				    Communicate.logger.debug(e1.toString());
			    }
		    }
			//update TABLE
			try{
				stment=conn.prepareStatement("insert into device_"+ID+" values("+
				           this.time+","+longitude+","+latitude+","+staStr+","+speStr+","+socStr+","+
							tMHStr+","+mMHStr+","+pMaxH2+","+volStr+","+curStr+","+mStStr+","+mSpStr+","+mToStr+","+alaSetStr
						   +");");
				stment.executeUpdate();
			}catch(SQLException e2){
				Communicate.logger.debug(e2.toString());
			}
		}
	}
	/*
	 * release database connection
	 * */
	public void releaseConn(){
		this.executor.shutdown();
		while(!this.executor.isTerminated());
		try {
			this.conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
	}
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public float getLangitude() {
		return longitude;
	}
	public void setLangitude(float langitude) {
		this.longitude = langitude;
	}
	public float getLatitude() {
		return latitude;
	}
	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}
	public byte getState() {
		return state;
	}
	public void setState(byte state) {
		this.state = state;
	}
	public short getSpeed() {
		return speed;
	}
	public void setSpeed(short speed) {
		this.speed = speed;
	}
	public byte getSoc() {
		return soc;
	}
	public void setSoc(byte soc) {
		this.soc = soc;
	}
	public short gettMaxH2() {
		return tMaxH2;
	}
	public void settMaxH2(short tMaxH2) {
		this.tMaxH2 = tMaxH2;
	}
	public int getmMaxH2() {
		return mMaxH2;
	}
	public void setmMaxH2(int mMaxH2) {
		this.mMaxH2 = mMaxH2;
	}
	public float getpMaxH2() {
		return pMaxH2;
	}
	public void setpMaxH2(float pMaxH2) {
		this.pMaxH2 = pMaxH2;
	}
	public int getVoltage() {
		return voltage;
	}
	public void setVoltage(int voltage) {
		this.voltage = voltage;
	}
	public short getCurrent() {
		return current;
	}
	public void setCurrent(short current) {
		this.current = current;
	}
	public byte getmState() {
		return mState;
	}
	public void setmState(byte mState) {
		this.mState = mState;
	}
	public int getmSpeed() {
		return mSpeed;
	}
	public void setmSpeed(int mSpeed) {
		this.mSpeed = mSpeed;
	}
	public int getmTorque() {
		return mTorque;
	}
	public void setmTorque(int mTorque) {
		this.mTorque = mTorque;
	}
	public byte getAlarm() {
		return alarm;
	}
	public void setAlarm(byte alarm) {
		this.alarm = alarm;
	}
}
