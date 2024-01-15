package open.source.nps.enumeration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum PensionFundManagerIdentifier {

	PFM001 (PensionFundManager.SBI),
	PFM002 (PensionFundManager.UTI),
	PFM003 (PensionFundManager.LIC),
	PFM004 (PensionFundManager.IDFC),
	PFM005 (PensionFundManager.KOTAK_MAHINDRA),
	PFM006 (PensionFundManager.RELIANCE_CAPITAL),
	PFM007 (PensionFundManager.ICICI_PRUDENTIAL),
	PFM008 (PensionFundManager.HDFC),
	PFM009 (PensionFundManager.DSP_BLACKROCK),
	PFM010 (PensionFundManager.ADITYA_BIRLA_SUN_LIFE),
	PFM011 (PensionFundManager.TATA),
	PFM012 (PensionFundManager.MAX_LIFE),
	PFM013 (PensionFundManager.AXIS),
	PFM014 (PensionFundManager.DSP),
	;

	private PensionFundManager manager;

	private PensionFundManagerIdentifier(PensionFundManager manager) {

		this.manager = manager;
	}

	@JsonProperty
	public String getId() {

		return super.name();
	}

	@JsonProperty
	public PensionFundManager getManager() {

		return this.manager;
	}

	@JsonCreator
	public static PensionFundManagerIdentifier fromJson(@JsonProperty("id") String id) {

		if (null == id) {
			return null;
		}
		for (PensionFundManagerIdentifier pensionFundManagerIdentifier : PensionFundManagerIdentifier.values()) {
			if (pensionFundManagerIdentifier.getId().equals(id)) {
				return pensionFundManagerIdentifier;
			}
		}
		return null;
	}

	@JsonCreator
	public static PensionFundManagerIdentifier fromJson(@JsonProperty("manager") PensionFundManager manager) {

		if (null == manager) {
			return null;
		}
		for (PensionFundManagerIdentifier pensionFundManagerIdentifier : PensionFundManagerIdentifier.values()) {
			if (pensionFundManagerIdentifier.getManager().equals(manager)) {
				return pensionFundManagerIdentifier;
			}
		}
		return null;
	}

}
