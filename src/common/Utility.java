package common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

public class Utility {
	//startΪ��λ�ֽڣ�endΪ��λ�ֽ�
    public static int bytes2int(byte[] bytes,int start,int end){
    	int result=0x0ff;
    	result=bytes[start]&result;
    	for(int i=1;i<=(end-start);i++){
    		result=result<<8;
    		result=result|(bytes[start+i]&0x0ff);
    	}
    	return result;
    }
    public static short bytes2short(byte[] bytes,int start,int end){
    	short result=0x0ff;
    	result=(short) (bytes[start]&result);
    	for(int i=1;i<=(end-start);i++){
    		result=(short) (result<<8);
    		result=(short)(result | (bytes[start+i]&0x0ff));
    	}
    	return result;
    }
    //��request���ɻ�ȡ���ݱ��ʱ����IDԼ����Ϣ
    public static JSONObject sheetInfo(HttpServletRequest request) throws IOException {
    	JSONObject info=null;
    	StringBuilder builder=new StringBuilder();
    	BufferedReader reader=new BufferedReader(new InputStreamReader(request.getInputStream(),"utf-8"));
    	String line=null;
    	while((line=reader.readLine())!= null){
    		builder.append(line);
    	}
    	info=JSONObject.fromObject(builder.toString());
    	return info;
    }
}
