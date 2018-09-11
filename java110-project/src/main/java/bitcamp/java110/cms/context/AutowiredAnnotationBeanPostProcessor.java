package bitcamp.java110.cms.context;

import java.lang.reflect.Method;
import java.util.Collection;

import bitcamp.java110.cms.annotation.Autowired;
import bitcamp.java110.cms.annotation.Component;


public class AutowiredAnnotationBeanPostProcessor{

    ApplicationContext beanContainer;

    public void postProcess(ApplicationContext beanContainer) {

        Collection<Object> objList = beanContainer.objPool.values();

        // 목록에서 객체를 꺼내 @Autowired가 붙은 매서드를 찾는다.
        for(Object obj : objList) {


            Method[] methods = obj.getClass().getDeclaredMethods();
            for(Method m : methods) {

                if (!m.isAnnotationPresent(Autowired.class)) {
                    continue;
                }

                // setter 매서드의 파라미터 타입을 알아낸다.
                Class<?> paramType = m.getParameterTypes()[0];

                // 그 파라미터 타입과 일치하는 객체를 objPool에서 꺼낸다.
                Object dependency = beanContainer.getBean(paramType);
                if(dependency == null) {
                    continue;   
                }


                //System.out.printf("%s() 호출 \n" , m.getName());
                try {
                    m.invoke(obj, dependency);
                    System.out.printf("%s() 호출됨 \n" , m.getName());
                }catch(Exception e){};
            }
        }
    }

   

}
