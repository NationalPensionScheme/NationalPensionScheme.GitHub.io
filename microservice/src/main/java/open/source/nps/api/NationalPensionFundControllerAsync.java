package open.source.nps.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import open.source.nps.enumeration.PensionFundSchemeIdentifier;
import reactor.core.publisher.Mono;

@Log4j2
@Validated
@RestController
@RequestMapping("/national-pension-fund")
@RequiredArgsConstructor
public class NationalPensionFundControllerAsync {

    @GetMapping("/schemes")
	public Mono<?> getSchemes() {

		log.info("get schemes request");

		return Mono.just(PensionFundSchemeIdentifier.values());
    }

}