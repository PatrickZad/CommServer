package communicate;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



class DeviceListener implements Runnable {
	private ServerSocket svrSocket;
	private ExecutorService executor;
	public DeviceListener(ServerSocket ss){
		this.svrSocket=ss;
		this.executor=Executors.newCachedThreadPool();
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
        while(true){	
		try{
        		    Socket socket=svrSocket.accept();//�ȴ��豸socket����
        		    Device device=new Device(socket,Communicate.executor);               
        		    Communicate.devices.add(device);//�����豸��socket
        		    device.setIndex(Communicate.devices.indexOf(device));
        		    //ʹ���̳߳أ�ӦΪnewCachedThreadPool
        		    this.executor.execute(device);
        		    //new Thread(device).start();//�����豸��������
        	}catch(IOException e){
        		//e.printStackTrace();
        	}
        }
	}

}