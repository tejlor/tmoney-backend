package pl.telech.tmoney.commons.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommonsTests {

	@Test
	public void allControllerMethodsHaveTest() {
		var reflections = new Reflections("pl.telech.tmoney", Scanners.TypesAnnotated);
		List<Class<?>> controllers = reflections.getTypesAnnotatedWith(RestController.class).stream()
				.filter(c -> !c.getName().endsWith("AbstractController"))
				.collect(Collectors.toList());
		
		for(Class<?> controller : controllers) {
			log.debug("Controller: " + controller.getName());
			Set<String> controllerMethods = Arrays.stream(controller.getDeclaredMethods())
				.filter(m -> Modifier.isPublic(m.getModifiers()))
				.filter(m -> m.isAnnotationPresent(RequestMapping.class))
				.map(m -> m.getName())
				.collect(Collectors.toSet());
				
			Class<?> testClass;
			try {
				testClass = Class.forName(controller.getName() + "Test");
			} 
			catch (ClassNotFoundException e) {
				Assertions.fail("Unable to find Test class form controller " + controller.getName());
				return;
			}
			
			Set<String> testMethods = Arrays.stream(testClass.getDeclaredMethods())
					.filter(m -> Modifier.isPublic(m.getModifiers()))
					.filter(m -> m.isAnnotationPresent(Test.class))
					.map(m -> m.getName())
					.collect(Collectors.toSet());
			
			assertThat(testMethods).as(controller.getName()).containsAll(controllerMethods);
		}
	}
}
