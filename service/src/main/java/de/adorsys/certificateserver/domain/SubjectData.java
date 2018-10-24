package de.adorsys.certificateserver.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;

import org.bouncycastle.asn1.x500.X500Name;

@Data
@AllArgsConstructor
public class SubjectData {
    private PrivateKey privateKey;
    private PublicKey publicKey;
    private X500Name x500name;
    private Integer serialNumber;
    private Date startDate;
    private Date endDate;
}