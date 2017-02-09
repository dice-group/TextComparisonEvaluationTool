package Engines.DependencyParser;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.parser.nndep.DependencyParser;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.util.logging.Redwood;

import java.io.StringReader;
import java.util.List;

/**
 * Demonstrates how to first use the tagger, then use the NN dependency
 * parser. Note that the parser will not work on untagged text.
 *
 * @author Jon Gauthier
 */
public class DependencyParserUsCase  
{

  /** A logger for this class */
  private static Redwood.RedwoodChannels log = Redwood.channels(DependencyParserUsCase.class);

  public static void main(String[] args) 
  {
    String modelPath = DependencyParser.DEFAULT_MODEL;
    String taggerPath = "edu/stanford/nlp/models/pos-tagger/english-left3words/english-left3words-distsim.tagger";

    String allowed = "I can almost always tell when movies use fake dinosaurs.";
    String test = "at wo/uld Central [[Mikhail [[The. ";

    MaxentTagger tagger = new MaxentTagger(taggerPath);
    DependencyParser parser = DependencyParser.loadFromModelFile(modelPath);

    DocumentPreprocessor tokenizer = new DocumentPreprocessor(new StringReader(test));
    for (List<HasWord> sentence : tokenizer) 
    {
      List<TaggedWord> tagged = tagger.tagSentence(sentence);
      GrammaticalStructure gs = parser.predict(tagged);

      // Print typed dependencies
      log.info(gs);
    }
  }

}