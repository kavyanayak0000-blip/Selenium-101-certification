package SeleniumJavaAssignment;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;

public class BaseClass {

    private String username = "kavyanayak0000";
    private String accesskey = "LT_G37n5mJsTpmbYE70JY4jdLKds4pENYdWH86XnGWHSrxy4nL";
    private String hubURL = "hub.lambdatest.com/wd/hub";

    // Thread-safe WebDriver
    protected static ThreadLocal<RemoteWebDriver> driver = new ThreadLocal<>();

    // Getter
    public static RemoteWebDriver getDriver() {
        return driver.get();
    }

    @SuppressWarnings("deprecation")
	@Parameters({"platformname","browser","browserversion"})
    @BeforeTest
    public void setUp(String platformname,
                      String browser,
                      String browserversion) {

        DesiredCapabilities capabilities = new DesiredCapabilities();

        capabilities.setCapability("platformName", platformname);
        capabilities.setCapability("browserName", browser);
        capabilities.setCapability("browserVersion", browserversion);

        HashMap<String, Object> ltOptions = new HashMap<>();

        ltOptions.put("username", username);
        ltOptions.put("accessKey", accesskey);
        ltOptions.put("visual", true);
        ltOptions.put("video", true);
        ltOptions.put("network", true);
        ltOptions.put("build", "Final Build");
        ltOptions.put("project", "LambdaTest");
        ltOptions.put("name", browser + " Execution");
        ltOptions.put("console", true);
        ltOptions.put("terminal", true);
        ltOptions.put("w3c", true);

        capabilities.setCapability("LT:Options", ltOptions);

        try {

            driver.set(new RemoteWebDriver(
                    new URL("https://" + username + ":" + accesskey + "@" + hubURL),
                    capabilities));
            System.out.println("Session ID: " + getDriver().getSessionId());

        } catch (MalformedURLException e) {

            e.printStackTrace();

        }

    }

    @BeforeMethod
    public void setURI() {

        getDriver().manage().window().maximize();

        getDriver().get("https://www.testmuai.com/selenium-playground/");

    }

    @AfterMethod
    public void takeScreenshot(ITestResult result) {

        if (!result.isSuccess()) {

            TakesScreenshot ts = (TakesScreenshot) getDriver();

            File source = ts.getScreenshotAs(OutputType.FILE);

            File destination = new File(
                    System.getProperty("user.dir")
                            + "/snaps/"
                            + result.getName()
                            + "_"
                            + Thread.currentThread().getId()
                            + ".png");

            try {

                FileHandler.copy(source, destination);

            } catch (IOException e) {

                e.printStackTrace();

            }

        }

    }

    @AfterTest
    public void tearDown() {

        if (getDriver() != null) {

            getDriver().quit();

            driver.remove();

        }

    }

}