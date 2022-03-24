package pl.telech.tmoney.commons.config.filter;

import static lombok.AccessLevel.PRIVATE;

import java.io.*;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

/*
 * Wrapper for request, which allows read body content twice.
 * Rest controller reads body and closes stream, which does not allow to read it again in AppLogAspect.
 */
@Slf4j
@FieldDefaults(level = PRIVATE)
public class RequestWrapper extends HttpServletRequestWrapper {

	@Getter
	final String body;
	
	public RequestWrapper(HttpServletRequest request) throws IOException {
		super(request);

		var sb = new StringBuilder();
		BufferedReader bufferedReader = null;
		try {
			InputStream inputStream = request.getInputStream();
			if (inputStream != null) {
				bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
				char[] charBuffer = new char[128];
				int bytesRead = -1;
				while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
					sb.append(charBuffer, 0, bytesRead);
				}
			} 
		} 
		catch (IOException e) {
			log.error(e.getMessage());
		} 
		finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} 
				catch (IOException e) {
					log.error(e.getMessage());
				}
			}
		}
		body = sb.toString();
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		return new ServletInputStream() {
			private ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body.getBytes());
			
			@Override
			public int read() throws IOException {
				return byteArrayInputStream.read();
			}

			@Override
			public boolean isFinished() {
				return false;
			}

			@Override
			public boolean isReady() {
				return true;
			}

			@Override
			public void setReadListener(ReadListener arg0) {

			}
		};
	}

	@Override
	public BufferedReader getReader() throws IOException {
		return new BufferedReader(new InputStreamReader(getInputStream()));
	}
}
