package com.shutterfly.services.example.services.impl;
import com.shutterfly.services.example.dao.api.IpifyDao;
import com.shutterfly.services.example.services.api.IpifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IpifyServiceImpl implements IpifyService {

    private final IpifyDao ipifyDao;

    @Autowired
    public IpifyServiceImpl(IpifyDao ipifyDao) {
        this.ipifyDao = ipifyDao;
    }

    @Override
    public String getIpAddress() {
        return ipifyDao.getIpAddress();
    }
}