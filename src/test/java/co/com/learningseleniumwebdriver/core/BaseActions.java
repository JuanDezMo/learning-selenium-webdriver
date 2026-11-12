package co.com.learningseleniumwebdriver.core;

import co.com.learningseleniumwebdriver.config.ConfigReader;
import co.com.learningseleniumwebdriver.driver.DriverManager;
import java.time.Duration;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Acciones básicas sobre la página que usan todas las clases de acciones de las pantallas.
 *
 * Todas esperan de forma explícita a que el elemento esté listo (nunca se usan pausas fijas)
 * y, si el elemento no aparece, fallan con un mensaje que indica el localizador y el tiempo esperado.
 *
 * Las clases de acciones de cada pantalla (por ejemplo LoginActions) extienden esta clase.
 */
public abstract class BaseActions {

    protected final WebDriver driver;
    private final int timeoutSeconds;
    private final WebDriverWait wait;

    /** Usa el navegador del hilo actual y el tiempo de espera de la configuración (timeout.seconds). */
    public BaseActions() {
        this(DriverManager.getDriver(), ConfigReader.load().getInt("timeout.seconds", 10));
    }

    public BaseActions(WebDriver driver, int timeoutSeconds) {
        this.driver = driver;
        this.timeoutSeconds = timeoutSeconds;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
    }

    /** Abre una URL. */
    public void open(String url) {
        driver.get(url);
    }

    /** Espera a que el elemento sea visible y lo devuelve. */
    protected WebElement waitUntilVisible(By locator) {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (TimeoutException e) {
            throw new TimeoutException("El elemento " + locator + " no fue visible después de "
                    + timeoutSeconds + " segundos", e);
        }
    }

    /** Espera a que el elemento sea clickeable y hace click. */
    protected void click(By locator) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
        } catch (TimeoutException e) {
            throw new TimeoutException("El elemento " + locator + " no fue clickeable después de "
                    + timeoutSeconds + " segundos", e);
        }
    }

    /** Espera a que el campo sea visible, lo limpia y escribe el texto. */
    protected void type(By locator, String text) {
        WebElement element = waitUntilVisible(locator);
        element.clear();
        element.sendKeys(text);
    }

    /** Espera a que el elemento sea visible y devuelve su texto. */
    protected String getText(By locator) {
        return waitUntilVisible(locator).getText();
    }

    /** Espera a que haya al menos un elemento visible y devuelve el texto de todos los que coinciden. */
    protected List<String> getTexts(By locator) {
        waitUntilVisible(locator);
        return driver.findElements(locator).stream().map(WebElement::getText).toList();
    }

    /** Espera a que la lista desplegable sea visible y elige la opción con ese texto. */
    protected void selectByVisibleText(By locator, String optionText) {
        new Select(waitUntilVisible(locator)).selectByVisibleText(optionText);
    }

    /** Espera hasta el tiempo máximo a que el elemento deje de estar visible; devuelve false si sigue visible. */
    protected boolean waitUntilGone(By locator) {
        try {
            return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
        } catch (TimeoutException e) {
            return false;
        }
    }

    /** Indica si el elemento está visible en este momento, sin esperar. */
    protected boolean isVisible(By locator) {
        return driver.findElements(locator).stream().anyMatch(WebElement::isDisplayed);
    }
}
