import java.util.HashMap;
import java.util.Random;

public class LanguageModel {

    

    // The map of this model.
    // Maps windows to lists of charachter data objects.
    HashMap<String, List> CharDataMap;
    
    // The window length used in this model.
    int windowLength;
    
    // The random number generator used by this model. 
	private Random randomGenerator;

   

    /** Constructs a language model with the given window length and a given
     *  seed value. Generating texts from this model multiple times with the 
     *  same seed value will produce the same random texts. Good for debugging. */
    public LanguageModel(int windowLength, int seed) {
        this.windowLength = windowLength;
        randomGenerator = new Random(seed);
        CharDataMap = new HashMap<String, List>();
    }

    /** Constructs a language model with the given window length.
     * Generating texts from this model multiple times will produce
     * different random texts. Good for production. */
    public LanguageModel(int windowLength) {
        this.windowLength = windowLength;
        randomGenerator = new Random();
        CharDataMap = new HashMap<String, List>();
    }

    /** Builds a language model from the text in the given file (the corpus). */
	public void train(String fileName) {
        String window = "";
        char c;
        In in = new In(fileName);

        for(int i=0; i<windowLength; i++)
        {
            c= in.readChar();
            window+=c;
        }

        

        while (!in.isEmpty()) 
        { 
            c  = in.readChar();
            List probs = new List();
            if(CharDataMap.containsKey(window))
            {
                probs = CharDataMap.get(window);
            }
            else 
            {
                CharDataMap.put(window, probs);
            }

            probs.update(c);
            CharDataMap.put(window, probs);

            window+=c;
            window = window.substring(1);
        }

        for (List probs : this.CharDataMap.values())
        {
              calculateProbabilities(probs);
        }
	}

    // Computes and sets the probabilities (p and cp fields) of all the
	// characters in the given list. */
	public void calculateProbabilities(List probs) {	
        int length = 0;
        Node current = probs.getFirstNode();
		for(int i=0; i< probs.getSize(); i++)
        {
           length+= current.cp.count;
           current= current.next;
        }

        double singleProbability = 1.0/length;

        current = probs.getFirstNode();
        double cp =0;
        Node previous = null;
        for(int i=0; i<probs.getSize() ; i++)
        {
            current.cp.p = singleProbability* current.cp.count;
            if(previous==null)
            {
                current.cp.cp = current.cp.p;
            }
            else
            {
                current.cp.cp = previous.cp.cp + current.cp.p;
            }
            previous = current;
            current = current.next;
           
        }
       

	}

    // Returns a random character from the given probabilities list.
	public char getRandomChar(List probs) {
		
        Node current = probs.getFirstNode();
        double random = randomGenerator.nextDouble();

        Node previous = null;

        while(current!=null)
        {
            if(random< current.cp.cp)
                return current.cp.chr;
            previous = current;
            current = current.next;
        }

        return previous.cp.chr; //incase random>0.9999..
	}

    /**
	 * Generates a random text, based on the probabilities that were learned during training. 
	 * @param initialText - text to start with. If initialText's last substring of size numberOfLetters
	 * doesn't appear as a key in Map, we generate no text and return only the initial text. 
	 * @param numberOfLetters - the size of text to generate
	 * @return the generated text
	 */
	public String generate(String initialText, int textLength) {
		if(initialText.length()<windowLength)
        {
            return initialText;
        }
        String generated = "";
        char c;
        List probs;
        String window = initialText.substring(0, windowLength);
        while(generated.length() < textLength)
        {  
            probs = CharDataMap.get(window);
            if (probs == null || !CharDataMap.containsKey(window))
            {
                return generated;
            }
            else
            {
                c = getRandomChar(probs);
                generated += c;
                window += c;
                window = window.substring(1);
            }

        }
        return generated;
	}

    /** Returns a string representing the map of this language model. */
	public String toString() {
		StringBuilder str = new StringBuilder();
		for (String key : CharDataMap.keySet()) {
			List keyProbs = CharDataMap.get(key);
			str.append(key + " : " + keyProbs + "\n");
		}
		return str.toString();
	}

    public static void main(String[] args) {

        int windowLength = Integer.parseInt(args[0]);
        String initialText = args[1];
        int generatedTextLength = Integer.parseInt(args[2]);
        Boolean randomGeneration = args[3].equals("random");
        String fileName = args[4];
        // Create the LanguageModel object
        LanguageModel lm;
        if (randomGeneration)
            lm = new LanguageModel(windowLength);
        else
            lm = new LanguageModel(windowLength, 20);
        // Trains the model, creating the map.
        lm.train(fileName);
        // Generates text, and prints it.
        System.out.println(lm.generate(initialText, generatedTextLength));
    }
}

    

