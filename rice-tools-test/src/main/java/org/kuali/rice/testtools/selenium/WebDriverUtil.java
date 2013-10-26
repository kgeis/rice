/**
 * Copyright 2005-2013 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.rice.testtools.selenium;

import com.thoughtworks.selenium.SeleneseTestBase;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.kuali.rice.testtools.common.Failable;
import org.kuali.rice.testtools.common.JiraAwareFailureUtil;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchFrameException;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * The goal of the WebDriverUtil class is to invert the dependencies on WebDriver from {@see WebDriverLegacyITBase} for reuse
 * without having to extend WebDriverLegacyITBase.
 * </p><p>
 * For compatibility with {@see JiraAwareFailureUtil}, external test framework asserts and fails should not be called from
 * WebDriverUtil, instead use WebDriverUtil asserts or explicit if checks and call a JiraAwareFailureUtil.fail method.
 * </p><p>
 * For the first example see waitFor
 * </p>
 * @see WebDriverLegacyITBase
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class WebDriverUtil {

    public static boolean jGrowlEnabled = false;

    public static boolean jsHighlightEnabled = false;

    /**
     * http://localhost:8080/kr-dev
     */
    public static final String DEFAULT_BASE_URL = "http://localhost:8080/kr-dev";

    /**
     * http://localhost:8080/krad-dev
     */
    public static final String DEFAULT_BASE_URL_KRAD = "http://localhost:8080/krad-dev";

    /**
     * <p>
     * Set to true to not close the browser after the test has run.
     * </p><p>
     * -Dremote.driver.dontTearDown=true
     * </p>
     */
    public static final String DONT_TEAR_DOWN_PROPERTY = "remote.driver.dontTearDown";

    /**
     * <p>
     * Set to true to not close the browser after the test has if the test failed.
     * </p><p>
     * -Dremote.driver.dontTearDownOnFailure=true
     * </p>
     */
    public static final String DONT_TEAR_DOWN_ON_FAILURE_PROPERTY = "remote.driver.dontTearDownOnFailure";

    /**
     * remote.public.driver
     */
    public static final String HUB_DRIVER_PROPERTY = "remote.public.driver";

    /**
     * -Dremote.public.hub=
     */
    public static final String HUB_PROPERTY = "remote.public.hub";

    /**
     * http://localhost:4444/wd/hub
     */
    public static final String HUB_URL_PROPERTY = "http://localhost:4444/wd/hub";

    /**
     * 1000 Milliseconds.
     * TODO parametrize for JVM Arg
     */
    public static int IMPLICIT_WAIT_TIME_LOOP_MS = 1000;

    /**
     * <p>
     * {@see IMPLICIT_WAIT_TIME_SECONDS_DEFAULT} to configure, default 30 seconds.
     * </p><p>
     * In code don't use this variable but call {@see configuredImplicityWait} to get the configured value.
     * </p>
     */
    public static int IMPLICIT_WAIT_TIME_SECONDS_DEFAULT = 30;

    /**
     * false
     * TODO upgrade to config via JVM param.
     */
    public static final boolean JGROWL_ERROR_FAILURE = false;

    /**
     * green (#66FF33)
     */
    public static final String JS_HIGHLIGHT_BACKGROUND = "#66FF33";

    /**
     * green (#66FF33)
     */
    public static final String JS_HIGHLIGHT_BOARDER = "#66FF33";

    /**
     * 400 milliseconds.
     */
    public static final int JS_HIGHLIGHT_MS = 400;

    /**
     * <p>
     * {@see JS_HIGHLIGHT_MS} as default.
     * </p><p>
     * -Dremote.driver.highlight.ms=
     * </p>
     */
    public static final String JS_HIGHLIGHT_MS_PROPERTY = "remote.driver.highlight.ms";

    /**
     * <p>
     * Highlighting of elements as selenium runs.
     * </p><p>
     * -Dremote.driver.highlight=true
     * </p>
     */
    public static final String JS_HIGHLIGHT_PROPERTY = "remote.driver.highlight";

    /**
     * TODO: playback for javascript highlighting.
     *
     * -Dremote.driver.highlight.input=
     */
    public static final String JS_HIGHLIGHT_INPUT_PROPERTY = "remote.driver.highlight.input";

    /**
     * <p>
     * Local proxy used for running tests thru jmeter.
     * </p><p>
     * Include host name and port number. Example: localhost:7777
     * </p><p>
     * -Dremote.public.proxy=
     * </p>
     */
    public static final String PROXY_HOST_PROPERTY = "remote.public.proxy";

    /**
     * <p>
     * Skip automatice login if set to anything other than true/
     * </p><p>
     * -Dremote.autologin=false
     * </p>
     */
    public static final String REMOTE_AUTOLOGIN_PROPERTY = "remote.autologin";

    /**
     * <p>
     * Set to true to enable jGrowl test messages.
     * </p><p>
     * When enabled, jGrowl messages will be sent when clicking on buttons and links identified by their text.
     * </p><p>
     * -Dremote.jgrowl.enabled=true
     * </p>
     */
    public static final String REMOTE_JGROWL_ENABLED = "remote.jgrowl.enabled";

    /**
     * Set -Dremote.login.uif=KNS to use old login screen.  Default value = KRAD
     */
    public static final String REMOTE_LOGIN_UIF = "remote.login.uif";

    /**
     * Set -Dremote.public.chrome= or WEBDRIVER_CHROME_DRIVER
     */
    public static final String REMOTE_PUBLIC_CHROME = "remote.public.chrome";

    /**
     * -Dremote.public.url=
     */
    public static final String REMOTE_PUBLIC_URL_PROPERTY = "remote.public.url";

    /**
     * <p>
     * Set -Dremote.public.wait.seconds to override DEFAULT_WAIT_SEC.
     * </p><p>
     * {@see IMPLICIT_WAIT_TIME_SECONDS_DEFAULT}
     * </p>
     */
    public static final String REMOTE_PUBLIC_WAIT_SECONDS_PROPERTY = "remote.public.wait.seconds";

    /**
     * Set -Dremote.public.user= to the username to login as
     */
    public static final String REMOTE_PUBLIC_USER_PROPERTY = "remote.public.user";

    /**
     * You probably don't want to really be using a userpool, set -Dremote.public.userpool= to base url if you must.
     */
    public static final String REMOTE_PUBLIC_USERPOOL_PROPERTY = "remote.public.userpool";

    /**
     * <p>
     * Time to wait for the URL used in setup to load, 120 seconds by default.
     * </p><p>
     * Sometimes this is the first hit on the app and it needs a bit longer than any other.
     * </p><p>
     * TODO parametrize for JVM Arg
     * <p>
     */
    public static final int SETUP_URL_LOAD_WAIT_SECONDS = 120;

    /**
     * Selenium's webdriver.chrome.driver parameter, you can set -Dwebdriver.chrome.driver= or Rice's REMOTE_PUBLIC_CHROME.
     */
    public static final String WEBDRIVER_CHROME_DRIVER = "webdriver.chrome.driver";

    /**
     * Setup the WebDriver test, login, and load the given web page
     *
     * @param username
     * @param url
     * @return driver
     * @throws Exception
     */
    public static WebDriver setUp(String username, String url) throws Exception {
        return setUp(username, url, null, null);
    }

    /**
     * Setup the WebDriver test, login, and load the given web page
     *
     * @param username
     * @param url
     * @param className
     * @param testName
     * @return driver
     * @throws Exception
     */
    public static WebDriver setUp(String username, String url, String className, String testName) throws Exception {
        if ("true".equals(System.getProperty(REMOTE_JGROWL_ENABLED, "false"))) {
            jGrowlEnabled = true;
        }

        if ("true".equals(System.getProperty(JS_HIGHLIGHT_PROPERTY, "false"))) {
            jsHighlightEnabled = true;
            if (System.getProperty(JS_HIGHLIGHT_INPUT_PROPERTY) != null) {
                InputStream in = WebDriverUtil.class.getResourceAsStream(System.getProperty(JS_HIGHLIGHT_INPUT_PROPERTY));
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String line = null;
                List<String> lines = new LinkedList<String>();
                while ((line = reader.readLine()) != null) {
                    lines.add(line);
                }
            }
        }

        WebDriver driver = null;
        if (System.getProperty(SauceLabsWebDriverHelper.REMOTE_DRIVER_SAUCELABS_PROPERTY) == null) {
            driver = getWebDriver();
        } else {
            SauceLabsWebDriverHelper saucelabs = new SauceLabsWebDriverHelper();
            saucelabs.setUp(className, testName);
            driver = saucelabs.getDriver();
        }

        driver.manage().timeouts().implicitlyWait(SETUP_URL_LOAD_WAIT_SECONDS, TimeUnit.SECONDS);

        if (!System.getProperty(SauceLabsWebDriverHelper.SAUCE_BROWSER_PROPERTY,"ff").equals("opera")) {
            driver.manage().window().maximize();
        }

        // TODO Got into the situation where the first url doesn't expect server, but all others do.  Readdress once
        // the NavIT WDIT conversion has been completed.
        if (!url.startsWith("http")) {
            url = getBaseUrlString() + url;
        }

        driver.get(url);
        driver.manage().timeouts().implicitlyWait(configuredImplicityWait(), TimeUnit.SECONDS);
        return driver;
    }

    /**
     *<p>
     * Calls {@see SauceLabsWebDriverHelper#tearDown} if {@see #REMOTE_PUBLIC_USERPOOL_PROPERTY} is enabled, calls a user pool
     * url with the given poolParamTest and poolParamUser.
     *</p>
     *
     * @param passed used by {@see SauceLabsWebDriverHelper#tearDown} to record Saucelabs test status can be null if Saucelabs
     * is not being used
     * @param sessionId used by {@see SauceLabsWebDriverHelper#tearDown} to record Saucelabs sessionId status can be null if Saucelabs
     * is not being used
     * @param poolParamTest can be null unless a user pool is being used
     * @param poolParamUser can be null unless a user pool is being used
     * @throws Exception
     */
    public static void tearDown(boolean passed, String sessionId, String poolParamTest, String poolParamUser) throws Exception {

        if (System.getProperty(SauceLabsWebDriverHelper.REMOTE_DRIVER_SAUCELABS_PROPERTY) != null) {
            SauceLabsWebDriverHelper.tearDown(passed, sessionId);
        }

        if (System.getProperty(REMOTE_PUBLIC_USERPOOL_PROPERTY) != null) {
            ITUtil.getHTML(ITUtil.prettyHttp(System.getProperty(REMOTE_PUBLIC_USERPOOL_PROPERTY) + "?test="
                    + poolParamTest + "&user=" + poolParamUser));
        }
    }

    /**
     * <p>
     * If an alert is present accept it print the alert text to System.out
     * </p>
     *
     * @param driver to accept alert on
     */
    public static void acceptAlertIfPresent(WebDriver driver) {
        if (WebDriverUtil.isAlertPresent(driver)) {
            System.out.println("Alert present " + WebDriverUtil.alertText(driver));
            alertAccept(driver);
        }
    }

    /**
     * <p>
     * Accept the javascript alert (clicking OK).
     * </p>
     *
     * @param driver WebDriver to accept alert on
     */
    public static void alertAccept(WebDriver driver) {
        Alert alert = driver.switchTo().alert();
        alert.accept();
    }

    /**
     * <p>
     * Dismiss the javascript alert (clicking Cancel).
     * </p>
     *
     * @param driver WebDriver to dismiss alert on
     */
    public static void alertDismiss(WebDriver driver) {
        Alert alert = driver.switchTo().alert();
        alert.dismiss();
    }

    /**
     * <p>
     * Return alert text.
     * </p>
     *
     * @param driver to get alert text from
     * @return alert text
     */
    public static String alertText(WebDriver driver) {
        return driver.switchTo().alert().getText();
    }

    /**
     * <p>
     * Fail if the button defined by the buttonText is enabled.
     * </p>
     *
     * @param driver to get the button from
     * @param buttonText to identify the button
     * @param failable to fail on if button identified by buttonText is enabled.
     */
    public static void assertButtonDisabledByText(WebDriver driver, String buttonText, Failable failable) {
        jGrowl(driver, "Assert", false, "Assert " + buttonText + " button is disabled");
        if (findButtonByText(driver, buttonText).isEnabled()) {
            failable.fail(buttonText + " button is not disabled");
        }
    }

    /**
     * <p>
     * Fail if the button defined by the buttonText is disabled.
     * </p>
     *
     * @param driver to get the button from
     * @param buttonText to identify the button
     * @param failable to fail on if button identified by buttonText is disabled.
     */
    public static void assertButtonEnabledByText(WebDriver driver, String buttonText, Failable failable) {
        jGrowl(driver, "Assert", false, "Assert " + buttonText + " button is enabled");
        if (!findButtonByText(driver, buttonText).isEnabled()) {
            failable.fail(buttonText + " button is not enabled");
        }
    }

    /***
     * {@see ITUtil#checkForIncidentReport}
     *
     * @param driver to get page source from
     * @param locator current locator
     * @param message to display and be matched against in the event of a failure
     */
    public static void checkForIncidentReport(WebDriver driver, String locator, Failable failable, String message) {
        ITUtil.checkForIncidentReport(driver.getPageSource(), locator, failable, message);
    }

    /**
     * <p>
     * <a href="http://code.google.com/p/chromedriver/downloads/list">ChromeDriver downloads</a>, {@see #REMOTE_PUBLIC_CHROME},
     * {@see #WEBDRIVER_CHROME_DRIVER}, and {@see #HUB_DRIVER_PROPERTY}
     * </p>
     *
     * @return chromeDriverService
     */
    public static ChromeDriverService chromeDriverCreateCheck() {
        String driverParam = System.getProperty(HUB_DRIVER_PROPERTY);
        // TODO can the saucelabs driver stuff be leveraged here?
        if (driverParam != null && "chrome".equals(driverParam.toLowerCase())) {
            if (System.getProperty(WEBDRIVER_CHROME_DRIVER) == null) {
                if (System.getProperty(REMOTE_PUBLIC_CHROME) != null) {
                    System.setProperty(WEBDRIVER_CHROME_DRIVER, System.getProperty(REMOTE_PUBLIC_CHROME));
                }
            }
            try {
                ChromeDriverService chromeDriverService = new ChromeDriverService.Builder()
                        .usingDriverExecutable(new File(System.getProperty(WEBDRIVER_CHROME_DRIVER)))
                        .usingAnyFreePort()
                        .build();
                return chromeDriverService;
            } catch (Throwable t) {
                throw new RuntimeException("Exception starting chrome driver service, is chromedriver ( http://code.google.com/p/chromedriver/downloads/list ) installed? You can include the path to it using -Dremote.public.chrome", t)   ;
            }
        }
        return null;
    }

    /**
     * <p>
     * Return the configured implicity wait seconds, {@see #REMOTE_PUBLIC_WAIT_SECONDS_PROPERTY} and {@see #IMPLICIT_WAIT_TIME_SECONDS_DEFAULT}.
     * </p>
     *
     * @return seconds for implicity wait
     */
    public static int configuredImplicityWait() {
        return Integer.parseInt(System.getProperty(REMOTE_PUBLIC_WAIT_SECONDS_PROPERTY, IMPLICIT_WAIT_TIME_SECONDS_DEFAULT + ""));
    }

    /**
     * <p>
     * Remove double line spacing.
     * </p>
     *
     * @param contents String to remove double line spacing from
     * @return String with double line spacing removed.
     */
    public static String deLinespace(String contents) {
        while (contents.contains("\n\n")) {
            contents = contents.replaceAll("\n\n", "\n");
        }

        return contents;
    }

    /**
     * <p>
     * If {@see #REMOTE_PUBLIC_USER_PROPERTY} property is set, return its value, else if {@see #REMOTE_PUBLIC_USERPOOL_PROPERTY}
     * is set use it to query the userpool service passing the testParam.
     * </p>
     * *
     * @param testParam to use if using a user pool
     * @return user
     */
    public static String determineUser(String testParam) {
        String user = null;

        if (System.getProperty(REMOTE_PUBLIC_USER_PROPERTY) != null) {
            return System.getProperty(REMOTE_PUBLIC_USER_PROPERTY);
        } else if (System.getProperty(REMOTE_PUBLIC_USERPOOL_PROPERTY) != null) { // deprecated
            String userResponse = ITUtil.getHTML(ITUtil.prettyHttp(System.getProperty(
                    REMOTE_PUBLIC_USERPOOL_PROPERTY) + "?test=" + testParam.trim()));
            return userResponse.substring(userResponse.lastIndexOf(":") + 2, userResponse.lastIndexOf("\""));
        }

        return user;
    }

    /**
     * <p>
     * Setting the JVM arg remote.driver.dontTearDown to y or t leaves the browser window open when the test has completed.
     * <p></p>
     * Valuable when debugging, updating, or creating new tests.  When implementing your own tearDown method rather than an
     * inherited one, it is a common courtesy to include this check and not stop and shutdown the browser window to make it
     * easy debug or update your test.
     * </p>
     *
     * @return true if the dontTearDownProperty is not set.
     */
    public static boolean dontTearDownPropertyNotSet() {
        return System.getProperty(DONT_TEAR_DOWN_PROPERTY) == null ||
                "f".startsWith(System.getProperty(DONT_TEAR_DOWN_PROPERTY).toLowerCase()) ||
                "n".startsWith(System.getProperty(DONT_TEAR_DOWN_PROPERTY).toLowerCase());
    }

    /**
     * Given the boolean parameter and depending on if {@see #DONT_TEAR_DOWN_ON_FAILURE_PROPERTY} is set to something other
     * than n, don't tear down the browser window on a test failure.

     * @param passed
     * @return
     */
    public static boolean dontTearDownOnFailure(boolean passed) {
        if (!"n".equalsIgnoreCase(System.getProperty(DONT_TEAR_DOWN_ON_FAILURE_PROPERTY, "n"))) {
            return passed;
        }
        return true;
    }

    private static String extractIncidentReportInfo(String contents, String linkLocator, String message) {
        String chunk =  contents.substring(contents.indexOf("Incident Feedback"), contents.lastIndexOf("</div>") );
        String docId = chunk.substring(chunk.lastIndexOf("Document Id"), chunk.indexOf("View Id"));
        docId = docId.substring(0, docId.indexOf("</span>"));
        docId = docId.substring(docId.lastIndexOf(">") + 2, docId.length());

        String viewId = chunk.substring(chunk.lastIndexOf("View Id"), chunk.indexOf("Error Message"));
        viewId = viewId.substring(0, viewId.indexOf("</span>"));
        viewId = viewId.substring(viewId.lastIndexOf(">") + 2, viewId.length());

        String stackTrace = chunk.substring(chunk.lastIndexOf("(only in dev mode)"), chunk.length());
        stackTrace = stackTrace.substring(stackTrace.indexOf("<span id=\"") + 3, stackTrace.length());
        stackTrace = stackTrace.substring(stackTrace.indexOf("\">") + 2, stackTrace.indexOf("</span>"));

        return "\nIncident report "+ message+ " navigating to "+ linkLocator+ " : View Id: "+ viewId.trim()+ " Doc Id: "+ docId.trim()+ "\nStackTrace: "+ stackTrace.trim();
    }

    private static String extractIncidentReportKim(String contents, String linkLocator, String message) {
        String chunk =  contents.substring(contents.indexOf("id=\"headerarea\""), contents.lastIndexOf("</div>") );
        String docIdPre = "type=\"hidden\" value=\"";
        String docId = chunk.substring(chunk.indexOf(docIdPre) + docIdPre.length(), chunk.indexOf("\" name=\"documentId\""));

        String stackTrace = chunk.substring(chunk.lastIndexOf("name=\"displayMessage\""), chunk.length());
        String stackTracePre = "value=\"";
        stackTrace = stackTrace.substring(stackTrace.indexOf(stackTracePre) + stackTracePre.length(), stackTrace.indexOf("name=\"stackTrace\"") - 2);

        return "\nIncident report "+ message+ " navigating to "+ linkLocator + " Doc Id: "+ docId.trim()+ "\nStackTrace: "+ stackTrace.trim();
    }

    /**
     * <p>
     * {@see JiraAwareFailureUtil#failOnMatchedJira(String, Failable)}.
     * </p>
     * @param contents to check for a jira aware fail match
     * @param failable to call fail on if a jira aware match is found
     */
    public static void failOnMatchedJira(String contents, Failable failable) {
        JiraAwareFailureUtil.failOnMatchedJira(contents, failable);
    }

    private static void failWithReportInfo(String contents, String linkLocator, String message) {
        final String incidentReportInformation = extractIncidentReportInfo(contents, linkLocator, message);
        SeleneseTestBase.fail(incidentReportInformation); // SeleneseTestBase.fail okay here as JiraAwareFailure has already been called
    }

    private static void failWithReportInfoForKim(String contents, String linkLocator, String message) {
        final String kimIncidentReport = extractIncidentReportKim(contents, linkLocator, message);
        SeleneseTestBase.fail(kimIncidentReport); // SeleneseTestBase.fail okay here as JiraAwareFailure has already been called
    }

    /**
     * <p>
     * Find Button by text.
     * </p>
     *
     * @param driver to find button on
     * @param buttonText text to find button by
     * @return WebElement of button with button text
     */
    public static WebElement findButtonByText(WebDriver driver, String buttonText) {
        return findElement(driver, By.xpath("//button[contains(text(), '" + buttonText + "')]"));
    }

    /**
     * <p>
     * Find and highlight the WebElement using the given WebDriver and By.
     * </p>
     *
     * @param driver driver to find on
     * @param by selector to find
     * @return
     */
    public static WebElement findElement(WebDriver driver, By by) {
        WebElement found = driver.findElement(by);
        WebDriverUtil.highlightElement(driver, found);
        return found;
    }

    /**
     * <p>
     * In order to run as a smoke test the ability to set the baseUrl via the JVM arg remote.public.url is required.
     * </p><p>
     * Trailing slashes are trimmed.  If the remote.public.url does not start with http:// it will be added.
     * </p>
     *
     * @return http://localhost:8080/kr-dev by default else the value of remote.public.url
     */
    public static String getBaseUrlString() {
        String baseUrl = System.getProperty(REMOTE_PUBLIC_URL_PROPERTY);
        if (baseUrl == null) {
            baseUrl = DEFAULT_BASE_URL;
        }
        baseUrl = ITUtil.prettyHttp(baseUrl);
        return baseUrl;
    }

    /**
     * <p>
     * Return the WebElement that has the attribute name with the given value.
     * </p>
     *
     * @param driver to get element from
     * @param attributeName attribute name to find element by
     * @param value for the attribute name to find element by
     * @return WebElement
     */
    public static WebElement getElementByAttributeValue(WebDriver driver, String attributeName, String value){
        return findElement(driver, By.cssSelector("[" + attributeName + "='" + value +"']"));
    }

    /**
     * <p>
     * In order to run as a smoke test under selenium grid the ability to set the hubUrl via the JVM arg remote.public.hub is required.
     * </p><p>
     * Trailing slashes are trimmed.  If the remote.public.hub does not start with http:// it will be added.
     * </p>
     *
     * @return http://localhost:4444/wd/hub by default else the value of remote.public.hub
     */
    public static String getHubUrlString() {
        String hubUrl = System.getProperty(HUB_PROPERTY);
        if (hubUrl == null) {
            hubUrl = HUB_URL_PROPERTY;
        }
        hubUrl = ITUtil.prettyHttp(hubUrl);
        if (!hubUrl.endsWith("/wd/hub")) {
            hubUrl = hubUrl + "/wd/hub";
        }
        return hubUrl;
    }

    /**
     * <p>
     * remote.public.driver set to chrome or firefox (null assumes firefox).
     * </p><p>
     * if remote.public.hub is set a RemoteWebDriver is created (Selenium Grid)
     * if proxy.host is set, the web driver is setup to use a proxy
     * </p>
     *
     * @return WebDriver or null if unable to create
     */
    public static WebDriver getWebDriver() {
        String driverParam = System.getProperty(HUB_DRIVER_PROPERTY);
        String hubParam = System.getProperty(HUB_PROPERTY);
        String proxyParam = System.getProperty(PROXY_HOST_PROPERTY);

        // setup proxy if specified as VM Arg
        DesiredCapabilities capabilities = new DesiredCapabilities();
        WebDriver webDriver = null;
        if (StringUtils.isNotEmpty(proxyParam)) {
            capabilities.setCapability(CapabilityType.PROXY, new Proxy().setHttpProxy(proxyParam));
        }

        if (hubParam == null) {
            if (driverParam == null || "firefox".equalsIgnoreCase(driverParam)) {
                FirefoxProfile profile = new FirefoxProfile();
                profile.setEnableNativeEvents(false);
                capabilities.setCapability(FirefoxDriver.PROFILE, profile);
                return new FirefoxDriver(capabilities);
            } else if ("chrome".equalsIgnoreCase(driverParam)) {
                return new ChromeDriver(capabilities);
            } else if ("safari".equals(driverParam)) {
                System.out.println("SafariDriver probably won't work, if it does please contact Erik M.");
                return new SafariDriver(capabilities);
            }
        } else {
            try {
                if (driverParam == null || "firefox".equalsIgnoreCase(driverParam)) {
                    return new RemoteWebDriver(new URL(getHubUrlString()), DesiredCapabilities.firefox());
                } else if ("chrome".equalsIgnoreCase(driverParam)) {
                    return new RemoteWebDriver(new URL(getHubUrlString()), DesiredCapabilities.chrome());
                }
            } catch (MalformedURLException mue) {
                System.out.println(getHubUrlString() + " " + mue.getMessage());
                mue.printStackTrace();
            }
        }
        return null;
    }

    /**
     * <p>
     * Highlight given WebElement.
     * </p>
     *
     * @param webDriver to execute highlight on
     * @param webElement to highlight
     */
    public static void highlightElement(WebDriver webDriver, WebElement webElement) {
        if (jsHighlightEnabled && webElement != null) {
            try {
                //                System.out.println("highlighting " + webElement.toString() + " on url " + webDriver.getCurrentUrl());
                JavascriptExecutor js = (JavascriptExecutor) webDriver;
                String jsHighlight = "element = arguments[0];\n"
                        + "originalStyle = element.getAttribute('style');\n"
                        + "element.setAttribute('style', originalStyle + \"; background: "
                        + JS_HIGHLIGHT_BACKGROUND + "; border: 2px solid " + JS_HIGHLIGHT_BOARDER + ";\");\n"
                        + "setTimeout(function(){\n"
                        + "    element.setAttribute('style', originalStyle);\n"
                        + "}, " + System.getProperty(JS_HIGHLIGHT_MS_PROPERTY, JS_HIGHLIGHT_MS + "") + ");";
                js.executeScript(jsHighlight, webElement);
            } catch (Throwable t) {
                System.out.println("Throwable during javascript highlight element");
                t.printStackTrace();
            }
        }
    }

    /**
     * <p>
     * Return true if an alert is present, false if not.
     * </p>
     *
     * @param driver to check for presents of alert on
     * @return true if there is an alert present, false if not
     */
    public static boolean isAlertPresent(WebDriver driver) {
        try {
            driver.switchTo().alert();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * <p>
     * Use the KRAD Login Screen or the old KNS Login Screen
     * </p>
     *
     * @return true if Krad login
     */
    public static boolean isKradLogin(){
        // check system property, default to KRAD
        String loginUif = System.getProperty(REMOTE_LOGIN_UIF);
        if (loginUif == null) {
            loginUif = ITUtil.REMOTE_UIF_KRAD;
        }

        return (ITUtil.REMOTE_UIF_KRAD.equalsIgnoreCase(loginUif));
    }

    /**
     * <p>
     * Display jGrowl.
     * </p>
     *
     * @param driver WebDriver to execute jGrowl on
     * @param jGrowlHeader header text for jGrowl
     * @param sticky true to set the jGrowl to sticky, false for not sticky
     * @param message message to display in the jGrowl
     * @param throwable message and stacktrace to included in jGrowl
     */
    public static void jGrowl(WebDriver driver, String jGrowlHeader, boolean sticky, String message, Throwable throwable) {
        if (jGrowlEnabled) { // check if jGrowl is enabled to skip over the stack trace extraction if it is not.
            jGrowl(driver, jGrowlHeader, sticky, message + " " + throwable.getMessage() + "\n" + ExceptionUtils.getStackTrace(throwable));
        }
    }

    /**
     * <p>
     * Display jGrowl.
     * </p>
     *
     * @param driver WebDriver to execute jGrowl on
     * @param jGrowlHeader header text for jGrowl
     * @param sticky true to set the jGrowl to sticky, false for not sticky
     * @param message message to display in the jGrowl
     */
    public static void jGrowl(WebDriver driver, String jGrowlHeader, boolean sticky, String message) {
        if (jGrowlEnabled) {
            try {
                String javascript="jQuery.jGrowl('" + message + "' , {sticky: " + sticky + ", header : '" + jGrowlHeader + "'});";
                ((JavascriptExecutor) driver).executeScript(javascript);
            } catch (Throwable t) {
                jGrowlException(t);
            }
        }
    }

    /**
     * <p>
     * Print jGrowl Excetion to System.out, if {@see #JGROWL_ERROR_FAILURE} is set to true, fail.
     * </p>
     *
     * @param throwable message and stack trace to print and if configured fail with
     */
    public static void jGrowlException(Throwable throwable) {
        String failMessage = throwable.getMessage() + "\n" + ExceptionUtils.getStackTrace(throwable);
        System.out.println("jGrowl failure " + failMessage);
        if (JGROWL_ERROR_FAILURE) {
            SeleneseTestBase.fail(failMessage); // SeleneseTestBase fail okay here as jGrowl failures are not Jira worthy yet
        }
    }

    /**
     * <p>
     * Logs in using the KRAD Login Page, if the JVM arg remote.autologin is set, auto login as admin will not be done.
     * </p>
     *
     * @param driver to login with
     * @param userName to login with
     * @param failable to fail on if there is a login problem
     * @throws InterruptedException
     */
    public static void kradLogin(WebDriver driver, String userName, Failable failable) throws InterruptedException {
            driver.findElement(By.name("login_user")).clear();
            driver.findElement(By.name("login_user")).sendKeys(userName);
            driver.findElement(By.id("Rice-LoginButton")).click();
            Thread.sleep(1000);
            String contents = driver.getPageSource();
            ITUtil.failOnInvalidUserName(userName, contents, failable);
            ITUtil.checkForIncidentReport(driver.getPageSource(), "Krad Login", failable, "Krad Login failure");
    }

    /**
     * <p>
     * Logs into the Rice portal using the KNS Style Login Page.
     * </p>
     *
     * @param driver to login with
     * @param userName to login with
     * @param failable to fail on if there is a login problem
     * @throws InterruptedException
     */
    public static void login(WebDriver driver, String userName, Failable failable) throws InterruptedException {
            driver.findElement(By.name("__login_user")).clear();
            driver.findElement(By.name("__login_user")).sendKeys(userName);
            driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
            Thread.sleep(1000);
            String contents = driver.getPageSource();
            ITUtil.failOnInvalidUserName(userName, contents, failable);
            ITUtil.checkForIncidentReport(driver.getPageSource(), "KNS Login", failable, "KNS Login failure");
    }

    /**
     * <p>
     * Login as KRAD or KNS if {@see #REMOTE_AUTOLOGIN_PROPERTY} is not set to true.
     * </p>
     *
     * @param driver to login with
     * @param user to login with
     * @param failable to fail on if there is a login problem
     * @throws InterruptedException
     */
    public static void loginKradOrKns(WebDriver driver, String user, Failable failable) throws InterruptedException {// login via either KRAD or KNS login page
        if ("true".equalsIgnoreCase(System.getProperty(REMOTE_AUTOLOGIN_PROPERTY, "true"))) {
            if (isKradLogin()){
                WebDriverUtil.kradLogin(driver, user, failable);
            } else {
                WebDriverUtil.login(driver, user, failable);
            }
        }
    }

    private static void processIncidentReport(String contents, String linkLocator, Failable failable, String message) {
        failOnMatchedJira(contents, failable);

        if (contents.indexOf("Incident Feedback") > -1) {
            failWithReportInfo(contents, linkLocator, message);
        }

        if (contents.indexOf("Incident Report") > -1) { // KIM incident report
            failWithReportInfoForKim(contents, linkLocator, message);
        }

        SeleneseTestBase.fail("\nIncident report detected " + message + "\n Unable to parse out details for the contents that triggered exception: " + deLinespace(
                contents)); // SeleneseTestBase.fail okay here as JiraAwareFailure has already been called
    }

    /**
     * <p>
     * Select frame defined by locator without throwing an Exception if it doesn't exist.
     * </p>
     *
     * @param driver to select frame on
     * @param locator to identify frame to select
     */
    public static void selectFrameSafe(WebDriver driver, String locator) {
        try {
            driver.switchTo().frame(locator);
        } catch (NoSuchFrameException nsfe) {
            // don't fail
        }
    }

    /**
     * <p>
     * Return the WebElement that has the attribute name with the given value within the given seconds to wait.
     * </p>
     *
     * @param driver to get element from
     * @param attribute name to find element by
     * @param attributeValue for the attribute name to find element by
     * @param waitSeconds number of seconds to wait
     * @return WebElement
     * @throws InterruptedException
     */
    public static WebElement waitAndGetElementByAttributeValue(WebDriver driver, String attribute, String attributeValue, int waitSeconds) throws InterruptedException {
        // jenkins implies that implicitlyWait is worse than sleep loop for finding elements by 100+ test failures on the old sampleapp
        //        driver.manage().timeouts().implicitlyWait(waitSeconds, TimeUnit.SECONDS);
        //        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);

        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);

        boolean failed = false;

        for (int second = 0;; second++) {
            Thread.sleep(1000);
            if (second >= waitSeconds)
                failed = true;
            try {
                if (failed || (getElementByAttributeValue(driver, attribute, attributeValue) != null)) {
                    break;
                }
            } catch (Exception e) {}
        }

        WebElement element = getElementByAttributeValue(driver, attribute, attributeValue);
        driver.manage().timeouts().implicitlyWait(WebDriverUtil.configuredImplicityWait(), TimeUnit.SECONDS);
        return element;
    }

    /**
     * <p>
     * Wait for the given amount of seconds, for the given by, using the given driver.  The message is displayed if the
     * by cannot be found.  No action is performed on the by, so it is possible that the by found is not visible or enabled.
     * </p>
     *
     * @param driver WebDriver to wait on
     * @param waitSeconds seconds to wait
     * @param by By to wait for
     * @param message to display if by is not found in waitSeconds
     * @throws InterruptedException
     */
    public static WebElement waitFor(WebDriver driver, int waitSeconds, By by, String message) throws InterruptedException {
        // jenkins implies that implicitlyWait is worse than sleep loop for finding elements by 100+ test failures on the old sampleapp
        //        driver.manage().timeouts().implicitlyWait(waitSeconds, TimeUnit.SECONDS);
        //        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);

        driver.manage().timeouts().implicitlyWait(IMPLICIT_WAIT_TIME_LOOP_MS, TimeUnit.MILLISECONDS);

        boolean failed = false;

        for (int second = 0;; second++) {
            Thread.sleep(1000);
            if (second >= waitSeconds)
                failed = true;
            try {
                if (failed || (driver.findElements(by)).size() > 0) {
                    break;
                }
            } catch (Exception e) {}
        }

        WebElement element = driver.findElement(by);  // NOTICE just the find, no action, so by is found, but might not be visible or enabled.
        driver.manage().timeouts().implicitlyWait(configuredImplicityWait(), TimeUnit.SECONDS);
        return element;
    }

    /**
     * <p>
     * Wait for WebElements.
     * </p>
     *
     * @param driver WebDriver to wait on
     * @param by By to wait for
     * @return
     * @throws InterruptedException
     */
    public static List<WebElement> waitFors(WebDriver driver, By by) throws InterruptedException {
        return waitFors(driver, configuredImplicityWait(), by, "");
    }

    /**
     * <p>
     * Wait for WebElements.
     * </p>
     *
     * @param driver WebDriver to wait on
     * @param by By to wait for
     * @param message to display if by is not found in waitSeconds
     * @return List of WebElements found
     * @throws InterruptedException
     */
    public static List<WebElement> waitFors(WebDriver driver, By by, String message) throws InterruptedException {
        return waitFors(driver, configuredImplicityWait(), by, message);
    }

   /**
    * <p>
    * Wait for the given amount of seconds, for the given by, using the given driver.  The message is displayed if the
    * by cannot be found.  No action is performed on the by, so it is possible that the by found is not visible or enabled.
    * </p>
    *
    * @param driver WebDriver to wait on
    * @param waitSeconds seconds to wait
    * @param by By to wait for
    * @param message to display if by is not found in waitSeconds
    * @return List of WebElements found
    * @throws InterruptedException
    */
    public static List<WebElement> waitFors(WebDriver driver, int waitSeconds, By by, String message) throws InterruptedException {
        // jenkins implies that implicitlyWait is worse than sleep loop for finding elements by 100+ test failures on the old sampleapp
        //        driver.manage().timeouts().implicitlyWait(waitSeconds, TimeUnit.SECONDS);
        //        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);

        driver.manage().timeouts().implicitlyWait(IMPLICIT_WAIT_TIME_LOOP_MS, TimeUnit.MILLISECONDS);

        boolean failed = false;

        for (int second = 0;; second++) {
            Thread.sleep(1000);
            if (second >= waitSeconds)
                failed = true;
            try {
                if (failed || (driver.findElements(by)).size() > 0) {
                    break;
                }
            } catch (Exception e) {}
        }

        driver.manage().timeouts().implicitlyWait(configuredImplicityWait(), TimeUnit.SECONDS);
        return driver.findElements(by);  // NOTICE just the find, no action, so by is found, but might not be visible or enabled.
    }
}