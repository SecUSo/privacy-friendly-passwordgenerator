package org.secuso.privacyfriendlypasswordgenerator;

import org.junit.Test;
import org.secuso.privacyfriendlypasswordgenerator.generator.TemplateFactory;

import static org.junit.Assert.assertEquals;

/**
 * Tests the TemplateFactory
 */

public class TemplateFactoryTest {

    @Test
    public void testCreateTemplateFromParameters(){

        String templateAll = TemplateFactory.createTemplateFromParameters(1,1,1,1,12);
        assertEquals("Template all is not created correctly", "saAnxxxxxxxx" , templateAll);

        String templateNoNumbers = TemplateFactory.createTemplateFromParameters(1,1,1,0,12);
        assertEquals("Template no numbers is not created correctly", "saAxxxxxxxxx" , templateNoNumbers);

        String templateNoLower = TemplateFactory.createTemplateFromParameters(1,0,1,1,12);
        assertEquals("Template no lower is not created correctly", "sAnxxxxxxxxx" , templateNoLower);

        String templateNoUpper = TemplateFactory.createTemplateFromParameters(1,1,0,1,12);
        assertEquals("Template no upper is not created correctly", "sanxxxxxxxxx" , templateNoUpper);

        String templateNoSpecial = TemplateFactory.createTemplateFromParameters(0,1,1,1,12);
        assertEquals("Template no special is not created correctly", "aAnxxxxxxxxx" , templateNoSpecial);
    }


}
