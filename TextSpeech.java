
import java.util.Locale; 
import javax.speech.Central; 
import javax.speech.synthesis.Synthesizer; 
import javax.speech.synthesis.SynthesizerModeDesc; 

public class TextSpeech 
{ 
	private Synthesizer synthesizer;
	private static TextSpeech INSTANCE;
	
	public static TextSpeech getInstance() throws Exception {
		if(INSTANCE == null) INSTANCE = new TextSpeech();
		return INSTANCE;
	}

	private TextSpeech(){
	}
	
	public void start() throws Exception{
		// set property as Kevin Dictionary 
		System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory"); 
			
		// Register Engine 
		Central.registerEngineCentral("com.sun.speech.freetts.jsapi.FreeTTSEngineCentral"); 

		// Create a Synthesizer 
		synthesizer = Central.createSynthesizer(new SynthesizerModeDesc(Locale.US));	

		// Allocate synthesizer 
		synthesizer.allocate();		 
		
		// Resume Synthesizer 
		synthesizer.resume();	
	}
	
	public void stop() throws Exception{
		// Deallocate the Synthesizer. 
		synthesizer.deallocate();
	}
	
	public void speak(String content) throws Exception
	{ 
		// speaks the given text until queue is empty. 
		synthesizer.speakPlainText(content, null);	
		synthesizer.waitEngineState(Synthesizer.QUEUE_EMPTY); 
	}	
} 
