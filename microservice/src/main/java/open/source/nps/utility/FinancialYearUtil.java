package open.source.nps.utility;

import java.util.HashMap;
import java.util.Map;

import open.source.nps.model.DateSegmented;
import open.source.nps.model.FinancialYear;

public class FinancialYearUtil {

	private static Map<Integer, FinancialYear> BUILT = new HashMap<Integer, FinancialYear>();

	private static void build(int actualFinancialYearStarting) {

		int actualFinancialYearEnding = actualFinancialYearStarting + 1;

		int open = actualFinancialYearStarting % 100;
		int close = actualFinancialYearEnding % 100;

		FinancialYear financialYear = new FinancialYear();
		financialYear.setStart(actualFinancialYearStarting);
		financialYear.setEnd(actualFinancialYearEnding);
		financialYear.setHumanReadable(actualFinancialYearStarting + "-" + actualFinancialYearEnding);
		financialYear.setShortHand(open + "-" + close);

		BUILT.put(actualFinancialYearStarting, financialYear);
	}

	public static FinancialYear get(int actualFinancialYearStarting) {

		if (!BUILT.containsKey(actualFinancialYearStarting)) {
			build(actualFinancialYearStarting);
		}

		return BUILT.get(actualFinancialYearStarting);
	}

	public static FinancialYear identify(DateSegmented dateSegmented) {

		if (null == dateSegmented) {
			return null;
		}

		int actualFinancialYearStarting = dateSegmented.getMm() > 3
				? dateSegmented.getYyyy()
				: (dateSegmented.getYyyy() - 1);

		return get(actualFinancialYearStarting);
	}

}
