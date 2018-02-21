package communicate;

import java.io.DataOutputStream;
//import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

class Client implements Runnable{
    private Socket socket;
    private String data;
    private byte[] bytesdata;
    private int index;
    public Client(Socket soc){
    	this.socket=soc;
    }
    //开启子线程发送数据
    public void setData(String data){
    	this.data=data;
    	this.bytesdata=null;
    }
    public void setData(byte[] bytes){
    	this.bytesdata=bytes;
    	this.data=null;
    }
	@Override
	public void run() {
		// TODO Auto-generated method stub
		DataOutputStream dataOuts=null;
		String data=null;
		if(this.data==null){
			List<Byte> byteList=new ArrayList<>();
			Byte bytObj;
			for(byte byt : bytesdata){
				bytObj=new Byte(byt);
				byteList.add(bytObj);
			}
		    data=byteList.toString();
		}else{
			data=this.data;
		}
		try{
			Communicate.logger.debug("StartTransfering2 Client "+this.toString());
			dataOuts=new DataOutputStream(socket.getOutputStream());
			//写字符数据
			//dataOuts.writeUTF(data);
			//写字节数据
			dataOuts.writeUTF(data);
			dataOuts.flush();
			//
			Communicate.logger.info("data sent!");
		}catch(Exception ioE){
			//连接失效
			//ioE.printStackTrace();
			try{
			    dataOuts.close();
			    socket.close();
			    Communicate.logger.debug("closedByServerFor"+ioE);
			    Communicate.clients.remove(this);
			}catch(Exception e){
				//e.printStackTrace();
				Communicate.logger.debug(e.getMessage());
			}
			//移除操作由HeartBeatsListener完成
			/*
			try{
			Communicate.clients.remove(index);//移除clients表
			}catch(Exception e){}
			*/
		}
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
		//开启心跳包监听
		HeartBeatsListener beatsLis=new HeartBeatsListener(this.socket,index);
		//new Thread(beatsLis).start();
		Communicate.executor.submit(beatsLis);
	}
    
}
