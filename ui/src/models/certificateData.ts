/**
 * Certificate Generator
 * Certificate Generator for Testing Purposes of PSD2 Sandbox Environment
 *
 * OpenAPI spec version: 1.0.0
 * Contact: swi@adorsys.de
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


/**
 * Certificate Data
 */
export interface CertificateData {
    aisp?: boolean;
    /**
     * Available in the Public Register of the appropriate National Competent Authority; 
     */
    authorizationNumber: string;
    piisp?: boolean;
    pisp?: boolean;
    /**
     * Registered name of your corporation
     */
    organizationName: string;
    /**
     * Name of the country your corporation is registered
     */
    countryName?: string;
    /**
     * Domain of your corporation
     */
    domainComponent?: string;
    /**
     * Name of the city of your corporation headquarter
     */
    localityName?: string;
    organizationUnit?: string;
    /**
     * Name of the state/province of your corporation headquarter
     */
    stateOrProvinceName?: string;
    /**
     * Number of days the certificate is valid
     */
    validity?: number;
}