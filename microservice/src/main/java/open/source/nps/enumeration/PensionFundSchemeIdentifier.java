package open.source.nps.enumeration;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum PensionFundSchemeIdentifier {

	SM001001 (PensionFundManagerIdentifier.PFM001, PensionFundScheme.GOVERNMENT_CENTRAL),
	SM001002 (PensionFundManagerIdentifier.PFM001, PensionFundScheme.GOVERNMENT_STATE),
	SM001003 (PensionFundManagerIdentifier.PFM001, PensionFundScheme.TIER_I_SCHEME_E),
	SM001004 (PensionFundManagerIdentifier.PFM001, PensionFundScheme.TIER_I_SCHEME_C),
	SM001005 (PensionFundManagerIdentifier.PFM001, PensionFundScheme.TIER_I_SCHEME_G),
	SM001006 (PensionFundManagerIdentifier.PFM001, PensionFundScheme.TIER_II_SCHEME_E),
	SM001007 (PensionFundManagerIdentifier.PFM001, PensionFundScheme.TIER_II_SCHEME_C),
	SM001008 (PensionFundManagerIdentifier.PFM001, PensionFundScheme.TIER_II_SCHEME_G),
	SM001009 (PensionFundManagerIdentifier.PFM001, PensionFundScheme.LITE_SCHEME_GOVERNMENT_PATTERN_NPS_TRUST_ACCOUNT),
	SM001010 (PensionFundManagerIdentifier.PFM001, PensionFundScheme.CORPORATE_CG),
	SM001011 (PensionFundManagerIdentifier.PFM001, PensionFundScheme.ATAL_PENSION_YOJANA_NPS_TRUST_ACCOUNT),
	SM001012 (PensionFundManagerIdentifier.PFM001, PensionFundScheme.TIER_I_SCHEME_A),
	SM001013 (PensionFundManagerIdentifier.PFM001, PensionFundScheme.TIER_II_SCHEME_A),
	SM001014 (PensionFundManagerIdentifier.PFM001, PensionFundScheme.TIER_II_TAX_SAVER_SCHEME_NPS_TRUST_ACCOUNT),
	SM001015 (PensionFundManagerIdentifier.PFM001, PensionFundScheme.ATAL_PENSION_YOJANA_FUND_SCHEME),
	SM001016 (PensionFundManagerIdentifier.PFM001, PensionFundScheme.TIER_II_COMPOSITE_SCHEME),

	SM002001 (PensionFundManagerIdentifier.PFM002, PensionFundScheme.GOVERNMENT_CENTRAL),
	SM002002 (PensionFundManagerIdentifier.PFM002, PensionFundScheme.GOVERNMENT_STATE),
	SM002003 (PensionFundManagerIdentifier.PFM002, PensionFundScheme.TIER_I_SCHEME_E),
	SM002004 (PensionFundManagerIdentifier.PFM002, PensionFundScheme.TIER_I_SCHEME_C),
	SM002005 (PensionFundManagerIdentifier.PFM002, PensionFundScheme.TIER_I_SCHEME_G),
	SM002006 (PensionFundManagerIdentifier.PFM002, PensionFundScheme.TIER_II_SCHEME_E),
	SM002007 (PensionFundManagerIdentifier.PFM002, PensionFundScheme.TIER_II_SCHEME_C),
	SM002008 (PensionFundManagerIdentifier.PFM002, PensionFundScheme.TIER_II_SCHEME_G),
	SM002009 (PensionFundManagerIdentifier.PFM002, PensionFundScheme.LITE_SCHEME_GOVERNMENT_PATTERN_NPS_TRUST_ACCOUNT),
	SM002010 (PensionFundManagerIdentifier.PFM002, PensionFundScheme.CORPORATE_CG),
	SM002011 (PensionFundManagerIdentifier.PFM002, PensionFundScheme.ATAL_PENSION_YOJANA_NPS_TRUST_ACCOUNT),
	SM002012 (PensionFundManagerIdentifier.PFM002, PensionFundScheme.TIER_I_SCHEME_A),
	SM002013 (PensionFundManagerIdentifier.PFM002, PensionFundScheme.TIER_II_SCHEME_A),
	SM002014 (PensionFundManagerIdentifier.PFM002, PensionFundScheme.TIER_II_TAX_SAVER_SCHEME_NPS_TRUST_ACCOUNT),
	SM002015 (PensionFundManagerIdentifier.PFM002, PensionFundScheme.ATAL_PENSION_YOJANA_FUND_SCHEME),
	SM002016 (PensionFundManagerIdentifier.PFM002, PensionFundScheme.TIER_II_COMPOSITE_SCHEME),

	SM003001 (PensionFundManagerIdentifier.PFM003, PensionFundScheme.GOVERNMENT_CENTRAL),
	SM003002 (PensionFundManagerIdentifier.PFM003, PensionFundScheme.GOVERNMENT_STATE),
	SM003003 (PensionFundManagerIdentifier.PFM003, PensionFundScheme.LITE_SCHEME_GOVERNMENT_PATTERN_NPS_TRUST_ACCOUNT),
	SM003004 (PensionFundManagerIdentifier.PFM003, PensionFundScheme.CORPORATE_CG),
	SM003005 (PensionFundManagerIdentifier.PFM003, PensionFundScheme.TIER_I_SCHEME_E),
	SM003006 (PensionFundManagerIdentifier.PFM003, PensionFundScheme.TIER_I_SCHEME_C),
	SM003007 (PensionFundManagerIdentifier.PFM003, PensionFundScheme.TIER_I_SCHEME_G),
	SM003008 (PensionFundManagerIdentifier.PFM003, PensionFundScheme.TIER_II_SCHEME_E),
	SM003009 (PensionFundManagerIdentifier.PFM003, PensionFundScheme.TIER_II_SCHEME_C),
	SM003010 (PensionFundManagerIdentifier.PFM003, PensionFundScheme.TIER_II_SCHEME_G),
	SM003011 (PensionFundManagerIdentifier.PFM003, PensionFundScheme.ATAL_PENSION_YOJANA_NPS_TRUST_ACCOUNT),
	SM003012 (PensionFundManagerIdentifier.PFM003, PensionFundScheme.TIER_I_SCHEME_A),
	SM003013 (PensionFundManagerIdentifier.PFM003, PensionFundScheme.TIER_II_SCHEME_A),
	SM003014 (PensionFundManagerIdentifier.PFM003, PensionFundScheme.TIER_II_TAX_SAVER_SCHEME_NPS_TRUST_ACCOUNT),
	SM003015 (PensionFundManagerIdentifier.PFM003, PensionFundScheme.ATAL_PENSION_YOJANA_FUND_SCHEME),
	SM003016 (PensionFundManagerIdentifier.PFM003, PensionFundScheme.TIER_II_COMPOSITE_SCHEME),

	SM004001 (PensionFundManagerIdentifier.PFM004, PensionFundScheme.TIER_I_SCHEME_E),
	SM004002 (PensionFundManagerIdentifier.PFM004, PensionFundScheme.TIER_I_SCHEME_C),
	SM004003 (PensionFundManagerIdentifier.PFM004, PensionFundScheme.TIER_I_SCHEME_G),
	SM004004 (PensionFundManagerIdentifier.PFM004, PensionFundScheme.TIER_II_SCHEME_E),
	SM004005 (PensionFundManagerIdentifier.PFM004, PensionFundScheme.TIER_II_SCHEME_C),
	SM004006 (PensionFundManagerIdentifier.PFM004, PensionFundScheme.TIER_II_SCHEME_G),
	SM004007 (PensionFundManagerIdentifier.PFM004, PensionFundScheme.LITE_SCHEME_GOVERNMENT_PATTERN_NPS_TRUST_ACCOUNT),

	SM005001 (PensionFundManagerIdentifier.PFM005, PensionFundScheme.TIER_I_SCHEME_E),
	SM005002 (PensionFundManagerIdentifier.PFM005, PensionFundScheme.TIER_I_SCHEME_C),
	SM005003 (PensionFundManagerIdentifier.PFM005, PensionFundScheme.TIER_I_SCHEME_G),
	SM005004 (PensionFundManagerIdentifier.PFM005, PensionFundScheme.TIER_II_SCHEME_E),
	SM005005 (PensionFundManagerIdentifier.PFM005, PensionFundScheme.TIER_II_SCHEME_C),
	SM005006 (PensionFundManagerIdentifier.PFM005, PensionFundScheme.TIER_II_SCHEME_G),
	SM005007 (PensionFundManagerIdentifier.PFM005, PensionFundScheme.LITE_SCHEME_GOVERNMENT_PATTERN_NPS_TRUST_ACCOUNT),
	SM005008 (PensionFundManagerIdentifier.PFM005, PensionFundScheme.TIER_I_SCHEME_A),
	SM005009 (PensionFundManagerIdentifier.PFM005, PensionFundScheme.TIER_II_SCHEME_A),
	SM005010 (PensionFundManagerIdentifier.PFM005, PensionFundScheme.TIER_II_TAX_SAVER_SCHEME_NPS_TRUST_ACCOUNT),

	SM006001 (PensionFundManagerIdentifier.PFM006, PensionFundScheme.TIER_I_SCHEME_E),
	SM006002 (PensionFundManagerIdentifier.PFM006, PensionFundScheme.TIER_I_SCHEME_C),
	SM006003 (PensionFundManagerIdentifier.PFM006, PensionFundScheme.TIER_I_SCHEME_G),
	SM006004 (PensionFundManagerIdentifier.PFM006, PensionFundScheme.TIER_II_SCHEME_E),
	SM006005 (PensionFundManagerIdentifier.PFM006, PensionFundScheme.TIER_II_SCHEME_C),
	SM006006 (PensionFundManagerIdentifier.PFM006, PensionFundScheme.TIER_II_SCHEME_G),
	SM006007 (PensionFundManagerIdentifier.PFM006, PensionFundScheme.LITE_SCHEME_GOVERNMENT_PATTERN_NPS_TRUST_ACCOUNT),
	SM006008 (PensionFundManagerIdentifier.PFM006, PensionFundScheme.TIER_I_SCHEME_A),
	SM006009 (PensionFundManagerIdentifier.PFM006, PensionFundScheme.TIER_II_SCHEME_A),

	SM007001 (PensionFundManagerIdentifier.PFM007, PensionFundScheme.TIER_I_SCHEME_E),
	SM007002 (PensionFundManagerIdentifier.PFM007, PensionFundScheme.TIER_I_SCHEME_C),
	SM007003 (PensionFundManagerIdentifier.PFM007, PensionFundScheme.TIER_I_SCHEME_G),
	SM007004 (PensionFundManagerIdentifier.PFM007, PensionFundScheme.TIER_II_SCHEME_E),
	SM007005 (PensionFundManagerIdentifier.PFM007, PensionFundScheme.TIER_II_SCHEME_C),
	SM007006 (PensionFundManagerIdentifier.PFM007, PensionFundScheme.TIER_II_SCHEME_G),
	SM007007 (PensionFundManagerIdentifier.PFM007, PensionFundScheme.LITE_SCHEME_GOVERNMENT_PATTERN_NPS_TRUST_ACCOUNT),
	SM007008 (PensionFundManagerIdentifier.PFM007, PensionFundScheme.TIER_I_SCHEME_A),
	SM007009 (PensionFundManagerIdentifier.PFM007, PensionFundScheme.TIER_II_SCHEME_A),
	SM007010 (PensionFundManagerIdentifier.PFM007, PensionFundScheme.TIER_II_TAX_SAVER_SCHEME),

	SM008001 (PensionFundManagerIdentifier.PFM008, PensionFundScheme.TIER_I_SCHEME_E),
	SM008002 (PensionFundManagerIdentifier.PFM008, PensionFundScheme.TIER_I_SCHEME_C),
	SM008003 (PensionFundManagerIdentifier.PFM008, PensionFundScheme.TIER_I_SCHEME_G),
	SM008004 (PensionFundManagerIdentifier.PFM008, PensionFundScheme.TIER_II_SCHEME_E),
	SM008005 (PensionFundManagerIdentifier.PFM008, PensionFundScheme.TIER_II_SCHEME_C),
	SM008006 (PensionFundManagerIdentifier.PFM008, PensionFundScheme.TIER_II_SCHEME_G),
	SM008007 (PensionFundManagerIdentifier.PFM008, PensionFundScheme.LITE_SCHEME_GOVERNMENT_PATTERN_NPS_TRUST_ACCOUNT),
	SM008008 (PensionFundManagerIdentifier.PFM008, PensionFundScheme.TIER_I_SCHEME_A),
	SM008009 (PensionFundManagerIdentifier.PFM008, PensionFundScheme.TIER_II_SCHEME_A),
	SM008010 (PensionFundManagerIdentifier.PFM008, PensionFundScheme.TIER_II_TAX_SAVER_SCHEME_NPS_TRUST_ACCOUNT),

	SM009001 (PensionFundManagerIdentifier.PFM009, PensionFundScheme.TIER_I_SCHEME_E),
	SM009002 (PensionFundManagerIdentifier.PFM009, PensionFundScheme.TIER_I_SCHEME_C),
	SM009003 (PensionFundManagerIdentifier.PFM009, PensionFundScheme.TIER_I_SCHEME_G),
	SM009004 (PensionFundManagerIdentifier.PFM009, PensionFundScheme.TIER_II_SCHEME_E),
	SM009005 (PensionFundManagerIdentifier.PFM009, PensionFundScheme.TIER_II_SCHEME_C),
	SM009006 (PensionFundManagerIdentifier.PFM009, PensionFundScheme.TIER_II_SCHEME_G),
	SM009007 (PensionFundManagerIdentifier.PFM009, PensionFundScheme.LITE_SCHEME_GOVERNMENT_PATTERN_NPS_TRUST_ACCOUNT),

	SM010001 (PensionFundManagerIdentifier.PFM010, PensionFundScheme.TIER_I_SCHEME_E),
	SM010002 (PensionFundManagerIdentifier.PFM010, PensionFundScheme.TIER_I_SCHEME_C),
	SM010003 (PensionFundManagerIdentifier.PFM010, PensionFundScheme.TIER_I_SCHEME_G),
	SM010004 (PensionFundManagerIdentifier.PFM010, PensionFundScheme.TIER_I_SCHEME_A),
	SM010005 (PensionFundManagerIdentifier.PFM010, PensionFundScheme.TIER_II_SCHEME_E),
	SM010006 (PensionFundManagerIdentifier.PFM010, PensionFundScheme.TIER_II_SCHEME_C),
	SM010007 (PensionFundManagerIdentifier.PFM010, PensionFundScheme.TIER_II_SCHEME_G),
	SM010008 (PensionFundManagerIdentifier.PFM010, PensionFundScheme.TIER_II_SCHEME_A),
	SM010009 (PensionFundManagerIdentifier.PFM010, PensionFundScheme.TIER_II_TAX_SAVER_SCHEME),

	SM011001 (PensionFundManagerIdentifier.PFM011, PensionFundScheme.TIER_I_SCHEME_E),
	SM011002 (PensionFundManagerIdentifier.PFM011, PensionFundScheme.TIER_I_SCHEME_C),
	SM011003 (PensionFundManagerIdentifier.PFM011, PensionFundScheme.TIER_I_SCHEME_G),
	SM011004 (PensionFundManagerIdentifier.PFM011, PensionFundScheme.TIER_I_SCHEME_A),
	SM011005 (PensionFundManagerIdentifier.PFM011, PensionFundScheme.TIER_II_SCHEME_E),
	SM011006 (PensionFundManagerIdentifier.PFM011, PensionFundScheme.TIER_II_SCHEME_C),
	SM011007 (PensionFundManagerIdentifier.PFM011, PensionFundScheme.TIER_II_SCHEME_G),
	SM011008 (PensionFundManagerIdentifier.PFM011, PensionFundScheme.TIER_II_TAX_SAVER_SCHEME),

	SM012001 (PensionFundManagerIdentifier.PFM012, PensionFundScheme.TIER_I_SCHEME_E),
	SM012002 (PensionFundManagerIdentifier.PFM012, PensionFundScheme.TIER_I_SCHEME_C),
	SM012003 (PensionFundManagerIdentifier.PFM012, PensionFundScheme.TIER_I_SCHEME_G),
	SM012004 (PensionFundManagerIdentifier.PFM012, PensionFundScheme.TIER_I_SCHEME_A),
	SM012005 (PensionFundManagerIdentifier.PFM012, PensionFundScheme.TIER_II_SCHEME_E),
	SM012006 (PensionFundManagerIdentifier.PFM012, PensionFundScheme.TIER_II_SCHEME_C),
	SM012007 (PensionFundManagerIdentifier.PFM012, PensionFundScheme.TIER_II_SCHEME_G),
	SM012008 (PensionFundManagerIdentifier.PFM012, PensionFundScheme.TIER_II_TAX_SAVER_SCHEME),

	SM013001 (PensionFundManagerIdentifier.PFM013, PensionFundScheme.TIER_I_SCHEME_E),
	SM013002 (PensionFundManagerIdentifier.PFM013, PensionFundScheme.TIER_I_SCHEME_C),
	SM013003 (PensionFundManagerIdentifier.PFM013, PensionFundScheme.TIER_I_SCHEME_G),
	SM013004 (PensionFundManagerIdentifier.PFM013, PensionFundScheme.TIER_I_SCHEME_A),
	SM013005 (PensionFundManagerIdentifier.PFM013, PensionFundScheme.TIER_II_SCHEME_E),
	SM013006 (PensionFundManagerIdentifier.PFM013, PensionFundScheme.TIER_II_SCHEME_C),
	SM013007 (PensionFundManagerIdentifier.PFM013, PensionFundScheme.TIER_II_SCHEME_G),
	SM013008 (PensionFundManagerIdentifier.PFM013, PensionFundScheme.TIER_II_TAX_SAVER_SCHEME),

	SM014001 (PensionFundManagerIdentifier.PFM014, PensionFundScheme.TIER_I_SCHEME_E),
	SM014002 (PensionFundManagerIdentifier.PFM014, PensionFundScheme.TIER_I_SCHEME_C),
	SM014003 (PensionFundManagerIdentifier.PFM014, PensionFundScheme.TIER_I_SCHEME_G),
	SM014004 (PensionFundManagerIdentifier.PFM014, PensionFundScheme.TIER_I_SCHEME_A),
	SM014005 (PensionFundManagerIdentifier.PFM014, PensionFundScheme.TIER_II_SCHEME_E),
	SM014006 (PensionFundManagerIdentifier.PFM014, PensionFundScheme.TIER_II_SCHEME_C),
	SM014007 (PensionFundManagerIdentifier.PFM014, PensionFundScheme.TIER_II_SCHEME_G),
	SM014008 (PensionFundManagerIdentifier.PFM014, PensionFundScheme.TIER_II_TAX_SAVER_SCHEME),

	;

	private PensionFundManagerIdentifier managerId;
	private PensionFundScheme scheme;

	private PensionFundSchemeIdentifier(PensionFundManagerIdentifier managerId, PensionFundScheme scheme) {

		this.managerId = managerId;
		this.scheme = scheme;
	}

	@JsonProperty
	public String getId() {

		return super.name();
	}

	@JsonProperty
	public PensionFundManagerIdentifier getManagerId() {

		return this.managerId;
	}

	@JsonProperty
	public PensionFundScheme getScheme() {

		return this.scheme;
	}

}
