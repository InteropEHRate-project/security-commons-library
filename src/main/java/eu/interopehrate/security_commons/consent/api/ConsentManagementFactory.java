package eu.interopehrate.security_commons.consent.api;


import eu.interopehrate.security_commons.consent.ConsentManagementImpl;

/**
 * Created by smenesid on 13/12/2020.
 */

public class ConsentManagementFactory {
    private ConsentManagementFactory() {}

    /**
     * Factory method for creating an instance of ConsentManagementFactory
     *
     * @return
     */
    public static ConsentManagement create() {
        return new ConsentManagementImpl();
    }

}
