package open.source.nps.utility;

import java.util.LinkedHashMap;
import java.util.Map;

import java.util.stream.Collectors;

public class Sorters {

	public static <K, V extends Comparable<? super V>> Map<K, V> sortMapByValueDescending(Map<K, V> map) {

		return map.entrySet()
			.stream()
			.sorted(Map.Entry.<K, V>comparingByValue().reversed())
			.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
	}

}
