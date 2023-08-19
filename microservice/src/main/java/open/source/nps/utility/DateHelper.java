package open.source.nps.utility;

import java.util.HashMap;
import java.util.Map;

import open.source.nps.model.DateSegmented;

public class DateHelper {

	private static final Map<Integer, Integer> MONTH_VS_DAYS = new HashMap<Integer, Integer>();

	static {
		MONTH_VS_DAYS.put(1, 31);
		MONTH_VS_DAYS.put(2, 28);
		MONTH_VS_DAYS.put(3, 31);
		MONTH_VS_DAYS.put(4, 30);
		MONTH_VS_DAYS.put(5, 31);
		MONTH_VS_DAYS.put(6, 30);
		MONTH_VS_DAYS.put(7, 31);
		MONTH_VS_DAYS.put(8, 31);
		MONTH_VS_DAYS.put(9, 30);
		MONTH_VS_DAYS.put(10, 31);
		MONTH_VS_DAYS.put(11, 30);
		MONTH_VS_DAYS.put(12, 31);
	}


	public static boolean isLeapYear(int yyyy) {

		// year 2005 , 2006 , 2007 are not leap years
		// year 2004 , 2008 are leap years

		boolean isDivisibleBy4 = (0 == (yyyy % 4));

		boolean isDivisibleBy100 = (0 == (yyyy % 100));
		boolean isDivisibleBy400 = (0 == (yyyy % 400));

		// year 1700 , 1800 , 1900 are not leap years
		// year 1600 , 2000 are leap years

		if (isDivisibleBy100) {
			return isDivisibleBy400;
		} else {
			return isDivisibleBy4;
		}
	}

	public static int getDaysInMonth(int mm, int yyyy) {

		if (2 == mm && isLeapYear(yyyy)) {
			return 29;
		} else {
			return MONTH_VS_DAYS.get(mm);
		}
	}

	public static String getStringMMDDYYYY(int mm, int dd, int yyyy) {

		StringBuilder stringBuilder = new StringBuilder();

		if (mm <= 9) {
			stringBuilder.append("0");
		}
		stringBuilder.append(mm);

		stringBuilder.append("/");

		if (dd <= 9) {
			stringBuilder.append("0");
		}
		stringBuilder.append(dd);

		stringBuilder.append("/");

		stringBuilder.append(yyyy);

		return stringBuilder.toString();
	}

	public static String getStringMMDDYYYY(DateSegmented dateSegmented) {

		return getStringMMDDYYYY(dateSegmented.getMm(), dateSegmented.getDd(), dateSegmented.getYyyy());
	}

	public static DateSegmented getNextYearDate(DateSegmented dateSegmented) {

		boolean isLeapDay = isLeapYear(dateSegmented.getYyyy())
				&& 2 == dateSegmented.getMm()
				&& 29 == dateSegmented.getDd();

		DateSegmented futureDateSegmented = DateSegmented.builder()
				.mm(dateSegmented.getMm())
				.dd(isLeapDay
						? dateSegmented.getDd() - 1
						: dateSegmented.getDd())
				.yyyy(dateSegmented.getYyyy() + 1)
				.build();

		return futureDateSegmented;
	}

}
