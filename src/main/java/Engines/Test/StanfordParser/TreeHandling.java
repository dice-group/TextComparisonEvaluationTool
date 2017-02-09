package Engines.Test.StanfordParser;

/**
* USING THE EDITED EXAMPLE: 
* 
* A Stanford CoreNLP tree visualizer.  
* Copyright (C) 2014  Long Qiu 
 
* This program is free software; you can redistribute it and/or 
* modify it under the terms of the GNU General Public License 
* as published by the Free Software Foundation; either version 2 
* of the License, or (at your option) any later version. 
 
* This program is distributed in the hope that it will be useful, 
* but WITHOUT ANY WARRANTY; without even the implied warranty of 
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
* GNU General Public License for more details. 
 
* You should have received a copy of the GNU General Public License 
* along with this program; if not, write to the Free Software 
* Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA. 
*/
 
/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved. 
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions 
 * are met: 
 * 
 *   - Redistributions of source code must retain the above copyright 
 *     notice, this list of conditions and the following disclaimer. 
 * 
 *   - Redistributions in binary form must reproduce the above copyright 
 *     notice, this list of conditions and the following disclaimer in the 
 *     documentation and/or other materials provided with the distribution. 
 * 
 *   - Neither the name of Oracle or the names of its 
 *     contributors may be used to endorse or promote products derived 
 *     from this software without specific prior written permission. 
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS 
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, 
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR 
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR 
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, 
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, 
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR 
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF 
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING 
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */  
 
/*
 * The Dynamic Tree part is based on an example provided by Richard Stanford, 
 * a tutorial reader. 
 */ 
 
 
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection; 
import java.util.LinkedList; 
import java.util.List; 
import java.util.Properties; 
 
import javax.swing.tree.DefaultMutableTreeNode; 
import edu.stanford.nlp.ling.CoreLabel; 
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation; 
import edu.stanford.nlp.pipeline.Annotation; 
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.trees.EnglishGrammaticalStructure; 
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.Labeled;
import edu.stanford.nlp.trees.LabeledScoredTreeFactory; 
import edu.stanford.nlp.trees.PennTreebankLanguagePack; 
import edu.stanford.nlp.trees.Tree; 
import edu.stanford.nlp.trees.TreeFactory;
import edu.stanford.nlp.trees.TreebankLanguagePack;
import edu.stanford.nlp.trees.TypedDependency; 
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation; 
import edu.stanford.nlp.semgraph.SemanticGraph; 
import edu.stanford.nlp.semgraph.SemanticGraphEdge; 
import edu.stanford.nlp.util.CoreMap; 
 
public class TreeHandling { 
 private static StanfordCoreNLP pipeline; 
 static TreeFactory lstf = new LabeledScoredTreeFactory(); 
 private static final String ANALYZERS  ="tokenize, ssplit, pos, lemma, parse";  
 static Properties props; 
  
  
 public static void main(String[] args){ 
  String allowed = "Here are some example sentences for SCNTreeViewer. There are a few symbols in Scala that are special and cannot be defined or used as method names. You have to understand this."; 
  String test1 = "]] and [[anti-colorado]] and [[san jases and the british army]] for the film was created by [[paris county league]] and [[tamil nadu state assembly election, 1971|1967 elections]]. ";
  String test2 = "A quick brown fox jumped over the lazy dog. اردو";
  String harderror = "the the in Kurhaus the are, 60s in Institute the libraries the originally district. other line is officer of County, [[dojin www.atimes.com/atimes/China/FB04Ad04.html last linseed Poms elsewhere of Tianyong]] overall the Albums]] federal .";
  
  test(harderror);
  test(allowed);
  test(test1);
  test(test2);
  System.out.println("DONE");
  
  showDepTree(harderror);
  showDepTree(allowed); 
  showDepTree(test1); 
  showDepTree(test2); 
   
 } 
  
 private static void lazyInit(){ 
  if(props != null){ 
   return; 
  } 
  props = new Properties(); 
  //No effect. Still can't recognize it 
  //props.put("pos.model", "edu/stanford/nlp/models/pos-tagger/english-left3words/english-left3words-distsim.tagger"); 
 
  props.put("annotators", ANALYZERS); 
  pipeline = new StanfordCoreNLP(props);  
  System.err.println("Loading done."); 
 } 
 
 
 public static Annotation parseArticle(String text){ 
  Annotation document = new Annotation(text); 
  // run all Annotators on this text 
  pipeline.annotate(document); 
  return document; 
 } 
 
 public static ArrayList<Tree> getParseTree(Annotation document){ 
  ArrayList<Tree> forest = new ArrayList<Tree>(); 
  List<CoreMap> sentences = document.get(SentencesAnnotation.class); 
  for(CoreMap sentence: sentences) 
  { 
   // this is the parse tree of the current sentence 
   Tree tree = sentence.get(TreeAnnotation.class);  
   // Alternatively, this is the Stanford dependency graph of the current sentence, but without punctuations 
   // SemanticGraph dependencies = sentence.get(BasicDependenciesAnnotation.class); 
   forest.add(tree); 
  } 
  return forest; 
 } 
  
 public static Collection<TypedDependency> getDeps(Tree parseTree){ 
  //Dependencies, punctuations included 
  GrammaticalStructure gs = new EnglishGrammaticalStructure(parseTree, new PennTreebankLanguagePack().punctuationWordAcceptFilter()); 
  Collection<TypedDependency> tdl = gs.typedDependencies();  // typedDependenciesCollapsed() eats the prepositions, etc 
  gs = new EnglishGrammaticalStructure(parseTree); 
  tdl.addAll(gs.typedDependencies());
  return tdl;
 } 
  
 public static void showDepTree(String text){ 
  if(props == null){ 
   lazyInit(); 
  } 
  Annotation document  = parseArticle(text); 
  ArrayList<Tree> parseTrees = getParseTree(document); 
 
  DefaultMutableTreeNode rootParse = new DefaultMutableTreeNode(); 
  for(int i=0; i<parseTrees.size(); i++){ 
   rootParse.add(toDMTree(null, parseTrees.get(i))); 
  } 
  
  for(Tree tree: parseTrees) System.out.println(getDeps(tree));
 } 
  
  
 
 public static DefaultMutableTreeNode toDMTree(DefaultMutableTreeNode root, Tree tree){ 
  if(root == null){ 
   root = new DefaultMutableTreeNode(); 
  } 
   
  String nodeContent = tree.nodeString(); 
  root.setUserObject(nodeContent); 
  for(Tree c: tree.children()){ 
   DefaultMutableTreeNode n = toDMTree(null, c); 
   root.add(n); 
  } 
  return root; 
 } 
 
 
 public static DefaultMutableTreeNode toDMTree(IndexedWord root, SemanticGraph dependencies){ 
 
  if(root == null){ 
   root = dependencies.getFirstRoot(); 
  } 
 
  DefaultMutableTreeNode node = new DefaultMutableTreeNode(); 
 
  String nodeContent = root.value(); 
 
  for(SemanticGraphEdge edge: dependencies.edgeIterable()){ 
   if(edge.getDependent().equals(root)){ 
    nodeContent = "<-"+ edge.getRelation() +"- "+nodeContent; 
    break; 
   } 
  } 
 
  node.setUserObject(nodeContent); 
  for(IndexedWord c: dependencies.getChildList(root)){ 
   DefaultMutableTreeNode n = toDMTree(c, dependencies); 
   node.add(n); 
  } 
  return node; 
 } 
 
 
 public static Tree makeTreeRobust(Collection<TypedDependency> tdl){ 
  LinkedList<TypedDependency> toAssemble = new LinkedList<TypedDependency>(); 
  for(TypedDependency dep: tdl){ 
   toAssemble.add(dep); 
  } 
  Tree tree = makeTree(toAssemble, true, null); 
  return tree; 
 } 
 /**
  *  
  * @param tdl 
  * @param fail set true to return a null tree if the constructiion fails 
  * @param tree null to build a new tree, or you can start with a partial tree 
  * @return 
  * Feb 22. a gov could be missing, which fails the tree construction process. 
  * Use GrammaticalStructure.root() instead, whose POS nodes to be removed. 
  */ 
 public static Tree makeTree(LinkedList<TypedDependency> toAssemble, boolean fail, Tree tree){ 
  if(tree == null){ 
   tree = lstf.newTreeNode("DUMMYROOT",null); 
  } 
 
  toAssemble.add(null);// 
  int counter = toAssemble.size(); 
 
  while(toAssemble.size()>0){ 
   //1. pick the next dep 
   TypedDependency dep = toAssemble.getFirst(); 
   if(dep == null){ 
    toAssemble.poll(); 
    if(counter-- >0){ 
     toAssemble.add(null); 
     continue; 
    }else{ 
     if(toAssemble.size()>0 && fail){ 
      tree = null; 
     } 
     break; 
    } 
   } 
 
   //2. assemble it onto the tree 
   Tree newRoot = putOnBranch(dep, tree); 
   //2.1 success -> remove it from the set 
   toAssemble.remove(dep); 
   if(newRoot!=null){ 
    tree = newRoot; 
    //    System.out.println(tree+" BetterText.makeTree()"); 
    //    System.out.println("Added:\t"+dep.gov() +"-->"+dep.dep()); 
   }else{ 
    //2.2 fail -> put it back at the tail of the set 
    //    System.out.println("Skipd:\t"+dep.gov() +"-->"+dep.dep()); 
    //    System.out.print("."); 
    toAssemble.add(toAssemble.size(), dep); 
   } 
  } 
  return tree.getChild(0); 
 } 
 
 private static Tree putOnBranch(TypedDependency dep, Tree tree){ 
  /*
   * Each node is a tree with a single child 
   */ 
  Tree mySubtree = lstf.newTreeNode(dep.gov().backingLabel(), new LinkedList<Tree>()); 
  mySubtree.setValue("[<-"+ dep.reln() +"-] "+dep.dep().value());//nudge in the dependency relation information 
 
  if(tree.children().length==0){ 
   if(tree.label().value().toString().equals("DUMMYROOT")){ 
    tree.addChild(mySubtree); 
    return tree; 
   }else{ 
    //Shouldn't happen 
    System.err.println("Forgot to add a child earlier."); 
    return null; 
   } 
  }else{ 
   //   System.err.println(dep.dep().label() +"\t[on]\t" + tree.label()); 
   for(Tree child:tree.children()){ 
    //if dep is child's parent, insert dep between child and its parent 
    if( ((CoreLabel)child.label()).index() == ((CoreLabel) ((Labeled) dep.dep()).label()).index()){ 
     tree.removeChild(tree.objectIndexOf(child)); 
     mySubtree.addChild(child); 
    } 
   } 
   if(mySubtree.children().length>1){ 
    tree.addChild(mySubtree); 
    return tree; 
   } 
    
   for(Tree child:tree.children()){ 
    //if dep is Child's sibling, or child 
    if( ((CoreLabel)child.label()).index() == ((CoreLabel) ((Labeled) dep.gov()).label()).index()){ 
     tree.addChild(mySubtree); 
     return tree; 
    } 
 
    if(child.children().length>0){ 
     if(putOnBranch(dep, child)!=null){ 
      return tree; 
     } 
    } 
   } 
  } 
  //    tree.getLeaves() == null 
  //check its childrens, recurisively. 
  return null; 
 } 
 
  
  public static void test(String text)
  {
      TreebankLanguagePack tlp = new PennTreebankLanguagePack();
      GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
      LexicalizedParser lp = LexicalizedParser.loadModel("edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz");
      lp.setOptionFlags(new String[]{"-maxLength", "500", "-retainTmpSubcategories"});
      TokenizerFactory<CoreLabel> tokenizerFactory =
              PTBTokenizer.factory(new CoreLabelTokenFactory(), "");
      List<CoreLabel> wordList = tokenizerFactory.getTokenizer(new StringReader(text)).tokenize();
      Tree tree = lp.apply(wordList);
      GrammaticalStructure gs = gsf.newGrammaticalStructure(tree);
      Collection<TypedDependency> tdl = gs.typedDependenciesCCprocessed(true);

  }
 
} 
