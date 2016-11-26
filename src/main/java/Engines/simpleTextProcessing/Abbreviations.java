package Engines.simpleTextProcessing;

import java.util.HashSet;
import java.util.Set;

/**
 * This class contains the abbreviations of http://www.englishleap.com/other-resources/abbreviations from date 17.11.16
 * @author TTurke
 *
 */
public class Abbreviations 
{
	private Set <String> abbreviations_latin = new HashSet<String>();
	private Set <String> abbreviations_names = new HashSet<String>();
	private Set <String> abbreviations_general = new HashSet<String>();
	private Set <String> abbreviations_grammar = new HashSet<String>();
	
	public Abbreviations()
	{
		abbreviations_latin.add("A.D");
		abbreviations_latin.add("a.m");
		abbreviations_latin.add("c.");
		abbreviations_latin.add("ca.");
		abbreviations_latin.add("C.V");
		abbreviations_latin.add("e.g.");
		abbreviations_latin.add("Et al.");
		abbreviations_latin.add("Etc.");
		abbreviations_latin.add("i.e.");
		abbreviations_latin.add("p.a");
		abbreviations_latin.add("p.m");
		abbreviations_latin.add("P.S");
		abbreviations_latin.add("R.I.P");
		abbreviations_latin.add("Stat");
		
		abbreviations_names.add("Dr.");
		abbreviations_names.add("Gen.");
		abbreviations_names.add("Hon.");
		abbreviations_names.add("Mr.");
		abbreviations_names.add("Mrs.");
		abbreviations_names.add("Ms.");
		abbreviations_names.add("Prof.");
		abbreviations_names.add("Rev.");
		abbreviations_names.add("Sr.");
		abbreviations_names.add("Jr.");
		abbreviations_names.add("St.");
		
		abbreviations_general.add("Assn.");
		abbreviations_general.add("Ave.");
		abbreviations_general.add("Dept.");
		abbreviations_general.add("Est.");
		abbreviations_general.add("Fig.");
		abbreviations_general.add("Hrs.");
		abbreviations_general.add("Inc.");
		abbreviations_general.add("Mt.");
		abbreviations_general.add("No.");
		abbreviations_general.add("Oz.");
		abbreviations_general.add("Sq.");
		abbreviations_general.add("St.");
		abbreviations_general.add("Vs");
		
		abbreviations_grammar.add("Abbr.");
		abbreviations_grammar.add("Adj.");
		abbreviations_grammar.add("Adv.");
		abbreviations_grammar.add("Obj.");
		abbreviations_grammar.add("Pl.");
		abbreviations_grammar.add("Poss.");
		abbreviations_grammar.add("Prep.");
		abbreviations_grammar.add("Pron.");
		abbreviations_grammar.add("Pseud.");
		abbreviations_grammar.add("Sing.");
		abbreviations_grammar.add("Syn.");
		abbreviations_grammar.add("Trans.");
		abbreviations_grammar.add("V.");
		abbreviations_grammar.add("Vb.");
	}

	public Set<String> getAbbreviations_latin() {
		return abbreviations_latin;
	}

	public Set<String> getAbbreviations_names() {
		return abbreviations_names;
	}

	public Set<String> getAbbreviations_general() {
		return abbreviations_general;
	}

	public Set<String> getAbbreviations_grammar() {
		return abbreviations_grammar;
	}
	
	
}
