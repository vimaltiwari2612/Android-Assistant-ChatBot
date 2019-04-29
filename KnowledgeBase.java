import java.util.*;
import java.io.*;
import java.text.*;

public class KnowledgeBase
{
	public static TreeMap<String,String> quesResponseMap=null;

	public static TreeMap<String,String> getKnowledgeBase(String fileName) throws Exception
	{
		quesResponseMap = new TreeMap<String,String>();
		RandomAccessFile raf = new RandomAccessFile(fileName, "rw");
		raf.seek(0);
		while(raf.getFilePointer()<raf.length())
		{
			String line= raf.readLine().trim();
			if(!line.trim().isEmpty()){
				String[] lines = line.split("~");
				String question = lines[0];
				String answer = lines[1];
				quesResponseMap.put(question,answer);
			}
		}
		raf.close();
		return quesResponseMap;
	}

	public static void saveNewKnowledge(TreeMap<String,String> newKnowledge ,String fileName) throws Exception 
	{
		RandomAccessFile raf = new RandomAccessFile(fileName, "rw");
		long fileLength = raf.length();
		raf.seek(fileLength);
		String toBeWritten="";
		for(Map.Entry<String,String> entry : newKnowledge.entrySet()) {
			  String key = entry.getKey();
  			  String value = entry.getValue();
			  toBeWritten += key+" ~ "+value+"\r\n";
			}
		raf.writeBytes(toBeWritten);
		raf.close();
	}
	
	public static void saveLogs(String chat,String username) throws Exception 
	{
		
		File dir = new File(System.getProperty("user.dir")+"/logs");
		dir.mkdir();
		Date date = new Date() ;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss") ;
		String fileName="Chat History with "+username+" on "+dateFormat.format(date)+".txt";
		File file = new File(dir,fileName);
		file.createNewFile();
		RandomAccessFile raf = new RandomAccessFile(file, "rw");
		raf.writeBytes(chat);
		raf.close();
	}

}
