package com.xajiusuo.jpa.util;


import com.xajiusuo.jpa.util.exception.MappException;

import java.io.PrintStream;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

abstract class AbstractMapp {
	private static final DateFormat DF_yyyy_MM_dd_HH_mm_ss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final DateFormat DF_yyyy_MM_dd = new SimpleDateFormat("yyyy-MM-dd");
	private static final DateFormat DF_yyyy_MM = new SimpleDateFormat("yyyy-MM-dd");
	private static final DateFormat DF_yyyy = new SimpleDateFormat("yyyy");
	private static final DateFormat DF_yyyy_MM_DD_HH_mm = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private static final DateFormat DF_MM_dd_yyyy = new SimpleDateFormat("MM/dd/yyyy");

	private boolean isControl = true;

	/**
	 * @since 要加载的实体
	 * */
	protected Object entity = null;

	/**
	 * @since 实体类
	 * */
	protected Class<? extends Object> entityclass = null;

	/**
	 * @since 父类Mapp
	 * */
	protected IMapp superMapp = null;

	/**
	 * @since 实体父级类
	 * */
	protected Class<? extends Object> superclass = null;

	/**
	 * @since 定义变量属性集合
	 * */
	protected Map<String, Field> map_df = new HashMap<String, Field>();
	
	/**
	 * @since 定义变量属性集合（不区分大小写）
	 * */
	protected Map<String, Field> map_df0 = new HashMap<String, Field>();

	/**
	 * @since 定义常量属性集合
	 * */
	protected Map<String, Field> map_ds = new HashMap<String, Field>();

	/**
	 * @since 定义方法集合
	 * */
	protected Map<String, Method> map_dm = new HashMap<String, Method>();

	/***
	 * 嵌套对象
	 */
	protected Map<String,IMapp> objs = new HashMap<String,IMapp>();

	/**
	 * @since 输出平台
	 * */
	protected PrintStream out;

	/**
	 * @since 默认平台
	 * */
	private static PrintStream oldout = System.out;

	protected AbstractMapp() {
	}

	protected AbstractMapp(Object entity) {
		load(entity);
	}

	protected void isunload() {
		if(entity == null)
			throw new MappException("实体为空");
	}

	protected void isload() {
		if(this.entity != null) {
			throw new MappException("实体未被解除");
		}
	}

	protected IMapp getParentIMapp() {
		if(superMapp == null) {
			if(entityclass.getSuperclass() == null)
				return null;
			superMapp = Mapp.createMapp();
			((Mapp)superMapp).entity = this.entity;
			superclass = entityclass.getSuperclass();
			((Mapp)superMapp).loadclass(superclass);
		}
		return superMapp;
	}

	protected final void loadclass(Class<?> entityclass) {
		this.entityclass = entityclass;
		Field[] fds = this.entityclass.getDeclaredFields();
		Method[] mds = this.entityclass.getDeclaredMethods();
		for(Field fd : fds) {
			fd.setAccessible(isControl);
			if(fd.toGenericString().indexOf("final") < 0){
				map_df.put(fd.getName(), fd);
				map_df0.put(fd.getName().toLowerCase(), fd);
			}else{
				map_ds.put(fd.getName(), fd);
			}
		}
		for(Method md : mds) {
			md.setAccessible(isControl);
			map_dm.put(md.getName(), md);
		}
		((Mapp)this).superMapp = ((Mapp)this).getParentIMapp();
	}

	public IMapp load(Object entity) {
		try {
			if(entity == null) {
				throw new NullPointerException();
			}
			if(this.entity != null && this.entity.getClass().equals(entity.getClass())){
				this.entity = entity;
				return (Mapp)this;
			}
			isload();
			unLoad();
			this.entity = entity;
			loadclass(entity.getClass());
		}catch(Exception e) {
			throw new RuntimeException(e);
		}
		return (Mapp)this;
	}

	public void reload(Object entity) {
		unLoad();
		load(entity);
	}

	public void unLoad() {
		if(entity == null)
			return;
		synchronized(entity) {
			if(superMapp != null)
				superMapp.unLoad();
			map_df.clear();
			map_df0.clear();
			map_ds.clear();
			map_dm.clear();
			objs.values().forEach(m -> m.unLoad());
			objs.clear();
		}
	}

	public String getFieldType(String fieldName) {
		Field f = null;
		isunload();
		f = getField(fieldName);
		if(f != null)
			return f.toGenericString();
		return null;
	}

	public String getTypeMethodString(String fieldName) {
		Method f = null;
		try {
			isunload();
			f = getMethod(fieldName);
		}catch(Exception e) {
			e.printStackTrace();
		}
		if(f != null)
			return f.toGenericString();
		return null;
	}

	/**
	 * @since 获得fieldName对应属性
	 * */
	public Field getField(String fieldName) {
		Field fl = null;
		isunload();

		if(fl == null)
			fl = map_df.get(fieldName);
		if(fl == null)
			fl = map_df0.get(fieldName.toLowerCase());
		if(fl == null)
			fl = map_ds.get(fieldName);
		if(fl == null) {
			if(superMapp != null)
				fl = superMapp.getField(fieldName);
		}
		if(fl == null)
			throw new MappException(fieldName);
		return fl;
	}

	/**
	 * @since 获得methodName对应方法
	 * */
	public Method getMethod(String methodName) {
		Method method = null;
		try {
			isunload();
			method = map_dm.get(methodName);
			if(method == null)
				method = superMapp.getMethod(methodName);
			if(method == null)
				throw new MappException(methodName);
		}catch(Exception e) {
		}
		return method;
	}

	public Object invoke(String methodName, Object... args) {
		Method m = getMethod(methodName);
		if(m == null) {
			return superMapp.invoke(methodName, args);
		}else {
			try {
				return m.invoke(entity, args);
			}catch(Exception e) {
				throw new MappException(e);
			}
		}
	}

	public void invokeSetMethod(String methodName, Object value) {
		Method m = getMethod(methodName);
		if(m == null) {
			superMapp.invokeSetMethod(methodName, value);
		}else {
			try {
				m.invoke(entity, getField0(m.getReturnType().getSimpleName(), value));
			}catch(Exception e) {
				throw new MappException(e);
			}
		}
	}

	/**
	 * @since 获得所有方法
	 * */
	public Collection<Method> getMethods() {
		isunload();
		return map_dm.values();
	}

	public Object getObjectbyField(String fieldName) {
		if(fieldName.contains(".")){
			String pro = fieldName.substring(0,fieldName.indexOf("."));
			IMapp m = objs.get(pro);
			if(m == null){
				m = Mapp.createMapp(getObjectbyField(pro));
				objs.put(pro,m);
			}
			return m.getObjectbyField(fieldName.substring(fieldName.indexOf(".") + 1));
		}

		Object obj = null;
		try {
			isunload();
			try {
				obj = map_dm.get("get" + fieldName.substring(0,1).toUpperCase() + fieldName.substring(1)).invoke(entity);
			} catch (Exception e) {}

			if(obj == null){
				obj = getField(fieldName).get(entity);
			}
		}catch(Exception e1) {
			throw new MappException(fieldName, e1);
		}
		return obj;
	}

	protected boolean noProperties(String key) {
		if(map_ds.containsKey(key)) {
			throw new MappException("不包含的属性");
		}else {
			if(superMapp != null) {
				if(((Mapp)superMapp).noProperties(key))
					return false;
			}
		}
		return true;
	}

	/**
	 * @since 是否变量
	 * */
	protected boolean isvaried(String key) {
		if(map_df.containsKey(key))
			return true;
		else {
			if(superMapp != null) {
				if(((Mapp)superMapp).isvaried(key)) {
					return true;
				}
			}
		}
		throw new MappException(key);
	}

	/**
	 * @since 是否常量
	 * */
	protected boolean isConstant(String key) {
		if(map_ds.containsKey(key))
			return true;
		if(superMapp != null) {
			if(((Mapp)superMapp).isConstant(key))
				return true;
		}
		throw new MappException(key);
	}

	public boolean containsField(String fieldName) {
		try {
			return isvaried(fieldName);
		}catch(MappException e) {
			return false;
		}
	}

	public boolean containsConstant(String fieldName) {
		try {
			return isConstant(fieldName);
		}catch(Exception e) {
			return false;
		}
	}

	public boolean containsMethod(String methodName) {
		if(map_dm.containsKey(methodName))
			return true;
		else if(superMapp != null) {
			return superMapp.containsMethod(methodName);
		}else {
			return false;
		}
	}

	public void setObject(String fieldName, Object value) {
		try {
			isvaried(fieldName);
			noProperties(fieldName);
			if(map_df.containsKey(fieldName)) {
				map_df.get(fieldName).set(entity, getField0(getSimpleName(fieldName), value));
			}else {
				if(superMapp != null) {
					superMapp.setObject(fieldName, value);
				}
			}
		}catch(Exception e) {
		}
	}
	

	public void setObjectIgnore(String fieldname, Object value) {
		try {
			String fieldName = fieldname.toLowerCase();
			if(map_df0.containsKey(fieldName)) {
				map_df0.get(fieldName).set(entity, getField0(getSimpleName(fieldName), value));
			}else {
				if(superMapp != null) {
					superMapp.setObject(fieldName, value);
				}
			}
		}catch(Exception e) {
		}
	}
	
	public void setValue(Map<String,Object> map){
		if(map != null){
			for(String s : map.keySet()){
				setObject(s, map.get(s));
			}
		}
	}
	
	public void setValueIgnore(Map<String,Object> map){
		if(map != null){
			for(String s : map.keySet()){
				setObjectIgnore(s, map.get(s));
			}
		}
	}

	public void setObjectbyString(String fieldName, String value) {
		try {
			if(map_df.containsKey(fieldName)) {
				map_df.get(fieldName).set(entity, getField0(getSimpleName(fieldName), value));
			}
		}catch(Exception e) {
		}
	}

	public boolean isDate(String fieldName) {
		return "Date".equals(getSimpleName(fieldName));
	}

	public String getSimpleName(String fieldName) {
		try {
			return getField(fieldName).getType().getSimpleName();
		}catch(Exception e) {
			return null;
		}
	}

	public Set<String> getFields() {
		Set<String> list = new HashSet<String>();
		list.addAll(map_df.keySet());
		if(superMapp != null)
			list.addAll(superMapp.getFields());
		return list;
	}

	public Class<?> getFieldInterface(String field, int... num) {
		if(getField(field) == null)
			return null;
		Class<?> res = getField(field).getType();
		if(num == null || num.length < 1)
			return res;
		for(int i = 0; i < num.length; i++) {
			try {
				res = (Class<?>)res.getInterfaces()[num[i]];
			}catch(Exception e) {
				return null;
			}
		}
		return res;
	}

	public Class<?> getFieldSuper(String field, int grade, int... num) {
		if(getField(field) == null)
			return null;
		Class<?> res = getField(field).getType();
		if(grade == 0)
			return res;
		for(int i = 0; i < grade; i++) {
			try {
				res = res.getSuperclass();
			}catch(Exception e) {
				return null;
			}
		}
		if(num != null) {
			for(int i = 0; i < num.length; i++) {
				try {
					res = (Class<?>)res.getInterfaces()[num[i]];
				}catch(Exception e) {
					return null;
				}
			}
		}
		return res;
	}

	public List<String> getFieldByClass(Class<? extends Serializable> className) {
		List<String> res = new ArrayList<String>(0);
		Iterator<String> df = map_df.keySet().iterator();
		while(df.hasNext()) {
			String f = df.next();
			try {
				if(isin(getField(f).getType(), className)) {
					res.add(f);
				}
			}catch(Exception e) {
			}
		}
		return res;
	}

	private boolean isin(Object obj, Class<?> className) {
		if(obj == null || className == null)
			return false;
		if(obj.getClass().equals(className))
			return true;
		Class<?> t = obj.getClass();
		while(t != null) {
			if(t.getClass().equals(className))
				return true;
			t = t.getSuperclass();
		}
		Type[] ts = obj.getClass().getGenericInterfaces();
		if(isinterface(ts, className))
			return true;
		return false;
	}

	private boolean isinterface(Type[] ts, Class<?> className) {
		if(ts == null || ts.length < 1)
			return false;
		for(Type t : ts) {
			System.out.println((Class<?>)t);
			if(((Class<?>)t).equals(className))
				return true;
			else if(isinterface(((Class<?>)t).getGenericInterfaces(), className))
				return true;
		}
		return false;
	}

	public Map<String, Object> getProperties() {
		Map<String, Object> list = new HashMap<String, Object>(map_ds);
		if(superMapp != null)
			list.putAll(superMapp.getProperties());
		return list;
	}

	public void setout(PrintStream out) {
		this.out = out;
		if(superMapp != null)
			((Mapp)superMapp).out = out;
	}

	public void showAllBody(boolean... allinfor) {
		boolean all = false;
		if(allinfor != null && allinfor.length > 0)
			all = allinfor[0];
		if(entity == null) {
			throw new MappException("没有实体");
		}
		if(out != null)
			System.setOut(out);
		System.out.println("\n===============================");
		System.out.println("对象" + entityclass.getSimpleName() + "常量、变量、方法");
		System.out.println("===============================\n");
		showConstDeclare(all);// 显示自定义常量属性
		showFieldDeclare(all);// 显示自定义变量属性
		showMethodDeclare(all);// 显示自定义方法
		if(superMapp != null) {
			superMapp.showAllBody(all);
		}
	}

	public void showByParm(int... value) {
		try {
			if(entity == null) {
				throw new MappException("实体为空");
			}
			if(out != null)
				System.setOut(out);
			if(value == null)
				return;
			for(int key : value) {
				System.out.println("==========================");
				switch(key) {
					case IMapp.show_field_declare:
						System.out.println("该类变量属性：" + entityclass.getSimpleName());
						System.out.println("==========================\n");
						showFieldDeclare(false);
						break;
					case IMapp.show_field_parend:
						System.out.println("父级变量属性");
						System.out.println("==========================\n");
						showFieldParent();
						break;
					case IMapp.show_method_declare:
						System.out.println("该类方法：" + entityclass.getSimpleName());
						System.out.println("==========================\n");
						showMethodDeclare(false);
						break;
					case IMapp.show_method_parend:
						System.out.println("父类方法");
						System.out.println("==========================\n");
						showMethodParent();
						break;
					case IMapp.show_const_declare:
						System.out.println("该类常量属性：" + entityclass.getSimpleName());
						System.out.println("==========================\n");
						showConstDeclare(false);
						break;
					case IMapp.show_const_parend:
						System.out.println("父类常量属性");
						System.out.println("==========================\n");
						showConstParent();
						break;
					default:
						break;
				}
			}
			System.out.println("==========================");
		}finally {
			System.setOut(oldout);
		}
	}

	/**
	 * @param all
	 * @since 定义常量属性
	 * */
	protected void showConstDeclare(boolean all) {
		if(all) {
			for(String key : map_ds.keySet()) {
				System.out.printf("属性：常量\t%s\n值：\t%s\n\n", getFieldType(key), getObjectbyField(key));
			}
		}else {
			for(String key : map_ds.keySet()) {
				System.out.printf("属性：常量\t%s\t%s\n\n", getField(key).getType().getSimpleName() + "\t" + key,
						getObjectbyField(key));
			}
		}
	}

	/**
	 * @since 父类常量属性
	 * */
	private void showConstParent() {
		if(superMapp != null)
			((Mapp)superMapp).showConstDeclare(false);
	}

	/**
	 * @since 定义变量属性
	 * */
	protected void showFieldDeclare(boolean all) {
		if(all) {
			for(String key : map_df.keySet()) {
				System.out.printf("属性：变量\t%s\t%s\n\n", getFieldType(key), getObjectbyField(key));
			}
		}else {
			for(String key : map_df.keySet()) {
				System.out.printf("属性：变量\t%s\t%s\n\n", getField(key).getType().getSimpleName() + "\t" + key,
						getObjectbyField(key));
			}
		}
	}

	/**
	 * @since 父类变量属性
	 * */
	private void showFieldParent() {
		if(superMapp != null)
			((Mapp)superMapp).showFieldDeclare(false);
	}

	/**
	 * @since 定义方法
	 * */
	protected void showMethodDeclare(boolean all) {
		Set<String> set = map_dm.keySet();
		if(all) {
			for(String key : set) {
				System.out.printf("定义方法：\t%s\n默认返回值：\t%s\n\n", getTypeMethodString(key), getMethod(key)
						.getDefaultValue());
			}
		}else {
			for(String key : set) {
				System.out
						.printf("方法：\t%s\t%s\n\n", getMethod(key).toGenericString(), getMethod(key).getDefaultValue());
			}
		}
	}

	/**
	 * @since 父类方法
	 * */
	private void showMethodParent() {
		if(superMapp != null)
			((Mapp)superMapp).showMethodDeclare(false);
	}

	public Long getVersion() {
		Field field = null;
		try {
			field = getField("serialVersionUID");
			return Long.parseLong(field.get(entity).toString());
		}catch(Exception e) {
		}
		return 0L;
	}

	/**
	 * @since 将字符串或对象赋值给变量
	 * */
	@SuppressWarnings("deprecation")
	public Object getField0(String type, Object value) {
		try {
			if("String".equals(type)) {
				return value.toString();
			}else if("int".equals(type) || "Integer".equals(type)) {
				if(!(value instanceof Integer))
					return Integer.valueOf(value.toString());
			}else if("Date".equals(type)) {
				if(!(value instanceof Date)) {
					try{
						return new Date(value.toString());
					}catch(Exception ee) {
						try {
							return DF_yyyy_MM_dd_HH_mm_ss.parse(value.toString());
						}catch(Exception e) {
							try {
								return DF_yyyy_MM_DD_HH_mm.parse(value.toString());
							}catch(Exception e1) {
								try {
									return DF_MM_dd_yyyy.parse(value.toString());
								}catch(Exception e2) {
									try {
										return DF_yyyy_MM_dd.parseObject(value.toString());
									}catch(Exception e3) {
										try {
											return DF_yyyy_MM.parseObject(value.toString());
										}catch(Exception e4) {
											try {
												return DF_yyyy.parseObject(value.toString());
											}catch(Exception e5) {
												return null;
											}
										}
									}
								}
							}
						}
					}
				}else{
					return value;
				}
			}else if("double".equals(type) || "Double".equals(type)) {
				if(!(value instanceof Double))
					return Double.valueOf(value.toString());
			}else if("char".equals(type) || "Character".equals(type)) {
				if(!(value instanceof Character))
					return Character.valueOf(value.toString().charAt(0));
			}else if("boolean".equals(type) || "Boolean".equals(type)) {
				if(!(value instanceof Boolean)) {
					if("1".equals(value) || "0".equals(value) || "true".equals(value) || "false".equals(value)) {
						return "true".equals(value) || "1".equals(value);
					}else {
						return null;
					}
				}
			}else if("short".equals(type) || "Short".equals(type)) {
				if(!(value instanceof Short))
					return Short.valueOf(value.toString());
			}else if("long".equals(type) || "Long".equals(type)) {
				if(!(value instanceof Long))
					return Long.valueOf(value.toString());
			}else if("byte".equals(type) || "Byte".equals(type)) {
				if(!(value instanceof Byte))
					return Byte.valueOf(value.toString());
			}else if("BigDecimal".equals(type)) {
				if(!(value instanceof BigDecimal))
					return BigDecimal.valueOf(Double.valueOf(value.toString()));
			}else if("BigInteger".equals(type)) {
				if(!(value instanceof BigInteger))
					return BigInteger.valueOf(Long.valueOf(value.toString()));
			}else if("Timestamp".equals(type)) {
				if(!(value instanceof Timestamp))
					return new Timestamp(((Date)getField0("Date", value)).getTime());
			}
		}catch(Exception e) {
			return null;
		}
		return value;

	}

	public void setControl(boolean isControl) {
		this.isControl = isControl;
	}

	public Class<?> getEntityClass() {
		return entityclass;
	}

	public <A extends Annotation> A getAnnotation(Class<A> annotationClass) {
		if(annotationClass == null)
			throw new NullPointerException();
		return (A)entityclass.getAnnotation(annotationClass);
	}

	public <A extends Annotation> A getMethodAnnotation(Class<A> annotationClass, String name) {
		return getMethod(name).getAnnotation(annotationClass);
	}

	public <A extends Annotation> A getMethodAnnotationAddGet(Class<A> annotationClass, String name) {
		if(name.toUpperCase().charAt(0) != name.charAt(0)) {
			return getMethod("get" + name.toUpperCase().charAt(0) + name.substring(1)).getAnnotation(annotationClass);
		}else {
			return null;
		}
	}

	public Method getProperByAnnotation(Class<? extends Annotation> annotationClass) {
		for(Method m : getMethods()) {
			if(m.getAnnotation(annotationClass) != null) {
				return m;
			}
		}
		return null;
	}

	public List<Method> getPropersByAnnotation(Class<? extends Annotation> annotationClass) {
		List<Method> list = new ArrayList<Method>(0);
		for(Method m : getMethods()) {
			if(m.getAnnotation(annotationClass) != null) {
				list.add(m);
			}
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	public <T> T getEntityByClass(T t) {
		return (T)entity;
	}
}
