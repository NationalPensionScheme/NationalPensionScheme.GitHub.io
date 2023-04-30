package open.source.nps.enumeration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum PensionFundScheme {

	ATAL_PENSION_YOJANA_NPS_TRUST_ACCOUNT ("Atal Pension Yojana - NPS Trust A/C"),
	CORPORATE_CG ("Corporate CG"),
	GOVERNMENT_CENTRAL ("Government - Central"),
	GOVERNMENT_STATE ("Government - State"),
	LITE_SCHEME_GOVERNMENT_PATTERN_NPS_TRUST_ACCOUNT ("Lite Scheme - Government Pattern - NPS Trust A/C"),
	TIER_I_SCHEME_A ("Tier I - Scheme A"),
	TIER_I_SCHEME_C ("Tier I - Scheme C"),
	TIER_I_SCHEME_E ("Tier I - Scheme E"),
	TIER_I_SCHEME_G ("Tier I - Scheme G"),
	TIER_II_SCHEME_A ("Tier II - Scheme A"),
	TIER_II_SCHEME_C ("Tier II - Scheme C"),
	TIER_II_SCHEME_E ("Tier II - Scheme E"),
	TIER_II_SCHEME_G ("Tier II - Scheme G"),
	TIER_II_TAX_SAVER_SCHEME_NPS_TRUST_ACCOUNT ("Tier II - Tax Saver Scheme - NPS Trust A/C"),
	TIER_II_TAX_SAVER_SCHEME ("Tier II - Tax Saver Scheme"),
	;

	private String value;

	private PensionFundScheme(String value) {

		this.value = value;
	}

	@JsonProperty
	public String getName() {

		return super.name();
	}

	@JsonProperty
	public String getValue() {

		return this.value;
	}

	@JsonCreator
	public static PensionFundScheme fromJson(@JsonProperty("value") String value) {

		for (PensionFundScheme pensionFundScheme : PensionFundScheme.values()) {
			if (pensionFundScheme.getValue().equals(value)) {
				return pensionFundScheme;
			}
		}
		return null;
	}

}
