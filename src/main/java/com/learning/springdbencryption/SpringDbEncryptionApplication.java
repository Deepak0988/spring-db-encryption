package com.learning.springdbencryption;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import javax.crypto.Cipher;
import java.security.NoSuchAlgorithmException;

@SpringBootApplication
@EnableAspectJAutoProxy
public class SpringDbEncryptionApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringDbEncryptionApplication.class, args);
    }

}
