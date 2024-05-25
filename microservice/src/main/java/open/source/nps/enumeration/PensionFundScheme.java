package open.source.nps.enumeration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum PensionFundScheme {

	ATAL_PENSION_YOJANA_NPS_TRUST_ACCOUNT ("Atal Pension Yojana - NPS Trust A/C", "atal-pension-yojana"),
	CORPORATE_CG ("Corporate CG", "corporate-cg"),
	GOVERNMENT_CENTRAL ("Government - Central", "government-central"),
	GOVERNMENT_STATE ("Government - State", "government-state"),
	LITE_SCHEME_GOVERNMENT_PATTERN_NPS_TRUST_ACCOUNT ("Lite Scheme - Government Pattern - NPS Trust A/C", "lite-scheme"),
	ATAL_PENSION_YOJANA_FUND_SCHEME ("APY - Fund Scheme", "apy-fund-scheme"),
	TIER_I_SCHEME_A ("Tier I - Scheme A", "tier-i-scheme-a"),
	TIER_I_SCHEME_C ("Tier I - Scheme C", "tier-i-scheme-c"),
	TIER_I_SCHEME_E ("Tier I - Scheme E", "tier-i-scheme-e"),
	TIER_I_SCHEME_G ("Tier I - Scheme G", "tier-i-scheme-g"),
	TIER_II_SCHEME_A ("Tier II - Scheme A", "tier-ii-scheme-a"),
	TIER_II_SCHEME_C ("Tier II - Scheme C", "tier-ii-scheme-c"),
	TIER_II_SCHEME_E ("Tier II - Scheme E", "tier-ii-scheme-e"),
	TIER_II_SCHEME_G ("Tier II - Scheme G", "tier-ii-scheme-g"),
	TIER_II_TAX_SAVER_SCHEME_NPS_TRUST_ACCOUNT ("Tier II - Tax Saver Scheme - NPS Trust A/C", "tier-ii-tax-saver-scheme"),
	TIER_II_TAX_SAVER_SCHEME ("Tier II - Tax Saver Scheme", "tier-ii-tax-saver-scheme"),
	TIER_II_COMPOSITE_SCHEME ("Tier II - Composite Scheme", "tier-ii-composite-scheme"),
	;

	private String value;

	private String categoryFileName;

	private PensionFundScheme(String value, String categoryFileName) {

		this.value = value;
		this.categoryFileName = categoryFileName;
	}

	@JsonProperty
	public String getName() {

		return super.name();
	}

	@JsonProperty
	public String getValue() {

		return this.value;
	}

	@JsonProperty
	public String getCategoryFileName() {

		return this.categoryFileName;
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
