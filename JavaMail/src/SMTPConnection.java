import java.net.*;
import java.io.*;
import java.util.*;
public class SMTPConnection {
public Socket connection;
public BufferedReader fromServer;
public DataOutputStream toServer;
private static final int SMTP_PORT = 25;
private static final String CRLF ="\r\n";
private boolean isConnected = false;
public  SMTPConnection(Envelope envelope) throws IOException {
	connection = new Socket(envelope.DestAddr, SMTP_PORT);
	fromServer=new BufferedReader(new InputStreamReader(connection.getInputStream()));
	toServer = new DataOutputStream(connection.getOutputStream());

	String reply = fromServer.readLine();
	if(parseReply(reply) != 220) {
	System.out.println("Error in connect.");
	System.out.println(reply);
	return; 
	}
	String localhost = (InetAddress.getLocalHost()).getHostName();
	try {
	sendCommand("HELO " + localhost, 250);
	} catch (IOException e) {
	System.out.println("HELO failed. Aborting.");
	return;
	}
	isConnected = true;
  }
public void send(Envelope envelope) throws IOException {
	sendCommand("MAIL FROM:<" + envelope.Sender + ">", 250);
	sendCommand("RCPT TO:<" + envelope.Recipient + ">", 250);
	sendCommand("DATA", 354);
	sendCommand(envelope.Message.toString() + CRLF + ".", 250);
	System.out.println("envelope message"+envelope.Message.toString());
	}
public void close() {
	isConnected = false; 
	try {
	sendCommand("QUIT", 221);
	connection.close();
	} catch (IOException e) {
	System.out.println("Unable to close connection: " + e);
	isConnected = true;
	}    }
private void sendCommand(String command, int rc) throws IOException {
	String reply = null;
	toServer.writeBytes(command + CRLF);
	reply = fromServer.readLine();
	if(parseReply(reply) != rc) {
	System.out.println("Error in command: " + command);
	System.out.println(reply);
	throw new IOException();
	}    }
private int parseReply(String reply)  {
	try
	{
	StringTokenizer parser = new StringTokenizer(reply);
	String replycode = parser.nextToken();
	System.out.println(replycode);
	return (new Integer(replycode)).intValue();
	}catch(Exception e){
	System.out.println("reply from mail server"+ e);}
	return 0;}
	
protected void finalize() throws Throwable {
	if(isConnected) {  close();}
	super.finalize();
    }}

