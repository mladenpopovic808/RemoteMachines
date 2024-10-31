package rs.raf.domaci3.integration.cleanerController;


import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.core.options.Constants.GLUE_PROPERTY_NAME;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features/userController")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "rs.raf.domaci3.integration.cleanerController")
public class CleanerControllerTests {
}
