package de.adorsys.psd2.sandbox.xs2a.service.pis;

import de.adorsys.psd2.consent.domain.payment.PisPaymentData;
import de.adorsys.psd2.consent.repository.PisPaymentDataRepository;
import de.adorsys.psd2.sandbox.xs2a.testdata.TestDataService;
import de.adorsys.psd2.sandbox.xs2a.testdata.domain.Account;
import de.adorsys.psd2.xs2a.core.consent.AspspConsentData;
import de.adorsys.psd2.xs2a.core.pis.TransactionStatus;
import de.adorsys.psd2.xs2a.domain.MessageErrorCode;
import de.adorsys.psd2.xs2a.exception.RestException;
import de.adorsys.psd2.xs2a.spi.domain.authorisation.SpiScaConfirmation;
import de.adorsys.psd2.xs2a.spi.domain.payment.SpiSinglePayment;
import de.adorsys.psd2.xs2a.spi.domain.payment.response.SpiPaymentExecutionResponse;
import de.adorsys.psd2.xs2a.spi.domain.psu.SpiPsuData;
import de.adorsys.psd2.xs2a.spi.domain.response.SpiResponse;
import de.adorsys.psd2.xs2a.spi.domain.response.SpiResponseStatus;
import de.adorsys.psd2.xs2a.spi.service.SpiPayment;
import java.util.Collections;
import java.util.Currency;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

class AbstractPaymentSpiImpl {


  @Autowired
  PisPaymentDataRepository paymentDataRepository;

  SpiResponse<TransactionStatus> getPaymentStatusById(
      SpiSinglePayment payment,
      AspspConsentData aspspConsentData) {

    Optional<TransactionStatus> paymentStatus = getPaymentStatusFromRepo(payment.getPaymentId());
    if (paymentStatus.isPresent()) {
      payment.setPaymentStatus(paymentStatus.get());
      return SpiResponse.<TransactionStatus>builder()
          .aspspConsentData(aspspConsentData)
          .payload(payment.getPaymentStatus())
          .success();
    }
    return SpiResponse.<TransactionStatus>builder()
        .fail(SpiResponseStatus.LOGICAL_FAILURE);
  }

  <T extends SpiSinglePayment> SpiResponse<T> getPaymentById(
      SpiPsuData psuData,
      T payment,
      AspspConsentData aspspConsentData) {
    Optional<TransactionStatus> paymentStatus = getPaymentStatusFromRepo(payment.getPaymentId());
    if (paymentStatus.isPresent()) {
      payment.setPaymentStatus(paymentStatus.get());
      return SpiResponse.<T>builder()
          .aspspConsentData(aspspConsentData)
          .payload(payment)
          .success();
    }
    return SpiResponse.<T>builder()
        .fail(SpiResponseStatus.LOGICAL_FAILURE);
  }

  SpiResponse<SpiPaymentExecutionResponse> checkTanAndSetStatusOfPayment(
      SpiPayment spiPayment,
      SpiScaConfirmation spiScaConfirmation,
      AspspConsentData aspspConsentData) {
    if (spiScaConfirmation.getTanNumber().equals(TestDataService.GLOBAL_TAN)) {
      Optional<List<PisPaymentData>> paymentDataList = paymentDataRepository
          .findByPaymentId(spiPayment.getPaymentId());

      if (paymentDataList.isPresent()) {
        PisPaymentData payment = paymentDataList.get().get(0);
        payment.getPaymentData().setTransactionStatus(TransactionStatus.ACCP);
        paymentDataRepository.save(payment);
        return SpiResponse.<SpiPaymentExecutionResponse>builder()
            .aspspConsentData(aspspConsentData)
            .payload(new SpiPaymentExecutionResponse(TransactionStatus.ACCP))
            .success();
      }

      return SpiResponse.<SpiPaymentExecutionResponse>builder()
          .aspspConsentData(aspspConsentData)
          .message(Collections.singletonList("Payment not found"))
          .fail(SpiResponseStatus.LOGICAL_FAILURE);
    }

    return SpiResponse.<SpiPaymentExecutionResponse>builder()
        .aspspConsentData(aspspConsentData)
        .message(Collections.singletonList("Wrong PIN"))
        .fail(SpiResponseStatus.UNAUTHORIZED_FAILURE);
  }

  void isCorrectCurrency(Optional<Account> account, SpiSinglePayment payment) {
    if (account.isPresent()) {
      Currency expectedCurrency = account.get().getCurrency();
      if (!(payment.getDebtorAccount().getCurrency().equals(expectedCurrency)
          && payment.getInstructedAmount().getCurrency().equals(expectedCurrency)
          && payment.getCreditorAccount().getCurrency().equals(expectedCurrency))) {
        throw new RestException(MessageErrorCode.FORMAT_ERROR);
      }
    }
  }

  private Optional<TransactionStatus> getPaymentStatusFromRepo(String paymentId) {
    Optional<List<PisPaymentData>> paymentData = paymentDataRepository
        .findByPaymentId(paymentId);

    return paymentData.map(pisPaymentData -> TransactionStatus
        .valueOf(pisPaymentData.get(0).getPaymentData().getTransactionStatus().name()));
  }
}
