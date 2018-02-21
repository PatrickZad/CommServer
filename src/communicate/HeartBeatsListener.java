package communicate;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

class HeartBeatsListener implements Runnable {
    private Socket socket;
    private int index;
    private long lastTime=System.currentTimeMillis();
    private DataInputStream beats=null;
    public HeartBeatsListener(Socket soc,int index){
    	this.socket=soc;
    	this.index=index;
    }
	@Override
	public void run() {
		// TODO Auto-generated method stub
        try{
        	while(true){
        		if(System.currentTimeMillis()-lastTime>5000){//心跳包超时
        			dropSocket();
        			break;
        		}
        	    beats=new DataInputStream(socket.getInputStream());
        	    /**/
        	 //   Communicate.logger.debug(this.socket.toString()+" Heart Beats Attached!");
        	    /**/
        	    this.lastTime=System.currentTimeMillis();
        	}
        }catch(IOException e){
        	//e.printStackTrace();
        	Communicate.logger.debug("SocketDropped for "+e.toString());
        	dropSocket();
        }
	}
	//关闭socket，移出client
    void dropSocket(){
    	
    	try{
    		Communicate.clients.remove(index);
    		beats.close();
    	    socket.close();
        }catch(Exception e){
        	Communicate.logger.debug("DropClientFailedFor "+e.toString());
        }
    }
}
