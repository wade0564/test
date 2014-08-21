package wade.anotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class AnotationTest {
	
	@Wade(vlaue="smart")
	private String nature;
	
	
	public static void main(String[] args) throws NoSuchFieldException, SecurityException {
		
		
		Annotation[] annotations = AnotationTest.class.getAnnotations();
		
		Field[] fields = AnotationTest.class.getFields();
		
		for (Field field : fields) {
			System.out.println(field.getName());
		}
		
		
		
		Wade wade = AnotationTest.class.getField("nature").getAnnotation(Wade.class);
		
		for (Annotation annotation : annotations) {
			System.out.println(annotation.toString());
			
		}
		
		System.out.println(wade.vlaue());
		
		
	}
	
}
