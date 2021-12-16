/*
 * Copyright 2002-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.aop.support;

import java.util.Arrays;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.junit.jupiter.api.Test;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.lang.Nullable;

/**
 * @author Dmitriy Kopylenko
 */
public class JdkRegexpMethodPointcutTests extends AbstractRegexpMethodPointcutTests {

	@Override
	protected AbstractRegexpMethodPointcut getRegexpMethodPointcut() {
		return new JdkRegexpMethodPointcut();
	}

	@Test
	void testRegexpMethod() {
		ProxyFactory proxyFactory = new ProxyFactory(new Person());
		/*JdkRegexpMethodPointcut pointcut = new JdkRegexpMethodPointcut();
		pointcut.setPatterns(new String[]{".*run.*", ".*say.*"});*/
		AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
		pointcut.setExpression("@annotation(org.springframework.lang.Nullable)");
		Advice advice = (MethodInterceptor) invocation -> {
			String name = invocation.getMethod().getName();
			Object[] arguments = invocation.getArguments();
			System.out.println("============>" + name + "放行前拦截...,args:" + Arrays.toString(arguments));
			Object obj = invocation.proceed();
			System.out.println("============>" + name + "放行后拦截...,result:" + obj);
			return obj;
		};
		Advisor advisor = new DefaultPointcutAdvisor(pointcut, advice);
		proxyFactory.addAdvisor(advisor);
		Person proxy = (Person) proxyFactory.getProxy();
		Class<?> targetClass = AopUtils.getTargetClass(proxy);
		proxy.run();
		proxy.run(10);
		proxy.say();
		proxy.say("zhangsan", 10);
	}

}

class Person {

	@Nullable
	public int run() {
		System.out.println("我在run...");
		return 0;
	}

	public void run(int i) {
		System.out.println("我在run...<" + i + ">");
	}

	public void say() {
		System.out.println("我在say...");
	}

	public void sayHi(String name) {
		System.out.println("Hi," + name + ",你好");
	}

	public int say(String name, int i) {
		System.out.println(name + "----" + i);
		return 0;
	}

}
