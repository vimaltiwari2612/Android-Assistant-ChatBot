import java.util.*;
import java.text.*;

import java.util.Locale; 
import javax.speech.Central; 
import javax.speech.synthesis.Synthesizer; 
import javax.speech.synthesis.SynthesizerModeDesc; 

public class ChatBot
{
	private TreeMap<String,String> quesResponseMap=null;
	private TreeMap<String,String> localQuesResponseMap=null;
	private Set<String> questions=null;
	private Collection<String> answers=null;
	private String botAnswer="";
	private String chatHistory="";
	private String username="";
	
	private static final String BOT_INTRO="Hi! I am ChatBot 1.0 \nEnter your name ...\n";
	private static final String SORRY_RESPONSE = "Sorry! I did't get you.";
	private static final String SYSTEM_ERROR ="Sorry! there is an error in my system.";
	private static final String LEARN_REQUEST ="WHat should be my answer to this statement?";
	private static final String EXIT ="BYE! Have a nice day.";
	private static final String DATABASE ="KnowledgeBase.txt";
	private static final String BOT_NAME="Bot : ";

	private ChatBot() throws Exception
	{
		this.quesResponseMap=KnowledgeBase.getKnowledgeBase(ChatBot.DATABASE);
		this.localQuesResponseMap = new TreeMap<String,String>();
		if(this.quesResponseMap == null)
			this.botAnswer=ChatBot.SORRY_RESPONSE ;
		else
			{
			questions = quesResponseMap.keySet();
			answers = quesResponseMap.values();
			}
	}

	private String saveQuestionAndResponse(String question , String response)
	{
		if(!question.trim().isEmpty() && !response.trim().isEmpty()) {
			this.quesResponseMap.put(question,response);
			this.localQuesResponseMap.put(question,response);
		}
		this.botAnswer="Okey. Got it!";
		return this.botAnswer;
	}	

	private String getResponse(String question)
	{
		double globalRank=0.200;
		String localQuestion="";
		try{
			for(String ques:questions)
			{
				double localRank=StringSimilarity.similarity(ques,question);
				
				if(globalRank <localRank)
				{
					globalRank = localRank;
					localQuestion=ques;
				}
			}
			
			if(localQuestion.trim()!=""){
				this.botAnswer=quesResponseMap.get(localQuestion);
				if(this.botAnswer.trim().toLowerCase().contains("timedate")) {this.botAnswer = new Date().toString();}				
			}				
			else
				this.botAnswer=ChatBot.SORRY_RESPONSE+" "+ChatBot.LEARN_REQUEST;
		}catch(Exception e)
		{
			this.botAnswer=ChatBot.SYSTEM_ERROR;
		}
		return this.botAnswer;
	}


	private void checkExit(String exitStmt) throws Exception
	{
		if(exitStmt.toLowerCase().contains("exit"))
		{
			this.chatHistory += ChatBot.BOT_NAME+ChatBot.EXIT;
			System.out.print(ChatBot.BOT_NAME+ChatBot.EXIT );
			KnowledgeBase.saveNewKnowledge(this.localQuesResponseMap,ChatBot.DATABASE);
			KnowledgeBase.saveLogs(this.chatHistory,this.username);
			System.exit(0);
		}
	}
		
	public static void main(String arg[]) throws Exception{
		Scanner sc= new Scanner(System.in);
		ChatBot cb= new ChatBot();
		print(ChatBot.BOT_INTRO);
		speak(ChatBot.BOT_INTRO);
		cb.username=sc.nextLine();
		cb.chatHistory += "\n\n"+ChatBot.BOT_NAME+"Hi "+cb.username+". How can I help you?\r\n";
		print("\n\n"+ChatBot.BOT_NAME+"Hi "+cb.username+". How can I help you?");
		speak("Hi "+cb.username+". How can I help you?");
		while(true)
		{
			System.out.print(cb.username+" : ");
			String question = sc.nextLine();
			cb.chatHistory += cb.username+" : "+question+"\r\n";
			cb.checkExit(question.trim());
			String response = cb.getResponse(question);
			cb.chatHistory += ChatBot.BOT_NAME+response+"\r\n";
			print(ChatBot.BOT_NAME+response);
			if(response.toLowerCase().contains("sorry"))
			{
				System.out.print(cb.username+" : ");
				response = sc.nextLine();
				cb.chatHistory += cb.username+" : "+response;
				cb.checkExit(response.trim());
				cb.chatHistory += ChatBot.BOT_NAME+cb.saveQuestionAndResponse(question,response)+"\r\n";
				print(ChatBot.BOT_NAME+cb.saveQuestionAndResponse(question,response));
			}
	
		}
	}


	private static void print(String content){
		System.out.println(content);
	}
	
	public static void speak(String content)
	{ 
		try{
			// set property as Kevin Dictionary 
			System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory"); 
				
			// Register Engine 
			Central.registerEngineCentral("com.sun.speech.freetts.jsapi.FreeTTSEngineCentral"); 

			// Create a Synthesizer
			Synthesizer synthesizer = Central.createSynthesizer(new SynthesizerModeDesc(Locale.ROOT));	
			
			// Allocate synthesizer 
			synthesizer.allocate();		 
			
			// Resume Synthesizer 
			synthesizer.resume();	 
			
			// speaks the given text until queue is empty. 
			synthesizer.speakPlainText(content, null);	
			synthesizer.waitEngineState(Synthesizer.QUEUE_EMPTY); 
			
			// Deallocate the Synthesizer. 
			synthesizer.deallocate();	
		}catch(Exception e){
			
		}			
	}	
	
	
}
