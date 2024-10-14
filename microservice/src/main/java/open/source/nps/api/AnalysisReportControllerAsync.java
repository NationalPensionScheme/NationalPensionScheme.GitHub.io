package open.source.nps.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import open.source.nps.service.asynchronous.AnalysisReportServiceAsync;
import reactor.core.publisher.Mono;

@Log4j2
@Validated
@RestController
@RequestMapping("/generate-analysis-report")
@RequiredArgsConstructor
public class AnalysisReportControllerAsync {

	@Autowired
	private AnalysisReportServiceAsync analysisReportServiceAsync;


	@GetMapping("/yearly-average")
	public Mono<?> generateYearlyAverageReport() {

		log.info("generate yearly average report request");

		return analysisReportServiceAsync.generateYearlyAverageReport();
	}

	@GetMapping("/yearly-average-ranking")
	public Mono<?> generateYearlyAverageRankingReport() {

		log.info("generate yearly average ranking report request");

		return analysisReportServiceAsync.generateYearlyAverageRankingReport();
	}

	@GetMapping("/daily-basis-annual-growth")
	public Mono<?> generateDailyBasisAnnualGrowthReport() {

		log.info("generate daily basis annual growth report request");

		return analysisReportServiceAsync.generateDailyBasisAnnualGrowthReport();
	}

}
