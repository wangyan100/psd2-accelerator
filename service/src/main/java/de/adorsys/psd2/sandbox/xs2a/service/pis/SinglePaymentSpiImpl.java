package de.adorsys.psd2.sandbox.xs2a.service.pis;

import de.adorsys.psd2.sandbox.xs2a.testdata.TestDataService;
import de.adorsys.psd2.sandbox.xs2a.testdata.domain.Account;
import de.adorsys.psd2.sandbox.xs2a.testdata.domain.TestPsu;
import de.adorsys.psd2.xs2a.core.consent.AspspConsentData;
import de.adorsys.psd2.xs2a.core.pis.TransactionStatus;
import de.adorsys.psd2.xs2a.domain.MessageErrorCode;
import de.adorsys.psd2.xs2a.exception.RestException;
import de.adorsys.psd2.xs2a.spi.domain.SpiContextData;
import de.adorsys.psd2.xs2a.spi.domain.authorisation.SpiScaConfirmation;
import de.adorsys.psd2.xs2a.spi.domain.payment.SpiSinglePayment;
import de.adorsys.psd2.xs2a.spi.domain.payment.response.SpiPaymentExecutionResponse;
import de.adorsys.psd2.xs2a.spi.domain.payment.response.SpiSinglePaymentInitiationResponse;
import de.adorsys.psd2.xs2a.spi.domain.response.SpiResponse;
import de.adorsys.psd2.xs2a.spi.service.SinglePaymentSpi;
import java.util.Optional;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service
public class SinglePaymentSpiImpl extends AbstractPaymentSpiImpl implements SinglePaymentSpi {

  private TestDataService testDataService;

  public SinglePaymentSpiImpl(TestDataService testDataService) {
    this.testDataService = testDataService;
  }

  @Override
  public @NotNull SpiResponse<SpiSinglePaymentInitiationResponse> initiatePayment(
      @NotNull SpiContextData ctx,
      @NotNull SpiSinglePayment payment,
      @NotNull AspspConsentData initialAspspConsentData) {

    String debtorIban = payment.getDebtorAccount().getIban();
    Optional<TestPsu> psuId = testDataService.getPsuByIban(debtorIban);
    if (!psuId.isPresent()) {
      // TODO what should we do here? (e.g. no account with this IBAN exists)
      throw new RestException(MessageErrorCode.PAYMENT_FAILED);
    }

    if (testDataService.isBlockedPsu(psuId.get().getPsuId())) {
      throw new RestException(MessageErrorCode.SERVICE_BLOCKED);
    }

    Optional<Account> account = testDataService.getAccountByIban(psuId.get().getPsuId(),
        payment.getDebtorAccount().getIban());
    super.isCorrectCurrency(account, payment);

    SpiSinglePaymentInitiationResponse response = new SpiSinglePaymentInitiationResponse();
    String paymentId = UUID.randomUUID().toString();
    response.setPaymentId(paymentId);
    response.setTransactionStatus(TransactionStatus.RCVD);

    return new SpiResponse<>(response, initialAspspConsentData);
  }

  @Override
  public @NotNull SpiResponse<SpiSinglePayment> getPaymentById(
      @NotNull SpiContextData ctx,
      @NotNull SpiSinglePayment payment,
      @NotNull AspspConsentData aspspConsentData) {

    return super.getPaymentById(ctx.getPsuData(), payment, aspspConsentData);
  }

  @Override
  public @NotNull SpiResponse<TransactionStatus> getPaymentStatusById(
      @NotNull SpiContextData contextData,
      @NotNull SpiSinglePayment payment,
      @NotNull AspspConsentData aspspConsentData) {
    return super.getPaymentStatusById(payment, aspspConsentData);
  }

  @Override
  public @NotNull SpiResponse<SpiPaymentExecutionResponse> executePaymentWithoutSca(
      @NotNull SpiContextData spiContextData,
      @NotNull SpiSinglePayment spiSinglePayment,
      @NotNull AspspConsentData aspspConsentData) {

    return SpiResponse.<SpiPaymentExecutionResponse>builder()
        .aspspConsentData(aspspConsentData)
        .payload(new SpiPaymentExecutionResponse(TransactionStatus.ACTC))
        .success();
  }

  @Override
  public @NotNull SpiResponse<SpiPaymentExecutionResponse> verifyScaAuthorisationAndExecutePayment(
      @NotNull SpiContextData spiContextData,
      @NotNull SpiScaConfirmation spiScaConfirmation,
      @NotNull SpiSinglePayment spiSinglePayment,
      @NotNull AspspConsentData aspspConsentData) {
    return super.checkTanAndSetStatusOfPayment(
        spiSinglePayment,
        spiScaConfirmation,
        aspspConsentData
    );
  }
}
