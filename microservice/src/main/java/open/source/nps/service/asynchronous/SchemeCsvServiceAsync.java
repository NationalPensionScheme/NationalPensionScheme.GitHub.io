package open.source.nps.service.asynchronous;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import open.source.nps.enumeration.PensionFundSchemeIdentifier;
import open.source.nps.model.DateSegmented;
import open.source.nps.model.MinCsvLineData;
import open.source.nps.utility.Comparators;
import open.source.nps.utility.DateHelper;

@Log4j2
@Service
@RequiredArgsConstructor
public class SchemeCsvServiceAsync {

	@Value("${min.data.files.path}")
	private String minDataFilesPath;

	@Value("${year.start}")
	private Integer yearStart;

	@Value("${year.end}")
	private Integer yearEnd;

	// Scheme Id VS (Parsed Date VS Csv Record)
	private static final Map<String, Map<DateSegmented, MinCsvLineData>> LOADED_SCHEME_DATA = new HashMap<String, Map<DateSegmented, MinCsvLineData>>();


	private void parseLineAndPopulate(String csvLine, Map<DateSegmented, MinCsvLineData> dateVsCsvRecord) {

		String[] parts = csvLine.split(",");

		MinCsvLineData minCsvLineData = MinCsvLineData.builder()
				.date(parts[0])
				.originallyPresent(true)
				.netAssetValue(Float.parseFloat(parts[1]))
				.build();
		// log.info("parsed -> (minCsvLineData) {}", minCsvLineData);

		String[] dateParts = parts[0].split("/");

		DateSegmented dateSegmented = DateSegmented.builder()
			.mm(Integer.parseInt(dateParts[0]))
			.dd(Integer.parseInt(dateParts[1]))
			.yyyy(Integer.parseInt(dateParts[2]))
			.build();
		// log.info("parsed -> (dateSegmented) {}", dateSegmented);

		dateVsCsvRecord.put(dateSegmented, minCsvLineData);
	}

	private void readData(String schemeId, String schemeMinCsvFilePath) {

		log.info("read data -> (schemeId) {} (schemeMinCsvFilePath) {}", schemeId, schemeMinCsvFilePath);

		File schemeMinCsvFile = new File(schemeMinCsvFilePath);

		if (!schemeMinCsvFile.exists()) {

			log.info("min csv file does not exist -> (schemeId) {}", schemeId);
			return ;
		}

		Map<DateSegmented, MinCsvLineData> dateVsCsvRecord = new TreeMap<DateSegmented, MinCsvLineData>(Comparators.DATE_SEGMENTED_ASCENDING);

		try {

			FileReader fileReader = new FileReader(schemeMinCsvFile);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			int lineNumber = 0;
			String line = null;

			while (null != (line = bufferedReader.readLine())) {

				++lineNumber;
				// log.info("got -> (lineNumber) {} (line) {}", lineNumber, line);

				if (1 == lineNumber) {
					continue;
				}
				if (null != line && !line.trim().isEmpty()) {
					parseLineAndPopulate(line, dateVsCsvRecord);
				}
			}

			bufferedReader.close();
			fileReader.close();

		} catch (IOException e) {
			log.error(e);
		}

		LOADED_SCHEME_DATA.put(schemeId, dateVsCsvRecord);
	}

	private void fillMissingData(String schemeId) {

		log.info("fill missing data -> (schemeId) {}", schemeId);

		Map<DateSegmented, MinCsvLineData> dateVsCsvRecord = LOADED_SCHEME_DATA.get(schemeId);

		Float lastKnownNAV = null;

		for (int yyyy = yearStart; yyyy <= yearEnd; yyyy++) {
			for (int mm = 1; mm <= 12; mm++) {
				for (int dd = 1; dd <= DateHelper.getDaysInMonth(mm, yyyy); dd++) {

					DateSegmented dateSegmented = DateSegmented.builder()
						.mm(mm)
						.dd(dd)
						.yyyy(yyyy)
						.build();

					if (dateVsCsvRecord.containsKey(dateSegmented)) {

						MinCsvLineData originalData = dateVsCsvRecord.get(dateSegmented);
						lastKnownNAV = originalData.getNetAssetValue();

						continue;

					} else if (null != lastKnownNAV) {

						MinCsvLineData pseudoData = MinCsvLineData.builder()
								.date(DateHelper.getStringMMDDYYYY(mm, dd, yyyy))
								.originallyPresent(false)
								.netAssetValue(lastKnownNAV)
								.build();
						// log.info("built -> (pseudoData) {}", pseudoData);

						dateVsCsvRecord.put(dateSegmented, pseudoData);

					}

				}
			}
		}

		LOADED_SCHEME_DATA.put(schemeId, dateVsCsvRecord);
	}

	public void loadMinDataFiles() {

		log.info("load min data files");

		for (PensionFundSchemeIdentifier pensionFundSchemeIdentifier : PensionFundSchemeIdentifier.values()) {

			log.info("iteration -> (pensionFundSchemeIdentifier) {}", pensionFundSchemeIdentifier);

			String schemeId = pensionFundSchemeIdentifier.getId();

			if (null == LOADED_SCHEME_DATA.get(schemeId)) {

				String schemeMinCsvFilePath = minDataFilesPath + schemeId + "-min.csv";

				readData(schemeId, schemeMinCsvFilePath);

				fillMissingData(schemeId);
			}
		}
	}

	public Map<String, Map<DateSegmented, MinCsvLineData>> getAllSchemesData() {

		return LOADED_SCHEME_DATA;
	}

	public Map<DateSegmented, MinCsvLineData> getSchemeData(String schemeId) {

		return LOADED_SCHEME_DATA.get(schemeId);
	}

}
