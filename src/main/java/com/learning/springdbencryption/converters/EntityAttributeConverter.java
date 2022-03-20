package com.learning.springdbencryption.converters;

import com.learning.springdbencryption.service.EncService;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.cms.CMSException;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

@Converter
@Slf4j
public class EntityAttributeConverter implements AttributeConverter<String,byte[]>, ApplicationContextAware {

    private org.springframework.context.ApplicationContext context;

    @Override
    public byte[] convertToDatabaseColumn(String s) {
        try {
            return applyEncryption(s);
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (CMSException e) {
            e.printStackTrace();
        }
        return "".getBytes();
    }

    @Override
    public String convertToEntityAttribute(byte[] bytes) {
        try {
            return applyDecryption(bytes);
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CMSException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    private byte[] applyEncryption(String s) throws CertificateException, IOException, NoSuchProviderException, CMSException {
        EncService encService = context.getBean(EncService.class);
        return encService.encryptData(s.getBytes());
    }

    private String applyDecryption(byte[] bytes) throws UnrecoverableKeyException, CertificateException, KeyStoreException, NoSuchAlgorithmException, IOException, CMSException {
        EncService encService = context.getBean(EncService.class);
        return new String(encService.decryptData(bytes));
    }
}
