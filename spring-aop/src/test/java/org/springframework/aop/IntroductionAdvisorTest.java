package org.springframework.aop;

import org.aopalliance.intercept.MethodInvocation;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultIntroductionAdvisor;

class IntroductionAdvisorTest {

	@Test
	void testIntroductionInterceptor() {
		ProxyFactory proxyFactory = new ProxyFactory(new Person());
		proxyFactory.setProxyTargetClass(true);

		SomeInteIntroductionInterceptor advice = new SomeInteIntroductionInterceptor();
		Advisor advisor = new DefaultIntroductionAdvisor(advice, IOtherInte.class);

		proxyFactory.addAdvisor(advisor);

		IOtherInte proxy = (IOtherInte) proxyFactory.getProxy();
		proxy.doOther();
		System.out.println("============================");

		Person person = (Person) proxyFactory.getProxy();
		person.run();
	}

}

interface IOtherInte {
	void doOther();
}

class SomeInteIntroductionInterceptor implements IntroductionInterceptor, IOtherInte {

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		if (implementsInterface(invocation.getMethod().getDeclaringClass())) {
			System.out.println("我是引介增强的方法体~~~invoke");
			return invocation.getMethod().invoke(this, invocation.getArguments());
		}
		return invocation.proceed();
	}

	@Override
	public boolean implementsInterface(Class<?> intf) {
		return intf.isAssignableFrom(IOtherInte.class);
	}

	@Override
	public void doOther() {
		System.out.println("给人贴标签 doOther...");
	}
}

class Person {

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