import java.util.*;
import java.text.*;

public class ChatBot
{
	public TreeMap<String,String> quesResponseMap=null;
	public TreeMap<String,String> localQuesResponseMap=null;
	public Set<String> questions=null;
	public Collection<String> answers=null;
	public String botAnswer="";
	public String chatHistory="";
	public String username="";
	
	public static final String BOT_INTRO="Hi! I am ChatBot 1.0 \nEnter your name ...\n";
	public static final String SORRY_RESPONSE = "Sorry! I did't get you.";
	public static final String SYSTEM_ERROR ="Sorry! there is an error in my system.";
	public static final String LEARN_REQUEST ="WHat should be my answer to this statement?";
	public static final String EXIT ="BYE! Have a nice day.";
	public static final String DATABASE ="KnowledgeBase.txt";
	public static final String BOT_NAME="Bot : ";
	

	public ChatBot() throws Exception
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

	public String saveQuestionAndResponse(String question , String response)
	{
		this.quesResponseMap.put(question,response);
		this.localQuesResponseMap.put(question,response);
		this.botAnswer="Okey. Got it!";
		return this.botAnswer;
	}	

	public String getResponse(String question)
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


	public void checkExit(String exitStmt) throws Exception
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
		
	public static void main(String arg[]) throws Exception
	{
		Scanner sc= new Scanner(System.in);
		ChatBot cb= new ChatBot();
		System.out.println(ChatBot.BOT_INTRO);
		cb.username=sc.nextLine();
		cb.chatHistory += "\n\n"+ChatBot.BOT_NAME+"Hi "+cb.username+". How can I help you?\r\n";
		System.out.println("\n\n"+ChatBot.BOT_NAME+"Hi "+cb.username+". How can I help you?");
		while(true)
		{
			System.out.print(cb.username+" : ");
			String question = sc.nextLine();
			cb.chatHistory += cb.username+" : "+question+"\r\n";
			cb.checkExit(question.trim());
			String response = cb.getResponse(question);
			cb.chatHistory += ChatBot.BOT_NAME+response+"\r\n";
			System.out.println(ChatBot.BOT_NAME+response);
			if(response.toLowerCase().contains("sorry"))
			{
				System.out.print(cb.username+" : ");
				response = sc.nextLine();
				cb.chatHistory += cb.username+" : "+response;
				cb.checkExit(response.trim());
				cb.chatHistory += ChatBot.BOT_NAME+cb.saveQuestionAndResponse(question,response)+"\r\n";
				System.out.println(ChatBot.BOT_NAME+cb.saveQuestionAndResponse(question,response));
			}
	
		}
	
	}



	
	
}