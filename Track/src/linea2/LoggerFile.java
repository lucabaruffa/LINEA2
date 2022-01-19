package linea2;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

import javax.swing.JTextArea;

public class LoggerFile {
	
	private static String path = "C:/tracciabilita/log.txt";
	private static String dir = "c:/tracciabilita/";
	
	public LoggerFile() {
		newFile();
	}//fine costruttore

	public static void newFile() {
		
		try {
			
		        File filedir = new File(dir);

		        // true if the directory was created, false otherwise
		        if (filedir.mkdirs()) {
		            System.out.println("Directory is created!");
		        } else {
		            //System.out.println("Failed to create directory!");
		        }
			
			
			File file = new File(path);
			if (file.exists()) {
				//System.out.println("Il file " + path + " esiste");
			}
				
			else if (file.createNewFile())
				System.out.println("Il file " + path + " è stato creato");
			else
				System.out.println("Il file " + path + " non può essere creato");
			
			
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}//fine newFile
	
	
	public synchronized void write(String s) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date(System.currentTimeMillis());
		String dax = sdf.format(date);
		path = dir + ""+dax+"-linea2.txt";
		
		String timestamp = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
		try {
			File file = new File(path);
			
			if (getFileSizeMegaBytes(file)>1) {
				path = dir + ""+dax+"-linea2_2.txt";
				file = new File(path);
			}
			
			if (getFileSizeMegaBytes(file)>1) {
				path = dir + ""+dax+"-linea2_3.txt";
				file = new File(path);
			}
			
			if (getFileSizeMegaBytes(file)>1) {
				path = dir + ""+dax+"-linea2_4.txt";
				file = new File(path);
			}
			
			if (getFileSizeMegaBytes(file)>1) {
				path = dir + ""+dax+"-linea2_5.txt";
				file = new File(path);
			}
			
			if (getFileSizeMegaBytes(file)<=1) {
				FileWriter fw = new FileWriter(file,true);
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(timestamp +": " + s+"\n");
				bw.flush();
				bw.close();
			}
					
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	
	private static double getFileSizeMegaBytes(File file) {
		return (double)( file.length() / (1024 * 1024 * 1024)) ;
	}
	
public synchronized void writeBatteriaBloccata(String s) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date(System.currentTimeMillis());
		String dax = sdf.format(date);
		path = dir + ""+dax+"-batterie-bloccate.txt";
		
		String timestamp = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
		try {
			File file = new File(path);
			FileWriter fw = new FileWriter(file,true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(timestamp +": " + s+"\n");
			bw.flush();
			bw.close();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}


public static void readBatterieBloccate(JTextArea monitor) {
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	Date date = new Date(System.currentTimeMillis());
	String dax = sdf.format(date);
	path = dir + ""+dax+"-batterie-bloccate.txt";
	// Scrittura di dati da file
			FileReader fileInput;
			BufferedReader bufferReader;
			int i, num;
			try {
				// Lettura da file
				fileInput = new FileReader(path);
				bufferReader = new BufferedReader(fileInput);
	 
				String s = bufferReader.readLine();
				while (s != null) {
					monitor.append(s+"\n");
					s = bufferReader.readLine();
				}
	 
						 
				bufferReader.close();
				fileInput.close();
	 
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}

}//fine read
	
	
	public static void read(JTextArea monitor) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date(System.currentTimeMillis());
		String dax = sdf.format(date);
		path = dir + ""+dax+"-linea2.txt";
		// Scrittura di dati da file
				FileReader fileInput;
				BufferedReader bufferReader;
				int i, num;
				try {
					// Lettura da file
					fileInput = new FileReader(path);
					bufferReader = new BufferedReader(fileInput);
		 
					String s = bufferReader.readLine();
					while (s != null) {
						monitor.append(s+"\n");
						s = bufferReader.readLine();
					}
		 
							 
					bufferReader.close();
					fileInput.close();
		 
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	
	}//fine read
	
	
	
public synchronized void writeCSVbatteria(String s) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date(System.currentTimeMillis());
		String dax = sdf.format(date);
		path = dir + "CSV_batterie.csv";
		
		//String timestamp = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
		try {
			File file = new File(path);
						
			
				FileWriter fw = new FileWriter(file,true);
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write("" + s+"\n");
				bw.flush();
				bw.close();
			
					
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
}//fine classe

