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
import open.source.nps.enumeration.PensionFundScheme;
import open.source.nps.enumeration.PensionFundSchemeIdentifier;
import open.source.nps.model.DateSegmented;
import open.source.nps.model.FinancialYear;
import open.source.nps.model.MinCsvLineData;
import open.source.nps.model.PartialDate;
import open.source.nps.utility.Comparators;
import open.source.nps.utility.DateHelper;
import open.source.nps.utility.FinancialYearUtil;
import open.source.nps.utility.Sorters;
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

	@Autowired
	private CategorizeSchemeServiceAsync categorizeSchemeServiceAsync;

	private static Map<String, Map<String, Float>> schemeIdVsFinancialYearVsYearlyGrowthPercentAverage = null;


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
			Map<FinancialYear, Set<Float>> financialYearVsDayWiseYearlyGrowthPercentSet = new TreeMap<FinancialYear, Set<Float>>();

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

		if (null == schemeIdVsFinancialYearVsYearlyGrowthPercentAverage) {
			schemeIdVsFinancialYearVsYearlyGrowthPercentAverage = schemeIdVsAnalysis;
		}

		return Mono.just(schemeIdVsAnalysis);
	}

	private void updateToMaster(Map<String, Map<String, Integer>> masterTracker, Map<String, Map<String, Integer>> schemeIdVsFinancialYearVsYearlyGrowthAverageRanking) {

		for (String schemeId : schemeIdVsFinancialYearVsYearlyGrowthAverageRanking.keySet()) {

			Map<String, Integer> financialYearVsYearlyGrowthAverageRankingMaster = masterTracker.get(schemeId);
			Map<String, Integer> financialYearVsYearlyGrowthAverageRanking = schemeIdVsFinancialYearVsYearlyGrowthAverageRanking.get(schemeId);

			if (null == financialYearVsYearlyGrowthAverageRankingMaster) {
				financialYearVsYearlyGrowthAverageRankingMaster = new TreeMap<String, Integer>();
			}

			financialYearVsYearlyGrowthAverageRankingMaster.putAll(financialYearVsYearlyGrowthAverageRanking);

			masterTracker.put(schemeId, financialYearVsYearlyGrowthAverageRankingMaster);
		}
	}

	public Mono<?> generateYearlyAverageRankingReport() {

		log.info("generate yearly average ranking report");

		// use
		// schemeIdVsFinancialYearVsYearlyGrowthPercentAverage
		// transform

		Map<PensionFundScheme, Set<PensionFundSchemeIdentifier>> schemeCategoryVsSchemeIds = categorizeSchemeServiceAsync.getAllSchemeCategoryVsSchemeIds();

		Map<String, Map<String, Integer>> masterTracker = new TreeMap<String, Map<String, Integer>>();

		// for each scheme category
		for (PensionFundScheme pensionFundScheme : schemeCategoryVsSchemeIds.keySet()) {
			
			log.info("iteration -> (pensionFundScheme) {}", pensionFundScheme);

			// limited scheme ids
			Set<PensionFundSchemeIdentifier> pensionFundSchemeIdentifierSet = schemeCategoryVsSchemeIds.get(pensionFundScheme);
			
			log.info("got -> (pensionFundSchemeIdentifierSet) {}", pensionFundSchemeIdentifierSet);

			// -----------

			Map<String, Map<String, Float>> financialYearVsSchemeIdVsYearlyGrowthPercentAverage = new TreeMap<String, Map<String, Float>>();

			for (PensionFundSchemeIdentifier pensionFundSchemeIdentifier : pensionFundSchemeIdentifierSet) {
				
				log.info("iteration -> (pensionFundSchemeIdentifier) {}", pensionFundSchemeIdentifier);

				String schemeId = pensionFundSchemeIdentifier.getId();
				
				log.info("got -> (schemeId) {}", schemeId);

				Map<String, Float> financialYearVsYearlyGrowthPercentAverage = schemeIdVsFinancialYearVsYearlyGrowthPercentAverage.get(schemeId);
				
				log.info("got -> (financialYearVsYearlyGrowthPercentAverage) {}", financialYearVsYearlyGrowthPercentAverage);

				for (String financialYear : financialYearVsYearlyGrowthPercentAverage.keySet()) {
					
					log.info("iteration -> (financialYear) {}", financialYear);

					Float yearlyGrowthPercentAverage = financialYearVsYearlyGrowthPercentAverage.get(financialYear);
					
					log.info("got -> (yearlyGrowthPercentAverage) {}", yearlyGrowthPercentAverage);

					// fill
					Map<String, Float> schemeIdVsYearlyGrowthPercentAverage = financialYearVsSchemeIdVsYearlyGrowthPercentAverage.get(financialYear);
					
					log.info("got -> (schemeIdVsYearlyGrowthPercentAverage) {}", schemeIdVsYearlyGrowthPercentAverage);

					if (null == schemeIdVsYearlyGrowthPercentAverage) {
						schemeIdVsYearlyGrowthPercentAverage = new TreeMap<String, Float>();
					}

					schemeIdVsYearlyGrowthPercentAverage.put(schemeId, yearlyGrowthPercentAverage);
					
					log.info("updated -> (schemeIdVsYearlyGrowthPercentAverage) {}", schemeIdVsYearlyGrowthPercentAverage);

					financialYearVsSchemeIdVsYearlyGrowthPercentAverage.put(financialYear, schemeIdVsYearlyGrowthPercentAverage);
					
					log.info("updated -> (financialYearVsSchemeIdVsYearlyGrowthPercentAverage) {}", financialYearVsSchemeIdVsYearlyGrowthPercentAverage);
				}
			}

			// -----------
			
			log.info("--------------------------------------------------------------------------------------------------------------------------");

			// sort

			Map<String, Map<String, Integer>> schemeIdVsFinancialYearVsYearlyGrowthAverageRanking = new TreeMap<String, Map<String, Integer>>();
			
			Set<String> financialYearSet = financialYearVsSchemeIdVsYearlyGrowthPercentAverage.keySet();
			
			log.info("got -> (financialYearSet) {}", financialYearSet);

			for (String financialYear : financialYearSet) {
				
				log.info("iteration -> (financialYear) {}", financialYear);

				Map<String, Float> schemeIdVsYearlyGrowthPercentAverage = financialYearVsSchemeIdVsYearlyGrowthPercentAverage.get(financialYear);
				
				log.info("got -> (schemeIdVsYearlyGrowthPercentAverage) {}", schemeIdVsYearlyGrowthPercentAverage);

				Map<String, Float> schemeIdVsYearlyGrowthPercentAverageReverseSortedByValue = Sorters.sortMapByValueDescending(schemeIdVsYearlyGrowthPercentAverage);
				
				log.info("got -> (schemeIdVsYearlyGrowthPercentAverageReverseSortedByValue) {}", schemeIdVsYearlyGrowthPercentAverageReverseSortedByValue);

				// fill
				int rank = 0;

				for (String schemeId : schemeIdVsYearlyGrowthPercentAverageReverseSortedByValue.keySet()) {
					
					log.info("iteration -> (schemeId) {}", schemeId);

					Float yearlyGrowthPercentAverage = schemeIdVsYearlyGrowthPercentAverageReverseSortedByValue.get(schemeId);
					
					log.info("got -> (yearlyGrowthPercentAverage) {}", yearlyGrowthPercentAverage);

					int currentRank = rank;

					if (0.0f == yearlyGrowthPercentAverage) {
						currentRank = 0;
					} else {
						rank++;
						currentRank = rank;
					}
					
					log.info("identified -> (rank) {} (currentRank) {}", rank, currentRank);

					Map<String, Integer> financialYearVsYearlyGrowthAverageRanking = schemeIdVsFinancialYearVsYearlyGrowthAverageRanking.get(schemeId);
					
					log.info("got -> (financialYearVsYearlyGrowthAverageRanking) {}", financialYearVsYearlyGrowthAverageRanking);

					if (null == financialYearVsYearlyGrowthAverageRanking) {
						financialYearVsYearlyGrowthAverageRanking = new TreeMap<String, Integer>();
					}

					financialYearVsYearlyGrowthAverageRanking.put(financialYear, currentRank);
					
					log.info("updated -> (financialYearVsYearlyGrowthAverageRanking) {}", financialYearVsYearlyGrowthAverageRanking);

					schemeIdVsFinancialYearVsYearlyGrowthAverageRanking.put(schemeId, financialYearVsYearlyGrowthAverageRanking);
					
					log.info("updated -> (schemeIdVsFinancialYearVsYearlyGrowthAverageRanking) {}", schemeIdVsFinancialYearVsYearlyGrowthAverageRanking);
				}
			}

			log.info("--------------------------------------------------------------------------------------------------------------------------");

			log.info("got -> (masterTracker) {}", masterTracker);

			updateToMaster(masterTracker, schemeIdVsFinancialYearVsYearlyGrowthAverageRanking);

			log.info("updated -> (masterTracker) {}", masterTracker);

			log.info("**************************************************************************************************************************");

		}

		return Mono.just(masterTracker);
	}

	public Map<Integer, Map<PartialDate, Float>> generateDailyBasisAnnualGrowthReport(String schemeId) {

		log.info("generate daily basis annual growth report -> (schemeId) {}", schemeId);

		Map<DateSegmented, MinCsvLineData> schemeData = schemeCsvServiceAsync.getSchemeData(schemeId);

		Map<Integer, Map<PartialDate, Float>> calendarYearVsPartialDateVsAnnualGrowthPercentage = new TreeMap<Integer, Map<PartialDate, Float>>();

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

				if (!calendarYearVsPartialDateVsAnnualGrowthPercentage.containsKey(calendarYear)) {
					calendarYearVsPartialDateVsAnnualGrowthPercentage.put(calendarYear, new TreeMap<PartialDate, Float>(Comparators.PARTIAL_DATE_ASCENDING));
				}

				Map<PartialDate, Float> partialDateVsAnnualGrowth = calendarYearVsPartialDateVsAnnualGrowthPercentage.get(calendarYear);

				PartialDate partialDate = DateHelper.getPartialDate(dateSegmented);

				partialDateVsAnnualGrowth.put(partialDate, growthPercent);

				calendarYearVsPartialDateVsAnnualGrowthPercentage.put(calendarYear, partialDateVsAnnualGrowth);
			} catch (Exception exception) {
				String exceptionMessage = "encountered exception -> (schemeId) " + schemeId + " (dateSegmented) " + dateSegmented + " (futureDateSegmented) " + futureDateSegmented;
				ExceptionLogUtil.logException(exception, exceptionMessage);
			}

		}

		log.info("identified -> (schemeId) {} (calendarYearVsPartialDateVsAnnualGrowthPercentage) {}",
				schemeId, calendarYearVsPartialDateVsAnnualGrowthPercentage);

		// -------------------------------------------------------------------------------------------------------------------------------------

		Set<PartialDate> allPartialDates = new TreeSet<PartialDate>(Comparators.PARTIAL_DATE_ASCENDING);

		calendarYearVsPartialDateVsAnnualGrowthPercentage.values()
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

			for (Integer calendarYear : calendarYearVsPartialDateVsAnnualGrowthPercentage.keySet()) {
				Map<PartialDate, Float> calendarYearData = calendarYearVsPartialDateVsAnnualGrowthPercentage.get(calendarYear);
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
		calendarYearVsPartialDateVsAnnualGrowthPercentage.put(calendarYear, partialDateVsOverallAverageAnnualGrowth);

		log.info("identified -> (schemeId) {} (partialDateVsOverallAverageAnnualGrowth) {}",
				schemeId, partialDateVsOverallAverageAnnualGrowth);

		return calendarYearVsPartialDateVsAnnualGrowthPercentage;

		// -------------------------------------------------------------------------------------------------------------------------------------

		// fill in missing calendar years data if required

	}

	private void writeSchemeInvestmentAnalysisToJsonFile(String schemeId, Map<Integer, Map<PartialDate, Float>> calendarYearVsPartialDateVsAnnualGrowthPercentage) throws Exception {

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

		String jsonString = JsonUtil.convertObjectToJson(calendarYearVsPartialDateVsAnnualGrowthPercentage);

		bufferedWriter.write(jsonString);

		bufferedWriter.close();

		log.info("written data to file and closed the writer");
	}

	private String csvHeader(Set<Integer> calendarYearSet) {

		StringBuilder headerLine = new StringBuilder();

		headerLine.append("\"");
		headerLine.append("Month (MM)");
		headerLine.append("\"");

		headerLine.append(",");

		headerLine.append("\"");
		headerLine.append("Day (DD)");
		headerLine.append("\"");

		for (Integer calendarYear : calendarYearSet) {
			if (calendarYear == 0) {
				continue;
			}

			headerLine.append(",");

			headerLine.append("\"");
			headerLine.append(calendarYear);
			headerLine.append("\"");
		}

		headerLine.append(",");

		headerLine.append("\"");
		headerLine.append("Average Across Years");
		headerLine.append("\"");

		headerLine.append("\n");

		return headerLine.toString();
	}

	private String csvLine(PartialDate partialDate, Map<Integer, Map<PartialDate, Float>> calendarYearVsPartialDateVsAnnualGrowthPercentage) {

		StringBuilder recordLine = new StringBuilder();

		int mm = partialDate.getMm();
		int dd = partialDate.getDd();

		if (mm < 10) {
			recordLine.append(0);
		}
		recordLine.append(mm);

		recordLine.append(",");

		if (dd < 10) {
			recordLine.append(0);
		}
		recordLine.append(dd);

		Set<Integer> calendarYearSet = calendarYearVsPartialDateVsAnnualGrowthPercentage.keySet();

		for (Integer calendarYear : calendarYearSet) {
			if (calendarYear == 0) {
				continue;
			}

			recordLine.append(",");

			Map<PartialDate, Float> calendarYearData = calendarYearVsPartialDateVsAnnualGrowthPercentage.get(calendarYear);
			Float growthPercentage = calendarYearData.get(partialDate);

			if (null == growthPercentage) {
				recordLine.append(0);
			} else {
				recordLine.append(growthPercentage);
			}
		}

		recordLine.append(",");

		Map<PartialDate, Float> averageData = calendarYearVsPartialDateVsAnnualGrowthPercentage.get(0);
		Float averageGrowthPercentage = averageData.get(partialDate);

		recordLine.append(averageGrowthPercentage);

		recordLine.append("\n");

		calendarYearVsPartialDateVsAnnualGrowthPercentage.put(0, averageData);

		return recordLine.toString();
	}

	private void writeSchemeInvestmentAnalysisToCsvFile(String schemeId, Map<Integer, Map<PartialDate, Float>> calendarYearVsPartialDateVsAnnualGrowthPercentage) throws Exception {

		log.info("write investment csv -> (schemeId) {}", schemeId);

		String filePath = schemeInvestDataFilesPath + schemeId + FileExtension.CSV.getExtension();
		File file = new File(filePath);

		boolean fileExists = file.exists();
		log.info("file metadata -> (filePath) {} (fileExists) {}", filePath, fileExists);

		if (fileExists) {
			boolean fileDeleted = file.delete();
			log.info("got -> (fileDeleted) {}", fileDeleted);
		}

		FileWriter fileWriter = new FileWriter(file);
		BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

		Set<Integer> calendarYearSet = calendarYearVsPartialDateVsAnnualGrowthPercentage.keySet();
		log.info("got -> (calendarYearSet) {}", calendarYearSet);

		String headerLine = csvHeader(calendarYearSet);
		log.info("made -> (headerLine) {}", headerLine);
		bufferedWriter.write(headerLine);

		Set<PartialDate> allPartialDates = new TreeSet<PartialDate>(Comparators.PARTIAL_DATE_ASCENDING);

		calendarYearVsPartialDateVsAnnualGrowthPercentage.values()
				.stream()
				.map(partialDateVsAnnualGrowth -> partialDateVsAnnualGrowth.keySet())
				.forEach(partialDateSetForCalendarYear -> allPartialDates.addAll(partialDateSetForCalendarYear));

		for (PartialDate partialDate : allPartialDates) {
			String recordLine = csvLine(partialDate, calendarYearVsPartialDateVsAnnualGrowthPercentage);
			log.info("made -> (recordLine) {}", recordLine);
			bufferedWriter.write(recordLine);
		}

		bufferedWriter.close();

		log.info("written data to file and closed the writer");
	}

	public Mono<?> generateDailyBasisAnnualGrowthReport() {

		log.info("generate daily basis annual growth report");

		Set<String> schemeIds = schemeCsvServiceAsync.getAllSchemesData().keySet();

		schemeIds.stream()
			.forEach(schemeId -> {
				Map<Integer, Map<PartialDate, Float>> calendarYearVsPartialDateVsAnnualGrowthPercentage = generateDailyBasisAnnualGrowthReport(schemeId);
				try {
					writeSchemeInvestmentAnalysisToJsonFile(schemeId, calendarYearVsPartialDateVsAnnualGrowthPercentage);
				} catch (Exception exception) {
					ExceptionLogUtil.logException(exception, "Failed to write investment data json for : " + schemeId);
				}
				try {
					writeSchemeInvestmentAnalysisToCsvFile(schemeId, calendarYearVsPartialDateVsAnnualGrowthPercentage);
				} catch (Exception exception) {
					ExceptionLogUtil.logException(exception, "Failed to write investment data csv for : " + schemeId);
				}
			});

		return Mono.just(true);
	}

}
