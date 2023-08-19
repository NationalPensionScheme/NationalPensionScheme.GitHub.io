package open.source.nps.service.asynchronous;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.TurquoiseSpace.utility.ExceptionLogUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import open.source.nps.model.DateSegmented;
import open.source.nps.model.FinancialYear;
import open.source.nps.model.MinCsvLineData;
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

	public Mono<?> generateDailyBasisAnnualGrowthReport() {

		log.info("generate daily basis annual growth report request");

		return Mono.empty();
	}

}
