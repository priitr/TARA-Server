package ee.ria.sso.utils;


import java.io.ByteArrayInputStream;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import org.apache.axis.encoding.Base64;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x500.style.IETFUtils;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by serkp on 7.10.2017.
 */

public class X509Utils {

    private static final Logger log = LoggerFactory.getLogger(X509Utils.class);
    private static final String BEGIN_CERT = "-----BEGIN CERTIFICATE-----";
    private static final String END_CERT = "-----END CERTIFICATE-----";

    public static X509Certificate toX509Certificate(String encodedCertificate) {
        try {
            return (X509Certificate) CertificateFactory.getInstance("X.509")
                .generateCertificate(new ByteArrayInputStream(Base64.decode(encodedCertificate
                    .replaceAll(BEGIN_CERT, "").replaceAll(END_CERT, ""))));
        } catch (CertificateException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getSubjectCNFromCertificate(X509Certificate certificate) {
        try {
            X500Name x500name = new JcaX509CertificateHolder(certificate).getIssuer();
            RDN cn = x500name.getRDNs(BCStyle.CN)[0];
            return IETFUtils.valueToString(cn.getFirst().getValue());
        } catch (CertificateEncodingException e) {
            log.error("Unable to get issuer CN", e);
            return null;
        }
    }

}
