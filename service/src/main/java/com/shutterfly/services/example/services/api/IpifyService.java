package com.shutterfly.services.example.services.api;

import org.springframework.stereotype.Service;

@Service
public interface IpifyService {
    String getIpAddress();
}