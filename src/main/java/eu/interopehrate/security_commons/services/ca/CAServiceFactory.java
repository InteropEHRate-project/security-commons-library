package eu.interopehrate.security_commons.services.ca;

import eu.interopehrate.security_commons.services.ca.api.CAService;

public final class CAServiceFactory {

    public static CAService create(final String caUrl) {
        return new CAServiceImpl(caUrl);
    }

}
