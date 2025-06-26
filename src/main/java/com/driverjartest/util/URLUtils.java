package com.driverjartest.util;

import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Slf4j
public final class URLUtils {

    private static WebPageScreenShotUtil screenshotUtil;

    public static void setScreenshotUtil( WebPageScreenShotUtil screenshotUtil ) {

        URLUtils.screenshotUtil = screenshotUtil;
    }

    public static String get( String url ) {
        CompletableFuture<Optional<String>> screenshotFuture = CompletableFuture
                .supplyAsync( () -> tryScreenshotCapture( url ) )
                .orTimeout( 25 , TimeUnit.SECONDS );

        try {
            Optional<String> screenshotResult = screenshotFuture.get();
            if ( screenshotResult.isPresent() ) {
                log.info( "can't find OG:Image so get webpage screenshot" );
            }
            return "success";
        } catch ( InterruptedException | ExecutionException e ) {
            return "failed";
        }
    }

    private static Optional<String> tryScreenshotCapture(String url ) {

        try {
            log.debug( "스크린샷 캡처 시작: {}" , url );

            if ( screenshotUtil == null ) {
                log.warn( "WebPageScreenshotUtil이 주입되지 않았습니다." );
                return Optional.empty();
            }

            Optional<byte[]> screenshotBase64 = screenshotUtil.captureWebPageScreenshot(url);
            log.info( "스크린샷 캡처 완료" );

        }
        catch ( Exception e ) {
            log.debug( "스크린샷 캡처 실패: {}" , url , e );
            return Optional.empty();
        }

        return Optional.of( url );
    }
}
