package pl.telech.processor;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;

import org.springframework.web.bind.annotation.RequestMapping;

import com.google.auto.service.AutoService;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import pl.telech.processor.annotation.AutoMethod;
import pl.telech.processor.model.TemplateModel;

@SupportedAnnotationTypes({"pl.telech.processor.annotation.AutoMethod", "pl.telech.processor.annotation.AutoMethod.List"})
@SupportedSourceVersion(SourceVersion.RELEASE_11)
@AutoService(Processor.class)
public class AutoMethodProcessor extends AbstractProcessor {
	
	private static final String templateName = "controller.ftl";
	private static final String constructorClassName = "pl.telech.tmoney.%s.controller.%sBaseController";

	@Override
	public boolean process(Set<? extends TypeElement> annotationElements, RoundEnvironment roundEnv) {	
		Map<TypeElement, AutoMethod[]> annotatedClasses = findAnnotatedClasses(annotationElements, roundEnv);
		
		if (!annotatedClasses.isEmpty()) {
			Configuration conf = createConfiguration();
			Template template = getTemplate(conf);
			
			for (var entry : annotatedClasses.entrySet()) {
				TypeElement classElement = entry.getKey();
				AutoMethod[] annotations = entry.getValue();
				
				TemplateModel model = buildModel(classElement, annotations);

				try {
					printFile(template, model);
				} 
				catch (IOException | TemplateException e) {
					e.printStackTrace();
				}			
			}
		}

		return true;
	}
	
	private Map<TypeElement, AutoMethod[]> findAnnotatedClasses(Set<? extends TypeElement> annotationElements, RoundEnvironment roundEnv) {
		return annotationElements.stream()
			.flatMap(element -> roundEnv.getElementsAnnotatedWith(element).stream())
			.distinct()
			.filter(element -> element.getKind() == ElementKind.METHOD)
			.collect(Collectors.toMap(this::getClass, this::getAnnotations));
	}
	
	private TypeElement getClass(Element methodElement) {
		return (TypeElement) methodElement.getEnclosingElement();
	}
	
	private AutoMethod[] getAnnotations(Element methodElement) {
		return methodElement.getAnnotationsByType(AutoMethod.class);
	}
	
	private TemplateModel buildModel(TypeElement classElement, AutoMethod[] annotations) {
		String classFullName = classElement.getQualifiedName().toString();
		
		return TemplateModel.builder()
				.packageName(getPackageName(classFullName))
				.moduleName(getModuleName(classFullName))
				.path(getEndpointPath(classElement))
				.type(getTypeName(classFullName))
				.methods(getMethods(annotations))
				.build();
	}
	
	private String getPackageName(String classFullName) {
		return classFullName.substring(0, classFullName.lastIndexOf('.'));
	}
	
	private String getModuleName(String classFullName) {
		return classFullName.substring(classFullName.indexOf("tmoney") + 7, classFullName.lastIndexOf(".controller"));
	}

	private String getEndpointPath(TypeElement classElement) {
		return ((RequestMapping) classElement.getAnnotation(RequestMapping.class)).value()[0];
	}
	
	private String getTypeName(String classFullName) {
		return classFullName.substring(classFullName.lastIndexOf('.') + 1, classFullName.lastIndexOf("Controller"));
	}
	
	private List<String> getMethods(AutoMethod[] annotations) {
		return Arrays.stream(annotations)
				.map(annotation -> annotation.type().toString())
				.collect(Collectors.toList());
	}
	
	private Configuration createConfiguration() {
		var conf = new Configuration(Configuration.VERSION_2_3_32);
		conf.setClassForTemplateLoading(getClass(), "/templates/");
		conf.setDefaultEncoding("UTF-8");
		conf.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		conf.setLogTemplateExceptions(false);
		conf.setWrapUncheckedExceptions(true);
		conf.setFallbackOnNullLoopVariable(false);
		return conf;
	}
	
	private Template getTemplate(Configuration engine) {
		try {
			return engine.getTemplate(templateName);
		} 
		catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private void printFile(Template template, TemplateModel model) throws IOException, TemplateException {
		String filePath = String.format(constructorClassName, model.getModuleName(), model.getType());
		
		JavaFileObject builderFile = processingEnv.getFiler().createSourceFile(filePath);
		try (Writer writer = builderFile.openWriter()) {
			template.process(model, writer);
		}
	}

}
