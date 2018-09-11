package bitcamp.java110.cms.context;

import java.lang.reflect.Method;
import java.util.Collection;

import bitcamp.java110.cms.annotation.Autowired;
import bitcamp.java110.cms.annotation.Component;

@Component
public class LogBeanPostProcessor implements BeanPostProcessor{

    ApplicationContext beanContainer;

    public void postProcess(ApplicationContext beanContainer) {

        Collection<Object> objList = beanContainer.objPool.values();

        // 목록에서 객체를 꺼내 @Autowired가 붙은 매서드를 찾는다.
        System.out.println("============================================================================");
        for(Object obj : objList) {
            
            System.out.println(obj.getClass().getName());
        }
        System.out.println("============================================================================");
    }

   

}
