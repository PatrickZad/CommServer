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
    //�������̷߳�������
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
			//д�ַ�����
			//dataOuts.writeUTF(data);
			//д�ֽ�����
			dataOuts.writeUTF(data);
			dataOuts.flush();
			//
			Communicate.logger.info("data sent!");
		}catch(Exception ioE){
			//����ʧЧ
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
			//�Ƴ�������HeartBeatsListener���
			/*
			try{
			Communicate.clients.remove(index);//�Ƴ�clients��
			}catch(Exception e){}
			*/
		}
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
		//��������������
		HeartBeatsListener beatsLis=new HeartBeatsListener(this.socket,index);
		//new Thread(beatsLis).start();
		Communicate.executor.submit(beatsLis);
	}
    
}