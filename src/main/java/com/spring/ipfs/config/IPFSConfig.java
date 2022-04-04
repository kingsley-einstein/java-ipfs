package com.spring.ipfs.config;

import io.ipfs.api.IPFS;
// import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class IPFSConfig {

  IPFS ipfs;

  // @Value("{ipfs.host}")
  // private String host;

  // @Value("{ipfs.port}")
  // private int port;

  public IPFSConfig() {
    ipfs = new IPFS("localhost", 5001);
  }

  public IPFS getIpfs() {
    return ipfs;
  }
}
