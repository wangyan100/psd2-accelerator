package de.adorsys.certificateserver;

public class CertificateException extends RuntimeException {

  public CertificateException() {
  }

  public CertificateException(String message) {
    super(message);
  }

  public CertificateException(String message, Throwable cause) {
    super(message, cause);
  }

  public CertificateException(Throwable cause) {
    super(cause);
  }

  public CertificateException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}
