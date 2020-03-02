/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.geekbang.thinking.in.spring.dependency.lookup;

import org.geekbang.thinking.in.spring.ioc.overview.domain.User;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.Service;

import javax.xml.ws.ServiceMode;

/**
 * 通过 {@link ObjectProvider} 进行依赖查找
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since
 */
@Import(ObjectProviderDemo.MyConfig.class)
public class ObjectProviderDemo { // @Configuration 是非必须注解

	public static void main(String[] args) {
		// 创建 BeanFactory 容器
		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
		// 将当前类 ObjectProviderDemo 作为配置类（Configuration Class）
		applicationContext.register(ObjectProviderDemo.class);
		// 启动应用上下文
		applicationContext.refresh();
		// 依赖查找集合对象
		lookupByObjectProvider(applicationContext);
		lookupIfAvailable(applicationContext);
		lookupByStreamOps(applicationContext);

		//bean的本就可以导入不需要注解
		System.out.println(applicationContext.getBean("myUser"));
		System.out.println(applicationContext.getBeanProvider(MyUser.class).getObject());

		System.out.println(applicationContext.getBean("myUser2"));

		// 关闭应用上下文
		applicationContext.close();

	}

	private static void lookupByStreamOps(AnnotationConfigApplicationContext applicationContext) {
		ObjectProvider<String> objectProvider = applicationContext.getBeanProvider(String.class);
//        Iterable<String> stringIterable = objectProvider;
//        for (String string : stringIterable) {
//            System.out.println(string);
//        }
		// Stream -> Method reference
		objectProvider.stream().forEach(System.out::println);
	}

	private static void lookupIfAvailable(AnnotationConfigApplicationContext applicationContext) {
		ObjectProvider<User> userObjectProvider = applicationContext.getBeanProvider(User.class);
		User user = userObjectProvider.getIfAvailable(User::createUser);
		System.out.println("当前 User 对象：" + user);
	}

	@Bean
	@Primary
	public String helloWorld() { // 方法名就是 Bean 名称 = "helloWorld"
		return "Hello,World";
	}

	@Bean
	public String message() {
		return "Message";
	}

	private static void lookupByObjectProvider(AnnotationConfigApplicationContext applicationContext) {
		ObjectProvider<String> objectProvider = applicationContext.getBeanProvider(String.class);
		System.out.println(objectProvider.getObject());
	}

	@Bean
	public MyUser myUser() {
		MyUser user = new MyUser();
		user.setCode("1");
		user.setName("name");
		return user;
	}


	static class MyUser {
		private String name;
		private String code;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		@Override
		public String toString() {
			return "MyUser{" +
					"name='" + name + '\'' +
					", code='" + code + '\'' +
					'}';
		}
	}

	@Configuration
	static class MyConfig {
	    @Bean
        @Primary
		public MyUser myUser2() {
			MyUser user = new MyUser();
			user.setCode("2");
			user.setName("name2");
			return user;
		}
	}
}
