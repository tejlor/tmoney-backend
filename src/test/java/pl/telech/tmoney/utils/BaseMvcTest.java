package pl.telech.tmoney.utils;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

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
		dbHelper.truncateDb(List.of("bank.account", "bank.category", "bank.entry"));
	}
	
	protected MvcResult get(String url) throws Exception {
		var request = MockMvcRequestBuilders.get(url)
				.contentType(MediaType.APPLICATION_JSON);
		
		return perform(request);
	}
	
	protected MvcResult get2(String url) throws Exception {
		var request = MockMvcRequestBuilders.get(url)
				.contentType(MediaType.APPLICATION_JSON);
		
		return performDelete(request);
	}
	
	protected <T> T get(String url, Class<T> responseClass) throws Exception {
		MvcResult result = get(url);
		return parseResponse(result, responseClass);
	}
	
	protected <T> T get(String url, TypeReference<T> responseClass) throws Exception {
		MvcResult result = get(url);
		return parseResponse(result, responseClass);
	}
	
	protected MvcResult post(String url, Object requestDto) throws Exception {
		var request = MockMvcRequestBuilders.post(url)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(objectMapper.writeValueAsString(requestDto));
		
		return perform(request);
	}
	
	protected <T> T post(String url, Object requestDto, Class<T> responseClass) throws Exception {
		MvcResult result = post(url, requestDto);
		return parseResponse(result, responseClass);
	}
	
	protected <T> T post(String url, Object requestDto, TypeReference<T> responseClass) throws Exception {
		MvcResult result = post(url, requestDto);
		return parseResponse(result, responseClass);
	}
	
	protected MvcResult put(String url, Object requestDto) throws Exception {
		var request = MockMvcRequestBuilders.put(url)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requestDto));
		
		return perform(request);
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
		
		return performDelete(request);
	}
	
	private MvcResult perform(MockHttpServletRequestBuilder request) throws Exception {
		return mock.perform(request)
			.andExpectAll(
					status().isOk(),
			        content().contentType("application/json"))
			.andReturn();
	}
	
	private MvcResult performDelete(MockHttpServletRequestBuilder request) throws Exception {
		return mock.perform(request)
			.andReturn();
	}
	
	private <T> T parseResponse(MvcResult result, Class<T> responseClass) throws Exception {
		return objectMapper.readValue(result.getResponse().getContentAsString(StandardCharsets.UTF_8), responseClass);
	}
	
	private <T> T parseResponse(MvcResult result, TypeReference<T> responseClass) throws Exception {
		return objectMapper.readValue(result.getResponse().getContentAsString(StandardCharsets.UTF_8), responseClass);
	}
	
}
