package com.driverjartest.util;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.ScreenshotType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class WebPageScreenShotUtil {

    private Playwright playwright;
    private Browser browser;

    private void initializePlaywright() {

        if ( playwright == null ) {
            playwright = Playwright.create();
            browser    = playwright.chromium().launch( new BrowserType.LaunchOptions()
                    .setHeadless( true )
                    .setArgs( java.util.Arrays.asList(
                            "--no-sandbox" ,
                            "--disable-dev-shm-usage" ,
                            "--disable-gpu" ,
                            "--disable-web-security" ,
                            "--allow-running-insecure-content" ,
                            "--ignore-certificate-errors" ,
                            "--disable-extensions" ,
                            "--disable-plugins" ,
                            "--disable-background-networking" ,
                            "--disable-background-timer-throttling" ,
                            "--disable-renderer-backgrounding" ,
                            "--disable-backgrounding-occluded-windows" ,
                            "--log-level=3"
                    ) )
            );
        }
    }

    public Optional<byte[]> captureWebPageScreenshot( String url ) {

        Page page = null;
        try {
            initializePlaywright();

            // 새 페이지 생성
            page = browser.newPage();

            // User-Agent 설정
            page.setExtraHTTPHeaders( java.util.Map.of(
                    "User-Agent" , "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
            ) );

            log.info( "webpage screenshot start: {}" , url );

            // 페이지 로드
            page.navigate( url , new Page.NavigateOptions().setTimeout( 30000 ) );

            // 페이지 로딩 완료 대기
            page.waitForLoadState( LoadState.NETWORKIDLE , new Page.WaitForLoadStateOptions().setTimeout( 30000 ) );

            // 추가 대기 시간
            page.waitForTimeout( 3000 );

            // 스크린샷 캡처
            byte[] screenshotBytes = page.screenshot( new Page.ScreenshotOptions()
                    .setFullPage( false )
                    .setType( ScreenshotType.PNG )
            );

            return Optional.of( screenshotBytes );

        }
        catch ( Exception e ) {
            log.error( "webpage screenshot failed: {}" , url , e );
            return Optional.empty();
        }
        finally {
            if ( page != null ) {
                try {
                    page.close();
                }
                catch ( Exception e ) {
                    log.warn( "Page terminate failed" , e );
                }
            }
        }
    }

}
