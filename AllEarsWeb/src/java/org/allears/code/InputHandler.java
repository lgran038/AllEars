/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.allears.code;

/**
 *
 * @author MSI Laptop
 */
public class InputHandler {

    /**
     * @return the ipaddr
     */
    public String getIpaddr() {
        return ipaddr;
    }

    /**
     * @param ipaddr the ipaddr to set
     */
    public void setIpaddr(String ipaddr) {
        this.ipaddr = ipaddr;
    }
    
    private String ipaddr;
    
    public InputHandler(){
        ipaddr = null;
    }
}
