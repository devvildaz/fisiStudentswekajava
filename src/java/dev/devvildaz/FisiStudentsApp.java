package dev.devvildaz;

import java.io.File;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.google.common.base.Strings;

import weka.core.Instances;
import weka.core.Attribute;
import weka.core.converters.CSVLoader;

public class FisiStudentsApp {
	private static final int capacityFmtString = 16;
	
	public static List<Attribute> getAttributes(Instances instances) {
		Enumeration<Attribute> enumAttributes = 
				instances.enumerateAttributes();
		
		Iterable<Attribute> iterable = () -> enumAttributes.asIterator();
		
		List<Attribute> dataAttributes = StreamSupport.stream(
			iterable.spliterator(), false
		).collect(Collectors.toList());
		
		return dataAttributes;
	}
	
	public static void print(Instances instances, int numRows, boolean reverse) {
		List<Attribute> attributes = getAttributes(instances);
		Supplier<Stream<String>>stream = () -> attributes.stream()
				.map((attr)-> {
					int length = attr.name().length();
					int restSpaces = capacityFmtString - length;
					String name = attr.name();
					name = Strings.padStart(name, restSpaces/2 + length, ' ');
					name = Strings.padEnd(name, restSpaces + length, ' ');
					return name;
				});
		String headerString = "|" + stream.get().collect(Collectors.joining(" | ")).concat(" |");
		String separator = String.join("", Collections.nCopies(headerString.length(), "-"));
		System.out.println(separator);
		System.out.println(headerString);
		System.out.println(separator);
		List<String> strings = stream.get().toList();
		
		int i = 0;
		int idx = 0;
		if(reverse) {
			idx = (instances.size() - 1) - numRows;
		}
		while(i < numRows) {
			System.out.print("|");
			for(int j = 0; j < attributes.size(); j++) {	
				String name = instances.get(idx).toString(j);
				int length = name.length();
				int restSpaces = strings.get(j).length() - length;
				name = Strings.padStart(name, restSpaces/2 + length, ' ');
				name = Strings.padEnd(name, restSpaces + length, ' ');
				System.out.print(name+ " | ");
			}
			System.out.println();
			i++;
			idx++;
		}
		System.out.println(separator);

	}
	
	public static void main(String[] args) throws Exception {
		final String PATH = "/home/devvildaz/Code/Java/eclipse/fisiStudents/DATA-PRUEBA_CALIFICACIONES  2017-2020-FISI.csv";
		// loader csv
		CSVLoader loader = new CSVLoader();
		// load source
		loader.setSource(new File(PATH));
		// get dataset object for this project
		Instances trainingData = loader.getDataSet();
		// printing a summary of attributes from this dataset
		
		// dataset.head(5)
		System.out.println("== Primeras 5 filas");
		print(trainingData, 5, false);
		System.out.print("\n");
		// dataset.tail(5)
		System.out.println("== Ultimas 5 filas");
		print(trainingData, 5, true);
		System.out.print("\n");
		// dataset.info()
		System.out.println(trainingData.toSummaryString());
	}
}
