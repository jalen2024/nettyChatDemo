package com.freddy.im.bean;

/**
 * socket的端口数据
 */
public class HostBean {

    public String host;
    public int port;

    public HostBean(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public String toString() {
        return "HostBean{" +
                "host='" + host + '\'' +
                ", port=" + port +
                '}';
    }
}
