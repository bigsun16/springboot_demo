package com.qihui.sun.security_springboot;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@SpringBootTest
class SecuritySpringbootApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    public void testBCrypt(){
        String password = BCrypt.hashpw("123", BCrypt.gensalt());
        System.out.println(password);
//        $2a$10$ja.WPLMYnyI7G1tXYE3ktufs2nKH1AS.0wWdXwMugAWLRnFUCCRTS
//        $2a$10$sUqmkNtoeZm4EG/tMaBl0OZ8i8MpkqnWH7Z2o8VLCSmBOx2SXhqjS
//        $10$ExiwHY8HvfPPoD2fRyTkQuoEg0LGB.hx62TGT8S3fwiHCiAMAqHRq
        boolean checkpw = BCrypt.checkpw("123", "$2a$10$ja.WPLMYnyI7G1tXYE3ktufs2nKH1AS.0wWdXwMugAWLRnFUCCRTS");
        boolean checkp2 = BCrypt.checkpw("123", "$2a$10$sUqmkNtoeZm4EG/tMaBl0OZ8i8MpkqnWH7Z2o8VLCSmBOx2SXhqjS");
        boolean checkp3 = BCrypt.checkpw("123456", "$10$ExiwHY8HvfPPoD2fRyTkQuoEg0LGB.hx62TGT8S3fwiHCiAMAqHRq");
        System.out.println(checkpw);
        System.out.println(checkp2);
        System.out.println(checkp3);
    }
}
