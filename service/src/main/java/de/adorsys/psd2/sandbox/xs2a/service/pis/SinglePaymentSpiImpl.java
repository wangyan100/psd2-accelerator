package de.adorsys.psd2.sandbox.xs2a.service.pis;

import de.adorsys.psd2.xs2a.spi.domain.authorisation.SpiScaConfirmation;
import de.adorsys.psd2.xs2a.spi.domain.common.SpiTransactionStatus;
import de.adorsys.psd2.xs2a.spi.domain.consent.AspspConsentData;
import de.adorsys.psd2.xs2a.spi.domain.payment.SpiSinglePayment;
import de.adorsys.psd2.xs2a.spi.domain.payment.response.SpiSinglePaymentInitiationResponse;
import de.adorsys.psd2.xs2a.spi.domain.psu.SpiPsuData;
import de.adorsys.psd2.xs2a.spi.domain.response.SpiResponse;
import de.adorsys.psd2.xs2a.spi.service.SinglePaymentSpi;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service
public class SinglePaymentSpiImpl implements SinglePaymentSpi {

  @Override
  public @NotNull SpiResponse<SpiSinglePaymentInitiationResponse> initiatePayment(
      @NotNull SpiPsuData psuData,
      @NotNull SpiSinglePayment payment,
      @NotNull AspspConsentData initialAspspConsentData) {

    SpiSinglePaymentInitiationResponse paymentResponse = new SpiSinglePaymentInitiationResponse();
    paymentResponse.setPaymentId("12345");
    paymentResponse.setTransactionStatus(SpiTransactionStatus.RCVD);

    return new SpiResponse<>(paymentResponse, initialAspspConsentData);
  }

  @Override
  public @NotNull SpiResponse<SpiSinglePayment> getPaymentById(
      @NotNull SpiPsuData psuData,
      @NotNull SpiSinglePayment payment,
      @NotNull AspspConsentData aspspConsentData) {
    return null;
  }

  @Override
  public @NotNull SpiResponse<SpiTransactionStatus> getPaymentStatusById(
      @NotNull SpiPsuData psuData,
      @NotNull SpiSinglePayment payment,
      @NotNull AspspConsentData aspspConsentData) {
    return null;
  }

  @Override
  public @NotNull SpiResponse<SpiResponse.VoidResponse> executePaymentWithoutSca(
      @NotNull SpiPsuData spiPsuData,
      @NotNull SpiSinglePayment spiSinglePayment,
      @NotNull AspspConsentData aspspConsentData) {
    return null;
  }

  @Override
  public @NotNull SpiResponse<SpiResponse.VoidResponse> verifyScaAuthorisationAndExecutePayment(
      @NotNull SpiPsuData spiPsuData,
      @NotNull SpiScaConfirmation spiScaConfirmation,
      @NotNull SpiSinglePayment spiSinglePayment,
      @NotNull AspspConsentData aspspConsentData) {
    return null;
  }
}