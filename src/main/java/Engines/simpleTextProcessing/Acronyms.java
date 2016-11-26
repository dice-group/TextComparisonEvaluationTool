package Engines.simpleTextProcessing;

import java.util.HashSet;
import java.util.Set;

/**
 * This class contains the acronyms of http://www.englishleap.com/other-resources/abbreviations from date 17.11.16
 * @author TTurke
 *
 */
public class Acronyms 
{
	private Set <String> acronym_general = new HashSet<String>();
	private Set <String> acronym_names = new HashSet<String>();
	private Set <String> acronym_educational = new HashSet<String>();	
	
	public Acronyms()
	{
		acronym_general.add("ATM");
		acronym_general.add("BPO");
		acronym_general.add("CAT");
		acronym_general.add("DNA");
		acronym_general.add("DVD");
		acronym_general.add("FAQ");
		acronym_general.add("HR");
		acronym_general.add("LCD");
		acronym_general.add("LED");
		acronym_general.add("PC");
		acronym_general.add("RAM");
		acronym_general.add("SONAR");
		acronym_general.add("USP");
		acronym_general.add("VIP");
		acronym_general.add("WWW");
		
		acronym_names.add("AIDS");
		acronym_names.add("ASEAN");
		acronym_names.add("CERN");
		acronym_names.add("FIFA");
		acronym_names.add("InterPol");
		acronym_names.add("NASA");
		acronym_names.add("NASCAR");
		acronym_names.add("SARS");
		acronym_names.add("UN");
		acronym_names.add("UNICEF");
		acronym_names.add("YMCA");
		acronym_names.add("HIV");
		
		acronym_educational.add("B.A");
		acronym_educational.add("B.Sc");
		acronym_educational.add("M.A");
		acronym_educational.add("M.Sc");
		acronym_educational.add("M.B.A");
		acronym_educational.add("MBBS");
		acronym_educational.add("MD");
		acronym_educational.add("PhD");
	}

	public Set<String> getAcronym_general() {
		return acronym_general;
	}

	public Set<String> getAcronym_names() {
		return acronym_names;
	}

	public Set<String> getAcronym_educational() {
		return acronym_educational;
	}
	
	
}
