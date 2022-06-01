package com.xwj.factorybean;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

/**
 * FactoryBean
 * @author buming
 * Email buming@uoko.com
 * @Date: 2022/06/01 16:29
 */
@Component
public class StudentFactoryBeans implements FactoryBean<Student> {
	@Override
	public Student getObject() throws Exception {
		Student student=new Student();
		student.setName("g");
		return student;
	}

	@Override
	public Class<?> getObjectType() {
		return Student.class;
	}
}
