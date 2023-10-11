package page;

import com.codeborne.selenide.SelenideElement;
import data.DataHelper;

import java.time.Duration;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class TransferPage {
    private final SelenideElement transferName = $("[data-test-id=action-deposit]");
    private final SelenideElement transHead = $(byText("Пополнение карты"));
    private final SelenideElement amountInput = $("[data-test-id=amount] input");
    private final SelenideElement fromInput = $("[data-test-id=from] input");
    private final SelenideElement transferButton = $("[data-test-id=action-transfer]");
    private final SelenideElement errorMessage = $("[data-test-id=action-transfer]");

    public TransferPage() {
        transHead.shouldBe(visible);
    }

    public void findErrorMessage(String expectedText) {
        errorMessage.shouldBe(exactText(expectedText), Duration.ofSeconds(15)).shouldBe(visible);
    }

    public void makeTransfer(String amountToTransfer, DataHelper.CardInfo cardInfo) {
        amountInput.setValue(amountToTransfer);
        fromInput.setValue(cardInfo.getCardNumber());
        transferButton.click();
    }

    public DashboardPage validTransfer(String amountToTransfer, DataHelper.CardInfo cardInfo) {
        makeTransfer(amountToTransfer, cardInfo);
        return new DashboardPage();
    }

}
