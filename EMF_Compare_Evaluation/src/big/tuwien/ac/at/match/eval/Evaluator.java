package big.tuwien.ac.at.match.eval;

import java.io.File;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Vector;

import org.semanticweb.owl.align.Alignment;

import fr.inrialpes.exmo.align.impl.BasicParameters;
import fr.inrialpes.exmo.align.impl.eval.PRecEvaluator;
import fr.inrialpes.exmo.align.parser.AlignmentParser;


@SuppressWarnings("deprecation")
public class Evaluator {

	public static void main(String[] args) throws Exception { 
		final String pathToManualMaps = "correspondences/manual/";
		final String pathToComputedMaps = "correspondences/emf_compare/";
		
		HashMap<String, String> map = new HashMap<String, String>();
		configureMappingFiles(map);		
		
		AlignmentParser aparser = new AlignmentParser(0); 
		Alignment reference;
		Alignment a1; 
		BasicParameters p = new BasicParameters();
		Vector<PRecEvaluator> results = new Vector<PRecEvaluator>();
		
		for(Entry<String, String> e : map.entrySet()){
			System.out.println("\n**** Results for " + e.getKey().substring(0,e.getKey().indexOf("_manual")) + " *****");
			PRecEvaluator evaluator;
			reference = aparser.parse( new File( pathToManualMaps + e.getKey() ).toURI() ); 
			a1 = aparser.parse( new File( pathToComputedMaps + e.getValue() ).toURI() ); 
			evaluator = new PRecEvaluator( reference, a1);
			evaluator.eval( (Properties) p );
			
			System.out.println("Expected alignments: " + evaluator.getExpected());
			System.out.println("Found alignments: " +evaluator.getFound());
			System.out.println("Correct alignments: " + evaluator.getCorrect());
			System.out.println("False alignments: " + (evaluator.getFound() - evaluator.getCorrect()));
			System.out.println("Missed alignments: " + (evaluator.getExpected() - evaluator.getCorrect()));
			System.out.println("Precision: " + evaluator.getPrecision());
			System.out.println("Recall: " + evaluator.getRecall());
			System.out.println("F-measure: " + evaluator.getFmeasure());
			
			results.add(evaluator);
		}
		
		avg_computation(results);
	}

	private static void configureMappingFiles(HashMap<String, String> map) {
		map.put("Ecore_2_EER_manual.xml", "Ecore_2_EER_emfcompare.xml");
		map.put("Ecore_2_WebML_manual.xml", "Ecore_2_WebML_emfcompare.xml");
		map.put("EER_2_WebML_manual.xml", "EER_2_WebML_emfcompare.xml");
		map.put("Ecore_2_UML1.4_manual.xml" , "Ecore_2_UML1.4_emfcompare.xml");
		map.put("Ecore_2_UML2.0_manual.xml" , "Ecore_2_UML2.0_emfcompare.xml");	
		map.put("UML1.4_2_UML2.0_manual.xml" , "UML1.4_2_UML2.0_emfcompare.xml");	
		map.put("EER_2_UML1.4_manual.xml", "EER_2_UML1.4_emfcompare.xml");	
		map.put("EER_2_UML2.0_manual.xml", "EER_2_UML2.0_emfcompare.xml");
		map.put("UML1.4_2_WebML_manual.xml", "UML1.4_2_WebML_emfcompare.xml");		
		map.put("UML2.0_2_WebML_manual.xml", "UML2.0_2_WebML_emfcompare.xml");
	}

	private static void avg_computation(Vector<PRecEvaluator> results) {
		double precision_sum = 0;
		double recall_sum = 0;
		double fmeasure_sum = 0;
		double scenarios = results.size();
		
		for(PRecEvaluator eval : results){
			precision_sum = precision_sum + eval.getPrecision();
			recall_sum = precision_sum + eval.getRecall();
			fmeasure_sum = fmeasure_sum + eval.getFmeasure();
		}
		
		System.out.println("\n********** SUMMARY ***********");
		System.out.println(" Avg Precision: " + precision_sum /scenarios);
		System.out.println(" Avg Recall: " + recall_sum / scenarios);
		System.out.println(" Avg F-measure: " + fmeasure_sum / scenarios);
	}

}
