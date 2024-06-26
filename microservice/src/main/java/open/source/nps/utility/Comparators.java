package open.source.nps.utility;

import java.util.Comparator;

import open.source.nps.model.DateSegmented;
import open.source.nps.model.PartialDate;

public class Comparators {

	public static final Comparator<DateSegmented> DATE_SEGMENTED_ASCENDING = new Comparator<DateSegmented>() {

		private int compareIntegers(int first, int second) {

			return (first == second)
					? 0
					: (first < second)
						? -1
						: 1;
		}

		@Override
		public int compare(DateSegmented first, DateSegmented second) {

			int yearComparison = compareIntegers(first.getYyyy(), second.getYyyy());
			if (0 == yearComparison) {
				int monthComparison = compareIntegers(first.getMm(), second.getMm());
				if (0 == monthComparison) {
					return compareIntegers(first.getDd(), second.getDd());
				}
				return monthComparison;
			}
			return yearComparison;
		}

	};

	public static final Comparator<PartialDate> PARTIAL_DATE_ASCENDING = new Comparator<PartialDate>() {

		private int compareIntegers(int first, int second) {

			return (first == second)
					? 0
					: (first < second)
						? -1
						: 1;
		}

		@Override
		public int compare(PartialDate first, PartialDate second) {

			int monthComparison = compareIntegers(first.getMm(), second.getMm());
			if (0 == monthComparison) {
				return compareIntegers(first.getDd(), second.getDd());
			}
			return monthComparison;
		}

	};

}
