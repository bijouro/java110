package bitcamp.java110.cms.context;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.ibatis.io.Resources;

import bitcamp.java110.cms.annotation.Component;

public class ApplicationContext {

    HashMap<String, Object> objPool = new HashMap<>();
    List<Class<?>> classes = new ArrayList<>();
    public ApplicationContext(String packageName) throws Exception{

        // 패키지 이름을 파일 경로로 바꾼다
        String path = packageName.replace(".","/");

        // 패키지 경로를 가지고 전체 파일 경로를 알아낸다.
        File file = Resources.getResourceAsFile(path);

        //System.out.println(file.getAbsolutePath());

        // 패키지 폴더에 들어 있는 파일 목록을 알아낸다.


        // 패키지 폴더에 들어 있는 클래스를 찾아 클래스를 로딩한 후 그 목록을 리턴한다 
        findClass(file, path);



        // 로딩된 클래스 목록을 뒤져서 @Component 애노테이션이 붙은 클래스에 대해
        // 인스턴스를 생성하여 objPool에 보관한다.
        createInstance();



        // 객체 생성 후에 실행할 작업이 있다면 BeanPostProcessor 구현체를 찾아 실행한다.
        callBeanPostProcessor();
 
        // 1) 인스턴스 생성
        // 2) 
        // 해당 패키지에 있는 클래스를 찾아서 인스턴스를 생성한 후에 
        // objPool에 보관한다.

    }

    // objPool에서 보관된 객체를 이름으로 찾아 리턴한다.
    public Object getBean(String name) {
        return objPool.get(name);
    }

    // 객체의  타입으로 objPool에 보관된 객체를 찾아 리턴한다 
    public Object getBean(Class<?> type) {
        Collection<Object> objList = objPool.values();
        for(Object obj : objList) {

            if (type.isInstance(obj)) {
                return obj;
            }
        }
        return null;        
    }

    public String[] getBeanDefinitionNames() {
        Set<String> keySet = objPool.keySet();
        String[] names = new String[keySet.size()];
        keySet.toArray(names);
        return names;
    }
    private void findClass(File path, String pacakagePath){

        File[] files = path.listFiles();
        for(File file : files) {
            if(file.isDirectory()) {
                findClass(file, pacakagePath + "/" + file.getName());
            }else {
                String className = (pacakagePath + "/" + file.getName())
                        .replace("/",".")
                        .replace(".class","");

                // 1) 클래스 이름을 가지고 .class 파일을 찾아 메모리에 로딩한다
                try {
                    Class<?> clazz = Class.forName(className);
                    classes.add(clazz); // 로딩한 클래스 정보를 목록에 보관한다.
                }catch(Exception e) {}
            }

        }
    }

    private void createInstance() {
        for(Class<?> clazz : classes) {
            // 인터페이스인경우 무시한다.
            if ( clazz.isInterface() ) {
                continue;
            }



            // 클래스에서 Component 애노테이션을 추출한다.
            Component anno = clazz.getAnnotation(Component.class);
            if ( anno == null) {
                // @Component 애노테이션이 붙지 않은 클래스는 제외한다.
                continue;   
            }           

            try {
                // 2) 로딩된 클래스 정보를 가지고 인스턴스를 생성한다
                // 먼저 해당 클래스의 생성자 정보를 얻는다.
                Constructor<?> constructor = clazz.getConstructor();
                // 생성자를 가지고 인스턴스를 생성한다.
                Object instance = constructor.newInstance();

                // 이름으로 인스턴스의 이름을 꺼낸다.
                //Field field = clazz.getField("name");



                // Component 애노테이션이 value 값이 있으면 그 값으로 객체를 저장
                // 없으면 클래스 이름으로 객체를 저장

                if(anno.value().length() > 0 ) {
                    objPool.put(anno.value(), instance);    
                }else {
                    objPool.put(clazz.getName(), instance);
                }


                // name 필드의 값을 꺼낸다.
                // Object name = field.get(instance);
                // System.out.println(clazz.getName() + " ===> " + name);

                // name 필드의 값으로 인스턴스를 objPool에 저장한다.
                // objPool.put((String)name, instance);



            }catch(Exception  e) {
                e.printStackTrace();
                System.out.printf("%s : 기본 생성자가 없습니다.\n", clazz.getName());
            }
        }
    }

    private void injectDependency() {
        // objPool에 보관된 객체 목록을 꺼낸다.

    }

    private void callBeanPostProcessor() {

        Collection<Object> objList = objPool.values();

        // objPool에 보관된 객체중에서 beanPostProcess 규칙을 준수하는 객체를 찾는다.
        for(Object obj : objList) {
            if(!BeanPostProcessor.class.isInstance(obj)) {
                continue;
            }

            BeanPostProcessor processor = (BeanPostProcessor)obj;
            processor.postProcess(this);
        }
    }

    /*private void PostProcessPerObject(BeanPostProcessor processor) {

        Set<String> keySet = objPool.keySet();
        for(String key : keySet) {
            processor.postProcess(objPool.get(key), key);
        }

    }
     */


}
