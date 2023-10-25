package utils;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;

@Sources("classpath:property.property")
public interface MyConfig extends Config {

    @Key("url")
    String url();

    @Key("email")
    String email();

    @Key("password")
    String password();

    @Key("wrongEmail")
    String wrongEmail();

    @Key("token")
    String token();
}
