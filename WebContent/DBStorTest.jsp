<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import="java.net.*,java.io.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>TestJSP</title>
</head>
<body>
<%
  byte[] info0={(byte)0x00,(byte)0x06,(byte)0xb1,(byte)0x41,(byte)0xc0,
          (byte)0x01,(byte)0x55,(byte)0x7d,(byte)0xe0,(byte)0x01,
          (byte)0x03,(byte)0xe8,(byte)0x32,(byte)0x02,(byte)0xbc,
          (byte)0x03,(byte)0xe8,(byte)0x01,(byte)0xf4,(byte)0x07,
          (byte)0xd0,(byte)0x2a,(byte)0xf8,(byte)0x01,(byte)0x65,
          (byte)0x90,(byte)0x9c,(byte)0x40,(byte)0x00,(byte)0x0E}; 
  byte[] info1={(byte)0x01,(byte)0x06,(byte)0xb1,(byte)0x41,(byte)0xc0,
          (byte)0x01,(byte)0x55,(byte)0x7d,(byte)0xe0,(byte)0x01,
          (byte)0x03,(byte)0xe8,(byte)0x32,(byte)0x02,(byte)0xbc,
          (byte)0x03,(byte)0xe8,(byte)0x01,(byte)0xf4,(byte)0x07,
          (byte)0xd0,(byte)0x2a,(byte)0xf8,(byte)0x01,(byte)0x65,
          (byte)0x90,(byte)0x9c,(byte)0x40,(byte)0x00,(byte)0x0E}; 
  byte[] info2={(byte)0x02,(byte)0x06,(byte)0xb1,(byte)0x41,(byte)0xc0,
          (byte)0x01,(byte)0x55,(byte)0x7d,(byte)0xe0,(byte)0x01,
          (byte)0x03,(byte)0xe8,(byte)0x32,(byte)0x02,(byte)0xbc,
          (byte)0x03,(byte)0xe8,(byte)0x01,(byte)0xf4,(byte)0x07,
          (byte)0xd0,(byte)0x2a,(byte)0xf8,(byte)0x01,(byte)0x65,
          (byte)0x90,(byte)0x9c,(byte)0x40,(byte)0x00,(byte)0x0E};
  byte[] back=new byte[8];
  String ip="localhost";int port=16000;Socket socket=new Socket(); 
  DataOutputStream outs=null;
  DataInputStream ins=null;
  try{
	socket.connect(new InetSocketAddress(ip,port));%>
<p>Connected!</p>
<%  outs=new DataOutputStream(socket.getOutputStream());
	outs.write(info0);
	outs.flush();
	ins=new DataInputStream(socket.getInputStream());
	ins.readFully(back);
	%>
<p>info:<%=info0.toString()%>
   back:<%=back.toString() %>
</p>
<%  outs=new DataOutputStream(socket.getOutputStream());
	outs.write(info1);
	outs.flush();
	ins=new DataInputStream(socket.getInputStream());
	ins.readFully(back);
	%>
<p>info:<%=info1.toString()%>
   back:<%=back.toString() %>
</p>
<%  outs=new DataOutputStream(socket.getOutputStream());
	outs.write(info2);
	outs.flush();
	ins=new DataInputStream(socket.getInputStream());
	ins.readFully(back);
	%>
<p>info:<%=info2.toString()%>
   back:<%=back.toString() %>
</p>
<%
     }catch(Exception e){%>
<p>Disconnected!</p>
<%e.printStackTrace();
if(socket!=null){
	socket.close();
}} %>
</body>
</html>