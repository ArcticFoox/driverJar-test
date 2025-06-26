package com.driverjartest.web;

import com.driverjartest.util.URLUtils;
import com.driverjartest.util.WebPageScreenShotUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
public class PlaywrightController {

    @Autowired
    private WebPageScreenShotUtil screenshotUtil;

    @RequestMapping("/playwright")
    public String getPlaywright(@RequestParam String url) {

        URLUtils.setScreenshotUtil(screenshotUtil);

        String result = URLUtils.get(url);

        log.info("스크린샷 캡처 완료: {}", result);

        return "Ok";
    }
    @RequestMapping("/playwright2")
    public String getPlaywright2(@RequestParam String url) {

        URLUtils.setScreenshotUtil(screenshotUtil);

        String result = URLUtils.get2(url);

        log.info("스크린샷 캡처 완료: {}", result);

        return "Ok";
    }
}
