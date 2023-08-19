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

		log.info("generate yearly report request");

		return analysisReportServiceAsync.generateYearlyAverageReport();
	}

}
