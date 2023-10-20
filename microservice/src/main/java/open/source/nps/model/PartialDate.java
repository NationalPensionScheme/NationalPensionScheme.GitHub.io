package open.source.nps.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PartialDate implements Serializable {

	private int dd;

	private int mm;

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		if (mm < 10) {
			stringBuilder.append("0");
		}
		stringBuilder.append(mm);
		stringBuilder.append("/");
		if (dd < 10) {
			stringBuilder.append("0");
		}
		stringBuilder.append(dd);
		return stringBuilder.toString();
	}

}
