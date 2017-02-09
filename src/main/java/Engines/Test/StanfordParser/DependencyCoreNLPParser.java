package Engines.Test.StanfordParser;

import java.util.Properties;

import edu.stanford.nlp.io.IOUtils;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.parser.nndep.DependencyParser;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.AnnotationPipeline;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.PropertiesUtils;
import edu.stanford.nlp.util.logging.Redwood;

/**
 * USING THE EDITED EXAMPLE:
 * 
 * "Demonstrates how to use the NN dependency
 * parser via a CoreNLP pipeline."
 *
 * @author Christopher Manning
 */
public class DependencyCoreNLPParser {

  /** A logger for this class */
  private static Redwood.RedwoodChannels log = Redwood.channels(DependencyCoreNLPParser.class);

  public static void main(String[] args) {
    String allowed = "I can almost always tell when movies use fake dinosaurs.";
    String test = "at wo/uld Central [[Mikhail [[The. ";
    
    
    Annotation ann = new Annotation(test);

    Properties props = PropertiesUtils.asProperties("annotators", "tokenize,ssplit,pos,depparse",
            										"depparse.model", DependencyParser.DEFAULT_MODEL
    												);

    AnnotationPipeline pipeline = new StanfordCoreNLP(props);

    pipeline.annotate(ann);

    for (CoreMap sent : ann.get(CoreAnnotations.SentencesAnnotation.class)) {
      SemanticGraph sg = sent.get(SemanticGraphCoreAnnotations.BasicDependenciesAnnotation.class);
      log.info(IOUtils.eolChar + sg.toString(SemanticGraph.OutputFormat.LIST));
    }
  }

}