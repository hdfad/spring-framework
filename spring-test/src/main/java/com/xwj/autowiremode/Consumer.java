package com.xwj.autowiremode;

import org.springframework.stereotype.Component;
 
@Component
public class Consumer {
 
    private Providera providera;
    private Providerb providerb;
 
    public void setProvidera(Providera a) {
        this.providera = a;
    }
 
    public void setProviderb(Providerb b) {
        this.providerb = b;
    }
 
    public void providerSout () {
        System.out.println(providera);
        System.out.println(providerb);
    }
 
}