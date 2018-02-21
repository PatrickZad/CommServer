package communicate;

import java.io.DataInputStream;
//import java.io.DataOutputStream;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import dataservice.StoreDataService;

class Device implements Runnable {
    private Socket socket;
    private int index;
    private ExecutorService executor;
    private Timestamp timstp;
    /*
    public Device(Socket socket){
    	this.socket=socket;
    }*/
    public Device(Socket socket,ExecutorService executor){
    	this.socket=socket;
        this.executor=executor;
    }
	@Override
	public void run() {
		// TODO Auto-generated method stub
		DataInputStream dataIns=null;
		byte[] bytes=null;
		StoreDataService service=null;
		boolean breakFlag=false;
		try{
			while(true){
				if(breakFlag){
					break;
				}
				//读设备数据
				dataIns=new DataInputStream(socket.getInputStream());
				//读字节数据
				bytes=new byte[30];//[dataIns.available()];
			    dataIns.readFully(bytes);
			    //性能优化，使用线程池
			    this.executor.execute(new SendDataTask(bytes));
			    //Communicate.executor.execute(new SendDataTask(bytes));
				
			    List<Byte> byteList=new ArrayList<>();
				Byte bytObj;
				for(byte byt : bytes){
					bytObj=new Byte(byt);
					byteList.add(bytObj);
				}
				
				Communicate.logger.info("data received:"+byteList.toString());
				Communicate.logger.info(Communicate.clients.size()+" ClientsConnected!");
				//添加数据记录
				try{
					
	                if(service==null){
	            	    service=new StoreDataService(Communicate.context);
				    }
	                this.timstp=new Timestamp(System.currentTimeMillis());
	                //service.storeData(bytes);
	                service.storeData(bytes, "'"+this.timstp.toString().substring(0, 19)+"'");
	                
				}catch(Exception e){
	            	//Communicate.logger.info("generate StoreDataService failed: "+e.toString());
	            	service=null;
	            	Communicate.logger.debug("Store failed for "+e.toString());
	            }
				
				//向设备反馈数据
				/*
				try{
		        dataOts=new DataOutputStream(socket.getOutputStream());
			    dataOts.write(recvConfirm);
			    dataOts.flush();
				}catch(Exception oe){
					//Communicate.logger.debug(oe.toString());
				}
				*/
			}
		}catch(Exception ioE){
			//连接已失效
			//ioE.printStackTrace();
			try{
				dataIns.close();
				this.socket.close();//关闭socket
			}catch(Exception e){
				//e.printStackTrace();
			}
			Communicate.devices.remove(index);//移出device表
            //释放数据库连接
			if(service!=null){
            	service.releaseConn();
            }
			breakFlag=true;
		}
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	

}
