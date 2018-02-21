package communicate;

import java.net.ServerSocket;
import java.net.Socket;

class ClientListener implements Runnable {
	private ServerSocket svrSocket;
	public ClientListener(ServerSocket ss){
		this.svrSocket=ss;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true){
        	try{
        	        Socket socket=svrSocket.accept();
        	        /*debug log*/
        	        //Communicate.logger.info("New Client Connected!");
        	        /*end of log*/
        	        Client client=new Client(socket);
        	        Communicate.clients.add(client);
        	        client.setIndex(Communicate.clients.indexOf(client));//±£´æclient
        	}catch(Exception e){
             //e.printStackTrace();
            }  
		}
	}
}