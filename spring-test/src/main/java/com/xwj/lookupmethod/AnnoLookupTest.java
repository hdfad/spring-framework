package com.xwj.lookupmethod;

import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.Component;

/**
 * @author buming
 * Email buming@uoko.com
 * @Date: 2023/04/11 11:35
 */
@ComponentScan("com.xwj.lookupmethod")
public class AnnoLookupTest {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext annotationConfigApplicationContext=new AnnotationConfigApplicationContext(AnnoLookupTest.class);
		La bean = annotationConfigApplicationContext.getBean(La.class);
		Lb lb = bean.getLb();
		Lb lb1 = bean.getLb();
		Ld ld = bean.getLd();
		System.out.println(ld);
		System.out.println(lb.equals(lb1));
	}

}

@Component
class La{
//	@Autowired
	Lb lb;
	@Autowired
	Lc lc;

	@Autowired
	Ld ld;

	@Lookup
	public Lb getLb() {
		return lb;
	}

	public Lc getLc() {
		return lc;
	}

	public void setLb(Lb lb) {
		this.lb = lb;
	}

	public void setLc(Lc lc) {
		this.lc = lc;
	}

	public Ld getLd() {
		return ld;
	}

	public void setLd(Ld ld) {
		this.ld = ld;
	}
}

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
class Lb{
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}

@Configuration
class Lc{
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Bean(autowire = Autowire.BY_TYPE)
	public Ld getObject(){
		return new Ld();
	}
}


class Ld{
	private String age;
	private Lb lb;

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public Lb getLb() {
		return lb;
	}

	public void setLb(Lb lb) {
		this.lb = lb;
	}
}