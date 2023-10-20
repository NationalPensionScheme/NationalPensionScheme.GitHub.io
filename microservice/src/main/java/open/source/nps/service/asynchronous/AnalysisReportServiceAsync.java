package open.source.nps.service.asynchronous;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.TurquoiseSpace.utility.ExceptionLogUtil;
import com.TurquoiseSpace.utility.JsonUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import open.source.nps.enumeration.FileExtension;
import open.source.nps.model.DateSegmented;
import open.source.nps.model.FinancialYear;
import open.source.nps.model.MinCsvLineData;
import open.source.nps.model.PartialDate;
import open.source.nps.utility.Comparators;
import open.source.nps.utility.DateHelper;
import open.source.nps.utility.FinancialYearUtil;
import reactor.core.publisher.Mono;

@Log4j2
@Service
@RequiredArgsConstructor
public class AnalysisReportServiceAsync {

	@Value("${year.start}")
	private Integer yearStart;

	@Value("${year.end}")
	private Integer yearEnd;

	@Value("${scheme.invest.data.files.path}")
	private String schemeInvestDataFilesPath;

	@Autowired
	private SchemeCsvServiceAsync schemeCsvServiceAsync;


	private Set<FinancialYear> getSupportedFinancialYearsRangeSet() {

		Set<FinancialYear> financialYearSet = new HashSet<FinancialYear>();

		for (int year = yearStart; year < yearEnd; year++) {

			FinancialYear financialYear = FinancialYearUtil.get(year);

			financialYearSet.add(financialYear);
		}

		return financialYearSet;
	}

	public Mono<?> generateYearlyAverageReport() {

		log.info("generate yearly average report");

		Set<String> schemeIds = schemeCsvServiceAsync.getAllSchemesData().keySet();

		Map<String, Map<String, Float>> schemeIdVsAnalysis = new TreeMap<String, Map<String, Float>>();

		for (String schemeId : schemeIds) {

			log.info("iteration -> (schemeId) {}", schemeId);
			Map<FinancialYear, Set<Float>> financialYearVsDayWiseYearlyGrowthPercentSet = new HashMap<FinancialYear, Set<Float>>();

			Map<DateSegmented, MinCsvLineData> schemeData = schemeCsvServiceAsync.getSchemeData(schemeId);

			for (DateSegmented dateSegmented : schemeData.keySet()) {

				MinCsvLineData minCsvLineData = schemeData.get(dateSegmented);

				if (minCsvLineData.isOriginallyPresent()) {

					FinancialYear financialYear = FinancialYearUtil.identify(dateSegmented);

					DateSegmented futureDateSegmented = DateHelper.getNextYearDate(dateSegmented);

					try {
						MinCsvLineData futureMinCsvLineData = schemeData.get(futureDateSegmented);

						float nav = minCsvLineData.getNetAssetValue();
						float futureNav = futureMinCsvLineData.getNetAssetValue();
						float diff = futureNav - nav;
						float growthPercent = 100 * diff / nav;

						if (!financialYearVsDayWiseYearlyGrowthPercentSet.containsKey(financialYear)) {
							financialYearVsDayWiseYearlyGrowthPercentSet.put(financialYear, new HashSet<Float>());
						}

						Set<Float> dayWiseYearlyGrowthPercentSet = financialYearVsDayWiseYearlyGrowthPercentSet.get(financialYear);

						dayWiseYearlyGrowthPercentSet.add(growthPercent);

						financialYearVsDayWiseYearlyGrowthPercentSet.put(financialYear, dayWiseYearlyGrowthPercentSet);
					} catch (Exception exception) {
						String exceptionMessage = "encountered exception -> (schemeId) " + schemeId + " (dateSegmented) " + dateSegmented + " (futureDateSegmented) " + futureDateSegmented;
						ExceptionLogUtil.logException(exception, exceptionMessage);
					}

				}

			}

			log.info("identified -> (schemeId) {} (financialYearVsDayWiseYearlyGrowthPercentSet) {}",
					schemeId, financialYearVsDayWiseYearlyGrowthPercentSet);

			// calculate average growth percent, for each financial year
			// Map<FinancialYear, Float> financialYearVsYearlyGrowthPercentAverage = new HashMap<FinancialYear, Float>();
			Map<String, Float> financialYearVsYearlyGrowthPercentAverage = new TreeMap<String, Float>();

			for (FinancialYear financialYear : financialYearVsDayWiseYearlyGrowthPercentSet.keySet()) {

				Set<Float> dayWiseYearlyGrowthPercentSet = financialYearVsDayWiseYearlyGrowthPercentSet.get(financialYear);

				int count = dayWiseYearlyGrowthPercentSet.size();

				float sumOfPercentage = 0.0f;

				for (float dayWiseYearlyGrowthPercent : dayWiseYearlyGrowthPercentSet) {
					sumOfPercentage = sumOfPercentage + dayWiseYearlyGrowthPercent;
				}

				float averagePercentage = sumOfPercentage / count;

				// financialYearVsYearlyGrowthPercentAverage.put(financialYear, averagePercentage);
				financialYearVsYearlyGrowthPercentAverage.put(financialYear.getHumanReadable(), averagePercentage);
			}

			log.info("identified -> (schemeId) {} (financialYearVsYearlyGrowthPercentAverage) {}",
					schemeId, financialYearVsYearlyGrowthPercentAverage);

			// fill missing financial years
			Set<FinancialYear> identifiedFinancialYearSet = financialYearVsDayWiseYearlyGrowthPercentSet.keySet();

			Set<FinancialYear> missingFinancialYearSet = getSupportedFinancialYearsRangeSet();
			missingFinancialYearSet.removeAll(identifiedFinancialYearSet);

			Map<String, Float> missingYearVsYearlyGrowthPercentAverage = new TreeMap<String, Float>();

			for (FinancialYear missingFinancialYear : missingFinancialYearSet) {
				missingYearVsYearlyGrowthPercentAverage.put(missingFinancialYear.getHumanReadable(), 0.0f);
			}

			log.info("identified -> (schemeId) {} (missingYearVsYearlyGrowthPercentAverage) {}",
					schemeId, missingYearVsYearlyGrowthPercentAverage);

			financialYearVsYearlyGrowthPercentAverage.putAll(missingYearVsYearlyGrowthPercentAverage);

			schemeIdVsAnalysis.put(schemeId, financialYearVsYearlyGrowthPercentAverage);

		}

		return Mono.just(schemeIdVsAnalysis);
	}

	public Map<Integer, Map<PartialDate, Float>> generateDailyBasisAnnualGrowthReport(String schemeId) {

		log.info("generate daily basis annual growth report -> (schemeId) {}", schemeId);

		Map<DateSegmented, MinCsvLineData> schemeData = schemeCsvServiceAsync.getSchemeData(schemeId);

		Map<Integer, Map<PartialDate, Float>> calendarYearVsPartialDateVsAnnualGrowthPecentage = new TreeMap<Integer, Map<PartialDate, Float>>();

		for (DateSegmented dateSegmented : schemeData.keySet()) {

			int calendarYear = dateSegmented.getYyyy();

			if (calendarYear >= yearEnd) {
				continue;
			}

			DateSegmented futureDateSegmented = DateHelper.getNextYearDate(dateSegmented);

			try {
				MinCsvLineData minCsvLineData = schemeData.get(dateSegmented);
				MinCsvLineData futureMinCsvLineData = schemeData.get(futureDateSegmented);

				float nav = minCsvLineData.getNetAssetValue();
				float futureNav = futureMinCsvLineData.getNetAssetValue();

				float diff = futureNav - nav;
				float growthPercent = 100 * diff / nav;

				if (!calendarYearVsPartialDateVsAnnualGrowthPecentage.containsKey(calendarYear)) {
					calendarYearVsPartialDateVsAnnualGrowthPecentage.put(calendarYear, new TreeMap<PartialDate, Float>(Comparators.PARTIAL_DATE_ASCENDING));
				}

				Map<PartialDate, Float> partialDateVsAnnualGrowth = calendarYearVsPartialDateVsAnnualGrowthPecentage.get(calendarYear);

				PartialDate partialDate = DateHelper.getPartialDate(dateSegmented);

				partialDateVsAnnualGrowth.put(partialDate, growthPercent);

				calendarYearVsPartialDateVsAnnualGrowthPecentage.put(calendarYear, partialDateVsAnnualGrowth);
			} catch (Exception exception) {
				String exceptionMessage = "encountered exception -> (schemeId) " + schemeId + " (dateSegmented) " + dateSegmented + " (futureDateSegmented) " + futureDateSegmented;
				ExceptionLogUtil.logException(exception, exceptionMessage);
			}

		}

		log.info("identified -> (schemeId) {} (calendarYearVsPartialDateVsAnnualGrowthPecentage) {}",
				schemeId, calendarYearVsPartialDateVsAnnualGrowthPecentage);

		// -------------------------------------------------------------------------------------------------------------------------------------

		Set<PartialDate> allPartialDates = new TreeSet<PartialDate>(Comparators.PARTIAL_DATE_ASCENDING);

		calendarYearVsPartialDateVsAnnualGrowthPecentage.values()
				.stream()
				.map(partialDateVsAnnualGrowth -> partialDateVsAnnualGrowth.keySet())
				.forEach(partialDateSetForCalendarYear -> allPartialDates.addAll(partialDateSetForCalendarYear));

		log.info("identified -> (schemeId) {} (allPartialDates) {}",
				schemeId, allPartialDates);

		// -------------------------------------------------------------------------------------------------------------------------------------

		// the average overall for that day, across all the years

		Map<PartialDate, Float> partialDateVsOverallAverageAnnualGrowth = new TreeMap<PartialDate, Float>(Comparators.PARTIAL_DATE_ASCENDING);

		for (PartialDate partialDate : allPartialDates) {

			int count = 0;
			float sumOfPercentage = 0.0f;

			for (Integer calendarYear : calendarYearVsPartialDateVsAnnualGrowthPecentage.keySet()) {
				Map<PartialDate, Float> calendarYearData = calendarYearVsPartialDateVsAnnualGrowthPecentage.get(calendarYear);
				Float calendarDateAnnualGrowth = calendarYearData.get(partialDate);

				if (null == calendarDateAnnualGrowth) {
					continue;
				}

				count++;
				sumOfPercentage = sumOfPercentage + calendarDateAnnualGrowth;
			}

			float averagePercentage = sumOfPercentage / count;

			partialDateVsOverallAverageAnnualGrowth.put(partialDate, averagePercentage);

		}

		// this calendar year 0 is for representing average of particular day across all the years

		int calendarYear = 0;
		calendarYearVsPartialDateVsAnnualGrowthPecentage.put(calendarYear, partialDateVsOverallAverageAnnualGrowth);

		log.info("identified -> (schemeId) {} (partialDateVsOverallAverageAnnualGrowth) {}",
				schemeId, partialDateVsOverallAverageAnnualGrowth);

		return calendarYearVsPartialDateVsAnnualGrowthPecentage;

		// -------------------------------------------------------------------------------------------------------------------------------------

		// fill in missing calendar years data if required

	}

	private void writeSchemeInvestmentAnalysisToFile(String schemeId, Map<Integer, Map<PartialDate, Float>> calendarYearVsPartialDateVsAnnualGrowthPecentage) throws Exception {

		String filePath = schemeInvestDataFilesPath + schemeId + FileExtension.JSON.getExtension();
		File file = new File(filePath);

		boolean fileExists = file.exists();
		log.info("file metadata -> (filePath) {} (fileExists) {}", filePath, fileExists);

		if (fileExists) {
			boolean fileDeleted = file.delete();
			log.info("got -> (fileDeleted) {}", fileDeleted);
		}

		FileWriter fileWriter = new FileWriter(file);
		BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

		String jsonString = JsonUtil.convertObjectToJson(calendarYearVsPartialDateVsAnnualGrowthPecentage);

		bufferedWriter.write(jsonString);

		bufferedWriter.close();

		log.info("written data to file and closed the writer");
	}

	public Mono<?> generateDailyBasisAnnualGrowthReport() {

		log.info("generate daily basis annual growth report");

		Set<String> schemeIds = schemeCsvServiceAsync.getAllSchemesData().keySet();

		schemeIds.stream()
			.forEach(schemeId -> {
				Map<Integer, Map<PartialDate, Float>> calendarYearVsPartialDateVsAnnualGrowthPecentage = generateDailyBasisAnnualGrowthReport(schemeId);
				try {
					writeSchemeInvestmentAnalysisToFile(schemeId, calendarYearVsPartialDateVsAnnualGrowthPecentage);
				} catch (Exception exception) {
					ExceptionLogUtil.logException(exception, "Failed to write investment data for : " + schemeId);
				}
			});

		return Mono.just(true);
	}

}
