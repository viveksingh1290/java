/*


Outgoing Mail (SMTP) Server
requires SSL: smtp.gmail.com (use authentication)
Use Authentication: Yes
Port for SSL: 465; Secure Sockets Layer (SSL)

*/


import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class JavaMailApp extends Frame
{
	
	private Button btSend = new Button("Send");
    private Button btClear = new Button("Clear");
    private Button btQuit = new Button("Quit");
    private Label serverLabel = new Label("Local mailserver:");
    private TextField serverField = new TextField("", 40);
    private Label fromLabel = new Label("From:");
    private TextField fromField = new TextField("", 40);
    private Label toLabel = new Label("To:"); 
    private TextField toField = new TextField("", 40);
    private Label ccLabel = new Label("cc:"); 
    private TextField ccField = new TextField("", 40);
    private Label bccLabel = new Label("bcc:"); 
    private TextField bccField = new TextField("", 40);
    private Label subjectLabel = new Label("Subject:");
    private TextField subjectField = new TextField("", 40);
    private Label messageLabel = new Label("Message:");
    private TextArea messageText = new TextArea(10, 40);
    
    
    public JavaMailApp() {
    	super("Java Mailclient");
    	
    	Panel serverPanel = new Panel(new BorderLayout());
    	Panel fromPanel = new Panel(new BorderLayout());
    	Panel toPanel = new Panel(new BorderLayout());
    	Panel ccPanel = new Panel(new BorderLayout());
    	Panel bccPanel = new Panel(new BorderLayout());
    	Panel subjectPanel = new Panel(new BorderLayout());
    	Panel messagePanel = new Panel(new BorderLayout());
    	serverPanel.add(serverLabel, BorderLayout.WEST);
    	serverPanel.add(serverField, BorderLayout.CENTER);
    	fromPanel.add(fromLabel, BorderLayout.WEST);
    	fromPanel.add(fromField, BorderLayout.CENTER);
    	toPanel.add(toLabel, BorderLayout.WEST);
    	toPanel.add(toField, BorderLayout.CENTER);
    	ccPanel.add(ccLabel, BorderLayout.WEST);
    	ccPanel.add(ccField, BorderLayout.CENTER);
    	bccPanel.add(bccLabel, BorderLayout.WEST);
    	bccPanel.add(bccField, BorderLayout.CENTER);
    	subjectPanel.add(subjectLabel, BorderLayout.WEST);
    	subjectPanel.add(subjectField, BorderLayout.CENTER);
    	messagePanel.add(messageLabel, BorderLayout.NORTH);	
    	messagePanel.add(messageText, BorderLayout.CENTER);
    	Panel fieldPanel = new Panel(new GridLayout(0, 1));
    	fieldPanel.add(serverPanel);
    	fieldPanel.add(fromPanel);
    	fieldPanel.add(toPanel);
    	fieldPanel.add(ccPanel);
    	fieldPanel.add(bccPanel);
    	fieldPanel.add(subjectPanel);
    	Panel buttonPanel = new Panel(new GridLayout(1, 0));
    	btSend.addActionListener(new SendListener());
    	btClear.addActionListener(new ClearListener());
    	//btClear.addActionListener(new ClearListener);
    	btQuit.addActionListener(new QuitListener());
    	buttonPanel.add(btSend);
    	buttonPanel.add(btClear);
    	buttonPanel.add(btQuit);
    	
    	/* Add, pack, and show. */
    	add(fieldPanel, BorderLayout.NORTH);
    	add(messagePanel, BorderLayout.CENTER);
    	add(buttonPanel, BorderLayout.SOUTH);
    	pack();
    	show();
    	
    }
    public static void main(String[] args )
    {
    	new JavaMailApp();
    }
    
    class SendListener implements ActionListener {
    	public void actionPerformed(ActionEvent event) {
    	    System.out.println("Sending mail");
    	    
    	    /* Check that we have the local mailserver */
    	    if ((serverField.getText()).equals("")) {
    		System.out.println("Need name of local mailserver!");
    		return;
    	    }

    	    /* Check that we have the sender and recipient. */
    	    if((fromField.getText()).equals("")) {
    		System.out.println("Need sender!");
    		return;
    	    }
    	    if((toField.getText()).equals("")) {
    		System.out.println("Need recipient!");
    		return;
    	    }
    	    
    	    
    	   // Message mailMessage = new Message(fromField.getText(),toField.getText(),subjectField.getText(),messageText.getText());
    	 //   Message mailMessage = new Message(fromField.getText(), 
				  //    toField.getText(), 
				   //   subjectField.getText(), 
				  //    messageText.getText());

  /* Check that the message is valid, i.e., sender and
     recipient addresses look ok. */
 // if(!mailMessage.isValid()) {
//	return;
 // }
    	    Properties props = new Properties();
        	props.put("mail.smtp.host", serverField.getText());
        	props.put("mail.smtp.socketFactory.port", "25");
    	props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
        	props.put("mail.smtp.auth", "true");
    	props.put("mail.smtp.port", "25");

    	Session session = Session.getDefaultInstance(props,
    	new javax.mail.Authenticator()
    	{
    		protected PasswordAuthentication getPasswordAuthentication()
    		{ return new PasswordAuthentication("username","*******");	}
    	});
    	try {
    		
    		InternetAddress[] tolist = InternetAddress.parse(toField.getText());
    		InternetAddress[] cclist = InternetAddress.parse(ccField.getText());
    		InternetAddress[] bccList = InternetAddress.parse(bccField.getText());
    	 Message message = new MimeMessage(session);
 	    message.setFrom(new InternetAddress(fromField.getText()));
 	   // message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toField.getText()));
 	   message.setRecipients(Message.RecipientType.TO, tolist);
		message.addRecipients(Message.RecipientType.CC, cclist);
		message.addRecipients(Message.RecipientType.BCC, bccList);
 	    message.setSubject(subjectField.getText());
 	    message.setText(messageText.getText());

 	    Transport.send(message);

 	    System.out.println("Mails were sent to all the recepients successfuly");

     	} catch (MessagingException e) {
     		System.out.println("Please enter email id with same domain or check  the email id");
     		System.out.println(e);
     	    
     	}
     
    	
 }	    
    	    
    	    
    	
    }
    	
    	class ClearListener implements ActionListener {
    		public void actionPerformed(ActionEvent e) {
    		    System.out.println("Clearing fields");
    		    fromField.setText("");
    		    toField.setText("");
    		    subjectField.setText("");
    		    messageText.setText("");
    		}
    	    }

    	    /* Quit. */
    	    class QuitListener implements ActionListener {
    		public void actionPerformed(ActionEvent e) {
    		    System.exit(0);
    		}
    	    }
    	    
   /* public static void main( String[] args )
    
   {
    	
    	
    	Properties props = new Properties();
    	props.put("mail.smtp.host", "ucmo.edu.s6a1.psmtp.com");
    	props.put("mail.smtp.socketFactory.port", "25");
	props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
    	props.put("mail.smtp.auth", "true");
	props.put("mail.smtp.port", "25");

	Session session = Session.getDefaultInstance(props,
	new javax.mail.Authenticator()
	{
		protected PasswordAuthentication getPasswordAuthentication()
		{ return new PasswordAuthentication("vxs61170@ucmo.edu","kkk");	}
	});

    	try {

            Message message = new MimeMessage(session);
	    message.setFrom(new InternetAddress("vxs61170@ucmo.edu"));
	    message.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse("viveksingh.c4@gmail.com"));
	    message.setSubject("Testing Subject");
	    message.setText("Dear Mail Crawler,\n\n No spam to my email, please!");

	    Transport.send(message);

	    System.out.println("Done");

    	} catch (MessagingException e) {
    	    throw new RuntimeException(e);
    	}
    }*/
}
