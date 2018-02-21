package communicate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;

import common.Utility;
import net.sf.json.JSONObject;

class SendDataTask implements Runnable {

//	private byte[] byteData;
	private JSONObject jasondata;
	//
	
	/*
	public SendDataTask(String data){
		this.data=data;
	}
	*/
	public SendDataTask(byte[] bytes){
		//this.byteData=bytes;
		this.jasondata=bytes2json(bytes);
	}
	//向clients广播data
	@Override
	public void run() {
		// TODO Auto-generated method stub
        if(!Communicate.clients.isEmpty()){
        	/*ExecutorService executor=Executors.newFixedThreadPool(Communicate.clients.size());
        	Communicate.logger.debug("ExcutorServiceGenerated");
            for(Client client : Communicate.clients){	
        	//client.setData(byteData);
            	client.setData(jasondata.toString());
            	executor.execute(client);
            	Communicate.logger.debug("MissionSubmited2"+client.toString());
            }
            executor.shutdown();
            */
        	//多线程发送
        	/*
        	List<Client> list;
        	if(Communicate.clients.size()>4)
        	{list=Communicate.clients.subList(Communicate.clients.size()-4, Communicate.clients.size());
        	}else{
        		list=Communicate.clients;
        	}*/
        	for(Client client : Communicate.clients){
            	client.setData(jasondata.toString());
            	//线程发送
            	//new Thread(client).start();
            	Communicate.executor.execute(client); 
            	Communicate.logger.debug(jasondata.toString()+" TransferedByThread to"+client.toString());
            }
            //迭代器，避免ConcurrentModificationException
            /*
            Iterator<Client> iter=Communicate.clients.iterator();
            Client client;
            while(iter.hasNext()){
            	client=iter.next();
            	new Thread(client).start();
            	Communicate.logger.debug(jasondata.toString()+"\nIteratorTransferedByThread to"+client.toString());
            }*/
        }
	}
    private JSONObject bytes2json(byte[] bytes){
    	String ID = null;
    	float longitude = 0;
    	float latitude=0;
    	byte state = 0;
    	String staStr = null;
    	short speed = 0;
    	String speStr;
    	byte soc=0;
    	String socStr;
    	short tMaxH2=0;
    	String tMHStr;
    	int mMaxH2=0;
    	String mMHStr;
    	float pMaxH2=0;
    	String pMHStr;
    	int voltage=0;
    	String volStr;
    	short current=0;
    	String curStr;
    	byte mState=0;
    	String mStStr = null;
    	int mSpeed=0;
    	String mSpStr;
    	int mTorque=0;
    	String mToStr;
    	byte alarm=0;
    	List<String> alaStr=new ArrayList<String>();
    	JSONObject jsondata=null;
    	//
    	if(bytes.length==30){
            Byte bytNum;
            int intNum;
            short shtNum=0;
            //LRC
            /*
            for(int i=1;i<29;i++){
        	   shtNum=(short) (shtNum+Utility.bytes2short(bytes, i, i));
        	}
            shtNum=(short)(~shtNum+1);
            if((byte)shtNum==bytes[29]){
            */
        	   //id
        	   bytNum=new Byte(bytes[0]);
        	   ID=bytNum.toString();
        	   //longitude
        	   intNum=Utility.bytes2int(bytes, 1, 4);
        	   longitude=(float)intNum/1000000;
        	   //latitude
        	   intNum=Utility.bytes2int(bytes, 5, 8);
        	   latitude=(float)intNum/1000000;
        	   //state
        	   state=bytes[9];
        	   //speed
        	   speed=Utility.bytes2short(bytes, 10, 11);
        	   //soc
        	   soc=bytes[12];
        	   //tMaxH2
        	   tMaxH2=Utility.bytes2short(bytes, 13, 14);
        	   //mMaxH2
        	   mMaxH2=Utility.bytes2int(bytes, 15, 16);
        	   //pMaxH2
        	   shtNum=Utility.bytes2short(bytes, 17, 18);
        	   pMaxH2=(float)shtNum/10;
        	   //voltage
        	   voltage=Utility.bytes2int(bytes, 19, 20);
        	   //current
        	   current=Utility.bytes2short(bytes, 21, 22);
        	   //mState
        	   mState=bytes[23];
        	   //mSpeed
        	   mSpeed=Utility.bytes2int(bytes, 24, 25);
        	   //mTorque
        	   mTorque=Utility.bytes2int(bytes, 26, 27);
        	   //alarm
        	   alarm=bytes[28];
    	   //}
      }
    	//transform
    	Float fltNum;
		Byte bytNum;
		Integer intNum;
		//
		switch(state){
		case 0x01 : staStr="就绪";break;
		case 0x02 : staStr="熄火";break;
		case 0x03 : staStr="其他";break;
		case (byte) 0xfe : staStr="异常";break;
		case (byte) 0xff : staStr="无效";break;
		}
		//
		switch(speed){
		case (short) 0xfffe:speStr="异常";break;
		case (short) 0xffff:speStr="无效";break;
		default: fltNum=new Float((float)speed/10);
		         speStr=fltNum.toString();
		}
		//SOC
		switch(soc){
		case (byte)0x0fe:socStr="异常";break;
		case (byte)0x0ff:socStr="无效";break;
		default : bytNum=new Byte(soc);
		          socStr=bytNum.toString();
		}
		//
		switch(tMaxH2){
		case (short)0xfffe:tMHStr="异常";break;
		case (short)0xffff:tMHStr="无效";break;
		default:fltNum=new Float(tMaxH2/10-40);
		        tMHStr=fltNum.toString();
		}
		//
		switch(mMaxH2){
		case 0x0fffe:mMHStr="异常";break;
		case 0x0ffff:mMHStr="无效";break;
		default:intNum=new Integer(mMaxH2);
		        mMHStr=intNum.toString();
		}
		//
		pMHStr=new Float(pMaxH2).toString();
		//
		switch(voltage){
		case 0x0fffe:volStr="异常";break;
		case 0x0ffff:volStr="无效";break;
		default: fltNum=new Float(voltage/10);
		         volStr=fltNum.toString();
		}
		//
		switch(current){
		case (short)0xfffe:curStr="异常";break;
		case (short)0xffff:curStr="无效";break;
		default :fltNum=new Float((float)current/10-1000);
		         curStr=fltNum.toString();
		}
		//
		switch(mState){
		case 0x01:mStStr="耗电";break;
		case 0x02:mStStr="发电";break;
		case 0x03:mStStr="关闭";break;
		case 0x04:mStStr="准备";break;
		case (byte)0xfe:mStStr="异常";break;
		}
		//
		switch(mSpeed){
		case 0x0fffe:mSpStr="异常";break;
		case 0x0ffff:mSpStr="无效";break;
		default : intNum=new Integer(mSpeed-20000);
		          mSpStr=intNum.toString();
		}
		//
		switch(mTorque){
		case 0x0fffe:mToStr="异常";break;
		case 0x0ffff:mToStr="无效";break;
		default :fltNum=new Float(((float)mTorque-20000)/10);
		         mToStr=fltNum.toString();
		}
		//
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
		//
		try{
			jsondata=new JSONObject();
			jsondata.put("ID",ID );
			fltNum=new Float(longitude);
			jsondata.put("longi",fltNum.toString());
			fltNum=new Float(latitude);
			jsondata.put("lati",fltNum.toString());
		    jsondata.put("state",staStr );
		    jsondata.put("speed",speStr );
		    jsondata.put("soc",socStr );
		    jsondata.put("tMaxH2",tMHStr );
		    jsondata.put("mMaxH2",mMHStr );
		    jsondata.put("pMaxH2",pMHStr );
		    jsondata.put("voltage", volStr);
		    jsondata.put("current",curStr );
		    jsondata.put("mState",mStStr );
		    jsondata.put("mSpeed",mSpStr);
		    jsondata.put("mTorgue",mToStr );
		    jsondata.put("alarm",alaStr );
		}catch(Exception e){
			e.printStackTrace();
		}
    	return jsondata;
    }
}
