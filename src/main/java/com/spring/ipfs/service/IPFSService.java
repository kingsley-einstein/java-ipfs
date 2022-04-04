package com.spring.ipfs.service;

import com.spring.ipfs.config.IPFSConfig;
import com.spring.ipfs.interfaces.IPFSServiceImpl;
import io.ipfs.api.IPFS;
import io.ipfs.api.MerkleNode;
import io.ipfs.api.NamedStreamable;
import io.ipfs.multihash.Multihash;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class IPFSService implements IPFSServiceImpl {

  @Autowired
  IPFSConfig config;

  @Override
  public String saveFile(MultipartFile file) {
    try {
      InputStream stream = new ByteArrayInputStream(file.getBytes());
      IPFS ipfs = config.getIpfs();

      NamedStreamable.InputStreamWrapper iStreamWrapper = new NamedStreamable.InputStreamWrapper(stream);
      MerkleNode res = ipfs.add(iStreamWrapper).get(0);
      return res.hash.toBase58();
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  @Override
  public String saveJson(Map<String, Object> json) {
    try {
      String mapAsString = mapToString(json);
      InputStream stream = new ByteArrayInputStream(mapAsString.getBytes());
      IPFS ipfs = config.getIpfs();
      NamedStreamable.InputStreamWrapper iStreamWrapper = new NamedStreamable.InputStreamWrapper(stream);
      MerkleNode res = ipfs.add(iStreamWrapper).get(0);
      return res.hash.toBase58();
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  @Override
  public byte[] getItem(String hash) {
    try {
      IPFS ipfs = config.getIpfs();
      Multihash multihash = Multihash.fromBase58(hash);
      return ipfs.cat(multihash);
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  private String mapToString(Map<String, Object> map) {
    return map
      .keySet()
      .stream()
      .map(key -> String.format("\"%s\":", key) + " " + map.get(key))
      .collect(Collectors.joining(", ", "{", "}"));
  }
}
