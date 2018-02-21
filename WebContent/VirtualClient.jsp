<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import="java.net.*,java.io.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>VirtualClient</title>
</head>
<body>
    <%
    String host="localhost";//"60.205.224.89";
    int port=18000;
    Socket socket=null;
    DataInputStream ins=null;
    String rec=null;
 
    	try{
    	socket=new Socket();
    	socket.connect(new InetSocketAddress(host,port),8000);
    	%>
    	<p>Connected!</p>
    	<%
    	while(true){
    		ins=new DataInputStream(socket.getInputStream());
    	    rec=ins.readUTF();
    	    %>
    	    <p>info:<%=rec %>
    	    <%
    	}
    	}catch (Exception e){
    		socket.close();
    	}
    
    %>
</body>
</html>