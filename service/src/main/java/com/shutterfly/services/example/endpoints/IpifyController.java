package com.shutterfly.services.example.endpoints;
import com.shutterfly.services.example.model.IpAddressModel;
import com.shutterfly.services.example.services.api.IpifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class IpifyController {

    private final IpifyService ipifyService;

    @Autowired
    public IpifyController(IpifyService ipifyService) {
        this.ipifyService = ipifyService;
    }
    @GetMapping("/ipify")
    public String showIpifyPage(Model model) {
       String ip=ipifyService.getIpAddress();
        model.addAttribute("ipAddressModel",new IpAddressModel(ip));
    return "ipify";
    }
    @GetMapping("/getIpAddress")
    @ResponseBody
    public ResponseEntity<Map<String, String>> getIpAddress() {
        // Logic to get the updated IP address
        String updatedIpAddress = ipifyService.getIpAddress();

        // Create a response map
        Map<String, String> response = new HashMap<>();
        response.put("ipAddress", updatedIpAddress);

        // Return the response as JSON
        return ResponseEntity.ok(response);
    }
}
