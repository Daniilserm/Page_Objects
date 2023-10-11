package test;

import data.DataHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import page.DashboardPage;
import page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static data.DataHelper.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoneyTransferTest {

    DashboardPage dashboardPage;

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = LoginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        dashboardPage = verificationPage.validVerify(verificationCode);

    }

    @Test
    void shouldTransferMoneyBetweenOwnCardsFirstToSecond() {
        var firstCardInfo = getFirstCardInfo();
        var secondCardInfo = getSecondCardInfo();
        var firstCardBalance = dashboardPage.getCardBalance(0);
        var secondCardBalance = dashboardPage.getCardBalance(1);
        var amount = generateValidAmount(firstCardBalance);
        var transferPage = dashboardPage.selectCardTransfer(secondCardInfo);
        dashboardPage = transferPage.validTransfer(String.valueOf(amount), firstCardInfo);
        var actualFirstCardBalance = dashboardPage.getCardBalance(0);
        var actualSecondCardBalance = dashboardPage.getCardBalance(1);
        var expectedFirstCardBalance = firstCardBalance - amount;
        var expectedSecondCardBalance = secondCardBalance + amount;
        assertEquals(actualFirstCardBalance, expectedFirstCardBalance);
        assertEquals(actualSecondCardBalance, expectedSecondCardBalance);
    }

    @Test
    void shouldTransferMoneyBetweenOwnCardsSecondToFirst() {
        var firstCardInfo = getFirstCardInfo();
        var secondCardInfo = getSecondCardInfo();
        var firstCardBalance = dashboardPage.getCardBalance(0);
        var secondCardBalance = dashboardPage.getCardBalance(1);
        var amount = generateValidAmount(secondCardBalance);
        var transferPage = dashboardPage.selectCardTransfer(firstCardInfo);
        dashboardPage = transferPage.validTransfer(String.valueOf(amount), secondCardInfo);
        var actualFirstCardBalance = dashboardPage.getCardBalance(0);
        var actualSecondCardBalance = dashboardPage.getCardBalance(1);
        var expectedFirstCardBalance = firstCardBalance + amount;
        var expectedSecondCardBalance = secondCardBalance - amount;
        assertEquals(actualFirstCardBalance, expectedFirstCardBalance);
        assertEquals(actualSecondCardBalance, expectedSecondCardBalance);
    }

    @Test
    void shouldNotTransferAmountExceedingBalanceFirstToSecond() {
        var firstCardInfo = getFirstCardInfo();
        var secondCardInfo = getSecondCardInfo();
        var firstCardBalance = dashboardPage.getCardBalance(0);
        var secondCardBalance = dashboardPage.getCardBalance(1);
        var amount = generateInvalidAmount(firstCardBalance);
        var transferPage = dashboardPage.selectCardTransfer(secondCardInfo);
        transferPage.makeTransfer(String.valueOf(amount), firstCardInfo);
        transferPage.findErrorMessage("Невозможен перевод суммы превыщающей баланс карты");
        var actualFirstCardBalance = dashboardPage.getCardBalance(0);
        var actualSecondCardBalance = dashboardPage.getCardBalance(1);
        assertEquals(actualFirstCardBalance, firstCardBalance);
        assertEquals(actualSecondCardBalance, secondCardBalance);
    }

    @Test
    void shouldNotTransferAmountExceedingBalanceSecondToFirst() {
        var firstCardInfo = getFirstCardInfo();
        var secondCardInfo = getSecondCardInfo();
        var firstCardBalance = dashboardPage.getCardBalance(0);
        var secondCardBalance = dashboardPage.getCardBalance(1);
        var amount = generateInvalidAmount(secondCardBalance);
        var transferPage = dashboardPage.selectCardTransfer(firstCardInfo);
        transferPage.makeTransfer(String.valueOf(amount), secondCardInfo);
        transferPage.findErrorMessage("Невозможен перевод суммы превыщающей баланс карты");
        var actualFirstCardBalance = dashboardPage.getCardBalance(0);
        var actualSecondCardBalance = dashboardPage.getCardBalance(1);
        assertEquals(actualFirstCardBalance, firstCardBalance);
        assertEquals(actualSecondCardBalance, secondCardBalance);
    }

}
