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
        		    Socket socket=svrSocket.accept();//等待设备socket连接
        		    Device device=new Device(socket,Communicate.executor);               
        		    Communicate.devices.add(device);//保存设备的socket
        		    device.setIndex(Communicate.devices.indexOf(device));
        		    //使用线程池，应为newCachedThreadPool
        		    this.executor.execute(device);
        		    //new Thread(device).start();//监听设备发送数据
        	}catch(IOException e){
        		//e.printStackTrace();
        	}
        }
	}

}
