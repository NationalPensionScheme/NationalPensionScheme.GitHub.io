package open.source.nps.service.asynchronous;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.TurquoiseSpace.utility.ExceptionLogUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import open.source.nps.enumeration.PensionFundScheme;
import open.source.nps.enumeration.PensionFundSchemeIdentifier;
import open.source.nps.model.DateSegmented;
import open.source.nps.model.MinCsvLineData;
import open.source.nps.utility.Comparators;
import open.source.nps.utility.DateHelper;

@Log4j2
@Service
@RequiredArgsConstructor
public class CategorizeSchemeServiceAsync {

	@Value("${scheme.category.data.files.path}")
	private String schemeCategoryDataFilesPath;

	@Autowired
	private SchemeCsvServiceAsync schemeCsvServiceAsync;


	private static final Map<String, Set<String>> CATEGORY_VS_SCHEME_IDS = new HashMap<String, Set<String>>();

	public void loadCategories() {

		for (PensionFundSchemeIdentifier pensionFundSchemeIdentifier : PensionFundSchemeIdentifier.values()) {
			String schemeId = pensionFundSchemeIdentifier.getId();
			String categoryFileName = pensionFundSchemeIdentifier.getScheme().getCategoryFileName();

			Set<String> schemeIds = CATEGORY_VS_SCHEME_IDS.get(categoryFileName);
			if (null == schemeIds) {
				schemeIds = new TreeSet<String>();
			}
			schemeIds.add(schemeId);
			CATEGORY_VS_SCHEME_IDS.put(categoryFileName, schemeIds);
		}
	}

	public Map<PensionFundScheme, Set<PensionFundSchemeIdentifier>> getAllSchemeCategoryVsSchemeIds() {

		Map<PensionFundScheme, Set<PensionFundSchemeIdentifier>> schemeCategoryVsSchemeIds = new HashMap<PensionFundScheme, Set<PensionFundSchemeIdentifier>>();

		for (PensionFundSchemeIdentifier pensionFundSchemeIdentifier : PensionFundSchemeIdentifier.values()) {

			PensionFundScheme pensionFundScheme = pensionFundSchemeIdentifier.getScheme();

			Set<PensionFundSchemeIdentifier> pensionFundSchemeIdentifierSet = schemeCategoryVsSchemeIds.get(pensionFundScheme);

			if (null == pensionFundSchemeIdentifierSet) {

				pensionFundSchemeIdentifierSet = new TreeSet<PensionFundSchemeIdentifier>();
			}

			pensionFundSchemeIdentifierSet.add(pensionFundSchemeIdentifier);

			schemeCategoryVsSchemeIds.put(pensionFundScheme, pensionFundSchemeIdentifierSet);
		}

		return schemeCategoryVsSchemeIds;
	}

	private Set<DateSegmented> identifyAllOriginalDatesInCategory(Set<String> schemeIds) {

		Set<DateSegmented> categoryAllDates = new TreeSet<>(Comparators.DATE_SEGMENTED_ASCENDING);

		schemeIds.stream()
				.map(schemeId -> schemeCsvServiceAsync.getSchemeData(schemeId))
				.map(dateVsData -> dateVsData.entrySet())
				.forEach(entrySet -> {
					entrySet.stream()
						.forEach(entry -> {
							DateSegmented dateSegmented = entry.getKey();
							MinCsvLineData minCsvLineData = entry.getValue();
							if (null != minCsvLineData && minCsvLineData.isOriginallyPresent()) {
								categoryAllDates.add(dateSegmented);
							}
						});
				});

		log.info("got -> (categoryAllDates) {}", categoryAllDates);
		return categoryAllDates;
	}

	private File getCategoryFile(String categoryFileName) {

		String absoluteFilePath = schemeCategoryDataFilesPath + categoryFileName + ".csv";
		log.info("constructed -> (absoluteFilePath) {}", absoluteFilePath);

		File categoryFile = new File(absoluteFilePath);
		return categoryFile;
	}

	private void removeCategoryFile(String categoryFileName) {

		File categoryFile = getCategoryFile(categoryFileName);

		if (categoryFile.exists()) {
			log.info("found category file -> {}", categoryFileName);
			boolean deletedFlag = categoryFile.delete();
			if (deletedFlag) {
				log.info("deleted category file -> {}", categoryFileName);
			} else {
				log.error("failed to delete category file -> {}", categoryFileName);
			}
		} else {
			log.error("missing category file -> {}", categoryFileName);
		}
	}

	private void createCategoryFile(String categoryFileName) {

		File categoryFile = getCategoryFile(categoryFileName);

		try {
			categoryFile.createNewFile();
			log.info("succesfully create blank category file -> {}", categoryFileName);
		} catch (Exception exception) {
			ExceptionLogUtil.logException(exception, "failed to create category file -> " + categoryFileName);
		}
	}

	private BufferedWriter getWriter(String categoryFileName) {

		File categoryFile = getCategoryFile(categoryFileName);

		BufferedWriter bufferedWriter = null;

		try {
			FileWriter fileWriter = new FileWriter(categoryFile);
			bufferedWriter = new BufferedWriter(fileWriter);
			log.info("succesfully got the file writer -> {}", categoryFileName);
		} catch (Exception exception) {
			ExceptionLogUtil.logException(exception, "failed to get file writer -> " + categoryFileName);
		}

		return bufferedWriter;
	}

	private void fillHeaderLineInCategoryFile(BufferedWriter bufferedWriter, Set<String> schemeIds, String categoryFileName) {

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Date");

		for (String schemeId : schemeIds) {
			stringBuilder.append(",");
			stringBuilder.append(schemeId);
		}

		stringBuilder.append("\n");

		String headerLine = stringBuilder.toString();
		log.info("made -> (headerLine) {}", headerLine);

		try {
			bufferedWriter.write(headerLine);
		} catch (Exception exception) {
			ExceptionLogUtil.logException(exception, "failed to write the header line -> " + categoryFileName);
		}
	}

	private void fillLineDataInCategoryFile(BufferedWriter bufferedWriter, DateSegmented dateSegmented, Set<String> schemeIds, String categoryFileName) {

		StringBuilder stringBuilder = new StringBuilder();

		String dateString = DateHelper.getStringMMDDYYYY(dateSegmented);
		stringBuilder.append(dateString);

		for (String schemeId : schemeIds) {
			Map<DateSegmented, MinCsvLineData> schemeDateVsData = schemeCsvServiceAsync.getSchemeData(schemeId);
			MinCsvLineData minCsvLineData = schemeDateVsData.get(dateSegmented);
			//log.info("fetched -> (schemeId) {} (dateSegmented) {} (minCsvLineData) {}", schemeId, dateSegmented, minCsvLineData);

			stringBuilder.append(",");
			if (null != minCsvLineData && minCsvLineData.isOriginallyPresent()) {
				stringBuilder.append(minCsvLineData.getNetAssetValue());
			}
		}

		stringBuilder.append("\n");

		String dataLine = stringBuilder.toString();
		//log.info("made -> (dataLine) {}", dataLine);

		try {
			bufferedWriter.write(dataLine);
		} catch (Exception exception) {
			ExceptionLogUtil.logException(exception, "failed to write the data line -> " + categoryFileName);
		}
	}

	private void closeWriter(String categoryFileName, BufferedWriter bufferedWriter) {

		try {
			bufferedWriter.close();
			log.info("succesfully closed the file writer -> {}", categoryFileName);
		} catch (Exception exception) {
			ExceptionLogUtil.logException(exception, "failed to close file writer -> " + categoryFileName);
		}
	}

	public void generateAllSchemeCategoryCsv() {

		if (CATEGORY_VS_SCHEME_IDS.isEmpty()) {
			return;
		}

		for (String categoryFileName : CATEGORY_VS_SCHEME_IDS.keySet()) {

			log.info("processing -> (categoryFileName) {}", categoryFileName);

			Set<String> schemeIds = CATEGORY_VS_SCHEME_IDS.get(categoryFileName);
			log.info("fetched -> (schemeIds) {}", schemeIds);

			Set<DateSegmented> categoryAllDates = identifyAllOriginalDatesInCategory(schemeIds);

			removeCategoryFile(categoryFileName);
			createCategoryFile(categoryFileName);

			BufferedWriter bufferedWriter = getWriter(categoryFileName);

			fillHeaderLineInCategoryFile(bufferedWriter, schemeIds, categoryFileName);
			for (DateSegmented dateSegmented : categoryAllDates) {
				fillLineDataInCategoryFile(bufferedWriter, dateSegmented, schemeIds, categoryFileName);
			}

			closeWriter(categoryFileName, bufferedWriter);
		}
	}

}
