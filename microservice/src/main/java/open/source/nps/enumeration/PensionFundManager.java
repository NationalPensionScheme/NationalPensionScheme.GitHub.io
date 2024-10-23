package open.source.nps.enumeration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum PensionFundManager {

	SBI ("SBI", "SBI Pension Funds Private Limited"),
	UTI ("UTI", "UTI Pension Fund Limited"),
	LIC ("LIC", "LIC Pension Fund Limited"),
	IDFC ("IDFC", "IDFC Pension Fund Management Company Limited"),
	KOTAK_MAHINDRA ("Kotak Mahindra", "Kotak Mahindra Pension Fund Limited"),
	RELIANCE_CAPITAL ("Reliance Capital", "Reliance Capital Pension Fund Limited"),
	ICICI_PRUDENTIAL ("ICICI Prudential", "ICICI Prudential Pension Funds Management Company Limited"),
	HDFC ("HDFC", "HDFC Pension Management Company Limited"),
	DSP_BLACKROCK ("DSP BlackRock", "DSP BlackRock Pension Fund Managers Private Limited"),
	ADITYA_BIRLA_SUN_LIFE ("Aditya Birla Sun Life", "Aditya Birla Sun Life Pension Management Limited"),
	TATA ("Tata", "Tata Pension Management Private Limited"),
	MAX_LIFE ("Max Life", "Max Life Pension Fund Management Limited"),
	AXIS ("Axis", "Axis Pension Fund Management Limited"),
	DSP ("DSP", "DSP Pension Fund Managers Private Limited"),
	;

	private String value;
	private String companyName;

	private PensionFundManager(String value, String companyName) {

		this.value = value;
		this.companyName = companyName;
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
	public String getCompanyName() {

		return this.companyName;
	}

	@JsonCreator
	public static PensionFundManager fromJson(@JsonProperty("value") String value) {

		for (PensionFundManager pensionFundManager : PensionFundManager.values()) {
			if (pensionFundManager.getValue().equals(value)) {
				return pensionFundManager;
			}
		}
		return null;
	}

}
