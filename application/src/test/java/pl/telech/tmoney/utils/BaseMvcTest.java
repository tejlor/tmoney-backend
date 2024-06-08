package pl.telech.tmoney.utils;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.ResourceUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import pl.telech.tmoney.commons.helper.DBHelper;

@SpringBootTest
@ActiveProfiles("junit")
@AutoConfigureMockMvc(addFilters = false)
@FieldDefaults(level = AccessLevel.PROTECTED)
public class BaseMvcTest {
	
	@Autowired
	ObjectMapper objectMapper;
	
	@Autowired
	MockMvc mock;
	
	@Autowired
	protected DBHelper dbHelper;
	
	
	@AfterEach
	void afterEach() {
		dbHelper.truncateDb(List.of("bank.account", "bank.category", "bank.entry", "bank.transfer_definition", "bank.category_to_account"));
	}
		
	protected <T> T get(String url, Class<T> responseClass) throws Exception {
		MvcResult result = get(url);
		return parseResponse(result, responseClass);
	}
	
	protected <T> T get(String url, TypeReference<T> responseClass) throws Exception {
		MvcResult result = get(url);
		return parseResponse(result, responseClass);
	}
	
	private MvcResult get(String url) throws Exception {
		var request = MockMvcRequestBuilders.get(url).contentType(MediaType.APPLICATION_JSON);		
		return performAndReturn(request);
	}
	
	protected MvcResult getResult(String url) throws Exception {
		var request = MockMvcRequestBuilders.get(url).contentType(MediaType.APPLICATION_JSON);		
		return perform(request);
	}
	
	protected MvcResult post(String url, Object requestDto) throws Exception {
		var request = MockMvcRequestBuilders.post(url)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(objectMapper.writeValueAsString(requestDto));
		
		return performAndReturn(request);
	}
	
	protected MvcResult postResult(String url, Object requestDto) throws Exception {
		var request = MockMvcRequestBuilders.post(url)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(objectMapper.writeValueAsString(requestDto));
		
		return perform(request);
	}
	
	protected MvcResult postFile(String url, String fileUrl) throws Exception {
		File file = ResourceUtils.getFile("classpath:" + fileUrl);	
		var multipartFile = new MockMultipartFile("file", null, "application/vnd.ms-excel", new FileInputStream(file));	
		var request = MockMvcRequestBuilders.multipart(url).file(multipartFile);
		
		return performAndReturn(request);
	}
	
	protected <T> T post(String url, Object requestDto, Class<T> responseClass) throws Exception {
		MvcResult result = post(url, requestDto);
		return parseResponse(result, responseClass);
	}
	
	protected <T> T post(String url, Object requestDto, TypeReference<T> responseClass) throws Exception {
		MvcResult result = post(url, requestDto);
		return parseResponse(result, responseClass);
	}
	
	protected <T> T postFile(String url, String fileUrl, TypeReference<T> responseClass) throws Exception {
		MvcResult result = postFile(url, fileUrl);
		return parseResponse(result, responseClass);
	}
	
	protected MvcResult put(String url, Object requestDto) throws Exception {
		var request = MockMvcRequestBuilders.put(url)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requestDto));
		
		return performAndReturn(request);
	}
	
	protected <T> T put(String url, Object requestDto, Class<T> responseClass) throws Exception {
		MvcResult result = put(url, requestDto);
		return parseResponse(result, responseClass);
	}
	
	protected <T> T put(String url, Object requestDto, TypeReference<T> responseClass) throws Exception {
		MvcResult result = put(url, requestDto);
		return parseResponse(result, responseClass);
	}
	
	protected MvcResult delete(String url) throws Exception {
		var request = MockMvcRequestBuilders.delete(url)
				.contentType(MediaType.APPLICATION_JSON);
		
		return perform(request);
	}
	
	private MvcResult performAndReturn(MockHttpServletRequestBuilder request) throws Exception {
		System.out.println("===== Start mock request");
		
		MvcResult result = mock.perform(request)
			.andExpectAll(
					status().is2xxSuccessful(),
			        content().contentType("application/json"))
			.andReturn();
		
		System.out.println("===== End mock request");
		
		return result;
	}
	
	private MvcResult perform(MockHttpServletRequestBuilder request) throws Exception {
		System.out.println("===== Start mock request");
		
		MvcResult result = mock.perform(request)
			.andReturn();
		
		System.out.println("===== End mock request");
		
		return result;
	}
	
	private <T> T parseResponse(MvcResult result, Class<T> responseClass) throws Exception {
		return objectMapper.readValue(result.getResponse().getContentAsString(StandardCharsets.UTF_8), responseClass);
	}
	
	private <T> T parseResponse(MvcResult result, TypeReference<T> responseClass) throws Exception {
		return objectMapper.readValue(result.getResponse().getContentAsString(StandardCharsets.UTF_8), responseClass);
	}
	
}
