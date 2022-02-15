package linea;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class SendEmailOffice365 {

	public void Send() {

		final String username = "";
		final String password = "";

		Properties prop = new Properties();
		prop.put("mail.smtp.host", "smtp.gmail.com");
		prop.put("mail.smtp.port", "465");
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.socketFactory.port", "465");
		prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

		Session session = Session.getInstance(prop, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("info.baruffa@gmail.com"));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("luca.baruffa@fiamm.com"));
			message.setSubject("CONFIGURAZIONE PLC MODIFICATA");
			message.setText(
					"Attenzione," + "\n\n la configurazione sul plc della LINEA 5 è stata modificata!\n\n" + "Saluti");

			Transport.send(message);

			System.out.println("Done");

		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	public void Send_Outlook() {

		Desktop desktop = Desktop.getDesktop();

		String message1 = "mailto:luca.baruffa@fiamm.com?subject=Test&body=HelloWorld";
		URI uri = URI.create(message1);

		Runtime runtime = Runtime.getRuntime();
		String cmd1 = (String) "$Outlook = New-Object -ComObject Outlook.Application";
		String cmd2 = (String) "$Mail = $Outlook.CreateItem(0)";
		String cmd3 = (String) "$Mail.To = 'luca.baruffa@fiamm.com'";
		String cmd4 = (String) "$Mail.Subject = 'Parametri PLC modificati'";
		String cmd5 = (String) "$Mail.Body ='Hello World'";
		String cmd6 = (String) "$Mail.Send()";

		try {
			Process proc = runtime.exec(cmd1);
			proc = runtime.exec(cmd2);
			proc = runtime.exec(cmd3);
			proc = runtime.exec(cmd4);
			proc = runtime.exec(cmd5);
			proc = runtime.exec(cmd6);
			proc.getOutputStream().close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//           

	}// fine send

	public void send_power() throws IOException {

		// preparazione del file
		Path path_template = Paths.get("c:\\sfw\\send_template.ps1");
		Path path = Paths.get("c:\\sfw\\send.ps1");
		Charset charset = StandardCharsets.UTF_8;
		try {
			String content = new String(Files.readAllBytes(path_template), charset);
			content = content.replaceAll("<messaggio>", "I parametri della linea sono stati modificati. Ciao");
			Files.write(path, content.getBytes(charset));
		} catch (IOException e) {
			// Simple exception handling, replace with what's necessary for your use case!
			throw new RuntimeException("Generating file failed", e);
		}

		System.out.println("AVVIO SCRIPT");

		String cmd = "cmd /c powershell -ExecutionPolicy RemoteSigned -noprofile -noninteractive C:\\sfw\\send.ps1";
		// String command = "powershell.exe \"C:\\sfw\\send.ps1\"";
		// String command = "powershell.exe -File \"C:\\sfw\\send.ps1\"";
		Process powerShellProcess = Runtime.getRuntime().exec(cmd);
		powerShellProcess.getOutputStream().close();
		/*
		 * final ProcessBuilder pb = new ProcessBuilder( "powershell.exe", "-Command",
		 * "C:\\sfw\\send.ps1" );
		 */
		// final Process p = pb.start();

		/*
		 * String command1 =
		 * "powershell.exe  $Outlook = New-Object -ComObject Outlook.Application";
		 * String command2 = "powershell.exe  $Mail = $Outlook.CreateItem(0)"; String
		 * command3 = "powershell.exe  $Mail.To = \"luca.baruffa@fiamm.com\""; String
		 * command4 = "powershell.exe  $Mail.Subject = \"Parametri PLC modificati\"";
		 * String command5 = "powershell.exe  $Mail.Body =\"Hello World\""; String
		 * command6 = "powershell.exe  $Mail.Send()";
		 * 
		 * 
		 * Process powerShellProcess = Runtime.getRuntime().exec(command1);
		 * powerShellProcess = Runtime.getRuntime().exec(command2); powerShellProcess =
		 * Runtime.getRuntime().exec(command3); powerShellProcess =
		 * Runtime.getRuntime().exec(command4); powerShellProcess =
		 * Runtime.getRuntime().exec(command5); powerShellProcess =
		 * Runtime.getRuntime().exec(command6);
		 * 
		 * 
		 * 
		 * 
		 * // Getting the results powerShellProcess.getOutputStream().close();
		 * 
		 */
		System.out.println("FINE SCRIPT");
		// stdout.close();

	}

}// fine classe