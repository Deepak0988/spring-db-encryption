package com.learning.springdbencryption.service;


import com.learning.springdbencryption.entity.EncTable;
import com.learning.springdbencryption.repo.EncTableRepo;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.cms.*;
import org.bouncycastle.cms.jcajce.JceCMSContentEncryptorBuilder;
import org.bouncycastle.cms.jcajce.JceKeyTransEnvelopedRecipient;
import org.bouncycastle.cms.jcajce.JceKeyTransRecipient;
import org.bouncycastle.cms.jcajce.JceKeyTransRecipientInfoGenerator;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.OutputEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.List;

@Service
@Slf4j
public class EncService{

    @Autowired
    ResourceLoader loader;

    @Autowired
    EncTableRepo encTableRepo;

    public byte[] encryptData(byte[] data) throws CertificateException, CMSException, IOException, NoSuchProviderException {
        X509Certificate encryptionCertificate = getCertificate();
        byte[] encryptedData = null;
        if (null != data && null != encryptionCertificate) {
            CMSEnvelopedDataGenerator cmsEnvelopedDataGenerator = new CMSEnvelopedDataGenerator();

            JceKeyTransRecipientInfoGenerator jceKey = new JceKeyTransRecipientInfoGenerator(encryptionCertificate);
            cmsEnvelopedDataGenerator.addRecipientInfoGenerator(jceKey);
            CMSTypedData msg = new CMSProcessableByteArray(data);
            OutputEncryptor encryptor = new JceCMSContentEncryptorBuilder(CMSAlgorithm.AES128_CBC).setProvider("BC").build();
            CMSEnvelopedData cmsEnvelopedData = cmsEnvelopedDataGenerator.generate(msg, encryptor);
            encryptedData = cmsEnvelopedData.getEncoded();
        }
        return encryptedData;
    }

    public byte[] decryptData(byte[] encryptedData) throws CMSException, UnrecoverableKeyException, CertificateException, KeyStoreException, NoSuchAlgorithmException, IOException {
        PrivateKey decryptionKey = getKeystore();
        byte[] decryptedData = null;
        if (null != encryptedData && null != decryptionKey) {
            CMSEnvelopedData envelopedData = new CMSEnvelopedData(encryptedData);

            Collection<RecipientInformation> recipients = envelopedData.getRecipientInfos().getRecipients();
            KeyTransRecipientInformation recipientInfo = (KeyTransRecipientInformation) recipients.iterator().next();
            JceKeyTransRecipient recipient = new JceKeyTransEnvelopedRecipient(decryptionKey);

            return recipientInfo.getContent(recipient);
        }
        return "".getBytes();
    }

    public void save() {
        encTableRepo.save(EncTable.builder().id(5L).encData("Test5").build());
    }

    public List<EncTable> get() {
        return encTableRepo.findAll();
    }


    private X509Certificate getCertificate() throws CertificateException, NoSuchProviderException, IOException {
        BouncyCastleProvider provider = new BouncyCastleProvider();
        Security.addProvider(provider);
        CertificateFactory certFactory = CertificateFactory.getInstance("X.509", "BC");
        InputStream is = loader.getResource("classpath:OAMP.cer").getInputStream();
        return (X509Certificate) certFactory.generateCertificate(is);
    }

    private PrivateKey getKeystore() throws KeyStoreException, UnrecoverableKeyException, NoSuchAlgorithmException, CertificateException, IOException {
        BouncyCastleProvider provider = new BouncyCastleProvider();
        Security.addProvider(provider);

        char[] keystorePassword = "changeit".toCharArray();
        char[] keyPassword = "changeit".toCharArray();

        KeyStore keystore = KeyStore.getInstance("PKCS12");
        keystore.load(loader.getResource("classpath:OAMP.p12").getInputStream(), keystorePassword);
        PrivateKey key = (PrivateKey) keystore.getKey("oamp", keyPassword);
        return key;
    }
}

