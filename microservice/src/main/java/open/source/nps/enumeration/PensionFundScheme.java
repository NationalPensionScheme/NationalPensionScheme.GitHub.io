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
	UPS_CG_SCHEME ("UPS CG Scheme", "ups-cg-scheme"),
	UPS_POOL_CG_SCHEME ("UPS Pool CG Scheme", "ups-pool-cg-scheme"),
	LITE_SCHEME_GOVERNMENT_PATTERN_NPS_TRUST_ACCOUNT ("Lite Scheme - Government Pattern - NPS Trust A/C", "lite-scheme"),
	ATAL_PENSION_YOJANA_FUND_SCHEME ("APY - Fund Scheme", "apy-fund-scheme"),
	TIER_I_SCHEME_A ("Tier I - Scheme A", "tier-i-scheme-a"),
	TIER_I_SCHEME_C ("Tier I - Scheme C", "tier-i-scheme-c"),
	TIER_I_SCHEME_E ("Tier I - Scheme E", "tier-i-scheme-e"),
	TIER_I_SCHEME_G ("Tier I - Scheme G", "tier-i-scheme-g"),
	TIER_I_NPS_JEEVAN_SWARNA_RETIREMENT_YOJANA_LIFES_GOLDEN_PLAN ("Tier I - NPS Jeevan Swarna Retirement Yojana - Life's Golden Plan", "tier-i-nps-jeevan-swarna-retirement-yojana-lifes-golden-plan"),
	TIER_I_NPS_WEALTH_BUILDER_EQUITY_SCHEME ("Tier I - NPS Wealth Builder Equity Scheme", "tier-i-nps-wealth-builder-equity-scheme"),
	TIER_I_NPS_SMART_BALANCE ("Tier I - NPS Smart Balance", "tier-i-nps-smart-balance"),
	TIER_I_NPS_KUBER_EQUITY_FUND ("Tier I - NPS Kuber Equity Fund", "tier-i-nps-kuber-equity-fund"),
	TIER_I_NPS_MY_FAMILY_MY_FUTURE ("Tier I - NPS My Family My Future", "tier-i-nps-my-family-my-future"),
	TIER_I_NPS_SURAKSHIT_INCOME_FUND ("Tier I - NPS Surakshit Income Fund", "tier-i-nps-surakshit-income-fund"),
	TIER_I_NPS_EQUITY_ADVANTAGE_FUND ("Tier I - NPS Equity Advantage Fund", "tier-i-nps-equity-advantage-fund"),
	TIER_I_NPS_SECURE_RETIREMENT_EQUITY_FUND ("Tier I - NPS Secure Retirement Equity Fund", "tier-i-nps-secure-retirement-equity-fund"),
	TIER_I_NPS_SMART_RETIREMENT_FUND ("Tier I - NPS Smart Retirement Fund", "tier-i-nps-smart-retirement-fund"),
	TIER_I_NPS_GOLDEN_YEARS_GROWTH_FUND ("Tier I - NPS Golden Years Growth Fund", "tier-i-nps-golden-years-growth-fund"),
	TIER_I_NPS_LONG_TERM_EQUITY_FUND ("Tier I - NPS Long Term Equity Fund", "tier-i-nps-long-term-equity-fund"),
	TIER_II_SCHEME_A ("Tier II - Scheme A", "tier-ii-scheme-a"),
	TIER_II_SCHEME_C ("Tier II - Scheme C", "tier-ii-scheme-c"),
	TIER_II_SCHEME_E ("Tier II - Scheme E", "tier-ii-scheme-e"),
	TIER_II_SCHEME_G ("Tier II - Scheme G", "tier-ii-scheme-g"),
	//TIER_II_TAX_SAVER_SCHEME_NPS_TRUST_ACCOUNT ("Tier II - Tax Saver Scheme - NPS Trust A/C", "tier-ii-tax-saver-scheme"),
	TIER_II_TAX_SAVER_SCHEME ("Tier II - Tax Saver Scheme", "tier-ii-tax-saver-scheme"),
	TIER_II_COMPOSITE_SCHEME ("Tier II - Composite Scheme", "tier-ii-composite-scheme"),
	TIER_II_NPS_WEALTH_BUILDER_EQUITY_SCHEME ("Tier II - NPS Wealth Builder Equity Scheme", "tier-ii-nps-wealth-builder-equity-scheme"),
	TIER_II_NPS_SURAKSHIT_INCOME_FUND ("Tier II - NPS Surakshit Income Fund", "tier-ii-nps-surakshit-income-fund"),
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
